package TablaDeSimbolos;

import lexical.Token;

public class Atributo {
    private Token nombre;
    private Tipo tipo;

    public Atributo(Token nombre, Tipo tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre.getLexeme();
    }

    public Token getToken() {
        return nombre;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void estaBienDeclarado() {
        if (tipo.getToken().getTokenId().toString().equals("id_Class")) {
            throw new RuntimeException("Error: El atributo " + this.nombre + " no tiene un tipo declarado.");
        }
    }
}
