package TablaDeSimbolos.Tipos;

public class TipoInt extends TipoPrimitivo{
    public TipoInt(){
        super(new lexical.Token(lexical.TokenId.lit_int, "int", 0));
    }

    @Override
    public boolean esCompatibleCon(Tipo otroTipo) {
        return otroTipo instanceof TipoInt;
    }
}
