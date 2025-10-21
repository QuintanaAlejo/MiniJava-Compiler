package TablaDeSimbolos.NodosAST.expresion.operandos;

import TablaDeSimbolos.NodosAST.encadenado.NodoEncadenado;
import TablaDeSimbolos.NodosAST.expresion.NodoOperando;

public class NodoAcceso extends NodoOperando {
    private NodoEncadenado encadenado;

    public NodoAcceso(){

    }

    public void setEncadenado(NodoEncadenado encadenado){
        this.encadenado = encadenado;
    }
}
