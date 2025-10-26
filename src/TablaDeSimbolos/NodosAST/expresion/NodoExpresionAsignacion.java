package TablaDeSimbolos.NodosAST.expresion;

import TablaDeSimbolos.Tipos.Tipo;
import exceptions.SemanticException;
import lexical.Token;

public class NodoExpresionAsignacion extends NodoExpresionCompuesta{
    private NodoExpresion izquierda;
    private NodoExpresion derecha;
    private Token token;

    public NodoExpresionAsignacion(NodoExpresion izquierda, NodoExpresion derecha, Token token) {
        this.izquierda = izquierda;
        this.derecha = derecha;
        this.token = token;
    }

    @Override
    public Tipo chequear() throws SemanticException {
        Tipo destino = izquierda.chequear();
        Tipo origen = derecha.chequear();

        if (destino.esCompatibleCon(origen)) {
            return destino;
        } else {
            throw new SemanticException(token.getLexeme(), "Asignacion invalida: no se puede asignar un valor de tipo " + origen.getNombre() + " a una variable de tipo " + destino.getNombre(), token.getLinea());
        }

        //TODO: COMO CHEQUEO ENCADENADOS?
    }
}
