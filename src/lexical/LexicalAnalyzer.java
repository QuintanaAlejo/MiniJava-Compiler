package lexical;

import exceptions.LexicalException;
import sourcemanager.SourceManager;

import java.io.IOException;
import java.util.Objects;

public class LexicalAnalyzer {
    String lexeme;
    char actualChar;
    SourceManager sourceManager;
    Keywords tokenId = new Keywords();

    public LexicalAnalyzer(SourceManager sourceManager) throws IOException {
        this.sourceManager = sourceManager;
        updateActualChar();
    }

    public Token getToken() throws LexicalException{
        lexeme = "";
        return initialState();
    }

    private void updateLexeme(){
        lexeme += actualChar;
    }

    public int getLineNumber(){
        return sourceManager.getLineNumber();
    }

    private void updateActualChar() {
        try {
            actualChar = sourceManager.getNextChar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Token initialState() throws LexicalException {
        switch (actualChar){
            // White spaces
            case ' ', '\n', '\t', '\r': {
                updateActualChar();
                return initialState();
            }

            // Punctuation
            case '(': {
                updateLexeme();
                updateActualChar();
                return openParenthesisState();
            }
            case ')': {
                updateLexeme();
                updateActualChar();
                return closeParenthesisState();
            }
            case '{': {
                updateLexeme();
                updateActualChar();
                return openKeyState();
            }
            case '}': {
                updateLexeme();
                updateActualChar();
                return closeKeyState();
            }
            case '.': {
                updateLexeme();
                updateActualChar();
                return dotState();
            }
            case ',': {
                updateLexeme();
                updateActualChar();
                return comaState();
            }
            case ';': {
                updateLexeme();
                updateActualChar();
                return semicolonState();
            }
            case ':': {
                updateLexeme();
                updateActualChar();
                return colonState();
            }

            // Operators
            case '=': {
                updateLexeme();
                updateActualChar();
                return assignmentState();
            }
            case '<': {
                updateLexeme();
                updateActualChar();
                return lessState();
            }
            case '>': {
                updateLexeme();
                updateActualChar();
                return greaterState();
            }
            case '+': {
                updateLexeme();
                updateActualChar();
                return plusState();
            }
            case '-': {
                updateLexeme();
                updateActualChar();
                return minusState();
            }
            case '!': {
                updateLexeme();
                updateActualChar();
                return notState();
            }
            case '*': {
                updateLexeme();
                updateActualChar();
                return multiplicationState();
            }
            case '%': {
                updateLexeme();
                updateActualChar();
                return moduleState();
            }
            case '/': {
                updateLexeme();
                updateActualChar();
                return divisionState();
            }
            case '&': {
                updateLexeme();
                updateActualChar();
                return andState_1();
            }
            case '|': {
                updateLexeme();
                updateActualChar();
                return orState_1();
            }
            case '"': {
                updateLexeme();
                updateActualChar();
                return litStringState();
            }
            case '\'': {
                updateLexeme();
                updateActualChar();
                return litCharState_1();
            }
            case '?': {
                updateLexeme();
                updateActualChar();
                return new Token(TokenId.op_ternary, lexeme, sourceManager.getLineNumber());
            }
            // End of file
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
                    int errorColumnNumber = sourceManager.getColumnNumber();
                    updateLexeme();
                    updateActualChar();
                    throw new LexicalException(lexeme, sourceManager.getLineNumber(), errorColumnNumber,"Simbolo no reconocido.");
                }
            }
        }
    }

