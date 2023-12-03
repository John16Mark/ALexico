package parser.statement;

import parser.expression.Expression;

public class StmtPrint extends Statement {
    final Expression expression;

    StmtPrint(Expression expression) {
        this.expression = expression;
    }
}
