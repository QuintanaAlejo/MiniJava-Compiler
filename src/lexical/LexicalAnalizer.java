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

    public Token getToken() throws LexicalException, IOException {
        lexeme = "";
        return e0();
    }

    private void updateLexeme(){
        lexeme += actualChar;
    }

    private void updateActualChar() throws IOException {
        actualChar = sourceManager.getNextChar();
    }

    public Token e0 () throws LexicalException, IOException {
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
                return e32();
            }
            case ')': {
                updateLexeme();
                return e33();
            }
            case '{': {
                updateLexeme();
                return e34();
            }
            case '}': {
                updateLexeme();
                return e35();
            }
            case '.': {
                updateLexeme();
                return e36();
            }
            case ',': {
                updateLexeme();
                return e37();
            }
            case ';': {
                updateLexeme();
                return e38();
            }
            case ':': {
                updateLexeme();
                return e39();
            }

            // Operators
            case '=': {
                return e14();
            }
            case '<': {
                return e16();
            }
            case '>': {
                return e18();
            }
            case '+': {
                return e20();
            }
            case '-': {
                return e22();
            }
            case '!': {
                return e24();
            }
            case '*': {
                updateLexeme();
                return e26();
            }
            case '%': {
                updateLexeme();
                return e27();
            }
            case '/': {
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
                    throw new LexicalException(lexeme, 1, 1, lexeme); // Ajustar parametros de exception
                }
            }
        }
    }

    public Token e6() throws IOException, LexicalException{
        return null; // Implementar me da fiaca ahora
    }

    public Token metVarState() throws IOException, LexicalException{
        if (Character.isLetterOrDigit(actualChar) || actualChar == '_'){
            updateLexeme();
            updateActualChar();
            return metVarState();
        } else{
            //System.out.printf("actual char: '%s'\n", actualChar);
            TokenId id = tokenId.getTokenId(lexeme);
            return new Token(Objects.requireNonNullElse(id, TokenId.id_MetVar), lexeme, sourceManager.getLineNumber());
        }
    }

    public Token numberState() throws IOException, LexicalException{
        if (Character.isDigit(actualChar)){
            updateLexeme();
            updateActualChar();
            return numberState();
        } else {
            return new Token(TokenId.lit_int, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e3() throws IOException, LexicalException{
        updateLexeme();
        updateActualChar();
        if (actualChar == '"'){
            updateLexeme();
            return new Token(TokenId.lit_string, lexeme, sourceManager.getLineNumber());
        } else if (actualChar == SourceManager.END_OF_FILE || actualChar == '\n'){
            throw new LexicalException(lexeme, 1, 1, lexeme); // Ajustar parametros de exception
        } else {
            return e3();
        }
    }

    public Token classState() throws IOException, LexicalException {
        if (Character.isLetterOrDigit(actualChar) || actualChar == '_'){
            updateLexeme();
            updateActualChar();
            return classState();
        } else {
            return new Token(TokenId.id_Class, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e9() throws IOException, LexicalException {
        updateActualChar();
        if (actualChar == '/'){
            return e10();
        } else if (actualChar == '*') {
            return e11();
        } else {
            return new Token(TokenId.op_division, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e10() throws IOException, LexicalException {
        updateActualChar();
        if (actualChar == '\n') {
            lexeme="";
            return e0();
        } else {
            return e10();
        }
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
        if (actualChar == '/') {
            return e0();
        } else {
            return e11();
        }
    }

    public Token e14() throws IOException, LexicalException {
        updateLexeme();
        updateActualChar();
        if (actualChar == '='){
            updateLexeme();
            return e15();
        } else {
            return new Token(TokenId.assignment, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e15() throws IOException, LexicalException{
        return new Token(TokenId.op_equal, lexeme, sourceManager.getLineNumber());
    }

    public Token e16() throws IOException, LexicalException{
        updateLexeme();
        updateActualChar();
        if (actualChar == '='){
            updateLexeme();
            return e17();
        } else {
            return new Token(TokenId.op_less, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e17() throws IOException, LexicalException{
        return new Token(TokenId.op_lessOrEqual, lexeme, sourceManager.getLineNumber());
    }

    public Token e18() throws IOException, LexicalException{
        updateLexeme();
        updateActualChar();
        if (actualChar == '='){
            return e19();
        } else {
            return new Token(TokenId.op_greater, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e19() throws IOException, LexicalException{
        return new Token(TokenId.op_greaterOrEqual, lexeme, sourceManager.getLineNumber());
    }

    public Token e20() throws IOException, LexicalException{
        updateLexeme();
        updateActualChar();
        if (actualChar == '+'){
            return e21();
        } else {
            return new Token(TokenId.op_plus, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e21() {
        return new Token(TokenId.increment, lexeme, sourceManager.getLineNumber());
    }

    public Token e22() throws IOException, LexicalException{
        updateLexeme();
        updateActualChar();
        if (actualChar == '-'){
            return e23();
        } else {
            return new Token(TokenId.op_minus, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token e23() {
        return new Token(TokenId.decrement, lexeme, sourceManager.getLineNumber());
    }

    public Token e24() throws IOException, LexicalException{
        updateLexeme();
        updateActualChar();
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

    public Token e28() throws IOException, LexicalException{
        updateLexeme();
        updateActualChar();
        if (actualChar == '&'){
            return e29();
        } else {
            throw new LexicalException(lexeme, 1, 1, lexeme); // Ajustar parametros de exception
        }
    }

    public Token e29() {
        return new Token(TokenId.op_and, lexeme, sourceManager.getLineNumber());
    }

    public Token e30() throws IOException, LexicalException{
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
