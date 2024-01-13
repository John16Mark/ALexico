package parser.statement;

import java.util.ArrayList;

import interprete.TablaSimbolos;
import parser.Program;
import parser.expression.Expression;

public class StmtIf extends Statement {
    final Expression condition;
    final Statement thenBranch;
    final Statement elseBranch;
    public final static String nombre = "StmtIf";

    public StmtIf(Expression condition, Statement thenBranch, Statement elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public void execute(TablaSimbolos ts) {
        if((Boolean)condition.solve(ts)) {
            thenBranch.execute(ts);
        } else {
            elseBranch.execute(ts);
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
        System.out.print(Program.getNombreExpression(condition)+"\n");System.out.print("\033[0m");
        condition.imprimir(nivel+1, lista);
        lista.remove(lista.size()-1);
        /// THEN
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
        System.out.print(Program.getNombreStatement(thenBranch)+"\n");System.out.print("\033[0m");
        thenBranch.imprimir(nivel+1, lista);
        lista.remove(lista.size()-1);
        /// CONDITION
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
        if(elseBranch != null) {
            System.out.print(Program.getNombreStatement(elseBranch)+"\n");System.out.print("\033[0m");
            elseBranch.imprimir(nivel+1, lista);
        } else {
            System.out.print("\033[31mnull\n");System.out.print("\033[0m");
        }
        lista.remove(lista.size()-1);
    }
}
