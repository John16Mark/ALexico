package parser.statement;

import parser.expression.Expression;

public class StmtLoop extends Statement {
    final Expression condition;
    final Statement body;
    public final static String nombre = "StmtLoop";

    public StmtLoop(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }
}
