package TablaDeSimbolos.Tipos;

import lexical.TokenId;

public class TipoNull extends TipoPrimitivo{
    public TipoNull(){
        super(new lexical.Token(TokenId.kw_null, "null", 0));
    }

    @Override
    public boolean esCompatibleCon(Tipo otroTipo) {
        return true; // El tipo null es compatible con cualquier otro tipo
    }
}
