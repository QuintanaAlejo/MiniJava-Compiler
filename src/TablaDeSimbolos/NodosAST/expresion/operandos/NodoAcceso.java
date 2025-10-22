package TablaDeSimbolos.NodosAST.expresion.operandos;

import TablaDeSimbolos.NodosAST.encadenado.NodoEncadenado;
import TablaDeSimbolos.NodosAST.expresion.NodoOperando;
import TablaDeSimbolos.Tipo;

public abstract class NodoAcceso extends NodoOperando {
    private NodoEncadenado encadenado;

    public abstract Tipo chequear();
    public abstract void generar();

    public void setEncadenado(NodoEncadenado encadenado){
        this.encadenado = encadenado;
    }

}
