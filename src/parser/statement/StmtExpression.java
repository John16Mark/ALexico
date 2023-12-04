package parser.statement;

import parser.expression.*;

public class StmtExpression extends Statement {
    final Expression expression;
    public final static String nombre = "StmtExpression";

    public StmtExpression(Expression expression) {
        this.expression = expression;
    }
}
