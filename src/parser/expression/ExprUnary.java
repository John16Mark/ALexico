package parser.expression;

import token.Token;

public class ExprUnary extends Expression{
    final Token operator;
    final Expression right;
    public final static String nombre = "ExprUnary";

    public ExprUnary(Token operator, Expression right) {
        this.operator = operator;
        this.right = right;
    }
}
