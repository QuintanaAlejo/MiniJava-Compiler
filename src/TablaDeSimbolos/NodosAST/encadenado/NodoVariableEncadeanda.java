package TablaDeSimbolos.NodosAST.encadenado;

import Main.Main;
import TablaDeSimbolos.Atributo;
import TablaDeSimbolos.Clase;
import TablaDeSimbolos.Tipos.Tipo;
import TablaDeSimbolos.Tipos.TipoPrimitivo;
import exceptions.SemanticException;
import lexical.Token;

public class NodoVariableEncadeanda extends NodoEncadenado{
    private Token token;
    private NodoEncadenado siguiente;

    public NodoVariableEncadeanda(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public boolean terminaEnVariable(){
        return siguiente == null;
    }

    public Tipo chequear(Tipo tipoAnterior) throws SemanticException {
        if(tipoAnterior instanceof TipoPrimitivo){
            throw new SemanticException(token.getLexeme(), "No se puede encadenar a un tipo primitivo", token.getLinea());
        }

        if(tipoAnterior == null){
            throw new SemanticException(token.getLexeme(), "El metodo no tiene atributos.", token.getLinea());
        }

        Clase anterior = Main.TS.getClase(tipoAnterior.getNombre());
        if (anterior == null) {
            throw new SemanticException(token.getLexeme(), "La clase " + tipoAnterior.getNombre() + " no existe", token.getLinea());
        }

        Atributo atributo = anterior.getAtributos().get(token.getLexeme());
        if (atributo == null) {
            throw new SemanticException(token.getLexeme(), "El atributo " + token.getLexeme() + " no existe en la clase " + anterior.getNombre(), token.getLinea());
        }

        Tipo tipoActual = atributo.getTipo();
        if (siguiente != null) {
            return siguiente.chequear(tipoActual);
        } else {
            return tipoActual;
        }
    }

    public void setSiguiente(NodoEncadenado siguiente) {
        this.siguiente = siguiente;
    }

}
