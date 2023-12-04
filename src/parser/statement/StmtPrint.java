package parser.statement;

import java.util.ArrayList;

import parser.Program;
import parser.expression.Expression;

public class StmtPrint extends Statement {
    final Expression expression;

    public StmtPrint(Expression expression) {
        this.expression = expression;
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
        System.out.print(Program.getNombreExpression(expression)+"\n");System.out.print("\033[0m");
        expression.imprimir(nivel+1, lista);

        lista.remove(lista.size()-1);
    }
}
