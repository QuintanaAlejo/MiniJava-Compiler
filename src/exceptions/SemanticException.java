package exceptions;

public class SemanticException extends Exception{
    private final String currentToken;
    private final String expectedToken;
    private final int line;

    public SemanticException(String currentToken, String expectedTokenName, int line) {
        this.currentToken = currentToken;
        this.expectedToken = expectedTokenName;
        this.line = line;
    }
    public String getExpectedToken() {
        return expectedToken;
    }

    public String toString() {
        return "probando";
    }
}
