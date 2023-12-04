package parser.expression;

import token.Token;

public class ExprSuper extends Expression {
    // final Token keyword;
    final Token method;
    public final static String nombre = "ExprSuper";

    ExprSuper(Token method) {
        // this.keyword = keyword;
        this.method = method;
    }
}