package TablaDeSimbolos.NodosAST.sentencia;

import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import lexical.Token;

public abstract class NodoAsignacion extends NodoSentencia{
    private NodoExpresion ladoDerecho;
    private NodoAcceso ladoIzquierdo;

    public NodoAsignacion(NodoAcceso ladoIzquierdo, NodoExpresion ladoDerecho, Token token) {
        this.ladoDerecho = ladoDerecho;
        this.ladoIzquierdo = ladoIzquierdo;
    }

    public void setLadoDerecho(NodoExpresion ladoDerecho) {
        this.ladoDerecho = ladoDerecho;
    }

    public void setLadoIzquierdo(NodoAcceso ladoIzquierdo) {
        this.ladoIzquierdo = ladoIzquierdo;
    }
}
