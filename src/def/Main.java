package def;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
	// Cambio de prueba yty ewwea
	static boolean existenErrores = false;

    public static void main(String[] args) throws IOException {
        
    	// Ejecuta archivos si hay uno o más argumentos
    	if(args.length != 0) {
    		for (int i=0; i<args.length; i++) {
    			existenErrores = false;
    			System.out.print("\n"+args[i]+"\n\n");
    			ejecutarArchivo(args[i]);
    		}
    	}
    	else{
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
            
            for(Token token : tokens){
                System.out.println(token);
            }
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
    static void error(int linea, String mensaje){
        reportar(linea, "", mensaje);
    }

    private static void reportar(int linea, String posicion, String mensaje){
        System.err.println(
                "[linea " + linea + "] Error " + posicion + ": " + mensaje
        );
        existenErrores = true;
        
        //System.exit(1);
    }

}