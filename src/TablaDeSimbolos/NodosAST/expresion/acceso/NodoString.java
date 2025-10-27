package TablaDeSimbolos.NodosAST.expresion.acceso;

import TablaDeSimbolos.NodosAST.encadenado.NodoEncadenado;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import TablaDeSimbolos.Tipos.Tipo;
import TablaDeSimbolos.Tipos.TipoReferencia;
import lexical.Token;
import lexical.TokenId;

public class NodoString extends NodoAcceso {
    private Token token;
    private NodoEncadenado encadenado;

    public NodoString(Token token) {
        this.token = token;
    }

    public void setEncadenado(NodoEncadenado encadenado) {
        this.encadenado = encadenado;
    }

    @Override
    public Tipo chequear() {
        return new TipoReferencia(new Token(TokenId.lit_string, "String", token.getLinea()) );
    }
}
