package parser.statement;

import parser.expression.Expression;

public class StmtReturn extends Statement {
    final Expression value;
    public final static String nombre = "StmtReturn";

    public StmtReturn(Expression value) {
        this.value = value;
    }
}
