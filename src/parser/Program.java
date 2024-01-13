package parser;

import java.util.ArrayList;
import java.util.List;

import interprete.TablaSimbolos;
import parser.expression.*;
import parser.statement.*;

public class Program {
    List<Statement> statements;
    public TablaSimbolos tablaGlobal;
    public final static String nombre = "Program";

    public Program(List<Statement> lista, TablaSimbolos tg){
        this.statements = lista;
        this.tablaGlobal = tg;
    }

    public List<Statement> getList() {
        return statements;
    }

    public void imprimir(int nivel, ArrayList<Boolean> lista){
        System.out.print(nombre+"\n");
        for(int i=0; i<statements.size(); i++){
            Statement hijo = statements.get(i);
            if(i == statements.size()-1){
                lista.add(false);
            } else {
                lista.add(true);
            }

            for(int j=0; j<(lista.size()); j++){
                if(j == lista.size()-1){
                    if(i != statements.size()-1){
                        System.out.print(" ├─");
                    } else {
                        System.out.print(" └─");
                    }
                } else {
                    if(lista.get(j)){
                        System.out.print(" │ ");
                    } else {
                        System.out.print("   ");
                    }
                }
            }
            System.out.print(getNombreStatement(hijo)+"\n");System.out.print("\033[0m");
            hijo.imprimir(nivel+1, lista);

            lista.remove(lista.size()-1);
        }
    }

    public static String getNombreStatement(Statement s){
        if(s instanceof StmtBlock){
            return "StmtBlock";
        }
        if(s instanceof StmtClass){
            return "StmtClass";
        }
        if(s instanceof StmtExpression){
            return "StmtExpression";
        }
        if(s instanceof StmtFunction){
            return "StmtFunction";
        }
        if(s instanceof StmtIf){
            return "StmtIf";
        }
        if(s instanceof StmtLoop){
            return "StmtLoop";
        }
        if(s instanceof StmtPrint){
            return "StmtPrint";
        }
        if(s instanceof StmtReturn){
            return "StmtReturn";
        }
        if(s instanceof StmtVar){
            return "StmtVar";
        }
        return "\033[91mDESCONOCIDO\033[0m";
    }

    public static String getNombreExpression(Expression e){
        if(e instanceof ExprAssign){
            return "ExprAssign";
        }
        if(e instanceof ExprBinary){
            return "ExprBinary";
        }
        if(e instanceof ExprCallFunction){
            return "ExprCallFunction";
        }
        if(e instanceof ExprGet){
            return "ExprGet";
        }
        if(e instanceof ExprGrouping){
            return "ExprGrouping";
        }
        if(e instanceof ExprLiteral){
            return "ExprLiteral";
        }
        if(e instanceof ExprLogical){
            return "ExprLogical";
        }
        if(e instanceof ExprUnary){
            return "ExprUnary";
        }
        if(e instanceof ExprVariable){
            return "ExprVariable";
        }
        return "\033[91mDESCONOCIDO\033[0m";
    }
}
