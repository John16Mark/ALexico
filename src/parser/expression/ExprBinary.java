package parser.expression;

import token.Token;

public class ExprBinary extends Expression{
    final Expression left;
    final Token operator;
    final Expression right;
    public final static String nombre = "ExprBinary";

    public ExprBinary(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

}
