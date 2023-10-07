package def;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {

    private static final Map<String, TipoToken> palabrasReservadas;

    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and",    TipoToken.AND);
        palabrasReservadas.put("else",   TipoToken.ELSE);
        palabrasReservadas.put("false",  TipoToken.FALSE);
        palabrasReservadas.put("for",    TipoToken.FOR);
        palabrasReservadas.put("fun",    TipoToken.FUN);
        palabrasReservadas.put("if",     TipoToken.IF);
        palabrasReservadas.put("null",   TipoToken.NULL);
        palabrasReservadas.put("or",     TipoToken.OR);
        palabrasReservadas.put("print",  TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("true",   TipoToken.TRUE);
        palabrasReservadas.put("var",    TipoToken.VAR);
        palabrasReservadas.put("while",  TipoToken.WHILE);
    }

    private final String source;

    private final List<Token> tokens = new ArrayList<>();
    
    public Scanner(String source){
        this.source = source + " ";
    }

    public List<Token> scan() throws Exception {
        int estado = 0;
        String lexema = "";
        char c;

        for(int i=0; i<source.length(); i++){
            c = source.charAt(i);

            switch (estado){
                case 0:
                	// Greater than / greater equal than
                	if(c == '>') {
                		estado = 1;
                		lexema += c;
                	}
                	// Lesser than / lesser equal than
                	else if(c == '<') {
                		estado = 4;
                		lexema += c;
                	}
                	// Equal than / Asignador
                	else if(c == '=') {
                		estado = 7;
                		lexema += c;
                	}
                	// Negación / Diferente de
                	else if(c == '!') {
                		estado = 10;
                		lexema += c;
                	}
                	// Identificador y palabras reservadas
                	else if(Character.isLetter(c)){
                        estado = 13;
                        lexema += c;
                    }
                    // Número
                    else if(Character.isDigit(c)){
                        estado = 15;
                        lexema += c;

                        /*while(Character.isDigit(c)){
                            lexema += c;
                            i++;
                            c = source.charAt(i);
                        }
                        Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema));
                        lexema = "";
                        estado = 0;
                        tokens.add(t);
                        */

                    }
                    else if(c == '"') {
                    	estado = 24;
                    	lexema += c;
                    }
                    break;
                    
                case 1:
                	// Operador relacional GE
                	if(c == '=') {
                		lexema += c;
                		
                        Token t = new Token(TipoToken.GREATER_EQUAL, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    // Operador relacional GT
                	} else {
                        Token t = new Token(TipoToken.GREATER, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                	}
                	break;
                	
                case 4:
                	// Operador relacional LE
                	if(c == '=') {
                		lexema += c;
                		
                        Token t = new Token(TipoToken.LESS_EQUAL, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    // Operador relacional LT
                	} else {
                        Token t = new Token(TipoToken.LESS, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                	}
                	break;
                	
                case 7:
                	// Operador relacional igual que
                	if(c == '=') {
                		lexema += c;
                		
                        Token t = new Token(TipoToken.EQUAL_EQUAL, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    // Asignador 
                	} else {
                        Token t = new Token(TipoToken.EQUAL, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                	}
                	break;
                	
                case 10:
                	// Operador relacional DIFFERENTE DE
                	if(c == '=') {
                		lexema += c;
                		
                        Token t = new Token(TipoToken.BANG_EQUAL, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    // BANG
                	} else {
                        Token t = new Token(TipoToken.BANG, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                	}
                	break;
                	
                case 13:
                    if(Character.isLetterOrDigit(c)){
                        estado = 13;
                        lexema += c;
                    }
                    else{
                        TipoToken tt = palabrasReservadas.get(lexema);

                        if(tt == null){
                            Token t = new Token(TipoToken.IDENTIFIER, lexema);
                            tokens.add(t);
                        }
                        else{
                            Token t = new Token(tt, lexema);
                            tokens.add(t);
                        }

                        estado = 0;
                        lexema = "";
                        i--;

                    }
                    break;

                case 15:
                    if(Character.isDigit(c)){
                        estado = 15;
                        lexema += c;
                    }
                    else if(c == '.'){

                    }
                    else if(c == 'E'){

                    }
                    else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                    
                case 24:
                	if(c == '\n') {
                		Main.error(i, "No se esperaba salto de línea");
                	} else if(c == '"'){
                		lexema += c;
                		Token t = new Token(TipoToken.STRING, lexema, lexema.substring(1, lexema.length()-1));
                        tokens.add(t);
                        
                		estado = 0;
                		lexema = "";
                		//i--;
                	} else {
                		lexema += c;
                		estado = 24;
                	}
                	break;
            }


        }


        return tokens;
    }
}
