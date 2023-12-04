package parser.statement;

import java.util.ArrayList;

import parser.Program;
import parser.expression.Expression;

public class StmtReturn extends Statement {
    final Expression value;

    public StmtReturn(Expression value) {
        this.value = value;
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
        value.imprimir(nivel+1, lista);

        lista.remove(lista.size()-1);
    }
}
