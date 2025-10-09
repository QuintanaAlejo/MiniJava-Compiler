package TablaDeSimbolos;

import Main.Main;
import lexical.Token;
import exceptions.SemanticException;

public class TipoReferencia implements Tipo{
    private Token token;

    public TipoReferencia(Token token) {
        this.token = token;
    }

    @Override
    public Token getToken() {
        return token;
    }

    @Override
    public void estaBienDeclarado() throws SemanticException {
        // Consultar si la clase está declarada en la tabla de símbolos
        if (!Main.TS.existeClase(token.getLexeme())){
            throw new SemanticException(token.getLexeme(), "La clase del tipo de retorno no existe.", token.getLinea());
        }
    }

    @Override
    public String getNombre() {
        return token.getLexeme();
    }
}
