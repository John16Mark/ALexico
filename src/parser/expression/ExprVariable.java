package parser.expression;

import java.util.ArrayList;

import interprete.TablaSimbolos;
import token.Token;

public class ExprVariable extends Expression {
    final Token name;

    public ExprVariable(Token name) {
        this.name = name;
    }

    @Override
    public Object solve(TablaSimbolos ts) {
        return ts.obtener(name.getLexema());
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