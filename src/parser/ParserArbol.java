package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import def.Main;
import parser.arbol.Nodo;
import token.TipoToken;
import token.Token;

public class ParserArbol implements Parser{

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;

    public ParserArbol(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }

    @Override
    public boolean parse() {
        // Inicia el analizador léxico
        Nodo nodo = PROGRAM();
        ArrayList<Boolean> lista = new ArrayList<>();
        if(preanalisis.getTipo() == TipoToken.EOF && !hayErrores){
            if(def.Main.debug){
                System.out.print("\n");
            }
            System.out.println("\033[94mAnálisis Sintáctico Correcto\033[0m");
            if(def.Main.debug){
                System.out.println("\n\033[92m  Árbol Sintáctico\033[0m");
                nodo.imprimir(0, lista);
            }
            return  true;
        }else {
            System.out.println("\033[91mSe encontraron errores\033[0m");
        }
        return false;
    }

    // PROGRAM -> DECLARATION
    private Nodo PROGRAM(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo nodo = DECLARATION();      aux.add(nodo);
        return new Nodo(aux, "PROGRAM");
    }

    /*************************************************************************************************
                                                DECLARACIONES
    *************************************************************************************************/

    // DECLARATION -> FUN_DECL DECLARATION | VAR_DECL DECLARATION | STATEMENT DECLARATION | Ɛ
    private Nodo DECLARATION(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo nodo;
        switch (preanalisis.getTipo()) {
            case TipoToken.FUN:
                nodo = FUN_DECL();         aux.add(nodo);
                nodo = DECLARATION();      aux.add(nodo);
                break;
            case TipoToken.VAR:
                nodo = VAR_DECL();         aux.add(nodo);
                nodo = DECLARATION();      aux.add(nodo);
                break;
            case TipoToken.BANG:
            case TipoToken.MINUS:
            case TipoToken.TRUE:
            case TipoToken.FALSE:
            case TipoToken.NULL:
            case TipoToken.NUMBER:
            case TipoToken.STRING:
            case TipoToken.IDENTIFIER:
            case TipoToken.LEFT_PAREN:
            case TipoToken.FOR:
            case TipoToken.IF:
            case TipoToken.PRINT:
            case TipoToken.RETURN:
            case TipoToken.WHILE:
            case TipoToken.LEFT_BRACE:
                nodo = STATEMENT();        aux.add(nodo);
                nodo = DECLARATION();      aux.add(nodo);
                break;
            default:
                break;
        }
        return new Nodo(aux, "DECLARATION");
    }

    // FUN_DECL -> fun FUNCTION
    private Nodo FUN_DECL(){
        ArrayList<Object> aux = new ArrayList<>();

        match(TipoToken.FUN);           aux.add(previous());
        Nodo nodo = FUNCTION();         aux.add(nodo);
        return new Nodo(aux, "FUN_DECL");
    }

    // VAR_DECL -> var id VAR_INT ;
    private Nodo VAR_DECL(){
        ArrayList<Object> aux = new ArrayList<>();

        match(TipoToken.VAR);           aux.add(previous());
        match(TipoToken.IDENTIFIER);    aux.add(previous());
        Nodo nodo = VAR_INIT();         aux.add(nodo);
        match(TipoToken.SEMICOLON);     aux.add(previous());
        return new Nodo(aux, "VAR_DECL");
    }

    // VAR_INIT -> = EXPRESSION
    private Nodo VAR_INIT(){
        ArrayList<Object> aux = new ArrayList<>();

        if(preanalisis. getTipo() == TipoToken.EQUAL){
            match(TipoToken.EQUAL);         aux.add(previous());
            Nodo naux = EXPRESSION();       aux.add(naux);
        }
        return new Nodo(aux, "VAR_INIT");
    }

    /*************************************************************************************************
                                                SENTENCIAS
    *************************************************************************************************/

