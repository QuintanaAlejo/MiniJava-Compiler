package TablaDeSimbolos.NodosAST.encadenado;

import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.Parametro;
import TablaDeSimbolos.Tipos.Tipo;
import lexical.Token;

import java.util.ArrayList;
import java.util.List;

public class NodoMetodoLlamadaEncadenada extends NodoEncadenado{
    private NodoEncadenado siguiente;
    private List<NodoExpresion> parametros;
    private Token id;

    public NodoMetodoLlamadaEncadenada(Token id){
        this.id = id;
    }

    public void setParametros(List<NodoExpresion> parametros){
        this.parametros = parametros;
    }

    public void setSiguiente(NodoEncadenado siguiente){
        this.siguiente = siguiente;
    }

    public Tipo chequear(Tipo tipoAnterior){
        //Implementar
        return null;
    }
}
