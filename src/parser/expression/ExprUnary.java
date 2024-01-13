package parser.expression;

import java.util.ArrayList;

import interprete.TablaSimbolos;
import parser.Program;
import token.TipoToken;
import token.Token;

public class ExprUnary extends Expression{
    final Token operator;
    final Expression right;

    public ExprUnary(Token operator, Expression right) {
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Object solve(TablaSimbolos ts) {
        Object resultado = right.solve(ts);
        TipoToken op = operator.getTipo();

        if(op == TipoToken.BANG) {
            if(resultado instanceof Boolean) {
                return !((Boolean)resultado);
            }
        } else if(op == TipoToken.MINUS) {
            if(resultado instanceof Number){
                return -toNumber(resultado);
            }
        }
        throw new RuntimeException("\033[31mOperación indefinida.\033[0m");
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

    private double toNumber(Object object) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }
        throw new IllegalArgumentException("El objeto no es un número");
    }
}
