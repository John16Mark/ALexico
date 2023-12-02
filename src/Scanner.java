import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import token.TipoToken;
import token.Token;

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
        int i;
        for(i=0; i<source.length(); i++){
            c = source.charAt(i);
            if(Main.existenErrores) {
            	break;
            }
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
                	// Cadena
                    else if(c == '"') {
                    	estado = 24;
                    	lexema += c;
                    }
                	// Comentario monolínea / slash
                    else if(c == '/') {
                    	estado = 26;
                    	lexema += c;
                    }
                    else if(c == ',') {
                    	lexema += c;
                    	Token t = new Token(TipoToken.COMMA, lexema, getLinea(i));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == '.') {
                    	lexema += c;
                    	Token t = new Token(TipoToken.DOT, lexema, getLinea(i));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == ';') {
                    	lexema += c;
                    	Token t = new Token(TipoToken.SEMICOLON, lexema, getLinea(i));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == '-') {
                    	lexema += c;
                    	Token t = new Token(TipoToken.MINUS, lexema, getLinea(i));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == '+') {
                    	lexema += c;
                    	Token t = new Token(TipoToken.PLUS, lexema, getLinea(i));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == '*') {
                    	lexema += c;
                    	Token t = new Token(TipoToken.STAR, lexema, getLinea(i));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == '(') {
                    	lexema += c;
                    	Token t = new Token(TipoToken.LEFT_PAREN, lexema, getLinea(i));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == ')') {
                    	lexema += c;
                    	Token t = new Token(TipoToken.RIGHT_PAREN, lexema, getLinea(i));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == '{') {
                    	lexema += c;
                    	Token t = new Token(TipoToken.LEFT_BRACE, lexema, getLinea(i));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == '}') {
                    	lexema += c;
                    	Token t = new Token(TipoToken.RIGHT_BRACE, lexema, getLinea(i));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                	// Ignora los caracteres de espacio, tabulación, salto de línea, y vuelta de carro
                    else if(c == ' ' || c == '\t' || c == '\n' || c == 13) {
                    	
                    }
                    else {
                    	Main.error(getLinea(i), "No se esperaba '"+c+"'");
                    }
                    break;
                    
                case 1:
                	// Operador relacional GE
                	if(c == '=') {
                		lexema += c;
                		
                        Token t = new Token(TipoToken.GREATER_EQUAL, lexema, getLinea(i));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                    // Operador relacional GT
                	} else {
                        Token t = new Token(TipoToken.GREATER, lexema, getLinea(i));
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
                		
                        Token t = new Token(TipoToken.LESS_EQUAL, lexema, getLinea(i));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                    // Operador relacional LT
                	} else {
                        Token t = new Token(TipoToken.LESS, lexema, getLinea(i));
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
                		
                        Token t = new Token(TipoToken.EQUAL_EQUAL, lexema, getLinea(i));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                    // Asignador 
                	} else {
                        Token t = new Token(TipoToken.EQUAL, lexema, getLinea(i));
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
                		
                        Token t = new Token(TipoToken.BANG_EQUAL, lexema, getLinea(i));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                    // BANG
                	} else {
                        Token t = new Token(TipoToken.BANG, lexema, getLinea(i));
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
                            Token t = new Token(TipoToken.IDENTIFIER, lexema, getLinea(i));
                            tokens.add(t);
                        }
                        else{
                            Token t = new Token(tt, lexema, getLinea(i));
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
                    	estado = 16;
                    	lexema += c;
                    }
                    else if(c == 'E'){
                    	estado = 18;
                    	lexema += c;
                    }
                    else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema), getLinea(i));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                    
                case 16:
                	if(Character.isDigit(c)) {
                		estado = 17;
                		lexema += c;
                	} else {
                		Main.error(getLinea(i), "Se esperaba dígito después del punto decimal");
                	}
                	break;
                   
                case 17:
                	if(Character.isDigit(c)) {
                		lexema += c;
                	} else if(c == 'E') {
                		estado = 18;
                		lexema += c;
                	} else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Float.valueOf(lexema), getLinea(i));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                	break;
                	
                case 18:
                	if(c == '+' || c == '-') {
                		estado = 19;
                		lexema += c;
                	} else if(Character.isDigit(c)){
                		estado = 20;
                		lexema += c;
                	} else {
                		Main.error(getLinea(i), "Se esperaba dígito o signo después del exponente");
                	}
                	break;
                	
                case 19:
                	if(Character.isDigit(c)) {
                		estado = 20;
                		lexema += c;
                	} else {
                		Main.error(getLinea(i), "Se esperaba dígito después del signo del exponente");
                	}
                	break;
    
                case 20:
                	if(Character.isDigit(c)) {
                		lexema += c;
                	} else {
                		Token t = new Token(TipoToken.NUMBER, lexema, Double.parseDouble(lexema), getLinea(i));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                	}
                	break;
                	
                /***** CADENA *****/
                case 24:
                	if(c == '\n') {
                		Main.error(getLinea(i), "No se esperaba salto de línea");
                	} else if(c == '"'){
                		lexema += c;
                		Token t = new Token(TipoToken.STRING, lexema, lexema.substring(1, lexema.length()-1), getLinea(i));
                        tokens.add(t);
                        
                		estado = 0;
                		lexema = "";
                	} else {
                		lexema += c;
                	}
                	break;
                	
                case 26:
                	// Comentario multilínea
                	if(c == '*') {
                		estado = 27;
                	// Comentario monolínea
                	} else if (c == '/') {
                		estado = 30;
                	// Operador división
                	} else {
                		lexema += c;
                		Token t = new Token(TipoToken.SLASH, lexema, getLinea(i));
                        tokens.add(t);
                		estado = 0;
                		lexema = "";
                		i--;
                	}
                	break;
                	
                case 27:
                	if(c == '*') {
                		estado = 28;
                	}
                	break;
                	
                case 28:
                	if(c == '*') {

                	// Fin comentario multilínea
                	} else if(c == '/') {
                		estado = 0;
                		lexema = "";
                	} else {
                		estado = 27;
                	}
                	break;
                	
                case 30:
                	// Fin comentario monolínea
                	if(c == '\n') {
                		estado = 0;
                		lexema = "";
                	} else {
                	}
                	
                	break;
            }


        }

        tokens.add(new Token(TipoToken.EOF, "", getLinea(i)));
        return tokens;
    }

    private int getLinea(int pos){
        int j=1;
        for(int i=0; i<pos; i++){
            if(source.charAt(i) == '\n'){
                j++;
            }
        }
        return j;
    }
}
