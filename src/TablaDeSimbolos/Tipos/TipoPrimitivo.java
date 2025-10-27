package TablaDeSimbolos.Tipos;

import lexical.Token;

public class TipoPrimitivo implements Tipo {
    private Token token;

    public TipoPrimitivo(Token token) {
        this.token = token;
    }

    public boolean esCompatibleCon(Tipo otroTipo) {
        if (otroTipo instanceof TipoNull){
            return false;
        } else {
            return otroTipo instanceof TipoPrimitivo;
        }
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
