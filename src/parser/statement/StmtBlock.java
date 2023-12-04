package parser.statement;

import java.util.ArrayList;
//import javax.swing.plaf.nimbus.State;
import java.util.List;

import parser.Program;

public class StmtBlock extends Statement{
    final List<Statement> statements;
    public final static String nombre = "StmtBlock";

    public StmtBlock(List<Statement> statements) {
        this.statements = statements;
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
