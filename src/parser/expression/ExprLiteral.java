package parser.expression;

public class ExprLiteral extends Expression {
    final Object value;
    public final static String nombre = "ExprLiteral";

    public ExprLiteral(Object value) {
        this.value = value;
    }
}
