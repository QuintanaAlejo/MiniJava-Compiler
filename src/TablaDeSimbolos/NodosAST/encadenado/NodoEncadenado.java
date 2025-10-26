package TablaDeSimbolos.NodosAST.encadenado;

import TablaDeSimbolos.Tipos.Tipo;

public abstract class NodoEncadenado {
    public abstract Tipo chequear(Tipo tipoAnterior);
    public abstract void setSiguiente(NodoEncadenado siguiente);
}
