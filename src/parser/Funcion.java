package parser;

import java.util.List;

import parser.statement.StmtBlock;
import token.Token;

public class Funcion {
    public Token nombre;
    public List<Token> parametros;
    public StmtBlock body;

    public Funcion(Token n, List<Token> p, StmtBlock b) {
        nombre = n;
        parametros = p;
        body = b;
    }
}
