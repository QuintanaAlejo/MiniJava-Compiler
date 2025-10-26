package TablaDeSimbolos.NodosAST.expresion;

import TablaDeSimbolos.Tipos.Tipo;
import TablaDeSimbolos.Tipos.TipoPrimitivo;
import lexical.Token;
import lexical.TokenId;

public class NodoExpresionVacia extends NodoExpresion{
    @Override
    public Tipo chequear() {
        return new TipoPrimitivo(new Token(TokenId.universal, "", 0));
    }
}
