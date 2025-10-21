package TablaDeSimbolos.NodosAST.expresion;

import TablaDeSimbolos.Tipo;
import exceptions.SemanticException;
import lexical.Token;

public abstract class NodoExpresion {
    public abstract Tipo chequear() throws SemanticException;

    public abstract void generar();
}
