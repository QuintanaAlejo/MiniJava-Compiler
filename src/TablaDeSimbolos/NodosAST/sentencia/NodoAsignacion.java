package TablaDeSimbolos.NodosAST.sentencia;

import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
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
    }
}
