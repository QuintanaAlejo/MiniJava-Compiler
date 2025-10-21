package TablaDeSimbolos.NodosAST.sentencia;

import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import lexical.Token;

public class NodoVarLocal extends NodoSentencia{
    private Token identificador;
    private NodoExpresion expresion;

    public NodoVarLocal(Token identificador, NodoExpresion expresion) {
        this.identificador = identificador;
        this.expresion = expresion;
    }

    @Override
    public void chequear() {
        // Implementar
    }

    @Override
    public void generar() {
        // Implementar
    }

    @Override
    public boolean isReturn() {
        return false;
    }

}
