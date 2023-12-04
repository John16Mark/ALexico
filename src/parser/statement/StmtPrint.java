package parser.statement;

import parser.expression.Expression;

public class StmtPrint extends Statement {
    final Expression expression;
    public final static String nombre = "StmtPrint";

    public StmtPrint(Expression expression) {
        this.expression = expression;
    }
}
