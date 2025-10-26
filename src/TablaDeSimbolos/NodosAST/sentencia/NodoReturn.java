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
}
