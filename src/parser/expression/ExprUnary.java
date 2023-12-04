package parser.expression;

import java.util.ArrayList;

import parser.Program;
import token.Token;

public class ExprUnary extends Expression{
    final Token operator;
    final Expression right;

    public ExprUnary(Token operator, Expression right) {
        this.operator = operator;
        this.right = right;
    }

    @Override
    public void imprimir(int nivel, ArrayList<Boolean> lista){
        // OPERATOR
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
        System.out.print("\033[95m"+operator.getLexema()+"\033[0m\n");
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
        System.out.print(Program.getNombreExpression(right)+"\n");System.out.print("\033[0m");
        right.imprimir(nivel+1, lista);
        lista.remove(lista.size()-1);
    }
}
