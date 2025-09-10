package exceptions;

import lexical.Token;

public class SyntacticException extends Exception{

    public SyntacticException() {
    }

    private static String errorConstructor(String expectedToken, Token actualToken){
        String error = ("Error sintactico en linea " + actualToken.getLinea() + ": Se esperaba " + expectedToken + " y se encontro \"" + actualToken.getLexeme()) + "\"";
        error += "\n\n[Error:" + actualToken.getLexeme() + "|" + actualToken.getLinea() + "]";
        return error;
    }
}