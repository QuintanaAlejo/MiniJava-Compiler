package TablaDeSimbolos;

import lexical.Token;

public class TipoPrimitivo implements Tipo{
    private Token token;

    public TipoPrimitivo(Token token) {
        this.token = token;
    }

    @Override
    public Token getToken() {
        return token;
    }

    @Override
    public void estaBienDeclarado() {
        // Los tipos primitivos siempre están bien declarados
    }

    @Override
    public String getNombre() {
        return token.getLexeme();
    }
}
