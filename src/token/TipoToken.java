package token;

public enum TipoToken {
    // Tokens de un sólo caracter
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    // Tokens de uno o dos caracteres
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    // Literales
    IDENTIFIER, STRING, NUMBER,

    // Palabras clave
    AND, ELSE, FALSE, FUN, FOR, IF, NULL, OR,
    PRINT, RETURN, TRUE, VAR, WHILE,

    EOF;

    public static String imprimir(TipoToken tt){
        String str = "";
        switch (tt) {
            case LEFT_PAREN:
                str = "()";
                break;
            case RIGHT_PAREN:
                str = ")";
                break;
            case LEFT_BRACE:
                str = "{";
                break;
            case RIGHT_BRACE:
                str = "}";
                break;
            case COMMA:
                str = ",";
                break;
            case DOT:
                str = ".";
                break;
            case MINUS:
                str = "-";
                break;
            case PLUS:
                str = "+";
                break;
            case SEMICOLON:
                str = ";";
                break;
            case SLASH:
                str = "/";
                break;
            case STAR:
                str = "*";
                break;
            case BANG:
                str = "!";
                break;
            case BANG_EQUAL:
                str = "!=";
                break;
            case EQUAL:
                str = "=";
                break;
            case EQUAL_EQUAL:
                str = "==";
                break;
            case GREATER:
                str = ">";
                break;
            case GREATER_EQUAL:
                str = ">=";
                break;
            case LESS:
                str = "<";
                break;
            case LESS_EQUAL:
                str = "<=";
                break;
            case IDENTIFIER:
                str = "id";
                break;
            case STRING:
                str = "string";
                break;
            case NUMBER:
                str = "number";
                break;
            case AND:
                str = "and";
                break;
            case ELSE:
                str = "else";
                break;
            case FALSE:
                str = "false";
                break;
            case FUN:
                str = "FUN";
                break;
            case FOR:
                str = "for";
                break;
            case IF:
                str = "if";
                break;
            case NULL:
                str = "null";
                break;
            case OR:
                str = "or";
                break;
            case PRINT:
                str = "print";
                break;
            case RETURN:
                str = "return";
                break;
            case TRUE:
                str = "true";
                break;
            case VAR:
                str = "var";
                break;
            case WHILE:
                str = "while";
                break;
            case EOF:
                str = "$";
                break;
            default:
                str = tt.name();
                break;
        }
        return str;
    }
}
