package parser.statement;

import java.util.ArrayList;

import interprete.TablaSimbolos;
import parser.Program;
import parser.expression.Expression;

public class StmtReturn extends Statement {
    public final Expression value;

    public StmtReturn(Expression value) {
        this.value = value;
    }

    public void execute(TablaSimbolos ts, String funcion) {
        if(value != null)
            ts.asignar(funcion, value.solve(ts));
    }

    @Override
    public void imprimir(int nivel, ArrayList<Boolean> lista){
        lista.add(false);
        for(int j=0; j<(lista.size()); j++){
            if(j == lista.size()-1){
                System.out.print(" └─");
            } else {
                if(lista.get(j)){
                    System.out.print(" │ ");
                } else {
                    System.out.print("   ");
                }
            }
        }
        System.out.print(Program.getNombreExpression(value)+"\n");System.out.print("\033[0m");
        if(value != null) {
            value.imprimir(nivel+1, lista);
        } else {

        }
        lista.remove(lista.size()-1);
    }
}