    // STATEMENT -> EXPR_STMT | FOR_STMT | IF_STMT | PRINT_STMT | RETURN_STMT | WHILE_STMT | BLOCK
    private Nodo STATEMENT(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo nodo;
        switch (preanalisis.getTipo()) {
            case TipoToken.BANG:
            case TipoToken.MINUS:
            case TipoToken.TRUE:
            case TipoToken.FALSE:
            case TipoToken.NULL:
            case TipoToken.NUMBER:
            case TipoToken.STRING:
            case TipoToken.IDENTIFIER:
            case TipoToken.LEFT_PAREN:
                nodo = EXPR_STMT();    aux.add(nodo);
                return new Nodo(aux, "STATEMENT");
            case TipoToken.FOR:
                nodo = FOR_STMT();     aux.add(nodo);
                return new Nodo(aux, "STATEMENT");
            case TipoToken.IF:
                nodo = IF_STMT();      aux.add(nodo);
                return new Nodo(aux, "STATEMENT");
            case TipoToken.PRINT:
                nodo = PRINT_STMT();   aux.add(nodo);
                return new Nodo(aux, "STATEMENT");
            case TipoToken.RETURN:
                nodo = RETURN_STMT();  aux.add(nodo);
                return new Nodo(aux, "STATEMENT");
            case TipoToken.WHILE:
                nodo = WHILE_STMT();   aux.add(nodo);
                return new Nodo(aux, "STATEMENT");
            case TipoToken.LEFT_BRACE:
                nodo = BLOCK();        aux.add(nodo);
                return new Nodo(aux, "STATEMENT");
            default:
                error(preanalisis.getLinea(), "Se esperaba inicio de sentencia");
                return null;
        }
    }

    // EXPR_STMT -> EXPRESSION;
    private Nodo EXPR_STMT(){
        ArrayList<Object> aux = new ArrayList<>();
        
        Nodo naux = EXPRESSION();       aux.add(naux);
        match(TipoToken.SEMICOLON);     aux.add(previous());
        return new Nodo(aux, "EXPR_STMT");
    }

    // FOR_STMT -> for ( FOR_STMT_1 FOR_STMT_2 FOR_STMT_3 ) STATEMENT
    private Nodo FOR_STMT(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo nodo;
        match(TipoToken.FOR);           aux.add(previous());
        match(TipoToken.LEFT_PAREN);    aux.add(previous());
        nodo = FOR_STMT_1();            aux.add(nodo);
        nodo = FOR_STMT_2();            aux.add(nodo);
        nodo = FOR_STMT_3();            aux.add(nodo);
        match(TipoToken.RIGHT_PAREN);   aux.add(previous());
        nodo = STATEMENT();             aux.add(nodo);
        return new Nodo(aux, "FOR_STMT");
    }

    // FOR_STMT_1 -> VAR_DECL | EXPR_STMT | ;
    private Nodo FOR_STMT_1(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo nodo;
        switch (preanalisis.getTipo()) {
            case TipoToken.VAR:
                nodo = VAR_DECL();              aux.add(nodo);
                return new Nodo(aux, "FOR_STMT_1");
            case TipoToken.BANG:
            case TipoToken.MINUS:
            case TipoToken.TRUE:
            case TipoToken.FALSE:
            case TipoToken.NULL:
            case TipoToken.NUMBER:
            case TipoToken.STRING:
            case TipoToken.IDENTIFIER:
            case TipoToken.LEFT_PAREN:
                nodo = EXPR_STMT();             aux.add(nodo);
                return new Nodo(aux, "FOR_STMT_1");
            case TipoToken.SEMICOLON:
                match(TipoToken.SEMICOLON);     aux.add(previous());
                return new Nodo(aux, "FOR_STMT_1");
            default:
                error(preanalisis.getLinea(), "Se esperaba inicio de sentencia");
                return null;
        }
    }

