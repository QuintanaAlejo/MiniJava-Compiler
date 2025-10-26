package TablaDeSimbolos.NodosAST.expresion;

import TablaDeSimbolos.Tipos.Tipo;
import exceptions.SemanticException;

public abstract class NodoExpresionCompuesta extends NodoExpresion{
    public abstract Tipo chequear() throws SemanticException;
}
