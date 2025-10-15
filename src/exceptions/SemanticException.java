package exceptions;

public class SemanticException extends Exception{
    private final String currentToken;
    private final String error;
    private final int line;

    public SemanticException(String currentToken, String error, int line) {
        this.currentToken = currentToken;
        this.error = error;
        this.line = line;
    }

    public void printError() {
        System.out.println("Error Semántico en linea " + line + ": " );
        System.out.println(error);
        System.out.println();
        System.out.println("[Error:" + currentToken + "|" + line + "]");
    }
}
