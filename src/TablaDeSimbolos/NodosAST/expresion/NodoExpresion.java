package TablaDeSimbolos.NodosAST.expresion;

import TablaDeSimbolos.Tipos.Tipo;
import exceptions.SemanticException;

public abstract class NodoExpresion {
    public abstract Tipo chequear() throws SemanticException;
    public abstract boolean tieneEncadenado();
}
