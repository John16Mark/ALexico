package parser.expression;

import token.Token;

public class ExprGet extends Expression{
    final Expression object;
    final Token name;
    public final static String nombre = "ExprGet";

    ExprGet(Expression object, Token name) {
        this.object = object;
        this.name = name;
    }
}
