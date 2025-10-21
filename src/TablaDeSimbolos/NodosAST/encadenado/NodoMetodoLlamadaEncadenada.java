package TablaDeSimbolos.NodosAST.encadenado;

import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.Parametro;
import lexical.Token;

import java.util.ArrayList;

public class NodoMetodoLlamadaEncadenada extends NodoEncadenado{
    private ArrayList<NodoExpresion> parametros;
    Token id;

    public NodoMetodoLlamadaEncadenada(Token id){
        this.id = id;
    }

    public void setParametros(ArrayList<NodoExpresion> parametros){
        this.parametros = parametros;
    }

}
