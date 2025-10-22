package TablaDeSimbolos.NodosAST.expresion.acceso;

import TablaDeSimbolos.NodosAST.encadenado.NodoEncadenado;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import TablaDeSimbolos.Tipo;
import lexical.Token;

public class NodoThis extends NodoAcceso {
    private Token token;
    private String nombreClase;
    private NodoEncadenado encadenadoOpcional;

    public NodoThis(Token token, String nombreClase) {
        this.token = token;
        this.nombreClase = nombreClase;
    }

    public Token getToken() {
        return token;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    @Override
    public void generar() {
        // Implementar la lógica de generación de código para 'this'
    }

    @Override
    public Tipo chequear() {
        // Implementar la lógica de chequeo de tipos para 'this'
        return null;
    }
}
