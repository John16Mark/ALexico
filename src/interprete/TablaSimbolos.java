package interprete;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TablaSimbolos {

    private final Map<String, Object> values = new HashMap<>();
    public TablaSimbolos sigTabla;

    public TablaSimbolos(TablaSimbolos sig) {
        this.sigTabla = sig;
    }

    public boolean add(String id, Object valor){
        if(existeIdentificador(id)) {
            return false;
        } else {
            values.put(id, valor);
            return true;
        }
    }

    boolean existeIdentificador(String identificador){
        return values.containsKey(identificador);
    }

    /*Object obtener(String identificador) {
        if (values.containsKey(identificador)) {
            return values.get(identificador);
        }
        throw new RuntimeException("Variable no definida '" + identificador + "'.");
    }*/

    public Object obtener(String identificador) {
        if (existeIdentificador(identificador)) {
            return values.get(identificador);
        } else if (sigTabla != null) {
            return sigTabla.obtener(identificador);
        }
        throw new RuntimeException("\033[31mVariable no definida '" + identificador + "'.\033[0m");
    }

    void asignar(String identificador, Object valor){
        values.put(identificador, valor);
    }

    public void imprimir() {
        System.out.println("╔════════════╦══════════════════════════════════╗");
        for (Map.Entry<String, Object> entrada : values.entrySet()) {
            System.out.print("║"+String.format("%-12s",entrada.getKey())+"║");
            String valor;
            if(entrada.getValue() == null) {
                valor = "NULL";
                System.out.println(String.format("\033[31m%-34s\033[0m",valor)+"║");
            } else {
                if (entrada.getValue() instanceof String) {
                    valor = "\"" + entrada.getValue() + "\"";
                    System.out.println(String.format("\033[96m%-34s\033[0m",valor)+"║");
                } else {
                    valor = entrada.getValue().toString();
                    System.out.println(String.format("%-34s\033[0m",valor)+"║");
                }
                
            } 
            
        }
        System.out.println("╚════════════╩══════════════════════════════════╝");
    }
}
