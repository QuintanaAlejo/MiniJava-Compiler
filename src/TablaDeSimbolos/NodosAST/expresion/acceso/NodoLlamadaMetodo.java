package TablaDeSimbolos.NodosAST.expresion.acceso;

import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import lexical.Token;

import java.util.List;

public class NodoLlamadaMetodo extends NodoAcceso {
    private Token id;
    private List<NodoExpresion> argumentos;

    public NodoLlamadaMetodo(Token id, List<NodoExpresion> argumentos) {
        this.id = id;
        this.argumentos = argumentos;
    }
}
