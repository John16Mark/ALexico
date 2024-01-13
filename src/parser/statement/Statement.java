package parser.statement;

import java.util.ArrayList;

import interprete.TablaSimbolos;

public abstract class Statement {

    public String nombre;

    public void imprimir(int nivel, ArrayList<Boolean> lista) {}
    public void execute(TablaSimbolos ts) {
        throw new RuntimeException("\033[31mFunción execute no definida.\033[0m\n");
    }
}
