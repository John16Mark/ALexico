package parser.arbol;

import java.util.ArrayList;

import token.TipoToken;
import token.Token;

public class Nodo {
    public ArrayList<Object> hijos = new ArrayList<>();
    String nombre;

    public Nodo(ArrayList<Object> h){
        for (int i=0; i<h.size(); i++) {
            hijos.add(h.get(i));
        }
    }

    public Nodo(ArrayList<Object> h, String n){
        for (int i=0; i<h.size(); i++) {
            hijos.add(h.get(i));
        }
        nombre = n;
    }

    public void imprimir(int nivel, ArrayList<Boolean> lista){
        Object h;
        if(nivel == 0){
            System.out.print(nombre+"\n");
        }
        for (int j=0; j<hijos.size(); j++) {
            h = hijos.get(j);
            if(j == hijos.size()-1){
                lista.add(false);
            } else {
                lista.add(true);
            }
            for(int i=0; i<(lista.size()); i++){
                if(i == lista.size()-1){
                    if(j != hijos.size()-1){
                        System.out.print(" ├─");
                    } else {
                        System.out.print(" └─");
                    }
                } else {
                    if(lista.get(i)){
                        System.out.print(" │ ");
                    } else {
                        System.out.print("   ");
                    }
                }
            }

            // Imprime el tipo del token si es token, o el nombre del hijo si es nodo
            if(h instanceof Token){
                System.out.print("\033[95m"+TipoToken.imprimir(((Token)h).getTipo())+"\n");
                System.out.print("\033[0m");
            } else if(h instanceof Nodo){
                if(nombre == null){
                    System.out.print("\033[91m");
                }
                System.out.print(((Nodo)h).nombre+"\n");System.out.print("\033[0m");
                ((Nodo)h).imprimir(nivel+1, lista);
            }
            lista.remove(lista.size()-1);
        }
    }


}
