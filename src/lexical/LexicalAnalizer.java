package lexical;

import exceptions.LexicalException;
import sourcemanager.SourceManager;

import java.io.IOException;
import java.util.Objects;

public class LexicalAnalizer {
    String lexeme;
    char actualChar;
    SourceManager sourceManager;
    Keywords tokenId = new Keywords();

    public LexicalAnalizer(SourceManager sourceManager) throws IOException {
        this.sourceManager = sourceManager;
        updateActualChar();
    }

    public Token getToken() throws LexicalException{
        lexeme = "";
        return e0();
    }

    private void updateLexeme(){
        lexeme += actualChar;
        //System.out.println("Actual lexeme: '" + lexeme + "'");
    }

    private void updateActualChar() {
        try {
            actualChar = sourceManager.getNextChar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Token e0 () throws LexicalException {
        //System.out.printf("Actual char: '%s'\n", actualChar);
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
            case SourceManager.END_OF_FILE: {
                return new Token(TokenId.EOF, lexeme, sourceManager.getLineNumber());
            }
            default:{
                if(Character.isUpperCase(actualChar)){
                    return classState();
                } else if (Character.isLowerCase(actualChar)) {
                    return metVarState();
                } else if (Character.isDigit(actualChar)){
                    return numberState();
                } else {
                    updateLexeme();
                    updateActualChar();
                    throw new LexicalException(lexeme, sourceManager.getLineNumber(), 1, lexeme); // Ajustar parametros de exception
                }
            }
        }
    }

    public Token e6() throws LexicalException{
        if (actualChar == '\\'){
            updateLexeme();
            updateActualChar();
            return e7();
        } else if (actualChar == '\''){
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), 1, lexeme); // Ajustar parametros de exception
        } else if (actualChar == SourceManager.END_OF_FILE || actualChar == '\n'){
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), 1, lexeme); // Ajustar parametros de exception
        } else {
            updateLexeme();
            updateActualChar();
            if (actualChar == '\''){
                updateLexeme();
                updateActualChar();
                return new Token(TokenId.lit_char, lexeme, sourceManager.getLineNumber());
            } else {
                throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), lexeme); // Ajustar parametros de exception
            }
        }
    }

    public Token e7() throws LexicalException{
        return null;
    }

    public Token metVarState() throws LexicalException{
        if (Character.isLetterOrDigit(actualChar) || actualChar == '_'){
            updateLexeme();
            updateActualChar();
            return metVarState();
        } else{
            TokenId id = tokenId.getTokenId(lexeme);
            return new Token(Objects.requireNonNullElse(id, TokenId.id_MetVar), lexeme, sourceManager.getLineNumber());
        }
    }

    public Token numberState() throws LexicalException{
        if (Character.isDigit(actualChar)){
            updateLexeme();
            updateActualChar();
            return numberState();
        } else {
            return new Token(TokenId.lit_int, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e3() throws LexicalException{
        updateLexeme();
        updateActualChar();
        if (actualChar == '"'){
            updateLexeme();
            updateActualChar();
            return new Token(TokenId.lit_string, lexeme, sourceManager.getLineNumber());
        } else if (actualChar == SourceManager.END_OF_FILE || actualChar == '\n'){
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), 1, lexeme); // Ajustar parametros de exception
        } else {
            return e3();
        }
    }

    public Token classState() throws LexicalException {
        if (Character.isLetterOrDigit(actualChar) || actualChar == '_'){
            updateLexeme();
            updateActualChar();
            return classState();
        } else {
            return new Token(TokenId.id_Class, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e9() throws LexicalException {
        if (actualChar == '/'){
            return e10();
        } else if (actualChar == '*') {
            return e11();
        } else {
            return new Token(TokenId.op_division, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e10() throws LexicalException {
        updateActualChar();
        if (actualChar == '\n') {
            lexeme = "";
            updateActualChar();
            return e0();
        } else {
            return e10();
        }
    }

    public Token e11() throws LexicalException {
        updateActualChar();
        if (actualChar == '*') {
            return e12();
        } else if (actualChar == SourceManager.END_OF_FILE){
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), 1, lexeme); // Ajustar parametros de exception
        } else {
            return e11();
        }
    }

    public Token e12() throws LexicalException{
        updateActualChar();
        if (actualChar == '/') {
            return e0();
        } else {
            return e11();
        }
    }

    public Token e14() throws LexicalException {
        if (actualChar == '='){
            updateLexeme();
            updateActualChar();
            return e15();
        } else {
            return new Token(TokenId.assignment, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e15() throws LexicalException{
        return new Token(TokenId.op_equal, lexeme, sourceManager.getLineNumber());
    }

    public Token e16() throws LexicalException{
        if (actualChar == '='){
            updateLexeme();
            updateActualChar();
            return e17();
        } else {
            return new Token(TokenId.op_less, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e17() throws LexicalException{
        return new Token(TokenId.op_lessOrEqual, lexeme, sourceManager.getLineNumber());
    }

    public Token e18() throws LexicalException{
        if (actualChar == '='){
            updateLexeme();
            updateActualChar();
            return e19();
        } else {
            return new Token(TokenId.op_greater, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e19() throws LexicalException{
        return new Token(TokenId.op_greaterOrEqual, lexeme, sourceManager.getLineNumber());
    }

    public Token e20() throws LexicalException{
        if (actualChar == '+'){
            return e21();
        } else {
            return new Token(TokenId.op_plus, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e21() {
        return new Token(TokenId.increment, lexeme, sourceManager.getLineNumber());
    }

    public Token e22() throws LexicalException{
        if (actualChar == '-'){
            return e23();
        } else {
            return new Token(TokenId.op_minus, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e23() {
        return new Token(TokenId.decrement, lexeme, sourceManager.getLineNumber());
    }

    public Token e24() throws LexicalException{
        if (actualChar == '='){
            return e25();
        } else {
            return new Token(TokenId.op_not, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e25() {
        return new Token(TokenId.op_notEqual, lexeme, sourceManager.getLineNumber());
    }

    public Token e26() {
        return new Token(TokenId.op_multiplication, lexeme, sourceManager.getLineNumber());
    }

    public Token e27() {
        return new Token(TokenId.op_modulo, lexeme, sourceManager.getLineNumber());
    }

    public Token e28() throws LexicalException{
        updateLexeme();
        updateActualChar();
        if (actualChar == '&'){
            return e29();
        } else {
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), 1, lexeme); // Ajustar parametros de exception
        }
    }

    public Token e29() {
        return new Token(TokenId.op_and, lexeme, sourceManager.getLineNumber());
    }

    public Token e30() throws LexicalException{
        updateLexeme();
        updateActualChar();
        if (actualChar == '|'){
            return e31();
        } else {
            throw new LexicalException(lexeme, 1, 1, lexeme); // Ajustar parametros de exception
        }
    }

    public Token e31() {
        return new Token(TokenId.op_or, lexeme, sourceManager.getLineNumber());
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
        return new Token(TokenId.punt_dot, lexeme, sourceManager.getLineNumber());
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
}
