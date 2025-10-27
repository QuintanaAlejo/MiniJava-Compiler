package TablaDeSimbolos.Tipos;

import lexical.TokenId;

public class TipoInt extends TipoPrimitivo{
    public TipoInt(){
        super(new lexical.Token(lexical.TokenId.lit_int, "int", 0));
    }

    @Override
    public boolean esCompatibleCon(Tipo otroTipo) {
        if (otroTipo instanceof TipoInt) {
            return true;
        } else {
            return otroTipo.getTokenPropio().getTokenId().equals(TokenId.kw_int) ||
                    otroTipo.getTokenPropio().getTokenId().equals(TokenId.lit_int);
        }
    }
}
