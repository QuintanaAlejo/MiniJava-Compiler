package TablaDeSimbolos.NodosAST.expresion.acceso;

import TablaDeSimbolos.Clase;
import TablaDeSimbolos.NodosAST.encadenado.NodoEncadenado;
import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import TablaDeSimbolos.Tipo;
import lexical.Token;

import java.util.List;

public class NodoLlamadaConstructor extends NodoAcceso {
    private Token token;
    private Clase clase;
    private List<NodoExpresion> parametros;
    private NodoEncadenado encadenado;

    public NodoLlamadaConstructor(Token token) {
        this.token = token;
    }

    public void setParametros(List<NodoExpresion> parametros) {
        this.parametros = parametros;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public void setEncadenado(NodoEncadenado encadenado) {
        this.encadenado = encadenado;
    }

    @Override
    public void generar() {
        // Implementar la lógica de generación de código para la llamada al constructor
    }

    @Override
    public Tipo chequear() {
        // Implementar la lógica de chequeo de tipos para la llamada al constructor
        return null;
    }
}
