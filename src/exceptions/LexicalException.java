package exceptions;

import lexical.LexicalAnalizer;

public class LexicalException extends Exception{
    private final String lexeme;
    private final int lineNumber;
    private final int column;

    public LexicalException (String lexeme, int lineNumber, int column, String line){
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
        this.column = column;
    }

    public void printMessage(){
        System.out.printf("Error lexico: '%s' en la linea %d, columna %d\n", lexeme, lineNumber, column);
    }
}
