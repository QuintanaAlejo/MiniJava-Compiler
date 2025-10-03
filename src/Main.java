import exceptions.SyntacticException;
import lexical.LexicalAnalyzer;
import sourcemanager.SourceManager;
import sourcemanager.SourceManagerImpl;
import syntactic.SyntacticAnalyzer;
import java.io.IOException;

public class Main {
    public static void main(String[] args){
        SourceManager sourceManager = new SourceManagerImpl();
        boolean error = false;
        LexicalAnalyzer lexicalAnalyzer;
        SyntacticAnalyzer syntacticAnalyzer;

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

        if (!error) {
            System.out.println("Compilacion exitosa");
            System.out.println();
            System.out.println("[SinErrores]");
        }
    }
}