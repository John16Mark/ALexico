package parser.expression;

import java.util.ArrayList;

import interprete.TablaSimbolos;

public abstract class Expression {

    public void imprimir(int nivel, ArrayList<Boolean> lista) {}
    public Object solve(TablaSimbolos ts) {
        throw new RuntimeException("\033[31mFunción solve no definida.\033[0m\n");
    }
}
