package parser.expression;

import java.util.ArrayList;

import interprete.TablaSimbolos;
import parser.statement.StmtFunction;
import token.Token;

public class ExprVariable extends Expression {
    final Token name;

    public ExprVariable(Token name) {
        this.name = name;
    }

    @Override
    public Object solve(TablaSimbolos ts) {
        Object valor = ts.obtener(name.getLexema());
        if(valor instanceof StmtFunction) {
            throw new RuntimeException("\033[31mFaltan los argumentos de la función '" + ((StmtFunction)valor).name.getLexema() + "'.\033[0m");
        } else {
            return valor;
        }
    }

    public Token getName(){
        return name;
    }

    @Override
    public void imprimir(int nivel, ArrayList<Boolean> lista){
        lista.add(false);
        for(int j=0; j<lista.size(); j++){
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
        System.out.print("\033[95m"+name.getLexema()+"\n");System.out.print("\033[0m");
        lista.remove(lista.size()-1);
    }
}