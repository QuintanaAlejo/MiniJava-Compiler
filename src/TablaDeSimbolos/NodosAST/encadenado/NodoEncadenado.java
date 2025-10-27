package TablaDeSimbolos.NodosAST.encadenado;

import TablaDeSimbolos.Tipos.Tipo;
import exceptions.SemanticException;

public abstract class NodoEncadenado {
    public abstract Tipo chequear(Tipo tipoAnterior) throws SemanticException;
    public abstract void setSiguiente(NodoEncadenado siguiente);
}
