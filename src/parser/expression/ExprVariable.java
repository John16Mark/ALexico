package parser.expression;

import token.Token;

public class ExprVariable extends Expression {
    final Token name;
    public final static String nombre = "ExprVariable";

    public ExprVariable(Token name) {
        this.name = name;
    }
}