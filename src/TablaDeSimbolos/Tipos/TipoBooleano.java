package TablaDeSimbolos.Tipos;

import lexical.Token;
import lexical.TokenId;

public class TipoBooleano extends TipoPrimitivo{
    public TipoBooleano(){
        super(new Token(TokenId.lit_boolean, "boolean", 0));
    }

    public boolean esCompatibleCon(Tipo otroTipo) {
        if (otroTipo instanceof TipoBooleano) {
            return true;
        } else {
            return otroTipo.getTokenPropio().getTokenId().equals(TokenId.kw_boolean) ||
                    otroTipo.getTokenPropio().getTokenId().equals(TokenId.lit_boolean);
        }
    }
}
