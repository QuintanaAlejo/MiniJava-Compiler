package TablaDeSimbolos.NodosAST.encadenado;

import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.Parametro;
import lexical.Token;

import java.util.ArrayList;
import java.util.List;

public class NodoMetodoLlamadaEncadenada extends NodoEncadenado{
    private List<NodoExpresion> parametros;
    Token id;

    public NodoMetodoLlamadaEncadenada(Token id){
        this.id = id;
    }

    public void setParametros(List<NodoExpresion> parametros){
        this.parametros = parametros;
    }

}
