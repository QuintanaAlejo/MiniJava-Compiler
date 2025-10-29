package TablaDeSimbolos.NodosAST.expresion.literal;

import TablaDeSimbolos.NodosAST.expresion.operandos.NodoLiteral;
import TablaDeSimbolos.Tipos.Tipo;
import TablaDeSimbolos.Tipos.TipoChar;
import lexical.Token;

public class NodoChar extends NodoLiteral {
    private Token token;

    public NodoChar(Token token) {
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
        return new TipoChar();
    }

}
