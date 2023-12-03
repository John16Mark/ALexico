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
        nodo.imprimir(0);
        if(preanalisis.getTipo() == TipoToken.EOF && !hayErrores){
            System.out.println("\033[94mAnálisis Sintáctico Correcto\033[0m");
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

        aux.add(preanalisis);match(TipoToken.FUN);
        Nodo nodo = FUNCTION();         aux.add(nodo);
        return new Nodo(aux, "FUN_DECL");
    }

    // VAR_DECL -> var id VAR_INT ;
    private Nodo VAR_DECL(){
        ArrayList<Object> aux = new ArrayList<>();

        aux.add(preanalisis);match(TipoToken.VAR);
        aux.add(preanalisis);match(TipoToken.IDENTIFIER);
        Nodo nodo = VAR_INIT();     aux.add(nodo);
        aux.add(preanalisis);match(TipoToken.SEMICOLON);
        return new Nodo(aux, "VAR_DECL");
    }

    // VAR_INIT -> = EXPRESSION
    private Nodo VAR_INIT(){
        ArrayList<Object> aux = new ArrayList<>();

        if(preanalisis. getTipo() == TipoToken.EQUAL){
            aux.add(preanalisis);match(TipoToken.EQUAL);
            Nodo naux = EXPRESSION();aux.add(naux);
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
        
        Nodo naux = EXPRESSION();   aux.add(naux);
        aux.add(preanalisis);       match(TipoToken.SEMICOLON);
        return new Nodo(aux, "EXPR_STMT");
    }

    // FOR_STMT -> for ( FOR_STMT_1 FOR_STMT_2 FOR_STMT_3 ) STATEMENT
    private Nodo FOR_STMT(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo nodo;
        aux.add(preanalisis);match(TipoToken.FOR);
        aux.add(preanalisis);match(TipoToken.LEFT_PAREN);
        nodo = FOR_STMT_1();       aux.add(nodo);
        nodo = FOR_STMT_2();       aux.add(nodo);
        nodo = FOR_STMT_3();       aux.add(nodo);
        aux.add(preanalisis);match(TipoToken.RIGHT_PAREN);
        nodo = STATEMENT();        aux.add(nodo);
        return new Nodo(aux, "FOR_STMT");
    }

    // FOR_STMT_1 -> VAR_DECL | EXPR_STMT | ;
    private Nodo FOR_STMT_1(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo nodo;
        switch (preanalisis.getTipo()) {
            case TipoToken.VAR:
                nodo = VAR_DECL();      aux.add(nodo);
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
                nodo = EXPR_STMT();     aux.add(nodo);
                return new Nodo(aux, "FOR_STMT_1");
            case TipoToken.SEMICOLON:
                aux.add(preanalisis);   match(TipoToken.SEMICOLON);
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
                Nodo naux = EXPRESSION();   aux.add(naux);
                aux.add(preanalisis);       match(TipoToken.SEMICOLON);
                return new Nodo(aux, "FOR_STMT_2");
            case TipoToken.SEMICOLON:
                aux.add(preanalisis);       match(TipoToken.SEMICOLON);
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
                Nodo naux = EXPRESSION();aux.add(naux);
                break;
            default:
                break;
        }
        return new Nodo(aux, "FOR_STMT_3");
    }

    // IF_STMT -> if ( EXPRESSION ) STATEMENT ELSE_STMT
    private Nodo IF_STMT(){
        ArrayList<Object> aux = new ArrayList<>();

        aux.add(preanalisis);match(TipoToken.IF);
        aux.add(preanalisis);match(TipoToken.LEFT_PAREN);
        Nodo nodo = EXPRESSION();   aux.add(nodo);
        aux.add(preanalisis);match(TipoToken.RIGHT_PAREN);
        nodo = STATEMENT();         aux.add(nodo);
        nodo = ELSE_STMT();         aux.add(nodo);
        return new Nodo(aux, "IF_STMT");
    }

    // ELSE_STMT -> else STATEMENT
    private Nodo ELSE_STMT(){
        ArrayList<Object> aux = new ArrayList<>();
        
        if(preanalisis.getTipo() == TipoToken.ELSE){
            aux.add(preanalisis);match(TipoToken.ELSE);
            Nodo nodo = STATEMENT();    aux.add(nodo);
        }
        return new Nodo(aux, "ELSE_STMT");
    }

    // PRINT_STMT -> print EXPRESSION ;
    private Nodo PRINT_STMT(){
        ArrayList<Object> aux = new ArrayList<>();

        aux.add(preanalisis);match(TipoToken.PRINT);
        Nodo naux = EXPRESSION();aux.add(naux);
        aux.add(preanalisis);match(TipoToken.SEMICOLON);
        return new Nodo(aux, "PRINT_STMT");
    }

    // RETURN_STMT -> return RETURN_EXP_OPC ;
    private Nodo RETURN_STMT(){
        ArrayList<Object> aux = new ArrayList<>();

        aux.add(preanalisis);match(TipoToken.RETURN);
        Nodo naux = RETURN_EXP_OPC();       aux.add(naux);
        aux.add(preanalisis);match(TipoToken.SEMICOLON);
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

        aux.add(preanalisis);match(TipoToken.WHILE);
        aux.add(preanalisis);match(TipoToken.LEFT_PAREN);
        Nodo nodo = EXPRESSION();   aux.add(nodo);
        aux.add(preanalisis);match(TipoToken.RIGHT_PAREN);
        nodo = STATEMENT();         aux.add(nodo);
        return new Nodo(aux, "WHILE_STMT");
    }

    // BLOCK -> { DECLARATION }
    private Nodo BLOCK(){
        ArrayList<Object> aux = new ArrayList<>();
        Nodo nodo;
        aux.add(preanalisis);match(TipoToken.LEFT_BRACE);
        nodo = DECLARATION();      aux.add(nodo);
        aux.add(preanalisis);match(TipoToken.RIGHT_BRACE);
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
            aux.add(preanalisis);match(TipoToken.EQUAL);
            Nodo naux = EXPRESSION();aux.add(naux);
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
            aux.add(preanalisis);match(TipoToken.OR);
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
            aux.add(preanalisis);match(TipoToken.AND);
            nodo = EQUALITY();      aux.add(nodo);
            nodo = LOGIC_AND_2();   aux.add(nodo);
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
            aux.add(preanalisis);match(TipoToken.BANG_EQUAL);
            nodo = COMPARISON();    aux.add(nodo);
            nodo = EQUALITY_2();    aux.add(nodo);
        } else if(preanalisis.getTipo() == TipoToken.EQUAL_EQUAL) {
            aux.add(preanalisis);match(TipoToken.EQUAL_EQUAL);
            nodo = COMPARISON();    aux.add(nodo);
            nodo = EQUALITY_2();    aux.add(nodo);
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
            aux.add(preanalisis);match(TipoToken.GREATER);
            nodo = TERM();          aux.add(nodo);
            nodo = COMPARISON_2();  aux.add(nodo);
        } else if(preanalisis.getTipo() == TipoToken.GREATER_EQUAL){
            aux.add(preanalisis);match(TipoToken.GREATER_EQUAL);
            nodo = TERM();          aux.add(nodo);
            nodo = COMPARISON_2();  aux.add(nodo);
        } else if(preanalisis.getTipo() == TipoToken.LESS){
            aux.add(preanalisis);match(TipoToken.LESS);
            nodo = TERM();          aux.add(nodo);
            nodo = COMPARISON_2();  aux.add(nodo);
        } else if(preanalisis.getTipo() == TipoToken.LESS_EQUAL){
            aux.add(preanalisis);match(TipoToken.LESS_EQUAL);
            nodo = TERM();          aux.add(nodo);
            nodo = COMPARISON_2();  aux.add(nodo);
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
            aux.add(preanalisis);match(TipoToken.MINUS);
            naux = FACTOR();    aux.add(naux);
            naux = TERM_2();    aux.add(naux);
        } else if(preanalisis.getTipo() == TipoToken.PLUS){
            aux.add(preanalisis);match(TipoToken.PLUS);
            naux = FACTOR();    aux.add(naux);
            naux = TERM_2();    aux.add(naux);
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
            aux.add(preanalisis);   match(TipoToken.SLASH);
            naux = UNARY();         aux.add(naux);
            naux = FACTOR_2();      aux.add(naux);
        } else if(preanalisis.getTipo() == TipoToken.STAR){
            aux.add(preanalisis);   match(TipoToken.STAR);
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
                aux.add(preanalisis);match(TipoToken.BANG);
                naux = UNARY();aux.add(naux);
                return new Nodo(aux, "UNARY");
            case TipoToken.MINUS:
                aux.add(preanalisis);match(TipoToken.MINUS);
                naux = UNARY();aux.add(naux);
                return new Nodo(aux, "UNARY");
            case TipoToken.TRUE:
            case TipoToken.FALSE:
            case TipoToken.NULL:
            case TipoToken.NUMBER:
            case TipoToken.STRING:
            case TipoToken.IDENTIFIER:
            case TipoToken.LEFT_PAREN:
                naux = CALL();aux.add(naux);
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
        
        Nodo naux = PRIMARY();  aux.add(naux);
        naux = CALL_2();        aux.add(naux);
        return new Nodo(aux, "CALL");
    }

    // CALL_2 -> ( ARGUMENTS_OPC ) CALL_2 | Ɛ
    private Nodo CALL_2(){
        ArrayList<Object> aux = new ArrayList<>();

        if(preanalisis.getTipo() == TipoToken.LEFT_PAREN){
            aux.add(preanalisis);match(TipoToken.LEFT_PAREN);
            Nodo naux = ARGUMENTS_OPC();    aux.add(naux);
            aux.add(preanalisis);match(TipoToken.RIGHT_PAREN);
            naux = CALL_2();                aux.add(naux);
        }
        return new Nodo(aux, "CALL_2");
    }

    // PRIMARY -> true | false | null | number | string | id | ( EXPRESSION )
    private Nodo PRIMARY(){
        ArrayList<Object> aux = new ArrayList<>();

        if(preanalisis.getTipo() == TipoToken.TRUE){
            aux.add(preanalisis);   match(TipoToken.TRUE);
            return new Nodo(aux, "PRIMARY");
        } else if(preanalisis.getTipo() == TipoToken.FALSE){
            aux.add(preanalisis);match(TipoToken.FALSE);
            return new Nodo(aux, "PRIMARY");
        } else if(preanalisis.getTipo() == TipoToken.NULL){
            aux.add(preanalisis);match(TipoToken.NULL);
            return new Nodo(aux, "PRIMARY");
        } else if(preanalisis.getTipo() == TipoToken.NUMBER){
            aux.add(preanalisis);match(TipoToken.NUMBER);
            return new Nodo(aux, "PRIMARY");
        } else if(preanalisis.getTipo() == TipoToken.STRING){
            aux.add(preanalisis);match(TipoToken.STRING);
            return new Nodo(aux, "PRIMARY");
        } else if(preanalisis.getTipo() == TipoToken.IDENTIFIER){
            aux.add(preanalisis);match(TipoToken.IDENTIFIER);
            return new Nodo(aux, "PRIMARY");
        } else if(preanalisis.getTipo() == TipoToken.LEFT_PAREN){
            aux.add(preanalisis);match(TipoToken.LEFT_PAREN);
            Nodo naux = EXPRESSION();aux.add(naux);
            aux.add(preanalisis);match(TipoToken.RIGHT_PAREN);
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

        aux.add(preanalisis);match(TipoToken.IDENTIFIER);
        aux.add(preanalisis);match(TipoToken.LEFT_PAREN);
        Nodo naux = PARAMETERS_OPC();aux.add(naux);
        aux.add(preanalisis);match(TipoToken.RIGHT_PAREN);
        naux = BLOCK();aux.add(naux);
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
            Nodo naux = PARAMETERS();aux.add(naux);
        }
        return new Nodo(aux, "PARAMETERS_OPC");
    }

    // PARAMETERS -> id PARAMETERS_2
    private Nodo PARAMETERS(){
        ArrayList<Object> aux = new ArrayList<>();

        aux.add(preanalisis);match(TipoToken.IDENTIFIER);
        Nodo naux = PARAMETERS_2();aux.add(naux);
        return new Nodo(aux, "PARAMETERS");
    }

    // PARAMETERS_2 -> , id PARAMETERS_2 | Ɛ
    private Nodo PARAMETERS_2(){
        ArrayList<Object> aux = new ArrayList<>();

        if(preanalisis.getTipo() == TipoToken.COMMA){
            aux.add(preanalisis);match(TipoToken.COMMA);
            aux.add(preanalisis);match(TipoToken.IDENTIFIER);
            Nodo naux = PARAMETERS_2();aux.add(naux);
        }
        return new Nodo(aux, "PARAMETERS_2");
    }

    // ARGUMENTS_OPC -> EXPRESSION ARGUMENTS | Ɛ
    private Nodo ARGUMENTS_OPC(){
        ArrayList<Object> aux = new ArrayList<>();

        TipoToken pre = preanalisis.getTipo();
        if(pre == TipoToken.BANG || pre == TipoToken.MINUS
        || pre == TipoToken.TRUE || pre == TipoToken.FALSE || pre == TipoToken.NULL || pre == TipoToken.NUMBER || pre == TipoToken.STRING || pre == TipoToken.IDENTIFIER || pre == TipoToken.LEFT_PAREN){
            Nodo naux = EXPRESSION();aux.add(naux);
            naux = ARGUMENTS();aux.add(naux);
        }
        return new Nodo(aux, "ARGUMENTS_OPC");
    }

    // ARGUMENTS -> , EXPRESSION ARGUMENTS | Ɛ
    private Nodo ARGUMENTS(){
        ArrayList<Object> aux = new ArrayList<>();

        if(preanalisis.getTipo() == TipoToken.COMMA){
            aux.add(preanalisis);match(TipoToken.COMMA);
            Nodo naux = EXPRESSION();aux.add(naux);
            naux = ARGUMENTS();aux.add(naux);
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

}
