package lexical;

import exceptions.LexicalException;
import sourcemanager.SourceManager;

import java.io.IOException;

public class LexicalAnalizer {
    String lexeme;
    char actualChar;
    SourceManager sourceManager;

    public Token getToken() throws LexicalException, IOException {
        lexeme = "";
        return e0();
    }

    public LexicalAnalizer(SourceManager sourceManager) throws IOException {
        this.sourceManager = sourceManager;
        updateActualChar();
    }

    public Token e0 () throws LexicalException, IOException {
        actualChar = sourceManager.getNextChar();

        switch (actualChar){
            // White spaces
            case ' ', '\n', '\t', '\r': {
                updateActualChar();
                return e0();
            }

            // Punctuation
            case '(': {
                updateLexeme();
                updateActualChar();
                return e32();
            }
            case ')': {
                updateLexeme();
                updateActualChar();
                return e33();
            }
            case '{': {
                updateLexeme();
                updateActualChar();
                return e34();
            }
            case '}': {
                updateLexeme();
                updateActualChar();
                return e35();
            }
            case '.': {
                updateLexeme();
                updateActualChar();
                return e36();
            }
            case ',': {
                updateLexeme();
                updateActualChar();
                return e37();
            }
            case ';': {
                updateLexeme();
                updateActualChar();
                return e38();
            }
            case ':': {
                updateLexeme();
                updateActualChar();
                return e39();
            }

            // Operators
            case '=': {
                updateLexeme();
                updateActualChar();
                return e14();
            }
            case '<': {
                updateLexeme();
                updateActualChar();
                return e16();
            }
            case '>': {
                updateLexeme();
                updateActualChar();
                return e18();
            }
            case '+': {
                updateLexeme();
                updateActualChar();
                return e20();
            }
            case '-': {
                updateLexeme();
                updateActualChar();
                return e22();
            }
            case '!': {
                updateLexeme();
                updateActualChar();
                return e24();
            }
            case '*': {
                updateLexeme();
                updateActualChar();
                return e26();
            }
            case '%': {
                updateLexeme();
                updateActualChar();
                return e27();
            }
            case '/': {
                updateLexeme();
                updateActualChar();
                return e9();
            }
            case '&': {
                updateLexeme();
                updateActualChar();
                return e28();
            }
            case '|': {
                updateLexeme();
                updateActualChar();
                return e30();
            }
            case '"': {
                updateLexeme();
                updateActualChar();
                return e3();
            }
            case '\'': {
                updateLexeme();
                updateActualChar();
                return e6();
            }
            default:{
                //Aca manejo string y digitos
                if(Character.isUpperCase(actualChar)){
                    updateLexeme();
                    updateActualChar();
                    return e2_may();
                } else if (Character.isLowerCase(actualChar)) {
                    updateLexeme();
                    updateActualChar();
                    return e2_min(); // Separar manejo de minusculas y mayusculas
                } else if (Character.isDigit(actualChar)){
                    updateLexeme();
                    updateActualChar();
                    return e1();
                } else {
                    updateLexeme();
                    // throw new LexicalException():
                }
            }
        }
        return null;
    }

    public Token e9() throws IOException, LexicalException {
        updateActualChar();
        if (actualChar == '/'){
            return e10();
        } else if (actualChar == '*') {
            return e11();
        } else {
            // return new Token de division;
        }
        return null;
    }

    public Token e10() throws IOException, LexicalException {
        updateActualChar();
        if (Character.isLetter(actualChar) || Character.isDigit(actualChar)){
            return e10();
        } else if (actualChar == '\n') {
            return e0();
        } else {
            //throw new LexicalException()
        }
        return null; // despues borrar los return null - corregir ta mal tengo que aceptar to-do
    }

    public Token e11() throws IOException, LexicalException {
        updateActualChar();
        if (actualChar == '*') {
            return e12();
        } else if (actualChar == SourceManager.END_OF_FILE){
            throw new LexicalException(lexeme, 1, 1, lexeme); // Ajustar parametros de exception
        } else {
            return e11();
        }
    }

    public Token e12() throws IOException, LexicalException{
        updateActualChar();
        if (Character.isLetter(actualChar) || Character.isDigit(actualChar)){
            return e11();
        } else if (actualChar == '/') {
            return e0();
        } else {
            //throw new LexicalException()
        }
        return null; // despues borrar los return null
    }

    public Token e14() throws IOException, LexicalException {
        updateLexeme();
        updateActualChar();
        if (actualChar == '='){
            return e15();
        } else {
            return new Token(TokenId.asignacion, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e15() throws IOException, LexicalException{
        return new Token(TokenId.op_igual, lexeme, sourceManager.getLineNumber());
    }

    public Token e16() throws IOException, LexicalException{
        updateLexeme();
        updateActualChar();
        if (actualChar == '='){
            return e17();
        } else {
            return new Token(TokenId.op_menor, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e17() throws IOException, LexicalException{
        return new Token(TokenId.op_menorIgual, lexeme, sourceManager.getLineNumber());
    }

    public Token e18() throws IOException, LexicalException{
        updateLexeme();
        updateActualChar();
        if (actualChar == '='){
            return e19();
        } else {
            return new Token(TokenId.op_mayor, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e19() throws IOException, LexicalException{
        return new Token(TokenId.op_mayorIgual, lexeme, sourceManager.getLineNumber());
    }

    public Token e24() throws IOException, LexicalException{
        updateLexeme();
        updateActualChar();
        if (actualChar == '='){
            return e25();
        } else {
            return new Token(TokenId.op_negacion, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e25() {
        return new Token(TokenId.op_distinto, lexeme, sourceManager.getLineNumber());
    }

    public Token e26() {
        return new Token(TokenId.op_multiplicacion, lexeme, sourceManager.getLineNumber());
    }

    public Token e27() {
        return new Token(TokenId.op_modulo, lexeme, sourceManager.getLineNumber());
    }

    public Token e32() {
        return new Token(TokenId.punt_openParenthesis, lexeme, sourceManager.getLineNumber());
    }

    public Token e33() {
        return new Token(TokenId.punt_closeParenthesis, lexeme, sourceManager.getLineNumber());
    }

    public Token e34() {
        return new Token(TokenId.punt_openKey, lexeme, sourceManager.getLineNumber());
    }

    public Token e35() {
        return new Token(TokenId.punt_closeKey, lexeme, sourceManager.getLineNumber());
    }

    public Token e36() {
        return new Token(TokenId.punt_point, lexeme, sourceManager.getLineNumber());
    }

    public Token e37() {
        return new Token(TokenId.punt_coma, lexeme, sourceManager.getLineNumber());
    }

    public Token e38() {
        return new Token(TokenId.punt_semicolon, lexeme, sourceManager.getLineNumber());
    }

    public Token e39() {
        return new Token(TokenId.punt_colon, lexeme, sourceManager.getLineNumber());
    }

    private void updateLexeme(){
        lexeme += actualChar;
    }

    private void updateActualChar() throws IOException {
        actualChar = sourceManager.getNextChar();
    }
}
