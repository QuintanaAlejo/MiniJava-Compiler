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
        }
        return null;
    }

    private void updateLexeme(){
        lexeme += actualChar;
    }

    private void updateActualChar() throws IOException {
        actualChar = sourceManager.getNextChar();
    }
}
