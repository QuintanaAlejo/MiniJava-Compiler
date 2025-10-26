package TablaDeSimbolos;

import TablaDeSimbolos.Tipos.Tipo;
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
        if (tipo.getTokenPropio().getTokenId().toString().equals("id_Class")) {
            if (!TS.existeClase(tipo.getTokenPropio().getLexeme())) {
                throw new SemanticException(tipo.getTokenPropio().getLexeme(), "El atributo " + nombre.getLexeme() + " es de tipo " + tipo.getTokenPropio().getLexeme() + " y la clase " + tipo.getTokenPropio().getLexeme() + " no existe.", tipo.getTokenPropio().getLinea());
            }
        }
    }
}
