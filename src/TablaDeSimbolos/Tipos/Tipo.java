package TablaDeSimbolos.Tipos;

import exceptions.SemanticException;
import lexical.Token;

public interface Tipo {
    Token getTokenPropio();
    String getNombre();
    void estaBienDeclarado() throws SemanticException;
    public boolean esCompatibleCon(Tipo otroTipo);
    public boolean esPrimitivo();
}
