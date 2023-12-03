package parser.statement;

import parser.expression.Expression;

public class StmtReturn extends Statement {
    final Expression value;

    StmtReturn(Expression value) {
        this.value = value;
    }
}
