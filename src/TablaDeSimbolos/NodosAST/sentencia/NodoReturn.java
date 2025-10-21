package TablaDeSimbolos.NodosAST.sentencia;

import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;

public class NodoReturn extends NodoSentencia{
    private NodoExpresion expresion;

    public NodoReturn(NodoExpresion expresion) {
        this.expresion = expresion;
    }

    @Override
    public void chequear() {
        // Implementación del chequeo semántico para el nodo return
    }

    @Override
    public void generar() {
        // Implementación de la generación de código para el nodo return
    }

    @Override
    public boolean isReturn() {
        return true;
    }
}
