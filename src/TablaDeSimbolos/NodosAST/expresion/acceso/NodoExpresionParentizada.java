package TablaDeSimbolos.NodosAST.expresion.acceso;

import TablaDeSimbolos.NodosAST.encadenado.NodoEncadenado;
import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import TablaDeSimbolos.Tipo;

public class NodoExpresionParentizada extends NodoAcceso {
    private NodoExpresion expresion;
    private NodoEncadenado encadenado;

    public NodoExpresionParentizada(NodoExpresion expresion) {
        this.expresion = expresion;
    }

    public void setEncadenado(NodoEncadenado encadenado) {
        this.encadenado = encadenado;
    }

    @Override
    public void generar() {
        // Lógica para generar código para el nodo de expresión parentizada
    }

    @Override
    public Tipo chequear() {
        // Lógica para chequear el tipo del nodo de expresión parentizada
        return null;
    }
}
