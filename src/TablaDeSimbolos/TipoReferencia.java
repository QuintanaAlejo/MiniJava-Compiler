package TablaDeSimbolos;

import lexical.Token;

public class TipoReferencia implements Tipo{
    private Token token;

    public TipoReferencia(Token token) {
        this.token = token;
    }

    @Override
    public Token getToken() {
        return token;
    }

    @Override
    public String getNombre() {
        return token.getLexeme();
    }
}
