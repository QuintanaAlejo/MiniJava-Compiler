package TablaDeSimbolos.NodosAST.expresion.acceso;

import TablaDeSimbolos.NodosAST.encadenado.NodoEncadenado;
import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import TablaDeSimbolos.Tipo;
import lexical.Token;

import java.util.List;

public class NodoLlamadaMetodoEstatico extends NodoAcceso {
    private Token clase;
    private Token metodo;
    private List<NodoExpresion> parametros;
    private NodoEncadenado encadenado;

    public NodoLlamadaMetodoEstatico(Token clase, Token metodo) {
        this.clase = clase;
        this.metodo = metodo;
    }

    public void setParametros(List<NodoExpresion> parametros) {
        this.parametros = parametros;
    }

    public void setEncadenado(NodoEncadenado encadenado) {
        this.encadenado = encadenado;
    }

    @Override
    public void generar() {
        // Lógica para generar código para la llamada a metodo estático
    }

    @Override
    public Tipo chequear() {
        // Lógica para chequear el tipo de la llamada a metodo estático
        return null;
    }

}
