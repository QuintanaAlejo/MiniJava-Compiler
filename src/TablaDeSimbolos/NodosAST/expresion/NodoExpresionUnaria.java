package TablaDeSimbolos.NodosAST.expresion;

import TablaDeSimbolos.Tipo;
import lexical.Token;

public class NodoExpresionUnaria extends NodoExpresion{
    private Token operador;
    private NodoOperando operando;

    public NodoExpresionUnaria(Token operador, NodoOperando operando) {
        this.operador = operador;
        this.operando = operando;
    }

    // Getters y setters
    public Token getOperador() {
        return operador;
    }

    public NodoOperando getOperando() {
        return operando;
    }

    @Override
    public Tipo chequear() {
        // Implementación del chequeo semántico para la expresión unaria
        return null;
    }

    @Override
    public void generar() {
        // Implementación de la generación de código para la expresión unaria
    }
}
