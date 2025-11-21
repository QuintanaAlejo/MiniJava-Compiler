package TablaDeSimbolos.NodosAST.expresion;

import TablaDeSimbolos.Tipos.Tipo;
import exceptions.SemanticException;

public abstract class NodoExpresion {
    protected boolean ladoIzquierdo = false;
    public abstract Tipo chequear() throws SemanticException;
    public abstract boolean tieneEncadenado();
    public abstract void generar();
    public void setEsLadoIzquierdo(boolean ladoIzquierdo){
        this.ladoIzquierdo = ladoIzquierdo;
    }
    public boolean esLadoIzquierdo(){
        return this.ladoIzquierdo;
    }
}
