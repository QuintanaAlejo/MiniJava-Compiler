package exceptions;

public class SyntacticException extends Exception {
    private final String currentToken;
    private final String expectedToken;
    private final int line;

    public SyntacticException(String currentToken, String expectedTokenName, int line) {
        this.currentToken = currentToken;
        this.expectedToken = expectedTokenName;
        this.line = line;
    }
    public String getExpectedToken() {
        return expectedToken;
    }

    public void printError() {
        System.out.println("Se esperaba un "+expectedToken+" pero se encontro "+currentToken);
        System.out.println();
        System.out.println("[Error:" + currentToken + "|" + line + "]");
    }
}