package TablaDeSimbolos.NodosAST.encadenado;

import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.Tipos.Tipo;
import exceptions.SemanticException;

import java.util.List;

public abstract class NodoEncadenado {
    public abstract Tipo chequear(Tipo tipoAnterior) throws SemanticException;
    public abstract void setSiguiente(NodoEncadenado siguiente);
    public abstract boolean terminaEnVariable();
    public abstract void generar();
    public abstract void setEsLadoIzquierdo(boolean ladoIzquierdo);
    public abstract void setArgumentos(List<NodoExpresion> argumentos);
}
