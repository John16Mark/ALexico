package parser.statement;

import java.util.ArrayList;

import interprete.TablaSimbolos;
import parser.Program;
import parser.expression.Expression;

public class StmtLoop extends Statement {
    final Expression condition;
    final Statement body;

    public StmtLoop(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void execute(TablaSimbolos ts) {
        while((Boolean)condition.solve(ts)){
            body.execute(ts);
        }
    }

    @Override
    public void imprimir(int nivel, ArrayList<Boolean> lista){
        /// CONDITION
        lista.add(true);
        for(int j=0; j<(lista.size()); j++){
            if(j == lista.size()-1){
                System.out.print(" ├─");
            } else {
                if(lista.get(j)){
                    System.out.print(" │ ");
                } else {
                    System.out.print("   ");
                }
            }
        }
        if(condition != null) {
            System.out.print(Program.getNombreExpression(condition)+"\n");System.out.print("\033[0m");
            condition.imprimir(nivel+1, lista);
        } else {
            System.out.print("\033[31mnull\n");System.out.print("\033[0m");
        }
        lista.remove(lista.size()-1);
        /// BODY
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
        if(body != null) {
            System.out.print(Program.getNombreStatement(body)+"\n");System.out.print("\033[0m");
            body.imprimir(nivel+1, lista);
        } else {
            System.out.print("\033[31mnull\n");System.out.print("\033[0m");
        }
        lista.remove(lista.size()-1);
    }
}
