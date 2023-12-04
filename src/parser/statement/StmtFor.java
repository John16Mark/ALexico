package parser.statement;

import parser.expression.Expression;

public class StmtFor extends Statement {
    final Statement expr1;
    final Expression condition;
    final Expression expr2;
    final Statement body;
    public final static String nombre = "StmtFor";

    public StmtFor(Statement st, Expression condition, Expression e2, Statement body) {
        this.expr1 = st;
        this.condition = condition;
        this.expr2 = e2;
        this.body = body;
    }
}
