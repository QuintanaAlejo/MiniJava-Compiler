package exceptions;

import lexical.LexicalAnalizer;

public class LexicalException extends Exception{
    private final String lexeme;
    private final int lineNumber;
    private final int column;
    private final String explain;

    public LexicalException (String lexeme, int lineNumber, int column, String explain){
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
        this.column = column;
        this.explain = explain;
    }

    public void printMessage(){
        System.out.printf("Error lexico! '%s' en la linea %d, columna %d: %s\n", lexeme, lineNumber, column, explain);
        System.out.println();
        System.out.println("[Error:"+lexeme+"|"+lineNumber+"]");
    }
}