    public Token litCharState_1() throws LexicalException{
        if (actualChar == '\\'){
            updateLexeme();
            updateActualChar();
            return litCharState_2();
        } else if (actualChar == '\''){
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), "Literal caracter vacio.");
        } else if (actualChar == SourceManager.END_OF_FILE || actualChar == '\n'){
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), "Literal caracter no cerrado.");
        } else {
            updateLexeme();
            updateActualChar();
            if (actualChar == '\''){
                updateLexeme();
                updateActualChar();
                return new Token(TokenId.lit_char, lexeme, sourceManager.getLineNumber());
            } else {
                throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), "Literal caracter invalido.");
            }
        }
    }

    public Token litCharState_2() throws LexicalException {
        if (actualChar == 'u') {
            updateLexeme();
            updateActualChar();
            return unicodeState();
        } else if (actualChar == SourceManager.END_OF_FILE || actualChar == '\n') {
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), "Literal caracter no cerrado.");
        } else {
            updateLexeme();
            updateActualChar();
            if (actualChar == '\'') {
                updateLexeme();
                updateActualChar();
                return new Token(TokenId.lit_char, lexeme, sourceManager.getLineNumber());
            } else {
                throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), "Literal caracter invalido.");
            }
        }
    }

    public Token unicodeState() throws LexicalException{
        for (int i = 0; i < 4; i++) {
            if (Character.isDigit(actualChar) || (actualChar >= 'a' && actualChar <= 'f') || (actualChar >= 'A' && actualChar <= 'F')){
                updateLexeme();
                updateActualChar();
            } else {
                throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), "Secuencia invalida.");
            }
        }
        if (actualChar == '\''){
            updateLexeme();
            updateActualChar();
            return new Token(TokenId.lit_char, lexeme, sourceManager.getLineNumber());
        } else {
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), "Literal caracter invalido.");
        }
    }

    public Token metVarState(){
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
        } else if (lexeme.length() <= 9) {
            return new Token(TokenId.lit_int, lexeme, sourceManager.getLineNumber());
        } else {
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), "Numero demasiado grande");
        }
    }

    public Token litStringState() throws LexicalException{
        if (actualChar =='\\'){
            updateLexeme();
            updateActualChar();
            if (actualChar == '"') {
                updateLexeme();
                updateActualChar();
                return litStringState();
            }
        }
        if (actualChar == '"'){
            updateLexeme();
            updateActualChar();
            return new Token(TokenId.lit_string, lexeme, sourceManager.getLineNumber());
        } else if (actualChar == SourceManager.END_OF_FILE || actualChar == '\n' || actualChar == '\r'){
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), "String no cerrado.");
        } else {
            updateLexeme();
            updateActualChar();
            return litStringState();
        }
    }

    public Token classState(){
        if (Character.isLetterOrDigit(actualChar) || actualChar == '_'){
            updateLexeme();
            updateActualChar();
            return classState();
        } else {
            return new Token(TokenId.id_Class, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token divisionState() throws LexicalException {
        if (actualChar == '/'){
            lexeme = "";
            return singleLineCommentState();
        } else if (actualChar == '*') {
            lexeme = "";
            return multiLineCommentState_1();
        } else {
            return new Token(TokenId.op_division, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token singleLineCommentState() throws LexicalException {
        updateActualChar();
        if (actualChar == '\n') {
            lexeme = "";
            updateActualChar();
            return initialState();
        } else {
            return singleLineCommentState();
        }
    }

    public Token multiLineCommentState_1() throws LexicalException {
        updateActualChar();
        if (actualChar == '*') {
            return multiLineCommentState_2();
        } else if (actualChar == SourceManager.END_OF_FILE){
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), "Comentario multilinea no cerrado"); // Ajustar parametros de exception
        } else {
            return multiLineCommentState_1();
        }
    }

    public Token multiLineCommentState_2() throws LexicalException{
        updateActualChar();
        if (actualChar == '/') {
            updateActualChar();
            return initialState();
        } else {
            return multiLineCommentState_1();
        }
    }

    public Token assignmentState(){
        if (actualChar == '='){
            updateLexeme();
            updateActualChar();
            return equalState();
        } else {
            return new Token(TokenId.assignment, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token equalState(){
        return new Token(TokenId.op_equal, lexeme, sourceManager.getLineNumber());
    }

    public Token lessState(){
        if (actualChar == '='){
            updateLexeme();
            updateActualChar();
            return lessOrEqualState();
        } else {
            return new Token(TokenId.op_less, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token lessOrEqualState(){
        return new Token(TokenId.op_lessOrEqual, lexeme, sourceManager.getLineNumber());
    }

    public Token greaterState(){
        if (actualChar == '='){
            updateLexeme();
            updateActualChar();
            return greaterOrEqualState();
        } else {
            return new Token(TokenId.op_greater, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token greaterOrEqualState(){
        return new Token(TokenId.op_greaterOrEqual, lexeme, sourceManager.getLineNumber());
    }

    public Token plusState(){
        if (actualChar == '+'){
            updateLexeme();
            updateActualChar();
            return incrementState();
        } else {
            return new Token(TokenId.op_plus, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token incrementState() {
        return new Token(TokenId.increment, lexeme, sourceManager.getLineNumber());
    }

    public Token minusState(){
        if (actualChar == '-'){
            updateLexeme();
            updateActualChar();
            return decrementState();
        } else {
            return new Token(TokenId.op_minus, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token decrementState() {
        return new Token(TokenId.decrement, lexeme, sourceManager.getLineNumber());
    }

    public Token notState(){
        if (actualChar == '='){
            updateLexeme();
            updateActualChar();
            return notEqualState();
        } else {
            return new Token(TokenId.op_not, lexeme, sourceManager.getLineNumber());
        }
    }

    public Token notEqualState() {
        return new Token(TokenId.op_notEqual, lexeme, sourceManager.getLineNumber());
    }

    public Token multiplicationState() {
        return new Token(TokenId.op_multiplication, lexeme, sourceManager.getLineNumber());
    }

    public Token moduleState() {
        return new Token(TokenId.op_module, lexeme, sourceManager.getLineNumber());
    }

    public Token andState_1() throws LexicalException{
        if (actualChar == '&'){
            updateLexeme();
            updateActualChar();
            return andState_2();
        } else {
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), "Operador invalido.");
        }
    }

    public Token andState_2() {
        return new Token(TokenId.op_and, lexeme, sourceManager.getLineNumber());
    }

    public Token orState_1() throws LexicalException{
        if (actualChar == '|'){
            updateLexeme();
            updateActualChar();
            return orState_2();
        } else {
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), "Operador invalido.");
        }
    }

    public Token orState_2() {
        return new Token(TokenId.op_or, lexeme, sourceManager.getLineNumber());
    }

    public Token openParenthesisState() {
        return new Token(TokenId.punt_openParenthesis, lexeme, sourceManager.getLineNumber());
    }

    public Token closeParenthesisState() {
        return new Token(TokenId.punt_closeParenthesis, lexeme, sourceManager.getLineNumber());
    }

    public Token openKeyState() {
        return new Token(TokenId.punt_openKey, lexeme, sourceManager.getLineNumber());
    }

    public Token closeKeyState() {
        return new Token(TokenId.punt_closeKey, lexeme, sourceManager.getLineNumber());
    }

    public Token dotState() {
        return new Token(TokenId.punt_dot, lexeme, sourceManager.getLineNumber());
    }

    public Token comaState() {
        return new Token(TokenId.punt_coma, lexeme, sourceManager.getLineNumber());
    }

    public Token semicolonState() {
        return new Token(TokenId.punt_semicolon, lexeme, sourceManager.getLineNumber());
    }

    public Token colonState() {
        return new Token(TokenId.punt_colon, lexeme, sourceManager.getLineNumber());
    }
}
