package TablaDeSimbolos.Tipos;

import lexical.Token;
import lexical.TokenId;

public class TipoBooleano extends TipoPrimitivo{
    public TipoBooleano(){
        super(new Token(TokenId.lit_boolean, "boolean", 0));
    }

    public boolean esCompatibleCon(Tipo otroTipo) {
        return otroTipo instanceof TipoBooleano;
    }
}
