package parser.statement;

import java.util.ArrayList;

import parser.Program;
import parser.expression.Expression;

public class StmtFor extends Statement {
    final Statement expr1;
    final Expression condition;
    final Expression expr2;
    final Statement body;
    public final static String nombre = "StmtFor";

    public StmtFor(Statement st, Expression condition, Expression e2, Statement body) {
        this.expr1 = st;
        this.condition = condition;
        this.expr2 = e2;
        this.body = body;
    }

    @Override
    public void imprimir(int nivel, ArrayList<Boolean> lista){
        /// EXPR1
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
        if(expr1 != null) {
            System.out.print(Program.getNombreStatement(expr1)+"\n");System.out.print("\033[0m");
            expr1.imprimir(nivel+1, lista);
        } else {
            System.out.print("\033[31mnull\n");System.out.print("\033[0m");
        }
        lista.remove(lista.size()-1);
        /// condition
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
        /// EXPR2
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
        if(expr2 != null) {
            System.out.print(Program.getNombreExpression(expr2)+"\n");System.out.print("\033[0m");
            expr2.imprimir(nivel+1, lista);
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
