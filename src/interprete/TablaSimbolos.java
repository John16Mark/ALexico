package interprete;

import java.util.HashMap;
import java.util.Map;

import parser.statement.Statement;

public class TablaSimbolos {

    private final Map<String, Object> values = new HashMap<>();
    public TablaSimbolos sigTabla;

    public TablaSimbolos(TablaSimbolos sig) {
        this.sigTabla = sig;
    }

    public boolean add(String id, Object valor){
        if(existeIdentificador(id)) {
            throw new RuntimeException("\033[31mIdentificador '" + id + "' ya definido.\033[0m");
        } else {
            values.put(id, valor);
            return true;
        }
    }

    public boolean existeIdentificador(String identificador){
        if(values.containsKey(identificador)) {
            return true;
        } else if (sigTabla != null) {
            return sigTabla.existeIdentificador(identificador);
        } else {
            return false;
        }
    }

    public Object obtener(String id) {
        if (!existeIdentificador(id)) {
            throw new RuntimeException("\033[31mError al obtener: Variable no definida '" + id + "'.\033[0m");
        } else {
            if(values.containsKey(id)) {
                return values.get(id);
            } else {
                return sigTabla.obtener(id);
            }
        }
    }

    public void asignar(String id, Object valor){
        if (!existeIdentificador(id)) {
            throw new RuntimeException("\033[31mError al asignar: Variable no definida '" + id + "'.\033[0m");
        } else {
            if(values.containsKey(id)) {
                values.put(id, valor);
            } else {
                sigTabla.asignar(id, valor);
            }
        }
        //values.put(identificador, valor);
    }

    public void imprimir() {
        System.out.println("╔═══════════════╦══════════════════════════════════╗");
        System.out.println("║ IDENTIFICADOR ║                VALOR             ║");
        System.out.println("╠═══════════════╬══════════════════════════════════╣");
        for (Map.Entry<String, Object> entrada : values.entrySet()) {
            System.out.print("║"+String.format("%-15s",entrada.getKey())+"║");
            String valor;
            if(entrada.getValue() == null) {
                valor = "NULL";
                System.out.println(String.format("\033[31m%-34s\033[0m",valor)+"║");
            } else {
                if (entrada.getValue() instanceof String) {
                    valor = "\"" + entrada.getValue() + "\"";
                    System.out.println(String.format("\033[96m%-34s\033[0m",valor)+"║");
                } else if(entrada.getValue() instanceof Statement) {
                    valor = "FUNCIÓN";
                    System.out.println(String.format("\033[95m%-34s\033[0m",valor)+"║");
                } else {
                    valor = entrada.getValue().toString();
                    System.out.println(String.format("%-34s\033[0m",valor)+"║");
                }
                
            } 
            
        }
        System.out.println("╚═══════════════╩══════════════════════════════════╝");
    }
}
