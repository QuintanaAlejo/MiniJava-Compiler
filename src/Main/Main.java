package Main;

import TablaDeSimbolos.TablaDeSimbolos;
import exceptions.SemanticException;
import exceptions.SyntacticException;
import lexical.LexicalAnalyzer;
import sourcemanager.SourceManager;
import sourcemanager.SourceManagerImpl;
import syntactic.SyntacticAnalyzer;

import java.io.*;
import java.util.Objects;

public class Main {
    public static TablaDeSimbolos TS;
    public static void main(String[] args){
        boolean error = false;
        SourceManager sourceManager = new SourceManagerImpl();
        LexicalAnalyzer lexicalAnalyzer;
        SyntacticAnalyzer syntacticAnalyzer;
        TS = new TablaDeSimbolos();
        String path;

        try {
            sourceManager.open(args[0]);
            lexicalAnalyzer = new LexicalAnalyzer(sourceManager);
            path = args[1];
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        try{
            syntacticAnalyzer = new SyntacticAnalyzer(lexicalAnalyzer);
            syntacticAnalyzer.startAnalysis();
        } catch (SyntacticException e){
            e.printError();
            error = true;
        } catch (SemanticException e){
            e.printError();
            error = true;
        }

        if(!error){
            try {
                TS.estaBienDeclarada();
                TS.consolidar();
                TS.chequear();
                generar(path);
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

    private static void generar(String path){
        File output;
        FileWriter writer;
        BufferedWriter bufferedWriter;
        TS.generar();

        try {
            output = new File(Objects.requireNonNullElse(path, "output.txt"));
            writer = new FileWriter(output);
            bufferedWriter = new BufferedWriter(writer);

            for(String instruccion : TS.getInstructionList()){
                writer.write(instruccion);
                writer.write("\n");
            }

            writer.close();
            bufferedWriter.close();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}