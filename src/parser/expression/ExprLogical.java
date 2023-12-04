package parser.expression;

import token.Token;

public class ExprLogical extends Expression{
    final Expression left;
    final Token operator;
    final Expression right;
    public final static String nombre = "ExprLogical";

    public ExprLogical(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}

