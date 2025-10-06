package Main;

import TablaDeSimbolos.TablaDeSimbolos;
import exceptions.SemanticException;
import exceptions.SyntacticException;
import lexical.LexicalAnalyzer;
import sourcemanager.SourceManager;
import sourcemanager.SourceManagerImpl;
import syntactic.SyntacticAnalyzer;
import java.io.IOException;

public class Main {
    public static TablaDeSimbolos TS;
    public static void main(String[] args){
        boolean error = false;

        SourceManager sourceManager = new SourceManagerImpl();
        LexicalAnalyzer lexicalAnalyzer;
        SyntacticAnalyzer syntacticAnalyzer;
        TS = new TablaDeSimbolos();
        try {
            sourceManager.open(args[0]);
            lexicalAnalyzer = new LexicalAnalyzer(sourceManager);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        try{
            syntacticAnalyzer = new SyntacticAnalyzer(lexicalAnalyzer);
            syntacticAnalyzer.startAnalysis();
        } catch (SyntacticException e) {
            e.printError();
            error = true;
        }

        if(!error){
            try {
                TS.estaBienDeclarada();
            } catch (SemanticException e) {
                e.printError();
                error = true;
            }
        }

        if (!error) {
            System.out.println("Compilacion exitosa");
            System.out.println();
            System.out.println("[SinErrores]");
        }
    }
}