package parser.expression;

import java.util.ArrayList;
import java.util.List;

import interprete.TablaSimbolos;
import parser.Program;
import parser.statement.StmtFunction;
import token.Token;

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
    public Object solve(TablaSimbolos ts) {
        String id;
        Object resultado = callee.solve(ts);
        //if(resultado instanceof StmtFunction) {
        id = ((StmtFunction)resultado).name.getLexema();
        if(!ts.existeFuncion(id)) {
            throw new RuntimeException("\033[31mFunción '" + id + "' no definida.\033[0m");
        } else {
            StmtFunction funcion = ((StmtFunction)resultado);
            TablaSimbolos ts2 = new TablaSimbolos(ts);
            if(arguments.size() != funcion.params.size()){
                throw new RuntimeException("\033[31mNúmero de argumentos inválida para función '"+id+"'.\033[0m");
            }
            for (int i=0; i<arguments.size(); i++) {
                //System.out.println("Se asignó: "+funcion.params.get(i).getLexema()+" -> "+arguments.get(i));
                ts2.asignar(funcion.params.get(i).getLexema(), arguments.get(i).solve(ts));
            }
            funcion.body.execute(ts2, id);
            return ts2.obtener(id);
        }
    }

    @Override
    public void imprimir(int nivel, ArrayList<Boolean> lista){
        // NAME
        if(arguments.isEmpty()){
            lista.add(false);
        } else {
            lista.add(true);
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
            System.out.print("\033[96m"+Program.getNombreExpression(e)+"\n");System.out.print("\033[0m");
            e.imprimir(nivel+1, lista);
            lista.remove(lista.size()-1);
        }
    }
}
