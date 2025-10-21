package TablaDeSimbolos.NodosAST.sentencia;

import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import lexical.Token;

public class NodoWhile extends NodoSentencia{
    private NodoExpresion condicion;
    private NodoSentencia cuerpo;
    private Token tokenWhile;

    public NodoWhile(NodoExpresion condicion, NodoSentencia cuerpo, Token tokenWhile) {
        this.condicion = condicion;
        this.cuerpo = cuerpo;
        this.tokenWhile = tokenWhile;
    }

    public NodoExpresion getCondicion() {
        return condicion;
    }

    public NodoSentencia getCuerpo() {
        return cuerpo;
    }

    public Token getTokenWhile() {
        return tokenWhile;
    }

    @Override
    public void chequear() {
        // Implementar chequeo semántico para el nodo while
    }

    @Override
    public void generar() {
        // Implementar generación de código para el nodo while
    }

    @Override
    public boolean isReturn() {
        return false; //Implement return
    }

}
