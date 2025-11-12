package TablaDeSimbolos.NodosAST.sentencia;

import TablaDeSimbolos.NodosAST.encadenado.NodoMetodoLlamadaEncadenada;
import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.NodosAST.expresion.NodoExpresionAsignacion;
import TablaDeSimbolos.NodosAST.expresion.acceso.NodoLlamadaConstructor;
import TablaDeSimbolos.NodosAST.expresion.acceso.NodoLlamadaMetodo;
import TablaDeSimbolos.NodosAST.expresion.acceso.NodoLlamadaMetodoEstatico;
import exceptions.SemanticException;
import lexical.Token;

public class NodoAsignacion extends NodoSentencia{
    private Token token;
    private NodoExpresion expresion;

    public NodoAsignacion(Token token, NodoExpresion expresion) {
        this.token = token;
        this.expresion = expresion;
    }

    @Override
    public void chequear() throws SemanticException {
        expresion.chequear();
        if (expresion instanceof NodoLlamadaConstructor){
            return;
        }
        if (!esValida() && !expresion.tieneEncadenado()) {
            throw new SemanticException(token.getLexeme(), "Expresión suelta como sentencia.", token.getLinea());
        }
    }

    private boolean esValida() {
        return (expresion instanceof NodoExpresionAsignacion ||
                expresion instanceof NodoLlamadaMetodo ||
                expresion instanceof NodoLlamadaMetodoEstatico );
    }
}
