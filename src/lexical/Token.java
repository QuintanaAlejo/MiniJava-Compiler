package lexical;

public class Token {

    TokenId tokenId;
    String lexeme;
    int line;

    public Token (TokenId tokenId, String lexeme, int linea){
        this.tokenId = tokenId;
        this.lexeme = lexeme;
        this.line = linea;
    }
    public TokenId getTokenId(){
        return tokenId;
    }
    public String getLexeme(){
        return lexeme;
    }
    public int getLinea(){
        return line;
    }
}