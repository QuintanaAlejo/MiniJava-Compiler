package TablaDeSimbolos.NodosAST.expresion;

import TablaDeSimbolos.Tipo;
import lexical.Token;

public class NodoExpresionBinaria extends NodoExpresion{
    private NodoExpresion izquierda;
    private NodoExpresion derecha;
    private Token operador;

    public NodoExpresionBinaria(Token operador){
        this.operador = operador;
    }

    // Getters y Setters
    public NodoExpresion getIzquierda() {
        return izquierda;
    }

    public void setIzquierda(NodoExpresion izquierda) {
        this.izquierda = izquierda;
    }

    public NodoExpresion getDerecha() {
        return derecha;
    }

    public void setDerecha(NodoExpresion derecha) {
        this.derecha = derecha;
    }

    public Token getOperador() {
        return operador;
    }

    @Override
    public Tipo chequear() {
        // Implementar chequeo semántico para la expresión binaria
        return null;
    }

    @Override
    public void generar() {
        // Implementar generación de código para la expresión binaria
    }
}
