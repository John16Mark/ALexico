package parser.statement;

import token.Token;

import java.util.ArrayList;

import interprete.TablaSimbolos;
import parser.Program;
import parser.expression.Expression;

public class StmtVar extends Statement {
    public final Token name;
    public final Expression initializer;

    public StmtVar(Token name, Expression initializer) {
        this.name = name;
        this.initializer = initializer;
    }

    @Override
    public void execute(TablaSimbolos ts) {
        String id = name.getLexema();

        if(ts.existeIdentificador(id)) {
            throw new RuntimeException("\033[31mIdentificador '" + id + "' ya definido.\033[0m");
        } 

        if(initializer == null) {
            ts.asignar(id, null);
        } else {
            ts.asignar(id, initializer.solve(ts));
        }
    }

    @Override
    public void imprimir(int nivel, ArrayList<Boolean> lista){
        // NAME
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
        System.out.print("\033[95m"+name.getLexema()+"\033[0m\n");
        lista.remove(lista.size()-1);
        // RIGHT
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
        if(initializer != null) {
            System.out.print(Program.getNombreExpression(initializer)+"\n");System.out.print("\033[0m");
            initializer.imprimir(nivel+1, lista);
        } else {
            System.out.print("\033[31mnull\n");System.out.print("\033[0m");
        }
        lista.remove(lista.size()-1);
    }
}
