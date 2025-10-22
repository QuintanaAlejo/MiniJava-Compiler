package TablaDeSimbolos.NodosAST.sentencia;

import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import lexical.Token;

public class NodoAsignacion extends NodoSentencia{
    private Token token;
    private NodoExpresion expresion;

    public NodoAsignacion(Token token, NodoExpresion expresion) {
        this.token = token;
        this.expresion = expresion;
    }

    @Override
    public void generar() {
        // Implementar la lógica de generación de código para la asignación
    }

    @Override
    public void chequear() {
        // Implementar la lógica de chequeo de tipos para la asignación
    }
}
