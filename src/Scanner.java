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
        int linea = 1;
    	int estado = 0;
        String lexema = "";
        char c;

        for(int i=0; i<source.length(); i++){
            c = source.charAt(i);
            if(c == '\n') {
            	linea++;
            }
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
                    	Token t = new Token(TipoToken.COMMA, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == '.') {
                    	lexema += c;
                    	Token t = new Token(TipoToken.DOT, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == ';') {
                    	lexema += c;
                    	Token t = new Token(TipoToken.SEMICOLON, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == '-') {
                    	lexema += c;
                    	Token t = new Token(TipoToken.MINUS, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == '+') {
                    	lexema += c;
                    	Token t = new Token(TipoToken.PLUS, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == '*') {
                    	lexema += c;
                    	Token t = new Token(TipoToken.STAR, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == '(') {
                    	lexema += c;
                    	Token t = new Token(TipoToken.LEFT_PAREN, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == ')') {
                    	lexema += c;
                    	Token t = new Token(TipoToken.RIGHT_PAREN, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == '{') {
                    	lexema += c;
                    	Token t = new Token(TipoToken.LEFT_BRACE, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == '}') {
                    	lexema += c;
                    	Token t = new Token(TipoToken.RIGHT_BRACE, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                	// Ignora los caracteres de espacio, tabulación, salto de línea, y vuelta de carro
                    else if(c == ' ' || c == '\t' || c == '\n' || c == 13) {
                    	
                    }
                    else {
                    	Main.error(linea, "No hay Token que inicie con caracter dado");
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
                        if(source.charAt(i-1) == '\n') {
                        	linea--;
                        }
                        i--;
                    // Operador relacional GT
                	} else {
                        Token t = new Token(TipoToken.GREATER, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        if(source.charAt(i-1) == '\n') {
                        	linea--;
                        }
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
                        if(source.charAt(i-1) == '\n') {
                        	linea--;
                        }
                        i--;
                    // Operador relacional LT
                	} else {
                        Token t = new Token(TipoToken.LESS, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        if(source.charAt(i-1) == '\n') {
                        	linea--;
                        }
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
                        if(source.charAt(i-1) == '\n') {
                        	linea--;
                        }
                        i--;
                    // Asignador 
                	} else {
                        Token t = new Token(TipoToken.EQUAL, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        if(source.charAt(i-1) == '\n') {
                        	linea--;
                        }
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
                        if(source.charAt(i-1) == '\n') {
                        	linea--;
                        }
                        i--;
                    // BANG
                	} else {
                        Token t = new Token(TipoToken.BANG, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        if(source.charAt(i-1) == '\n') {
                        	linea--;
                        }
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
                        if(source.charAt(i-1) == '\n') {
                        	linea--;
                        }
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
                        Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        if(source.charAt(i-1) == '\n') {
                        	linea--;
                        }
                        i--;
                    }
                    break;
                    
                case 16:
                	if(Character.isDigit(c)) {
                		estado = 17;
                		lexema += c;
                	} else {
                		Main.error(linea, "Se esperaba dígito después del punto decimal");
                	}
                	break;
                   
                case 17:
                	if(Character.isDigit(c)) {
                		lexema += c;
                	} else if(c == 'E') {
                		estado = 18;
                		lexema += c;
                	} else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Float.valueOf(lexema));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        if(source.charAt(i-1) == '\n') {
                        	linea--;
                        }
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
                		Main.error(linea, "Se esperaba dígito o signo después del exponente");
                	}
                	break;
                	
                case 19:
                	if(Character.isDigit(c)) {
                		estado = 20;
                		lexema += c;
                	} else {
                		Main.error(linea, "Se esperaba dígito después del signo del exponente");
                	}
                	break;
    
                case 20:
                	if(Character.isDigit(c)) {
                		lexema += c;
                	} else {
                		Token t = new Token(TipoToken.NUMBER, lexema, Double.parseDouble(lexema));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        if(source.charAt(i-1) == '\n') {
                        	linea--;
                        }
                        i--;
                	}
                	break;
                	
                /***** CADENA *****/
                case 24:
                	if(c == '\n') {
                		linea--;
                		Main.error(linea, "No se esperaba salto de línea");
                	} else if(c == '"'){
                		lexema += c;
                		Token t = new Token(TipoToken.STRING, lexema, lexema.substring(1, lexema.length()-1));
                        tokens.add(t);
                        
                		estado = 0;
                		lexema = "";
                		//i--;
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
                		Token t = new Token(TipoToken.SLASH, lexema);
                        tokens.add(t);
                		estado = 0;
                		lexema = "";
                		if(source.charAt(i-1) == '\n') {
                        	linea--;
                        }
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


        return tokens;
    }
}
