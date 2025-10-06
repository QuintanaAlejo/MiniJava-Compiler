package TablaDeSimbolos;

import lexical.Token;
import exceptions.SemanticException;

public class Parametro {
    public Token nombre;
    public Tipo tipo;

    public Parametro(Token nombre, Tipo tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre.getLexeme();
    }

    public Token getToken() {
        return nombre;
    }

    public void estaBienDeclarado() throws SemanticException {
        if (this.tipo == null) {
            throw new SemanticException(this.nombre.getLexeme(), "El parámetro no tiene un tipo declarado.", this.nombre.getLinea());
        }
    }
}
