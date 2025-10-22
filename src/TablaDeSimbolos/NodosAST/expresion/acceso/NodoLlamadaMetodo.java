package TablaDeSimbolos.NodosAST.expresion.acceso;

import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import lexical.Token;
import TablaDeSimbolos.Tipo;

import java.util.List;

public class NodoLlamadaMetodo extends NodoAcceso {
    private Token id;
    private List<NodoExpresion> argumentos;

    public NodoLlamadaMetodo(Token id, List<NodoExpresion> argumentos) {
        this.id = id;
        this.argumentos = argumentos;
    }

    @Override
    public void generar() {
        // Implementar la lógica de generación de código para la llamada al método
    }

    @Override
    public Tipo chequear() {
        // Implementar la lógica de chequeo de tipos para la llamada al método
        return null;
    }
}
