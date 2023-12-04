package parser.expression;

public class ExprGrouping extends Expression {
    final Expression expression;
    public final static String nombre = "ExprGrouping";

    public ExprGrouping(Expression expression) {
        this.expression = expression;
    }
}
