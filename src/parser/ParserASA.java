package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import def.Main;
import parser.expression.*;
import parser.statement.*;
import token.TipoToken;
import token.Token;

public class ParserASA implements Parser{

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;

    public ParserASA(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }

    @Override
    public boolean parse() {
        // Inicia el analizador léxico
        Program p = PROGRAM();
        ArrayList<Boolean> lista = new ArrayList<>();
        if(preanalisis.getTipo() == TipoToken.EOF && !hayErrores){
            if(def.Main.debug){
                System.out.print("\n");
            }
            System.out.println("\033[94mAnálisis Sintáctico Correcto\033[0m");
            if(def.Main.debug){
                System.out.println("\n\033[92m  Árbol de Sintaxis Abstracta\033[0m");
                p.imprimir(0, lista);
            }
            return  true;
        }else {
            System.out.println("\033[91mSe encontraron errores\033[0m");
        }
        return false;
    }

    // PROGRAM -> DECLARATION
    private Program PROGRAM(){
        List<Statement> stmts = DECLARATION(new ArrayList<Statement>());
        return new Program(stmts);
    }

    /*************************************************************************************************
                                                DECLARACIONES
    *************************************************************************************************/

    // DECLARATION -> FUN_DECL DECLARATION | VAR_DECL DECLARATION | STATEMENT DECLARATION | Ɛ
    private List<Statement> DECLARATION(List<Statement> lista){
        switch (preanalisis.getTipo()) {
            case TipoToken.FUN:
                Statement stmt = FUN_DECL();
                lista.add(stmt);
                return DECLARATION(lista);
            case TipoToken.VAR:
                stmt = VAR_DECL();
                lista.add(stmt);
                return DECLARATION(lista);
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
                stmt = STATEMENT();
                lista.add(stmt);
                return DECLARATION(lista);
            default:
                break;
        }
        return lista;
    }

    // FUN_DECL -> fun FUNCTION
    private Statement FUN_DECL(){
        match(TipoToken.FUN);
        List<Object> lista = FUNCTION();
        return new StmtFunction((Token)lista.get(0), (List<Token>)lista.get(1), (StmtBlock)lista.get(2));
    }

    // VAR_DECL -> var id VAR_INT ;
    private Statement VAR_DECL(){
        match(TipoToken.VAR);
        match(TipoToken.IDENTIFIER);
        Token id = previous();
        Expression expr = VAR_INIT();
        match(TipoToken.SEMICOLON);
        StmtVar stmtv = new StmtVar(id, expr);
        return stmtv;
    }

    // VAR_INIT -> = EXPRESSION
    private Expression VAR_INIT(){
        if(preanalisis. getTipo() == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            return EXPRESSION();
        }
        return null;
    }

    /*************************************************************************************************
                                                SENTENCIAS
    *************************************************************************************************/

