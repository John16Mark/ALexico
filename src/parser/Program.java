package parser;

import java.util.List;

import parser.statement.Statement;

public class Program {
    List<Statement> statements;

    public Program(List<Statement> lista){
        this.statements = lista;
    }
}
