package interprete;

import java.io.IOException;

import parser.Program;
import parser.statement.*;

public class Interprete {
    public Program program;

    public Interprete(Program p) {
        program = p;
    }

    public void interpretar() {
        System.out.println("Presione ENTER para continuar...");try{        System.in.read();}catch(Exception e){	e.printStackTrace();}
        clearScreen();
        System.out.println("\033[92m  Interpretación\033[0m");
        for (Statement statement : program.getList()) {
            if(statement instanceof StmtVar) {

            } else if(statement instanceof StmtFunction == false) {
                statement.execute(program.tablaGlobal);
            }
        }
    }

    public static void clearScreen() {
    try {
        String operatingSystem = System.getProperty("os.name"); // Detecta el sistema operativo

        if (operatingSystem.contains("Windows")) {
            // Limpia la pantalla en Windows
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } else {
            // Limpia la pantalla en Unix/Linux/MacOS
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    } catch (IOException | InterruptedException ex) {
        ex.printStackTrace();
    }
}
}
