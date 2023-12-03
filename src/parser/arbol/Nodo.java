package parser.arbol;

import java.util.ArrayList;

import token.TipoToken;
import token.Token;

public class Nodo {
    ArrayList<Object> hijos = new ArrayList<>();
    String nombre;

    public Nodo(ArrayList<Object> h){
        for (Object object : h) {
            hijos.add(object);
        }
    }

    public Nodo(ArrayList<Object> h, String n){
        for (Object object : h) {
            hijos.add(object);
        }
        nombre = n;
    }

    public void imprimir(int nivel){
        Object h;
        for (int j=0; j<hijos.size(); j++) {
            h = hijos.get(j);
            for(int i=0; i<nivel; i++){
                System.out.print("  ");
            }

            if(h instanceof Token){
                System.out.print("\033[95m"+TipoToken.imprimir(((Token)h).getTipo())+"\n");
                System.out.print("\033[0m");
            } else if(hijos.get(j) instanceof Nodo){
                if(nombre == null){
                    System.out.print("\033[91m");
                }
                System.out.print(nombre+"\n");System.out.print("\033[0m");
                ((Nodo)hijos.get(j)).imprimir(nivel+1);
            }
        }
    }


}
