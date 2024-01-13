package parser.statement;

import java.util.ArrayList;
//import javax.swing.plaf.nimbus.State;
import java.util.List;

import interprete.TablaSimbolos;
import parser.Program;

public class StmtBlock extends Statement{
    final List<Statement> statements;
    public final static String nombre = "StmtBlock";

    public StmtBlock(List<Statement> statements) {
        this.statements = statements;
    }
    
    @Override
    public void execute(TablaSimbolos ts) {
        TablaSimbolos ts2 = new TablaSimbolos(ts);
        for (Statement statement : statements) {
            if(statement instanceof StmtReturn) {
                ((StmtReturn)statement).value.solve(ts2);
                return;
            } else {
                statement.execute(ts2);
            }
        }
    }

    public void execute(TablaSimbolos ts, String funcion) {
        TablaSimbolos ts2 = new TablaSimbolos(ts);
        for (Statement statement : statements) {
            if(statement instanceof StmtReturn) {
                ((StmtReturn)statement).execute(ts2, funcion);
                return;
            } else {
                statement.execute(ts2);
            }
        }
    }

    @Override
    public void imprimir(int nivel, ArrayList<Boolean> lista){
        for (int j=0; j<statements.size(); j++) {
            Statement s = statements.get(j);
            if(j == statements.size()-1){
                lista.add(false);
            } else {
                lista.add(true);
            }
            for(int k=0; k<(lista.size()); k++){
                if(k == lista.size()-1){
                    if(j != statements.size()-1){
                        System.out.print(" ├─");
                    } else {
                        System.out.print(" └─");
                    }
                } else {
                    if(lista.get(k)){
                        System.out.print(" │ ");
                    } else {
                        System.out.print("   ");
                    }
                }
            }
            System.out.print(Program.getNombreStatement(s)+"\n");System.out.print("\033[0m");
            s.imprimir(nivel+1, lista);
            lista.remove(lista.size()-1);
        }
    }
}
