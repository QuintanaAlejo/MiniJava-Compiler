package TablaDeSimbolos.NodosAST.encadenado;

import lexical.Token;

public class NodoVariableEncadeanda extends NodoEncadenado{
    private Token token;
    private NodoEncadenado siguiente;

    public NodoVariableEncadeanda(Token token) {
        this.token = token;
    }
}
