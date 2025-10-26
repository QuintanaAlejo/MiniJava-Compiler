package TablaDeSimbolos.NodosAST.expresion;

import exceptions.SemanticException;
import TablaDeSimbolos.Tipos.Tipo;

public abstract class NodoOperando extends NodoExpresion{
    public abstract Tipo chequear() throws SemanticException;
}
