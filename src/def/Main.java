package def;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import token.Token;
import parser.*;
import scanner.Scanner;

public class Main {

	public static boolean existenErrores = false;
    static boolean debug = false;

    public static void main(String[] args) throws IOException {
        if(args.length > 1) {
            System.out.println("Uso correcto: interprete [archivo.txt]");
            // Convención defininida en el archivo "system.h" de UNIX
            System.exit(64);
        } else if(args.length == 1){
            System.out.print("\n"+args[0]+"\n\n");
            ejecutarArchivo(args[0]);
        } else{
            ejecutarPrompt();
        }
    }

    private static void ejecutarArchivo(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        ejecutar(new String(bytes, Charset.defaultCharset()));

        // Se indica que existe un error
        //if(existenErrores) System.exit(65);
    }

    private static void ejecutarPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for(;;){
            System.out.print(">>> ");
            String linea = reader.readLine();
            if(linea == null) break; // Presionar Ctrl + D
            ejecutar(linea);
            existenErrores = false;
        }
    }

    private static void ejecutar(String source) {
        try{
            Scanner scanner = new Scanner(source);
            List<Token> tokens = scanner.scan();
            
            if(debug) {
                for(Token token : tokens){
                    System.out.println(token.imprimir());
                }
            }
            
            Parser parser = new ASDR(tokens);
            parser.parse();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    /*
    El método error se puede usar desde las distintas clases
    para reportar los errores:
    Interprete.error(....);
     */
    public static void error(int linea, String mensaje){
        reportar(linea, "", mensaje);
    }

    public static void reportar(int linea, String posicion, String mensaje){
        System.out.println(
                "\033[91m[linea " + linea + "] Error " + posicion + ": " + mensaje + "\033[0m"
        );
        existenErrores = true;
        System.exit(1);
    }

    public static void reportar(int linea, String mensaje){
        System.out.println(
                "\033[91m [línea \033[31m" + linea + "\033[91m] Error: " + mensaje + "\033[0m"
        );
        existenErrores = true;
        System.exit(1);
    }

    /*private static void presentacion(){
        
    }*/

}