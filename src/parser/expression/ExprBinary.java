package parser.expression;

import java.util.ArrayList;

import interprete.TablaSimbolos;
import parser.Program;
import token.TipoToken;
import token.Token;

public class ExprBinary extends Expression{
    final Expression left;
    final Token operator;
    final Expression right;

    public ExprBinary(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Object solve(TablaSimbolos ts) {
        Object valueLeft = left.solve(ts);
        Object valueRight = right.solve(ts);
        TipoToken op = operator.getTipo();

        if(valueLeft == null || valueRight == null) {
            throw new RuntimeException("\033[31mOperación indefinida.\033[0m");
        }
        if(valueLeft instanceof Boolean || valueRight instanceof Boolean) {
            throw new RuntimeException("\033[31mOperadores incompatibles para tipo Boolean.\033[0m");
        }
        if(op == TipoToken.PLUS) {
            if(valueLeft instanceof Number && valueRight instanceof Number) {
                return toNumber(valueLeft) + toNumber(valueRight);
            } else if((valueLeft instanceof Number && valueRight instanceof String) || (valueLeft instanceof String && valueRight instanceof Number) || (valueLeft instanceof String && valueRight instanceof String)) {
                return ((String)valueLeft)+((String)valueRight);
            }
        } else if(op == TipoToken.MINUS) {
            if(valueLeft instanceof Number && valueRight instanceof Number) {
                return toNumber(valueLeft) - toNumber(valueRight);
            } else if(valueLeft instanceof String || valueRight instanceof String) {
                throw new RuntimeException("\033[31mOperador incompatible para tipo String: '+'\033[0m");
            }
        } else if(op == TipoToken.STAR) {
            if(valueLeft instanceof Number && valueRight instanceof Number) {
                return toNumber(valueLeft) * toNumber(valueRight);
            } else if(valueLeft instanceof String || valueRight instanceof String) {
                throw new RuntimeException("\033[31mOperador incompatible para tipo String: '*'\033[0m");
            }
        } else if(op == TipoToken.SLASH) {
            if(valueLeft instanceof Number && valueRight instanceof Number) {
                return toNumber(valueLeft) / toNumber(valueRight);
            } else if(valueLeft instanceof String || valueRight instanceof String) {
                throw new RuntimeException("\033[31mOperador incompatible para tipo String: '/'\033[0m");
            }
        }
        throw new RuntimeException("\033[31mOperación indefinida.\033[0m");
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
        System.out.print(Program.getNombreExpression(left)+"\n");System.out.print("\033[0m");
        left.imprimir(nivel+1, lista);
        lista.remove(lista.size()-1);
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
        // VALUE
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
