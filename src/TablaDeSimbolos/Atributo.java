package TablaDeSimbolos;

import exceptions.SemanticException;
import lexical.Token;

import static Main.Main.TS;

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

    public void estaBienDeclarado() throws SemanticException {
        if (tipo.getToken().getTokenId().toString().equals("id_Class")) {
            if (!TS.existeClase(tipo.getToken().getLexeme())) {
                throw new SemanticException(tipo.getToken().getLexeme(), "El atributo " + nombre.getLexeme() + " es de tipo " + tipo.getToken().getLexeme() + " y la clase " + tipo.getToken().getLexeme() + " no existe.", tipo.getToken().getLinea());
            }
        }
    }
}