    // FOR_STMT_2 -> EXPRESSION ; | ;
    private Nodo FOR_STMT_2(){
        ArrayList<Object> aux = new ArrayList<>();

        switch (preanalisis.getTipo()) {
            case TipoToken.BANG:
            case TipoToken.MINUS:
            case TipoToken.TRUE:
            case TipoToken.FALSE:
            case TipoToken.NULL:
            case TipoToken.NUMBER:
            case TipoToken.STRING:
            case TipoToken.IDENTIFIER:
            case TipoToken.LEFT_PAREN:
                Nodo naux = EXPRESSION();       aux.add(naux);
                match(TipoToken.SEMICOLON);     aux.add(previous());
                return new Nodo(aux, "FOR_STMT_2");
            case TipoToken.SEMICOLON:
                match(TipoToken.SEMICOLON);     aux.add(previous());
                return new Nodo(aux, "FOR_STMT_2");
            default:
                error(preanalisis.getLinea(), "Se esperaba inicio de sentencia");
                return null;
        }
    }

    // FOR_STMT_3 -> EXPRESSION | Ɛ
    private Nodo FOR_STMT_3(){
        ArrayList<Object> aux = new ArrayList<>();

        switch (preanalisis.getTipo()) {
            case TipoToken.BANG:
            case TipoToken.MINUS:
            case TipoToken.TRUE:
            case TipoToken.FALSE:
            case TipoToken.NULL:
            case TipoToken.NUMBER:
            case TipoToken.STRING:
            case TipoToken.IDENTIFIER:
            case TipoToken.LEFT_PAREN:
                Nodo naux = EXPRESSION();       aux.add(naux);
                break;
            default:
                break;
        }
        return new Nodo(aux, "FOR_STMT_3");
    }

    // IF_STMT -> if ( EXPRESSION ) STATEMENT ELSE_STMT
    private Nodo IF_STMT(){
        ArrayList<Object> aux = new ArrayList<>();

        match(TipoToken.IF);            aux.add(previous());
        match(TipoToken.LEFT_PAREN);    aux.add(previous());
        Nodo nodo = EXPRESSION();       aux.add(nodo);
        match(TipoToken.RIGHT_PAREN);   aux.add(previous());
        nodo = STATEMENT();             aux.add(nodo);
        nodo = ELSE_STMT();             aux.add(nodo);
        return new Nodo(aux, "IF_STMT");
    }

    // ELSE_STMT -> else STATEMENT
    private Nodo ELSE_STMT(){
        ArrayList<Object> aux = new ArrayList<>();
        
        if(preanalisis.getTipo() == TipoToken.ELSE){
            match(TipoToken.ELSE);      aux.add(previous());
            Nodo nodo = STATEMENT();    aux.add(nodo);
        }
        return new Nodo(aux, "ELSE_STMT");
    }

    // PRINT_STMT -> print EXPRESSION ;
    private Nodo PRINT_STMT(){
        ArrayList<Object> aux = new ArrayList<>();

        match(TipoToken.PRINT);         aux.add(previous());
        Nodo naux = EXPRESSION();       aux.add(naux);
        match(TipoToken.SEMICOLON);     aux.add(previous());
        return new Nodo(aux, "PRINT_STMT");
    }

    // RETURN_STMT -> return RETURN_EXP_OPC ;
    private Nodo RETURN_STMT(){
        ArrayList<Object> aux = new ArrayList<>();

        match(TipoToken.RETURN);            aux.add(previous());
        Nodo naux = RETURN_EXP_OPC();       aux.add(naux);
        match(TipoToken.SEMICOLON);         aux.add(previous());
        return new Nodo(aux, "RETURN_STMT");
    }

    // RETURN_EXP_OPC -> EXPRESSION | Ɛ
    private Nodo RETURN_EXP_OPC(){
        ArrayList<Object> aux = new ArrayList<>();

        switch (preanalisis.getTipo()) {
            case TipoToken.BANG:
            case TipoToken.MINUS:
            case TipoToken.TRUE:
            case TipoToken.FALSE:
            case TipoToken.NULL:
            case TipoToken.NUMBER:
            case TipoToken.STRING:
            case TipoToken.IDENTIFIER:
            case TipoToken.LEFT_PAREN:
                Nodo naux = EXPRESSION();   aux.add(naux);
                break;
            default:
                break;
        }
        return new Nodo(aux, "RETURN_EXP_OPC");
    }

