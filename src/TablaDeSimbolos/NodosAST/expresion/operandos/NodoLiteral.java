package TablaDeSimbolos.NodosAST.expresion.operandos;

import TablaDeSimbolos.NodosAST.expresion.NodoOperando;
import TablaDeSimbolos.Tipos.Tipo;

public abstract class NodoLiteral extends NodoOperando {
    public abstract Tipo chequear();
}
