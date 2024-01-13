package parser.expression;

import java.util.ArrayList;

import interprete.TablaSimbolos;
import parser.Program;
import token.Token;

public class ExprAssign extends Expression{
    final Token name;
    final Expression value;

    public ExprAssign(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Object solve(TablaSimbolos ts) {
        TablaSimbolos tabla = ts;
        Object valor = value.solve(ts);

        if(tabla.existeIdentificador(name.getLexema())) {
            tabla.asignar(name.getLexema(), valor);
            return valor;
        }
        throw new RuntimeException("\033[31mVariable no definida '" + name.getLexema() + "'.\033[0m");
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
        System.out.print("\033[95m"+name.getLexema()+"\033[0m\n");
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
        System.out.print(Program.getNombreExpression(value)+"\n");System.out.print("\033[0m");
        value.imprimir(nivel+1, lista);
        lista.remove(lista.size()-1);
    }
}
