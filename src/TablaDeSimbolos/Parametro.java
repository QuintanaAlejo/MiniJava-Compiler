package TablaDeSimbolos;

import TablaDeSimbolos.Tipos.Tipo;
import lexical.Token;
import exceptions.SemanticException;

import static Main.Main.TS;

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

    public Tipo getTipo() {
        return tipo;
    }

    public void estaBienDeclarado() throws SemanticException {
        if (tipo.getTokenPropio().getTokenId().toString().equals("id_Class")) {
            if (!TS.existeClase(tipo.getTokenPropio().getLexeme())) {
                throw new SemanticException(tipo.getTokenPropio().getLexeme(), "El parámetro " + nombre.getLexeme() + " es de tipo " + tipo.getTokenPropio().getLexeme() + " y la clase " + tipo.getTokenPropio().getLexeme() + " no existe.", nombre.getLinea());
            }
        }
    }
}
