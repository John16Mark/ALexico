package parser.statement;

import token.Token;
import parser.expression.Expression;

public class StmtVar extends Statement {
    final Token name;
    final Expression initializer;
    public final static String nombre = "StmtVar";

    public StmtVar(Token name, Expression initializer) {
        this.name = name;
        this.initializer = initializer;
    }
}
