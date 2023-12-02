package token;
public class Token {

	final TipoToken tipo;
    final String lexema;
    final Object literal;
    final int linea;

    public Token(TipoToken tipo, String lexema, int l) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = null;
        this.linea = l;
    }

    public Token(TipoToken tipo, String lexema, Object literal, int l) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
        this.linea = l;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Token)) {
            return false;
        }

        if(this.tipo == ((Token)o).tipo){
            return true;
        }

        return false;
    }

    public TipoToken getTipo() {
    	return tipo;
    }

    public Object getLiteral() {
    	return literal;
    }

    public int getLinea(){
        return linea;
    }
    
    public String toString() {
        return "<" + tipo + "\033[0m " + lexema + " " + literal + ">";
    }

    public String imprimir(){
        return "[\033[96m"+linea+"\033[0m] <\033[93m" + tipo + "\033[0m " + lexema + " >";
    }
}