    // WHILE_STMT -> while ( EXPRESSION ) STATEMENT
    private Nodo WHILE_STMT(){
        ArrayList<Object> aux = new ArrayList<>();

        match(TipoToken.WHILE);         aux.add(previous());
        match(TipoToken.LEFT_PAREN);    aux.add(previous());
        Nodo nodo = EXPRESSION();       aux.add(nodo);
        match(TipoToken.RIGHT_PAREN);   aux.add(previous());
        nodo = STATEMENT();             aux.add(nodo);
        return new Nodo(aux, "WHILE_STMT");
    }

    // BLOCK -> { DECLARATION }
    private Nodo BLOCK(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo nodo;
        match(TipoToken.LEFT_BRACE);    aux.add(previous());
        nodo = DECLARATION();           aux.add(nodo);
        match(TipoToken.RIGHT_BRACE);   aux.add(previous());
        return new Nodo(aux, "BLOCK");
    }

    /*************************************************************************************************
                                                EXPRESIONES
    *************************************************************************************************/

    // EXPRESSION -> ASSIGNMENT
    private Nodo EXPRESSION(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo nodo;
        nodo = ASSIGNMENT();    aux.add(nodo);
        return new Nodo(aux, "EXPRESSION");
    }

    // ASIGNMENT -> LOGIC_OR ASSIGNMENT_OPC
    private Nodo ASSIGNMENT(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo nodo;
        nodo = LOGIC_OR();          aux.add(nodo);
        nodo = ASSIGNMENT_OPC();    aux.add(nodo);
        return new Nodo(aux, "ASSIGNMENT");
    }

    // ASSIGNMENT_OPC -> = EXPRESSION | Ɛ
    private Nodo ASSIGNMENT_OPC(){
        ArrayList<Object> aux = new ArrayList<>();

        if(preanalisis.getTipo() == TipoToken.EQUAL){
            match(TipoToken.EQUAL);     aux.add(previous());
            Nodo naux = EXPRESSION();   aux.add(naux);
        }
        return new Nodo(aux, "ASSIGNMENT_OPC");
    }

    // LOGIC_OR -> LOGIC_AND lOGIC_OR_2
    private Nodo LOGIC_OR(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo nodo;
        nodo = LOGIC_AND();     aux.add(nodo);
        nodo = LOGIC_OR_2();    aux.add(nodo);
        return new Nodo(aux, "LOGIC_OR");
    }

    // LOGIC_OR_2 -> or LOGIC_AND LOGIC_OR_2 | Ɛ
    private Nodo LOGIC_OR_2(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo nodo;
        if(preanalisis.getTipo() == TipoToken.OR){
            match(TipoToken.OR);    aux.add(previous());
            nodo = LOGIC_AND();     aux.add(nodo);
            nodo = LOGIC_OR_2();    aux.add(nodo);
        }
        return new Nodo(aux, "LOGIC_OR_2");
    }

    // LOGIC_AND -> EQUALITY LOGIC_AND_2
    private Nodo LOGIC_AND(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo nodo;
        nodo = EQUALITY();      aux.add(nodo);
        nodo = LOGIC_AND_2();   aux.add(nodo);
        return new Nodo(aux, "LOGIC_AND");
    }

    // LOGIC_AND_2 -> and EQUALITY LOGIC_AND_2 | Ɛ
    private Nodo LOGIC_AND_2(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo nodo;
        if(preanalisis.getTipo() == TipoToken.AND){
            match(TipoToken.AND);       aux.add(previous());
            nodo = EQUALITY();          aux.add(nodo);
            nodo = LOGIC_AND_2();       aux.add(nodo);
        }
        return new Nodo(aux, "LOGIC_AND_2");
    }

    // EQUALITY -> COMPARISON EQUALITY_2
    private Nodo EQUALITY(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo nodo;
        nodo = COMPARISON();    aux.add(nodo);
        nodo = EQUALITY_2();    aux.add(nodo);
        return new Nodo(aux, "EQUALITY");
    }

