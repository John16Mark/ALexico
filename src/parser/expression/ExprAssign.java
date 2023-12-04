package parser.expression;

import token.Token;

public class ExprAssign extends Expression{
    final Token name;
    final Expression value;
    public final static String nombre = "ExprAssign";

    public ExprAssign(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }
}
