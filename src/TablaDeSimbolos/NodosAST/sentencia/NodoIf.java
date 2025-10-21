package TablaDeSimbolos.NodosAST.sentencia;

import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import exceptions.SemanticException;

public class NodoIf extends NodoSentencia {
    private NodoExpresion condicion;
    private NodoSentencia sentenciaThen;
    private NodoSentencia sentenciaElse;

    public NodoIf(NodoExpresion condicion, NodoSentencia sentenciaThen, NodoSentencia sentenciaElse) {
        this.condicion = condicion;
        this.sentenciaThen = sentenciaThen;
        this.sentenciaElse = sentenciaElse;
    }

    //Getters
    public NodoExpresion getCondicion() {
        return condicion;
    }

    public NodoSentencia getSentenciaThen() {
        return sentenciaThen;
    }

    public NodoSentencia getSentenciaElse() {
        return sentenciaElse;
    }

    @Override
    public void chequear() throws SemanticException {
        // Implementación del chequeo semántico para el nodo if
    }

    @Override
    public void generar() {
        // Implementación de la generación de código para el nodo if
    }

    @Override
    public boolean isReturn() {
        //Implement return
        return true;
    }
}
