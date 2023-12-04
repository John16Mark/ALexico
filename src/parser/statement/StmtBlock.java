package parser.statement;

//import javax.swing.plaf.nimbus.State;
import java.util.List;

public class StmtBlock extends Statement{
    final List<Statement> statements;
    public final static String nombre = "StmtBlock";

    public StmtBlock(List<Statement> statements) {
        this.statements = statements;
    }
}