    // EQUALITY_2 -> != COMPARISON EQUALITY_2 | == COMPARISON EQUALITY_2 | Ɛ
    private Nodo EQUALITY_2(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo nodo;
        if(preanalisis.getTipo() == TipoToken.BANG_EQUAL){
            match(TipoToken.BANG_EQUAL);    aux.add(previous());
            nodo = COMPARISON();            aux.add(nodo);
            nodo = EQUALITY_2();            aux.add(nodo);
        } else if(preanalisis.getTipo() == TipoToken.EQUAL_EQUAL) {
            match(TipoToken.EQUAL_EQUAL);   aux.add(previous());
            nodo = COMPARISON();            aux.add(nodo);
            nodo = EQUALITY_2();            aux.add(nodo);
        }
        return new Nodo(aux, "EQUALITY_2");
    }

    // COMPARISON -> TERM COMPARISON_2
    private Nodo COMPARISON(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo nodo;
        nodo = TERM();          aux.add(nodo);
        nodo = COMPARISON_2();  aux.add(nodo);
        return new Nodo(aux, "COMPARISON");
    }

    // COMPARISON_2 -> > TERM COMPARISON_2 | >= TERM COMPARISON_2 | < TERM COMPARISON_2 | <= TERM COMPARISON_2 | Ɛ
    private Nodo COMPARISON_2(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo nodo;
        if(preanalisis.getTipo() == TipoToken.GREATER){
            match(TipoToken.GREATER);           aux.add(previous());
            nodo = TERM();                      aux.add(nodo);
            nodo = COMPARISON_2();              aux.add(nodo);
        } else if(preanalisis.getTipo() == TipoToken.GREATER_EQUAL){
            match(TipoToken.GREATER_EQUAL);     aux.add(previous());
            nodo = TERM();                      aux.add(nodo);
            nodo = COMPARISON_2();              aux.add(nodo);
        } else if(preanalisis.getTipo() == TipoToken.LESS){
            match(TipoToken.LESS);              aux.add(previous());
            nodo = TERM();                      aux.add(nodo);
            nodo = COMPARISON_2();              aux.add(nodo);
        } else if(preanalisis.getTipo() == TipoToken.LESS_EQUAL){
            match(TipoToken.LESS_EQUAL);        aux.add(previous());
            nodo = TERM();                      aux.add(nodo);
            nodo = COMPARISON_2();              aux.add(nodo);
        }
        return new Nodo(aux, "COMPARISON_2");
    }

    // TERM -> FACTOR TERM_2
    private Nodo TERM(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo naux;
        naux = FACTOR();    aux.add(naux);
        naux = TERM_2();    aux.add(naux);
        return new Nodo(aux, "TERM");
    }

    // TERM_2 -> - FACTOR TERM_2 | + FACTOR TERM_2 | Ɛ
    private Nodo TERM_2(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo naux;
        if(preanalisis.getTipo() == TipoToken.MINUS){
            match(TipoToken.MINUS);     aux.add(previous());
            naux = FACTOR();            aux.add(naux);
            naux = TERM_2();            aux.add(naux);
        } else if(preanalisis.getTipo() == TipoToken.PLUS){
            match(TipoToken.PLUS);      aux.add(previous());
            naux = FACTOR();            aux.add(naux);
            naux = TERM_2();            aux.add(naux);
        }
        return new Nodo(aux, "TERM_2");
    }

    // FACTOR -> UNARY FACTOR_2
    private Nodo FACTOR(){
        ArrayList<Object> aux = new ArrayList<>();

        Nodo naux = UNARY();    aux.add(naux);
        naux = FACTOR_2();      aux.add(naux);
        return new Nodo(aux, "FACTOR");
    }

