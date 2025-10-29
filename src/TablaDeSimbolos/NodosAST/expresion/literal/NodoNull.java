package TablaDeSimbolos.NodosAST.expresion.literal;

import TablaDeSimbolos.NodosAST.expresion.operandos.NodoLiteral;
import TablaDeSimbolos.Tipos.Tipo;
import TablaDeSimbolos.Tipos.TipoNull;
import lexical.Token;

public class NodoNull extends NodoLiteral {
    private Token token;

    public NodoNull (Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public boolean tieneEncadenado() {
        return false;
    }

    @Override
    public Tipo chequear() {
        return new TipoNull();
    }
}
