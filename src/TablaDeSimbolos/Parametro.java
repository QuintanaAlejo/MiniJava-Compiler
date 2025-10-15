package TablaDeSimbolos;

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
        if (tipo.getToken().getTokenId().toString().equals("id_Class")) {
            if (!TS.existeClase(tipo.getToken().getLexeme())) {
                throw new SemanticException(tipo.getToken().getLexeme(), "El parámetro " + nombre.getLexeme() + " es de tipo " + tipo.getToken().getLexeme() + " y la clase " + tipo.getToken().getLexeme() + " no existe.", nombre.getLinea());
            }
        }
    }
}
