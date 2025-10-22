package TablaDeSimbolos.NodosAST.expresion;

import TablaDeSimbolos.NodosAST.sentencia.NodoAsignacion;
import TablaDeSimbolos.Tipo;
import lexical.Token;

public class NodoExpresionAsignacion extends NodoExpresion{
    private NodoExpresion izquierda;
    private NodoExpresion derecha;
    private Token token;

    public NodoExpresionAsignacion(NodoExpresion izquierda, NodoExpresion derecha, Token token) {
        this.izquierda = izquierda;
        this.derecha = derecha;
        this.token = token;
    }

    @Override
    public Tipo chequear() {
        // Implementar la lógica de chequeo de tipos para la asignación
        return null;
    }

    @Override
    public void generar() {
        // Implementar la lógica de generación de código para la asignación
    }
}
