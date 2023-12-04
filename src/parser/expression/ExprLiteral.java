package parser.expression;

import java.util.ArrayList;

import parser.Program;

public class ExprLiteral extends Expression {
    final Object value;

    public ExprLiteral(Object value) {
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
        if(value instanceof String){
            System.out.print("\033[95m\""+value+"\"\n");System.out.print("\033[0m");
        } else {
            System.out.print("\033[95m"+value+"\n");System.out.print("\033[0m");
        }
        lista.remove(lista.size()-1);
    }
}
