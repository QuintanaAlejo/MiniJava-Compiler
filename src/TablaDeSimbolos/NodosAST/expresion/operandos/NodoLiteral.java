package TablaDeSimbolos.NodosAST.expresion.operandos;

import TablaDeSimbolos.NodosAST.expresion.NodoOperando;
import TablaDeSimbolos.Tipo;
import lexical.Token;

public abstract class NodoLiteral extends NodoOperando {
    public abstract Tipo chequear();
    public abstract void generar();
}
