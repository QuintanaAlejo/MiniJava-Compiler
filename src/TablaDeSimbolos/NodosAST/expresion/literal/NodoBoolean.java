package TablaDeSimbolos.NodosAST.expresion.literal;

import TablaDeSimbolos.NodosAST.expresion.operandos.NodoLiteral;
import TablaDeSimbolos.Tipo;
import lexical.Token;

public class NodoBoolean extends NodoLiteral {
    private Token token;

    public NodoBoolean(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public Tipo chequear() {
        //Implementar
        return null;
    }

    @Override
    public void generar() {
        //Implementar
    }

}