    // FACTOR_2 -> / UNARY FACTOR_2 | * UNARY FACTOR_2 | Ɛ
    private Nodo FACTOR_2(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo naux;
        if(preanalisis.getTipo() == TipoToken.SLASH){
            match(TipoToken.SLASH); aux.add(previous());
            naux = UNARY();         aux.add(naux);
            naux = FACTOR_2();      aux.add(naux);
        } else if(preanalisis.getTipo() == TipoToken.STAR){
            match(TipoToken.STAR);  aux.add(previous());
            naux = UNARY();         aux.add(naux);
            naux = FACTOR_2();      aux.add(naux);
        }
        return new Nodo(aux, "FACTOR_2");
    }

    // UNARY -> ! UNARY | - UNARY | CALL
    private Nodo UNARY(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo naux;
        switch (preanalisis.getTipo()) {
            case TipoToken.BANG:
                match(TipoToken.BANG);      aux.add(previous());
                naux = UNARY();             aux.add(naux);
                return new Nodo(aux, "UNARY");
            case TipoToken.MINUS:
                match(TipoToken.MINUS);     aux.add(previous());
                naux = UNARY();             aux.add(naux);
                return new Nodo(aux, "UNARY");
            case TipoToken.TRUE:
            case TipoToken.FALSE:
            case TipoToken.NULL:
            case TipoToken.NUMBER:
            case TipoToken.STRING:
            case TipoToken.IDENTIFIER:
            case TipoToken.LEFT_PAREN:
                naux = CALL();              aux.add(naux);
                return new Nodo(aux, "UNARY");
            default:
                error(preanalisis.getLinea(), "Se esperaba '!', '-', o PRIMARY");
                break;
        }
        return null;
    }

    // CALL -> PRIMARY CALL_2
    private Nodo CALL(){
        ArrayList<Object> aux = new ArrayList<>();
        
        Nodo naux = PRIMARY();      aux.add(naux);
        naux = CALL_2();            aux.add(naux);
        return new Nodo(aux, "CALL");
    }