    // STATEMENT -> EXPR_STMT | FOR_STMT | IF_STMT | PRINT_STMT | RETURN_STMT | WHILE_STMT | BLOCK
    private Statement STATEMENT(){
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
                Statement stmt = EXPR_STMT();
                return stmt;
            case TipoToken.FOR:
                stmt = FOR_STMT();
                return stmt;
            case TipoToken.IF:
                stmt = IF_STMT();
                return stmt;
            case TipoToken.PRINT:
                stmt = PRINT_STMT();
                return stmt;
            case TipoToken.RETURN:
                stmt = RETURN_STMT();
                return stmt;
            case TipoToken.WHILE:
                stmt = WHILE_STMT();
                return stmt;
            case TipoToken.LEFT_BRACE:
                stmt = BLOCK();
                return stmt;
            default:
                error(preanalisis.getLinea(), "Se esperaba inicio de sentencia");
                return null;
        }
    }

    // EXPR_STMT -> EXPRESSION;
    private Statement EXPR_STMT(){
        Expression expr = EXPRESSION();
        match(TipoToken.SEMICOLON);
        return new StmtExpression(expr);
    }

    // FOR_STMT -> for ( FOR_STMT_1 FOR_STMT_2 FOR_STMT_3 ) STATEMENT
    private Statement FOR_STMT(){
        match(TipoToken.FOR);
        match(TipoToken.LEFT_PAREN);
        Statement decl = FOR_STMT_1();
        Expression condition = FOR_STMT_2();
        Expression assign = FOR_STMT_3();
        match(TipoToken.RIGHT_PAREN);
        Statement body = STATEMENT();

        if(condition == null){
            condition = new ExprLiteral(true);
        }

        if(assign != null){
            body = new StmtBlock(Arrays.asList(body, new StmtExpression(assign)));
        }

        StmtLoop stmtloop = new StmtLoop(condition, body);

        if(decl != null){
            StmtBlock block = new StmtBlock(Arrays.asList(decl, stmtloop));
            return block;
        } else {
            return stmtloop;
        }

        //return new StmtFor(decl, condition, assign, body);
    }

    // FOR_STMT_1 -> VAR_DECL | EXPR_STMT | ;
    private Statement FOR_STMT_1(){
        Statement stmt;
        switch (preanalisis.getTipo()) {
            case TipoToken.VAR:
                stmt = VAR_DECL();
                return stmt;
            case TipoToken.BANG:
            case TipoToken.MINUS:
            case TipoToken.TRUE:
            case TipoToken.FALSE:
            case TipoToken.NULL:
            case TipoToken.NUMBER:
            case TipoToken.STRING:
            case TipoToken.IDENTIFIER:
            case TipoToken.LEFT_PAREN:
                stmt = EXPR_STMT();
                return stmt;
            case TipoToken.SEMICOLON:
                match(TipoToken.SEMICOLON);
                return null;
            default:
                error(preanalisis.getLinea(), "Se esperaba inicio de sentencia");
                return null;
        }
    }

    // FOR_STMT_2 -> EXPRESSION ; | ;
    private Expression FOR_STMT_2(){
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
                Expression expr = EXPRESSION();
                match(TipoToken.SEMICOLON);
                return expr;
            case TipoToken.SEMICOLON:
                match(TipoToken.SEMICOLON);
                return null;
            default:
                error(preanalisis.getLinea(), "Se esperaba inicio de sentencia");
                return null;
        }
    }

    // FOR_STMT_3 -> EXPRESSION | Ɛ
    private Expression FOR_STMT_3(){
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
                Expression expr = EXPRESSION();
                return expr;
            default:
                break;
        }
        return null;
    }

    // IF_STMT -> if ( EXPRESSION ) STATEMENT ELSE_STMT
    private Statement IF_STMT(){
        match(TipoToken.IF);
        match(TipoToken.LEFT_PAREN);
        Expression expr2 = EXPRESSION();
        match(TipoToken.RIGHT_PAREN);
        Statement stat1 = STATEMENT();
        Statement stat2 = ELSE_STMT(); 
        return new StmtIf(expr2, stat1, stat2);
    }

    // ELSE_STMT -> else STATEMENT | Ɛ
    private Statement ELSE_STMT(){
        if(preanalisis.getTipo() == TipoToken.ELSE){
            match(TipoToken.ELSE);
            Statement stmt = STATEMENT();
            return stmt;
        }
        return null;
    }

    // PRINT_STMT -> print EXPRESSION ;
    private Statement PRINT_STMT(){
        match(TipoToken.PRINT);
        Expression expr = EXPRESSION();
        match(TipoToken.SEMICOLON);
        return new StmtPrint(expr);
    }

    // RETURN_STMT -> return RETURN_EXP_OPC ;
    private Statement RETURN_STMT(){
        match(TipoToken.RETURN);
        Expression expr = RETURN_EXP_OPC();
        match(TipoToken.SEMICOLON);
        return new StmtReturn(expr);
    }

    // RETURN_EXP_OPC -> EXPRESSION | Ɛ
    private Expression RETURN_EXP_OPC(){
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
                Expression expr = EXPRESSION();
                return expr;
            default:
                break;
        }
        return null;
    }

    // WHILE_STMT -> while ( EXPRESSION ) STATEMENT
    private Statement WHILE_STMT(){
        match(TipoToken.WHILE);
        match(TipoToken.LEFT_PAREN);
        Expression expr = EXPRESSION();
        match(TipoToken.RIGHT_PAREN);
        Statement body = STATEMENT();
        return new StmtLoop(expr, body);
    }

    // BLOCK -> { DECLARATION }
    private StmtBlock BLOCK(){
        match(TipoToken.LEFT_BRACE);
        List<Statement> statements = DECLARATION(new ArrayList<Statement>());
        match(TipoToken.RIGHT_BRACE);
        return new StmtBlock(statements);
    }

    /*************************************************************************************************
                                                EXPRESIONES
    *************************************************************************************************/

    // EXPRESSION -> ASSIGNMENT
    private Expression EXPRESSION(){
        Expression expr = ASSIGNMENT();
        return expr;
    }

    // ASIGNMENT -> LOGIC_OR ASSIGNMENT_OPC
    private Expression ASSIGNMENT(){
        Expression expr = LOGIC_OR();
        expr = ASSIGNMENT_OPC(expr);
        return expr;
    }

    // ASSIGNMENT_OPC -> = EXPRESSION | Ɛ
    private Expression ASSIGNMENT_OPC(Expression expr){
        if(preanalisis.getTipo() == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            Token operador = previous();
            Expression expr2 = EXPRESSION();
            if(expr instanceof ExprVariable){
                ExprAssign expa = new ExprAssign(((ExprVariable)expr).getName(), expr2);
                return expa;
            } else {
                error(operador.getLinea(), "Asignación inválida");
            }
        }
        return expr;
    }

    // LOGIC_OR -> LOGIC_AND lOGIC_OR_2
    private Expression LOGIC_OR(){
        Expression expr = LOGIC_AND();
        expr = LOGIC_OR_2(expr);
        return expr;
    }

    // LOGIC_OR_2 -> or LOGIC_AND LOGIC_OR_2 | Ɛ
    private Expression LOGIC_OR_2(Expression expr){
        if(preanalisis.getTipo() == TipoToken.OR){
            match(TipoToken.OR);
            Token operador = previous();
            Expression expr2 = LOGIC_AND();
            ExprLogical expl = new ExprLogical(expr, operador, expr2);
            return LOGIC_OR_2(expl);
        }
        return expr;
    }

    // LOGIC_AND -> EQUALITY LOGIC_AND_2
    private Expression LOGIC_AND(){
        Expression expr = EQUALITY();
        expr = LOGIC_AND_2(expr);
        return expr;
    }

    // LOGIC_AND_2 -> and EQUALITY LOGIC_AND_2 | Ɛ
    private Expression LOGIC_AND_2(Expression expr){
        if(preanalisis.getTipo() == TipoToken.AND){
            match(TipoToken.AND);
            Token operador = previous();
            Expression expr2 = EQUALITY();
            ExprLogical expl = new ExprLogical(expr, operador, expr2);
            return LOGIC_AND_2(expl);
        }
        return expr;
    }

    // EQUALITY -> COMPARISON EQUALITY_2
    private Expression EQUALITY(){
        Expression expr = COMPARISON();
        expr = EQUALITY_2(expr);
        return expr;
    }

    // EQUALITY_2 -> != COMPARISON EQUALITY_2 | == COMPARISON EQUALITY_2 | Ɛ
    private Expression EQUALITY_2(Expression expr){
        if(preanalisis.getTipo() == TipoToken.BANG_EQUAL){
            match(TipoToken.BANG_EQUAL);
            Token operador = previous();
            Expression expr2 = COMPARISON();
            ExprLogical expl = new ExprLogical(expr, operador, expr2);
            return EQUALITY_2(expl);
        } else if(preanalisis.getTipo() == TipoToken.EQUAL_EQUAL) {
            match(TipoToken.EQUAL_EQUAL);
            Token operador = previous();
            Expression expr2 = COMPARISON();
            ExprLogical expl = new ExprLogical(expr, operador, expr2);
            return EQUALITY_2(expl);
        }
        return expr;
    }

    // COMPARISON -> TERM COMPARISON_2
    private Expression COMPARISON(){
        Expression expr = TERM();
        expr = COMPARISON_2(expr);
        return expr;
    }

    // COMPARISON_2 -> > TERM COMPARISON_2 | >= TERM COMPARISON_2 | < TERM COMPARISON_2 | <= TERM COMPARISON_2 | Ɛ
    private Expression COMPARISON_2(Expression expr){
        if(preanalisis.getTipo() == TipoToken.GREATER){
            match(TipoToken.GREATER);
            Token operador = previous();
            Expression expr2 = TERM();
            ExprLogical expl = new ExprLogical(expr, operador, expr2);
            return COMPARISON_2(expl);
        } else if(preanalisis.getTipo() == TipoToken.GREATER_EQUAL){
            match(TipoToken.GREATER_EQUAL);
            Token operador = previous();
            Expression expr2 = TERM();
            ExprLogical expl = new ExprLogical(expr, operador, expr2);
            return COMPARISON_2(expl);
        } else if(preanalisis.getTipo() == TipoToken.LESS){
            match(TipoToken.LESS);
            Token operador = previous();
            Expression expr2 = TERM();
            ExprLogical expl = new ExprLogical(expr, operador, expr2);
            return COMPARISON_2(expl);
        } else if(preanalisis.getTipo() == TipoToken.LESS_EQUAL){
            match(TipoToken.LESS_EQUAL);
            Token operador = previous();
            Expression expr2 = TERM();
            ExprLogical expl = new ExprLogical(expr, operador, expr2);
            return COMPARISON_2(expl);
        }
        return expr;
    }

    // TERM -> FACTOR TERM_2
    private Expression TERM(){
        Expression expr = FACTOR();
        expr = TERM_2(expr);
        return expr;
    }

    // TERM_2 -> - FACTOR TERM_2 | + FACTOR TERM_2 | Ɛ
    private Expression TERM_2(Expression expr){
        if(preanalisis.getTipo() == TipoToken.MINUS){
            match(TipoToken.MINUS);
            Token operador = previous();
            Expression expr2 = FACTOR();
            ExprBinary expb = new ExprBinary(expr, operador, expr2);
            return TERM_2(expb);
        } else if(preanalisis.getTipo() == TipoToken.PLUS){
            match(TipoToken.PLUS);
            Token operador = previous();
            Expression expr2 = FACTOR();
            ExprBinary expb = new ExprBinary(expr, operador, expr2);
            return TERM_2(expb);
        }
        return expr;
    }

    // FACTOR -> UNARY FACTOR_2
    private Expression FACTOR(){
        Expression expr = UNARY();
        expr = FACTOR_2(expr);
        return expr;
    }

    // FACTOR_2 -> / UNARY FACTOR_2 | * UNARY FACTOR_2 | Ɛ
    private Expression FACTOR_2(Expression expr){
        if(preanalisis.getTipo() == TipoToken.SLASH){
            match(TipoToken.SLASH);
            Token operador = previous();
            Expression expr2 = UNARY();
            ExprBinary expb = new ExprBinary(expr, operador, expr2);
            return FACTOR_2(expb);
        } else if(preanalisis.getTipo() == TipoToken.STAR){
            match(TipoToken.STAR);
            Token operador = previous();
            Expression expr2 = UNARY();
            ExprBinary expb = new ExprBinary(expr, operador, expr2);
            return FACTOR_2(expb);
        }
        return expr;
    }

    // UNARY -> ! UNARY | - UNARY | CALL
    private Expression UNARY(){
        switch (preanalisis.getTipo()) {
            case TipoToken.BANG:
                match(TipoToken.BANG);
                Token operador = previous();
                Expression expr = UNARY();
                return new ExprUnary(operador, expr);
            case TipoToken.MINUS:
                match(TipoToken.MINUS);
                operador = previous();
                expr = UNARY();
                return new ExprUnary(operador, expr);
            case TipoToken.TRUE:
            case TipoToken.FALSE:
            case TipoToken.NULL:
            case TipoToken.NUMBER:
            case TipoToken.STRING:
            case TipoToken.IDENTIFIER:
            case TipoToken.LEFT_PAREN:
                return CALL();
            default:
                error(preanalisis.getLinea(), "Se esperaba '!', '-', o PRIMARY");
                break;
        }
        return null;
    }

    // CALL -> PRIMARY CALL_2
    private Expression CALL(){
        Expression expr = PRIMARY();
        expr = CALL_2(expr);
        return expr;
    }

    // CALL_2 -> ( ARGUMENTS_OPC ) CALL_2 | Ɛ
    private Expression CALL_2(Expression expr){
        if(preanalisis.getTipo() == TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);
            List<Expression> lstArguments = ARGUMENTS_OPC(new ArrayList<Expression>());
            match(TipoToken.RIGHT_PAREN);
            ExprCallFunction ecf = new ExprCallFunction(expr, lstArguments);
            return CALL_2(ecf);
        }
        return expr;
    }

    // PRIMARY -> true | false | null | number | string | id | ( EXPRESSION )
    private Expression PRIMARY(){
        if(preanalisis.getTipo() == TipoToken.TRUE){
            match(TipoToken.TRUE);
            return new ExprLiteral(true);
        } else if(preanalisis.getTipo() == TipoToken.FALSE){
            match(TipoToken.FALSE);
            return new ExprLiteral(false);
        } else if(preanalisis.getTipo() == TipoToken.NULL){
            match(TipoToken.NULL);
            return new ExprLiteral(null);
        } else if(preanalisis.getTipo() == TipoToken.NUMBER){
            match(TipoToken.NUMBER);
            Token numero = previous();
            return new ExprLiteral(numero.getLiteral());
        } else if(preanalisis.getTipo() == TipoToken.STRING){
            match(TipoToken.STRING);
            Token cadena = previous();
            return new ExprLiteral(cadena.getLiteral());
        } else if(preanalisis.getTipo() == TipoToken.IDENTIFIER){
            match(TipoToken.IDENTIFIER);
            Token id = previous();
            return new ExprVariable(id);
        } else if(preanalisis.getTipo() == TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);
            Expression expr = EXPRESSION();
            // Tiene que ser cachado aquello que retorna
            match(TipoToken.RIGHT_PAREN);
            return new ExprGrouping(expr);
        } else {
            error(preanalisis.getLinea(), "Se esperaba PRIMARY");
        }
        return null;
    }

    /*************************************************************************************************
                                                    OTRAS
    *************************************************************************************************/

    // FUNCTION ->  id ( PARAMETERS_OPC ) BLOCK
    private List<Object> FUNCTION(){
        match(TipoToken.IDENTIFIER);
        Token id = previous();
        match(TipoToken.LEFT_PAREN);
        List<Token> params = PARAMETERS_OPC(new ArrayList<Token>());
        match(TipoToken.RIGHT_PAREN);
        StmtBlock body = BLOCK();
        List<Object> lista = Arrays.asList(id, params, body);
        return lista;
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
    private List<Token> PARAMETERS_OPC(List<Token> lista){
        if(preanalisis.getTipo() == TipoToken.IDENTIFIER){
            /*List<Token> param = */return PARAMETERS(lista);
            /*for (Token token : param) {
                lista.add(token);
            }*/
        }
        return lista;
    }

    // PARAMETERS -> id PARAMETERS_2
    private List<Token> PARAMETERS(List<Token> lista){
        match(TipoToken.IDENTIFIER);
        Token id = previous();
        lista.add(id);
        return PARAMETERS_2(lista);
    }

    // PARAMETERS_2 -> , id PARAMETERS_2 | Ɛ
    private List<Token> PARAMETERS_2(List<Token> lista){
        if(preanalisis.getTipo() == TipoToken.COMMA){
            match(TipoToken.COMMA);
            match(TipoToken.IDENTIFIER);
            Token id = previous();
            lista.add(id);
            return PARAMETERS_2(lista);
        }
        return lista;
    }

    // ARGUMENTS_OPC -> EXPRESSION ARGUMENTS | Ɛ
    private List<Expression> ARGUMENTS_OPC(List<Expression> lista){
        TipoToken pre = preanalisis.getTipo();
        if(pre == TipoToken.BANG || pre == TipoToken.MINUS
        || pre == TipoToken.TRUE || pre == TipoToken.FALSE || pre == TipoToken.NULL || pre == TipoToken.NUMBER || pre == TipoToken.STRING || pre == TipoToken.IDENTIFIER || pre == TipoToken.LEFT_PAREN){
            Expression expr = EXPRESSION();
            lista.add(expr);
            List<Expression> args = ARGUMENTS(lista);
            for (Expression ex : args) {
                lista.add(ex);
            }
        }
        return lista;
    }

    // ARGUMENTS -> , EXPRESSION ARGUMENTS | Ɛ
    private List<Expression> ARGUMENTS(List<Expression> lista){
        if(preanalisis.getTipo() == TipoToken.COMMA){
            match(TipoToken.COMMA);
            Expression expr = EXPRESSION();
            lista.add(expr);
            return ARGUMENTS(lista);
        }
        return lista;
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
