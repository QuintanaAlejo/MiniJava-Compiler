package TablaDeSimbolos;

import TablaDeSimbolos.NodosAST.sentencia.NodoBloque;
import lexical.Token;
import java.util.HashMap;
import exceptions.SemanticException;

public class Constructor {
    private Token token;
    private HashMap<String, Parametro> parametros;
    private NodoBloque bloque;

    public Constructor (Token token) {
        this.token = token;
        this.parametros = new HashMap<>();
    }

    public HashMap<String, Parametro> getParametros() {
        return parametros;
    }

    public void estaBienDeclarado() throws SemanticException {
        for (Parametro parametro : this.parametros.values()) {
            parametro.estaBienDeclarado();
        }
    }

    public void chequear() throws SemanticException {
        if (this.bloque != null) {
            this.bloque.chequear();
        }
    }

    public String getNombre() {
        return token.getLexeme();
    }

    public Token getToken() {
        return token;
    }

    public void setBloque(NodoBloque bloque) {
        this.bloque = bloque;
    }

    public void agregarParametro(Parametro parametro) throws SemanticException {
        if (this.parametros.containsKey(parametro.getNombre())) {
            throw new SemanticException(parametro.getNombre(), "Parámetro repetido", parametro.getToken().getLinea()); //Ver
        }
        this.parametros.put(parametro.getNombre(), parametro);
    }
}
