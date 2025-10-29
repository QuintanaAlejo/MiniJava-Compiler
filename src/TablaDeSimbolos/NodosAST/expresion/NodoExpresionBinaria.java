package TablaDeSimbolos.NodosAST.expresion;

import TablaDeSimbolos.Tipos.Tipo;
import TablaDeSimbolos.Tipos.TipoBooleano;
import TablaDeSimbolos.Tipos.TipoInt;
import exceptions.SemanticException;
import lexical.Token;
import lexical.TokenId;

public class NodoExpresionBinaria extends NodoExpresionCompuesta{
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


    private boolean sonAmbosEnteros(Tipo tipoIzquierda, Tipo tipoDerecha) {
        // Chequear si ambos son distintos a null
        if (tipoIzquierda == null || tipoDerecha == null) {
            return false;
        }
        return tipoIzquierda.esCompatibleCon(new TipoInt()) && tipoIzquierda.esCompatibleCon(tipoDerecha);
    }

    private boolean sonAmbosBooleanos(Tipo tipoIzquierda, Tipo tipoDerecha) {
        if (tipoIzquierda == null || tipoDerecha == null) {
            return false;
        }
        return tipoIzquierda.esCompatibleCon(new TipoBooleano()) && tipoIzquierda.esCompatibleCon(tipoDerecha);
    }

    @Override
    public boolean tieneEncadenado() {
        return false;
    }

    @Override
    public Tipo chequear() throws SemanticException {
        Tipo tipoIzquierda = izquierda.chequear();
        Tipo tipoDerecha = derecha.chequear();

        switch (operador.getTokenId()) {
            case op_plus:
            case op_minus:
            case op_multiplication:
            case op_division:
            case op_module:
                if (sonAmbosEnteros(tipoIzquierda, tipoDerecha)) {
                    return new TipoInt();
                } else {
                    throw new SemanticException(operador.getLexeme(), "Tipos incompatibles para el operador " + operador.getLexeme(), operador.getLinea());
                }
            case op_greater:
            case op_less:
            case op_greaterOrEqual:
            case op_lessOrEqual:
                if (sonAmbosEnteros(tipoIzquierda, tipoDerecha)) {
                    return new TipoBooleano();
                } else {
                    throw new SemanticException(operador.getLexeme(), "Tipos incompatibles para el operador " + operador.getLexeme(), operador.getLinea());
                }
            case op_and:
            case op_or:
                if (sonAmbosBooleanos(tipoIzquierda, tipoDerecha)) {
                    return new TipoBooleano();
                } else {
                    throw new SemanticException(operador.getLexeme(), "Tipos incompatibles para el operador " + operador.getLexeme(), operador.getLinea());
                }
            case op_equal:
            case op_notEqual:
                if ((tipoIzquierda != null && (tipoIzquierda.esCompatibleCon(tipoDerecha) || tipoDerecha.esCompatibleCon(tipoIzquierda)))) {
                    return new TipoBooleano();
                } else {
                    throw new SemanticException(operador.getLexeme(), "Los tipos no son compatibles para el operador " + operador.getLexeme(), operador.getLinea());
                }
        }
        throw new SemanticException(operador.getLexeme(), "Expresion binaria invalida", operador.getLinea());
    }
}
