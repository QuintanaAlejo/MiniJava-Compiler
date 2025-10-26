package TablaDeSimbolos.Tipos;

public class TipoChar extends TipoPrimitivo{
    public TipoChar(){
        super(new lexical.Token(lexical.TokenId.lit_char, "char", 0));
    }

    @Override
    public boolean esCompatibleCon(Tipo otroTipo) {
        return otroTipo instanceof TipoChar;
    }
}
