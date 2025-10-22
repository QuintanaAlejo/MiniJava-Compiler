package TablaDeSimbolos.NodosAST.expresion;

import TablaDeSimbolos.Tipo;

public class NodoExpresionVacia extends NodoExpresion{
    @Override
    public Tipo chequear() {
        return null;
    }

    @Override
    public void generar() {

    }
}
