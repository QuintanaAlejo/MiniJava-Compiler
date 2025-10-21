package TablaDeSimbolos.NodosAST.sentencia;

import exceptions.SemanticException;
import lexical.Token;

public abstract class NodoSentencia{
    public abstract void chequear() throws SemanticException;
    public abstract void generar();
    public abstract boolean isReturn();
}
