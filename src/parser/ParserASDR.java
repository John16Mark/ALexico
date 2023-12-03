package parser;

import java.util.List;

import def.Main;
import token.TipoToken;
import token.Token;

public class ParserASDR implements Parser{

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;

    public ParserASDR(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }

    @Override
    public boolean parse() {
        // Inicia el analizador léxico
        PROGRAM();

        if(preanalisis.getTipo() == TipoToken.EOF && !hayErrores){
            System.out.println("\033[94mAnálisis Sintáctico Correcto\033[0m");
            return  true;
        }else {
            System.out.println("\033[91mSe encontraron errores\033[0m");
        }
        return false;
    }

    // PROGRAM -> DECLARATION
    private void PROGRAM(){
        DECLARATION();
    }

    /*************************************************************************************************
                                                DECLARACIONES
    *************************************************************************************************/

    // DECLARATION -> FUN_DECL DECLARATION | VAR_DECL DECLARATION | STATEMENT DECLARATION | Ɛ
    private void DECLARATION(){
        if(hayErrores)
            return;

        switch (preanalisis.getTipo()) {
            case TipoToken.FUN:
                FUN_DECL();
                DECLARATION();
                break;
            case TipoToken.VAR:
                VAR_DECL();
                DECLARATION();
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
                STATEMENT();
                DECLARATION();
                break;
        
            default:
                break;
        }
    }

    // FUN_DECL -> fun FUNCTION
    private void FUN_DECL(){
        if(hayErrores)
            return;

        match(TipoToken.FUN);
        FUNCTION();
    }

    // VAR_DECL -> var id VAR_INT ;
    private void VAR_DECL(){
        if(hayErrores)
            return;

        match(TipoToken.VAR);
        match(TipoToken.IDENTIFIER);
        VAR_INIT();
        match(TipoToken.SEMICOLON);
    }

    // VAR_INIT -> = EXPRESSION
    private void VAR_INIT(){
        if(hayErrores)
            return;

        if(preanalisis. getTipo() == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            EXPRESSION();
        }
    }

    /*************************************************************************************************
                                                SENTENCIAS
    *************************************************************************************************/

