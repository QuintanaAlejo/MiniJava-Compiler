package TablaDeSimbolos.Tipos;

import lexical.TokenId;

public class TipoChar extends TipoPrimitivo{
    public TipoChar(){
        super(new lexical.Token(lexical.TokenId.lit_char, "char", 0));
    }

    @Override
    public boolean esCompatibleCon(Tipo otroTipo) {
        if (otroTipo instanceof TipoChar) {
            return true;
        } else {
            return otroTipo.getTokenPropio().getTokenId().equals(TokenId.kw_char) ||
                    otroTipo.getTokenPropio().getTokenId().equals(TokenId.lit_char);
        }
    }
}
