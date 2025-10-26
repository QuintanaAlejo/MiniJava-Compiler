package TablaDeSimbolos.NodosAST.expresion.acceso;

import TablaDeSimbolos.NodosAST.encadenado.NodoEncadenado;
import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import TablaDeSimbolos.Tipos.Tipo;
import exceptions.SemanticException;

public class NodoExpresionParentizada extends NodoAcceso {
    private NodoExpresion expresion;
    private NodoEncadenado encadenado;

    public NodoExpresionParentizada(NodoExpresion expresion) {
        this.expresion = expresion;
    }

    public void setEncadenado(NodoEncadenado encadenado) {
        this.encadenado = encadenado;
    }

    @Override
    public Tipo chequear() throws SemanticException {
        if (encadenado == null) {
            return expresion.chequear();
        } else {
            Tipo tipoExpresion = expresion.chequear();
            return encadenado.chequear(tipoExpresion);
        }
    }
}