    // STATEMENT -> EXPR_STMT | FOR_STMT | IF_STMT | PRINT_STMT | RETURN_STMT | WHILE_STMT | BLOCK
    private void STATEMENT(){
        if(hayErrores)
            return;

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
                EXPR_STMT();
                break;
            case TipoToken.FOR:
                FOR_STMT();
                break;
            case TipoToken.IF:
                IF_STMT();
                break;
            case TipoToken.PRINT:
                PRINT_STMT();
                break;
            case TipoToken.RETURN:
                RETURN_STMT();
                break;
            case TipoToken.WHILE:
                WHILE_STMT();
                break;
            case TipoToken.LEFT_BRACE:
                BLOCK();
                break;
            default:
                error(preanalisis.getLinea(), "Se esperaba inicio de sentencia");
                break;
        }
    }

    // EXPR_STMT -> EXPRESSION;
    private void EXPR_STMT(){
        if(hayErrores)
            return;
        
        EXPRESSION();
        match(TipoToken.SEMICOLON);
    }

    // FOR_STMT -> for ( FOR_STMT_1 FOR_STMT_2 FOR_STMT_3 ) STATEMENT
    private void FOR_STMT(){
        if(hayErrores)
            return;

        match(TipoToken.FOR);
        match(TipoToken.LEFT_PAREN);
        FOR_STMT_1();
        FOR_STMT_2();
        FOR_STMT_3();
        match(TipoToken.RIGHT_PAREN);
        STATEMENT();
    }

    // FOR_STMT_1 -> VAR_DECL | EXPR_STMT | ;
    private void FOR_STMT_1(){
        if(hayErrores)
            return;

        switch (preanalisis.getTipo()) {
            case TipoToken.VAR:
                VAR_DECL();
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
                EXPR_STMT();
                break;
            case TipoToken.SEMICOLON:
                match(TipoToken.SEMICOLON);
                break;
            default:
                error(preanalisis.getLinea(), "Se esperaba inicio de sentencia");
                break;
        }
    }

    // FOR_STMT_2 -> EXPRESSION ; | ;
    private void FOR_STMT_2(){
        if(hayErrores)
            return;

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
                EXPRESSION();
                match(TipoToken.SEMICOLON);
                break;
            case TipoToken.SEMICOLON:
                match(TipoToken.SEMICOLON);
                break;
            default:
                error(preanalisis.getLinea(), "Se esperaba inicio de sentencia");
                break;
        }
    }

    // FOR_STMT_3 -> EXPRESSION | Ɛ
    private void FOR_STMT_3(){
        if(hayErrores)
            return;

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
                EXPRESSION();
                break;
            default:
                break;
        }
    }

    // IF_STMT -> if ( EXPRESSION ) STATEMENT ELSE_STMT
    private void IF_STMT(){
        if(hayErrores)
            return;

        match(TipoToken.IF);
        match(TipoToken.LEFT_PAREN);
        EXPRESSION();
        match(TipoToken.RIGHT_PAREN);
        STATEMENT();
        ELSE_STMT();
    }

    // ELSE_STMT -> else STATEMENT
    private void ELSE_STMT(){
        if(hayErrores)
            return;
        
        if(preanalisis.getTipo() == TipoToken.ELSE){
            match(TipoToken.ELSE);
            STATEMENT();
        }
    }

    // PRINT_STMT -> print EXPRESSION ;
    private void PRINT_STMT(){
        if(hayErrores)
            return;

        match(TipoToken.PRINT);
        EXPRESSION();
        match(TipoToken.SEMICOLON);
    }

    // RETURN_STMT -> return RETURN_EXP_OPC ;
    private void RETURN_STMT(){
        if(hayErrores)
            return;

        match(TipoToken.RETURN);
        RETURN_EXP_OPC();
        match(TipoToken.SEMICOLON);
    }

    // RETURN_EXP_OPC -> EXPRESSION | Ɛ
    private void RETURN_EXP_OPC(){
        if(hayErrores)
            return;

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
                EXPRESSION();
                break;
            default:
                break;
        }
    }

    // WHILE_STMT -> while ( EXPRESSION ) STATEMENT
    private void WHILE_STMT(){
        if(hayErrores)
            return;

        match(TipoToken.WHILE);
        match(TipoToken.LEFT_PAREN);
        EXPRESSION();
        match(TipoToken.RIGHT_PAREN);
        STATEMENT();
    }

    // BLOCK -> { DECLARATION }
    private void BLOCK(){
        if(hayErrores)
            return;

        match(TipoToken.LEFT_BRACE);
        DECLARATION();
        match(TipoToken.RIGHT_BRACE);
    }

    /*************************************************************************************************
                                                EXPRESIONES
    *************************************************************************************************/

    // EXPRESSION -> ASSIGNMENT
    private void EXPRESSION(){
        if(hayErrores)
            return;

        ASSIGNMENT();
    }

    // ASIGNMENT -> LOGIC_OR ASSIGNMENT_OPC
    private void ASSIGNMENT(){
        if(hayErrores)
            return;

        LOGIC_OR();
        ASSIGNMENT_OPC();
    }

    // ASSIGNMENT_OPC -> = EXPRESSION | Ɛ
    private void ASSIGNMENT_OPC(){
        if(hayErrores)
            return;

        if(preanalisis.getTipo() == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            EXPRESSION();
        }
    }

    // LOGIC_OR -> LOGIC_AND lOGIC_OR_2
    private void LOGIC_OR(){
        if(hayErrores)
            return;

        LOGIC_AND();
        LOGIC_OR_2();
    }

    // LOGIC_OR_2 -> or LOGIC_AND LOGIC_OR_2 | Ɛ
    private void LOGIC_OR_2(){
        if(hayErrores)
            return;

        if(preanalisis.getTipo() == TipoToken.OR){
            match(TipoToken.OR);
            LOGIC_AND();
            LOGIC_OR_2();
        }
    }

    // LOGIC_AND -> EQUALITY LOGIC_AND_2
    private void LOGIC_AND(){
        if(hayErrores)
            return;

        EQUALITY();
        LOGIC_AND_2();
    }

    // LOGIC_AND_2 -> and EQUALITY LOGIC_AND_2 | Ɛ
    private void LOGIC_AND_2(){
        if(hayErrores)
            return;

        if(preanalisis.getTipo() == TipoToken.AND){
            match(TipoToken.AND);
            EQUALITY();
            LOGIC_AND_2();
        }
    }

    // EQUALITY -> COMPARISON EQUALITY_2
    private void EQUALITY(){
        if(hayErrores)
            return;
        
        COMPARISON();
        EQUALITY_2();
    }

    // EQUALITY_2 -> != COMPARISON EQUALITY_2 | == COMPARISON EQUALITY_2 | Ɛ
    private void EQUALITY_2(){
        if(hayErrores)
            return;

        if(preanalisis.getTipo() == TipoToken.BANG_EQUAL){
            match(TipoToken.BANG_EQUAL);
            COMPARISON();
            EQUALITY_2();
        } else if(preanalisis.getTipo() == TipoToken.EQUAL_EQUAL) {
            match(TipoToken.EQUAL_EQUAL);
            COMPARISON();
            EQUALITY_2();
        }
    }

    // COMPARISON -> TERM COMPARISON_2
    private void COMPARISON(){
        if(hayErrores)
            return;

        TERM();
        COMPARISON_2();
    }

    // COMPARISON_2 -> > TERM COMPARISON_2 | >= TERM COMPARISON_2 | < TERM COMPARISON_2 | <= TERM COMPARISON_2 | Ɛ
    private void COMPARISON_2(){
        if(hayErrores)
            return;

        if(preanalisis.getTipo() == TipoToken.GREATER){
            match(TipoToken.GREATER);
            TERM();
            COMPARISON_2();
        } else if(preanalisis.getTipo() == TipoToken.GREATER_EQUAL){
            match(TipoToken.GREATER_EQUAL);
            TERM();
            COMPARISON_2();
        } else if(preanalisis.getTipo() == TipoToken.LESS){
            match(TipoToken.LESS);
            TERM();
            COMPARISON_2();
        } else if(preanalisis.getTipo() == TipoToken.LESS_EQUAL){
            match(TipoToken.LESS_EQUAL);
            TERM();
            COMPARISON_2();
        }
    }

    // TERM -> FACTOR TERM_2
    private void TERM(){
        if(hayErrores)
            return;
        
        FACTOR();
        TERM_2();
    }

    // TERM_2 -> - FACTOR TERM_2 | + FACTOR TERM_2 | Ɛ
    private void TERM_2(){
        if(hayErrores)
            return;
        
        if(preanalisis.getTipo() == TipoToken.MINUS){
            match(TipoToken.MINUS);
            FACTOR();
            TERM_2();
        } else if(preanalisis.getTipo() == TipoToken.PLUS){
            match(TipoToken.PLUS);
            FACTOR();
            TERM_2();
        }
    }

    // FACTOR -> UNARY FACTOR_2
    private void FACTOR(){
        if(hayErrores)
            return;

        UNARY();
        FACTOR_2();
    }

    // FACTOR_2 -> / UNARY FACTOR_2 | * UNARY FACTOR_2 | Ɛ
    private void FACTOR_2(){
        if(hayErrores)
            return;

        if(preanalisis.getTipo() == TipoToken.SLASH){
            match(TipoToken.SLASH);
            UNARY();
            FACTOR_2();
        } else if(preanalisis.getTipo() == TipoToken.STAR){
            match(TipoToken.STAR);
            UNARY();
            FACTOR_2();
        }
    }

    // UNARY -> ! UNARY | - UNARY | CALL
    private void UNARY(){
        if(hayErrores)
            return;
        
        switch (preanalisis.getTipo()) {
            case TipoToken.BANG:
                match(TipoToken.BANG);
                UNARY();
                break;
            case TipoToken.MINUS:
                match(TipoToken.MINUS);
                UNARY();
                break;
            case TipoToken.TRUE:
            case TipoToken.FALSE:
            case TipoToken.NULL:
            case TipoToken.NUMBER:
            case TipoToken.STRING:
            case TipoToken.IDENTIFIER:
            case TipoToken.LEFT_PAREN:
                CALL();
                break;
            default:
                error(preanalisis.getLinea(), "Se esperaba '!', '-', o PRIMARY");
                break;
        }
    }

    // CALL -> PRIMARY CALL_2
    private void CALL(){
        if(hayErrores)
            return;
        
        PRIMARY();
        CALL_2();
    }

    // CALL_2 -> ( ARGUMENTS_OPC ) CALL_2 | Ɛ
    private void CALL_2(){
        if(hayErrores)
            return;
        
        if(preanalisis.getTipo() == TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);
            ARGUMENTS_OPC();
            match(TipoToken.RIGHT_PAREN);
            CALL_2();
        }
    }

    // PRIMARY -> true | false | null | number | string | id | ( EXPRESSION )
    private void PRIMARY(){
        if(hayErrores)
            return;
        
        if(preanalisis.getTipo() == TipoToken.TRUE){
            match(TipoToken.TRUE);
        } else if(preanalisis.getTipo() == TipoToken.FALSE){
            match(TipoToken.FALSE);
        } else if(preanalisis.getTipo() == TipoToken.NULL){
            match(TipoToken.NULL);
        } else if(preanalisis.getTipo() == TipoToken.NUMBER){
            match(TipoToken.NUMBER);
        } else if(preanalisis.getTipo() == TipoToken.STRING){
            match(TipoToken.STRING);
        } else if(preanalisis.getTipo() == TipoToken.IDENTIFIER){
            match(TipoToken.IDENTIFIER);
        } else if(preanalisis.getTipo() == TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);
            EXPRESSION();
            match(TipoToken.RIGHT_PAREN);
        } else {
            error(preanalisis.getLinea(), "Se esperaba PRIMARY");
        }
    }

    /*************************************************************************************************
                                                    OTRAS
    *************************************************************************************************/

    // FUNCTION ->  id ( PARAMETERS_OPC ) BLOCK
    private void FUNCTION(){
        if(hayErrores)
            return;

        match(TipoToken.IDENTIFIER);
        match(TipoToken.LEFT_PAREN);
        PARAMETERS_OPC();
        match(TipoToken.RIGHT_PAREN);
        BLOCK();
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
    private void PARAMETERS_OPC(){
        if(hayErrores)
            return;

        if(preanalisis.getTipo() == TipoToken.IDENTIFIER){
            PARAMETERS();
        }
    }

    // PARAMETERS -> id PARAMETERS_2
    private void PARAMETERS(){
        if(hayErrores)
            return;

        match(TipoToken.IDENTIFIER);
        PARAMETERS_2();
    }

    // PARAMETERS_2 -> , id PARAMETERS_2 | Ɛ
    private void PARAMETERS_2(){
        if(hayErrores)
            return;

        if(preanalisis.getTipo() == TipoToken.COMMA){
            match(TipoToken.COMMA);
            match(TipoToken.IDENTIFIER);
            PARAMETERS_2();
        }
    }

    // ARGUMENTS_OPC -> EXPRESSION ARGUMENTS | Ɛ
    private void ARGUMENTS_OPC(){
        if(hayErrores)
            return;

        TipoToken pre = preanalisis.getTipo();
        if(pre == TipoToken.BANG || pre == TipoToken.MINUS
        || pre == TipoToken.TRUE || pre == TipoToken.FALSE || pre == TipoToken.NULL || pre == TipoToken.NUMBER || pre == TipoToken.STRING || pre == TipoToken.IDENTIFIER || pre == TipoToken.LEFT_PAREN){
            EXPRESSION();
            ARGUMENTS();
        }
    }

    // ARGUMENTS -> , EXPRESSION ARGUMENTS | Ɛ
    private void ARGUMENTS(){
        if(hayErrores)
            return;

        if(preanalisis.getTipo() == TipoToken.COMMA){
            match(TipoToken.COMMA);
            EXPRESSION();
            ARGUMENTS();
        }
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
