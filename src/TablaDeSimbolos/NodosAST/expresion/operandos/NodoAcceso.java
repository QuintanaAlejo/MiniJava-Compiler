package TablaDeSimbolos.NodosAST.expresion.operandos;

import TablaDeSimbolos.NodosAST.encadenado.NodoEncadenado;
import TablaDeSimbolos.NodosAST.expresion.NodoOperando;
import TablaDeSimbolos.Tipos.Tipo;
import exceptions.SemanticException;

public abstract class NodoAcceso extends NodoOperando {
    public abstract Tipo chequear() throws SemanticException;
    public abstract void setEncadenado(NodoEncadenado encadenado);
}
