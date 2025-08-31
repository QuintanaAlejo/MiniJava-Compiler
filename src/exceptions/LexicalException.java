package exceptions;

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

    public void printMessage(String entireLine){
        System.out.printf("Error lexico en la linea %d, columna %d: '%s' %s\n", lineNumber, column, lexeme, explain);
        System.out.println("Detalle: " + entireLine);
        for (int i = 0; i < column + 8; i++)
            System.out.print(" ");
        System.out.println("^");
        System.out.println("[Error:"+lexeme+"|"+lineNumber+"]");
    }
}
