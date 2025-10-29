package TablaDeSimbolos.Tipos;

import lexical.Token;
import lexical.TokenId;

public class TipoPrimitivo implements Tipo {
    private Token token;

    public TipoPrimitivo(Token token) {
        this.token = token;
    }

    public boolean esCompatibleCon(Tipo otroTipo) {
        if (otroTipo instanceof TipoNull){
            return false;
        } else {
            if (token.getTokenId() == TokenId.kw_int) {
                return otroTipo.getTokenPropio().getTokenId().equals(TokenId.kw_int) ||
                        otroTipo.getTokenPropio().getTokenId().equals(TokenId.lit_int);
            }

            if (token.getTokenId() == TokenId.kw_char){
                return otroTipo.getTokenPropio().getTokenId().equals(TokenId.kw_char) ||
                        otroTipo.getTokenPropio().getTokenId().equals(TokenId.lit_char);
            }

            if (token.getTokenId() == TokenId.kw_boolean){
                return otroTipo.getTokenPropio().getTokenId().equals(TokenId.kw_boolean) ||
                        otroTipo.getTokenPropio().getTokenId().equals(TokenId.lit_boolean);
            }
        }

        return otroTipo instanceof TipoPrimitivo;
    }

    public boolean esPrimitivo() {
        return true;
    }

    @Override
    public Token getTokenPropio() {
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