    // CALL_2 -> ( ARGUMENTS_OPC ) CALL_2 | Ɛ
    private Nodo CALL_2(){
        ArrayList<Object> aux = new ArrayList<>();

        if(preanalisis.getTipo() == TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);    aux.add(previous());
            Nodo naux = ARGUMENTS_OPC();    aux.add(naux);
            match(TipoToken.RIGHT_PAREN);   aux.add(previous());
            naux = CALL_2();                aux.add(naux);
        }
        return new Nodo(aux, "CALL_2");
    }

    // PRIMARY -> true | false | null | number | string | id | ( EXPRESSION )
    private Nodo PRIMARY(){
        ArrayList<Object> aux = new ArrayList<>();

        if(preanalisis.getTipo() == TipoToken.TRUE){
            match(TipoToken.TRUE);          aux.add(previous());
            return new Nodo(aux, "PRIMARY");
        } else if(preanalisis.getTipo() == TipoToken.FALSE){
            match(TipoToken.FALSE);         aux.add(previous());
            return new Nodo(aux, "PRIMARY");
        } else if(preanalisis.getTipo() == TipoToken.NULL){
            match(TipoToken.NULL);          aux.add(previous());
            return new Nodo(aux, "PRIMARY");
        } else if(preanalisis.getTipo() == TipoToken.NUMBER){
            match(TipoToken.NUMBER);        aux.add(previous());
            return new Nodo(aux, "PRIMARY");
        } else if(preanalisis.getTipo() == TipoToken.STRING){
            match(TipoToken.STRING);        aux.add(previous());
            return new Nodo(aux, "PRIMARY");
        } else if(preanalisis.getTipo() == TipoToken.IDENTIFIER){
            match(TipoToken.IDENTIFIER);    aux.add(previous());
            return new Nodo(aux, "PRIMARY");
        } else if(preanalisis.getTipo() == TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);    aux.add(previous());
            Nodo naux = EXPRESSION();       aux.add(naux);
            match(TipoToken.RIGHT_PAREN);   aux.add(previous());
            return new Nodo(aux, "PRIMARY");
        } else {
            error(preanalisis.getLinea(), "Se esperaba PRIMARY");
        }
        return null;
    }

    /*************************************************************************************************
                                                    OTRAS
    *************************************************************************************************/

    // FUNCTION ->  id ( PARAMETERS_OPC ) BLOCK
    private Nodo FUNCTION(){
        ArrayList<Object> aux = new ArrayList<>();

        match(TipoToken.IDENTIFIER);        aux.add(previous());
        match(TipoToken.LEFT_PAREN);        aux.add(previous());
        Nodo naux = PARAMETERS_OPC();       aux.add(naux);
        match(TipoToken.RIGHT_PAREN);       aux.add(previous());
        naux = BLOCK();                     aux.add(naux);
        return new Nodo(aux, "FUNCTION");
    }

    // FUNCTIONS -> FUN_DECL FUNCTIONS | Ɛ
    /*private void FUNCTIONS(){
        if(hayErrores)
            return;

        if(preanalisis.getTipo() == TipoToken.FUN){
            FUN_DECL();
            FUNCTIONS();
        }
    }*/

    // PARAMETERS_OPC -> PARAMETERS | Ɛ
    private Nodo PARAMETERS_OPC(){
        ArrayList<Object> aux = new ArrayList<>();

        if(preanalisis.getTipo() == TipoToken.IDENTIFIER){
            Nodo naux = PARAMETERS();       aux.add(naux);
        }
        return new Nodo(aux, "PARAMETERS_OPC");
    }

    // PARAMETERS -> id PARAMETERS_2
    private Nodo PARAMETERS(){
        ArrayList<Object> aux = new ArrayList<>();

        match(TipoToken.IDENTIFIER);        aux.add(previous());
        Nodo naux = PARAMETERS_2();         aux.add(naux);
        return new Nodo(aux, "PARAMETERS");
    }

    // PARAMETERS_2 -> , id PARAMETERS_2 | Ɛ
    private Nodo PARAMETERS_2(){
        ArrayList<Object> aux = new ArrayList<>();

        if(preanalisis.getTipo() == TipoToken.COMMA){
            match(TipoToken.COMMA);         aux.add(previous());
            match(TipoToken.IDENTIFIER);    aux.add(previous());
            Nodo naux = PARAMETERS_2();     aux.add(naux);
        }
        return new Nodo(aux, "PARAMETERS_2");
    }

    // ARGUMENTS_OPC -> EXPRESSION ARGUMENTS | Ɛ
    private Nodo ARGUMENTS_OPC(){
        ArrayList<Object> aux = new ArrayList<>();

        TipoToken pre = preanalisis.getTipo();
        if(pre == TipoToken.BANG || pre == TipoToken.MINUS
        || pre == TipoToken.TRUE || pre == TipoToken.FALSE || pre == TipoToken.NULL || pre == TipoToken.NUMBER || pre == TipoToken.STRING || pre == TipoToken.IDENTIFIER || pre == TipoToken.LEFT_PAREN){
            Nodo naux = EXPRESSION();       aux.add(naux);
            naux = ARGUMENTS();             aux.add(naux);
        }
        return new Nodo(aux, "ARGUMENTS_OPC");
    }

    // ARGUMENTS -> , EXPRESSION ARGUMENTS | Ɛ
    private Nodo ARGUMENTS(){
        ArrayList<Object> aux = new ArrayList<>();

        if(preanalisis.getTipo() == TipoToken.COMMA){
            match(TipoToken.COMMA);         aux.add(previous());
            Nodo naux = EXPRESSION();       aux.add(naux);
            naux = ARGUMENTS();             aux.add(naux);
        }
        return new Nodo(aux, "ARGUMENTS");
    }


    public void match(TipoToken tt){
        if(preanalisis.getTipo() == tt){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            error(preanalisis.getLinea(), "Se esperaba "+TipoToken.imprimir(tt));
        }
    }

    void error(int linea, String mensaje){
        hayErrores = true;
        System.out.println("\033[91mAnálisis Sintáctico Incorrecto\033[0m");
        Main.reportar(linea, mensaje);
    }

    private Token previous() {
        return this.tokens.get(i - 1);
    }
}
