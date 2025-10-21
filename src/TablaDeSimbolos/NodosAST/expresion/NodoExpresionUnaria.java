package TablaDeSimbolos.NodosAST.expresion;

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


}
