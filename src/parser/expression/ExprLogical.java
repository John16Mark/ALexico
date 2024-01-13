package parser.expression;

import java.util.ArrayList;

import interprete.TablaSimbolos;
import parser.Program;
import token.TipoToken;
import token.Token;

public class ExprLogical extends Expression{
    final Expression left;
    final Token operator;
    final Expression right;

    public ExprLogical(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Object solve(TablaSimbolos ts) {
        Object valueLeft = left.solve(ts);
        Object valueRight = right.solve(ts);
        TipoToken op = operator.getTipo();

        if(op == TipoToken.EQUAL_EQUAL) {
            return valueLeft == valueRight;

        } else if(op == TipoToken.BANG_EQUAL) {
            return valueLeft != valueRight;

        } else if(op == TipoToken.GREATER) {
            if(valueLeft instanceof Number && valueRight instanceof Number) {
                return toNumber(valueLeft) > toNumber(valueRight);
            }
            if(valueLeft instanceof String && valueRight instanceof String) {
                return ((String)valueLeft).length() > ((String)valueRight).length();
            }

        } else if(op == TipoToken.GREATER_EQUAL) {
            if(valueLeft instanceof Number && valueRight instanceof Number) {
                return toNumber(valueLeft) >= toNumber(valueRight);
            }
            if(valueLeft instanceof String && valueRight instanceof String) {
                return ((String)valueLeft).length() >= ((String)valueRight).length();
            }

        } else if(op == TipoToken.LESS) {
            if(valueLeft instanceof Number && valueRight instanceof Number) {
                return toNumber(valueLeft) < toNumber(valueRight);
            }
            if(valueLeft instanceof String && valueRight instanceof String) {
                return ((String)valueLeft).length() < ((String)valueRight).length();
            }

        } else if(op == TipoToken.LESS_EQUAL) {
            if(valueLeft instanceof Number && valueRight instanceof Number) {
                return toNumber(valueLeft) <= toNumber(valueRight);
            }
            if(valueLeft instanceof String && valueRight instanceof String) {
                return ((String)valueLeft).length() <= ((String)valueRight).length();
            }
        
        } else if(op == TipoToken.AND) {
            if(valueLeft instanceof Boolean && valueRight instanceof Boolean) {
                return (Boolean)valueLeft && (Boolean)valueRight;
            }

        } else if(op == TipoToken.OR) {
            if(valueLeft instanceof Boolean && valueRight instanceof Boolean) {
                return (Boolean)valueLeft || (Boolean)valueRight;
            }
        
        } throw new RuntimeException("\033[31mOperación indefinida.\033[0m");
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

