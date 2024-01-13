package parser.statement;

import token.Token;

import java.util.ArrayList;
import java.util.List;

import parser.Program;

public class StmtFunction extends Statement {
    public final Token name;
    final List<Token> params;
    final StmtBlock body;

    public StmtFunction(Token name, List<Token> params, StmtBlock body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }

    @Override
    public void imprimir(int nivel, ArrayList<Boolean> lista){
        /// NAME
        lista.add(true);
        for(int j=0; j<(lista.size()); j++){
            if(j == lista.size()-1){
                System.out.print(" ├─");
            } else {
                if(lista.get(j)){
                    System.out.print(" │ ");
                } else {
                    System.out.print("   ");
                }
            }
        }
        System.out.print("\033[95m"+name.getLexema()+"\n");System.out.print("\033[0m");
        lista.remove(lista.size()-1);
        // PARAMS
        for (int j=0; j<params.size(); j++) {
            Token t = params.get(j);
            lista.add(true);
            for(int k=0; k<(lista.size()); k++){
                if(k == lista.size()-1){
                    System.out.print(" ├─");
                } else {
                    if(lista.get(k)){
                        System.out.print(" │ ");
                    } else {
                        System.out.print("   ");
                    }
                }
            }
            System.out.print("\033[96m"+t.getLexema()+"\n");System.out.print("\033[0m");
            lista.remove(lista.size()-1);
        }
        /// BODY
        lista.add(false);
        for(int j=0; j<(lista.size()); j++){
            if(j == lista.size()-1){
                System.out.print(" └─");
            } else {
                if(lista.get(j)){
                    System.out.print(" │ ");
                } else {
                    System.out.print("   ");
                }
            }
        }
        if(body != null) {
            System.out.print(Program.getNombreStatement(body)+"\n");System.out.print("\033[0m");
            body.imprimir(nivel+1, lista);
        } else {
            System.out.print("\033[31mnull\n");System.out.print("\033[0m");
        }
        lista.remove(lista.size()-1);
    }
}
