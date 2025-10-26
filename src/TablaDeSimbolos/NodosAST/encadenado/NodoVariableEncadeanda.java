package TablaDeSimbolos.NodosAST.encadenado;

import TablaDeSimbolos.Tipos.Tipo;
import lexical.Token;

public class NodoVariableEncadeanda extends NodoEncadenado{
    private Token token;
    private NodoEncadenado siguiente;

    public NodoVariableEncadeanda(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public Tipo chequear(Tipo tipoAnterior) {
        // Implementar la lógica de chequeo de tipos para la variable encadenada
        return null; // Placeholder
    }

    public void setSiguiente(NodoEncadenado siguiente) {
        this.siguiente = siguiente;
    }

}
