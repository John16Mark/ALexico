package interprete;

import java.util.HashMap;
import java.util.Map;

import parser.statement.StmtFunction;

public class TablaSimbolos {

    private final Map<String, Object> values = new HashMap<>();
    private final Map<String, StmtFunction> functions = new HashMap<>();
    public TablaSimbolos sigTabla;

    public TablaSimbolos(TablaSimbolos sig) {
        this.sigTabla = sig;
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

    public boolean existeFuncion(String identificador){
        if(functions.containsKey(identificador)) {
            return true;
        } else if (sigTabla != null) {
            return sigTabla.existeFuncion(identificador);
        } else {
            return false;
        }
    }

    public Object obtener(String id) {
        if (!existeIdentificador(id)) {
            throw new RuntimeException("\033[31mError al obtener: Variable '" + id + "' no definida.\033[0m");
        } else {
            if(values.containsKey(id)) {
                return values.get(id);
            } else {
                return sigTabla.obtener(id);
            }
        }
    }

    public StmtFunction obtenerFuncion(String id) {
        if (!existeFuncion(id)) {
            throw new RuntimeException("\033[31mError al obtener: Función '" + id + "' no definida.\033[0m");
        } else {
            if(values.containsKey(id)) {
                return functions.get(id);
            } else {
                return sigTabla.obtenerFuncion(id);
            }
        }
    }

    public void asignar(String id, Object valor){
        if (!existeIdentificador(id)) {
            Object value = valor;
            if(valor instanceof StmtFunction) {
                functions.put(id, (StmtFunction)valor);
                values.put(id, null);
                value = null;
            }
            values.put(id, value);
        } else {
            if(values.containsKey(id)) {
                values.put(id, valor);
            } else {
                sigTabla.asignar(id, valor);
            }
        }
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
                } else {
                    valor = entrada.getValue().toString();
                    System.out.println(String.format("%-34s\033[0m",valor)+"║");
                }
                
            } 
            
        }
        System.out.println("╚═══════════════╩══════════════════════════════════╝");
        System.out.println("╔════════════════════════════╗");
        System.out.println("║         ID. FUNCIÓN        ║");
        System.out.println("╠════════════════════════════╣");
        for (Map.Entry<String, StmtFunction> entrada : functions.entrySet()) {
            System.out.println("║"+String.format("%-28s",entrada.getKey())+"║");
        }
        System.out.println("╚════════════════════════════╝");
        if(sigTabla != null){
            System.out.println("\n\033[92m  Siguiente nivel\033[0m");
            sigTabla.imprimir();
        }
    }
}
