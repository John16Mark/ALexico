package parser.expression;

import java.util.ArrayList;

//import def.Token;

import java.util.List;

import parser.Program;

public class ExprCallFunction extends Expression{
    final Expression callee;
    // final Token paren;
    final List<Expression> arguments;

    public ExprCallFunction(Expression callee, /*Token paren,*/ List<Expression> arguments) {
        this.callee = callee;
        // this.paren = paren;
        this.arguments = arguments;
    }

    @Override
    public void imprimir(int nivel, ArrayList<Boolean> lista){
        // NAME
        if(arguments.isEmpty()){
            lista.add(true);
        } else {
            lista.add(false);
        }
        for(int j=0; j<(lista.size()); j++){
            if(j == lista.size()-1){
                if(arguments.isEmpty()) {
                    System.out.print(" └─");
                } else {
                    System.out.print(" ├─");
                }
            } else {
                if(lista.get(j)){
                    System.out.print(" │ ");
                } else {
                    System.out.print("   ");
                }
            }
        }
        System.out.print(Program.getNombreExpression(callee)+"\n");System.out.print("\033[0m");
        callee.imprimir(nivel+1, lista);
        lista.remove(lista.size()-1);
        // ARGUMENT
        for (int j=0; j<arguments.size(); j++) {
            Expression e = arguments.get(j);
            if(j == arguments.size()-1){
                lista.add(false);
            } else {
                lista.add(true);
            }
            for(int k=0; k<(lista.size()); k++){
                if(k == lista.size()-1){
                    if(j != arguments.size()-1){
                        System.out.print(" ├─");
                    } else {
                        System.out.print(" └─");
                    }
                } else {
                    if(lista.get(k)){
                        System.out.print(" │ ");
                    } else {
                        System.out.print("   ");
                    }
                }
            }
            System.out.print(Program.getNombreExpression(e)+"\n");System.out.print("\033[0m");
            e.imprimir(nivel+1, lista);
            lista.remove(lista.size()-1);
        }
    }
}
