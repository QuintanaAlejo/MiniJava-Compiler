package TablaDeSimbolos.NodosAST.encadenado;

import Main.Main;
import TablaDeSimbolos.Atributo;
import TablaDeSimbolos.Clase;
import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.Tipos.Tipo;
import TablaDeSimbolos.Tipos.TipoPrimitivo;
import exceptions.SemanticException;
import lexical.Token;

import java.util.List;

public class NodoVariableEncadeanda extends NodoEncadenado{
    private Token token;
    private NodoEncadenado siguiente;
    private boolean ladoIzquierdo = false;
    private Clase anterior;

    public NodoVariableEncadeanda(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public void setEsLadoIzquierdo(boolean ladoIzquierdo){
        this.ladoIzquierdo = ladoIzquierdo;
    }

    @Override
    public void setArgumentos(List<NodoExpresion> argumentos){

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

        anterior = Main.TS.getClase(tipoAnterior.getNombre());
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

    @Override
    public void generar(){
        Atributo atr = anterior.getAtributos().get(token.getLexeme());
        if (!ladoIzquierdo){
            Main.TS.getInstructionList().add("LOADREF "+atr.getOffset()+"    ; Cargo direc atributo ");
        } else {
            Main.TS.getInstructionList().add("SWAP");
            Main.TS.getInstructionList().add("STOREREF "+atr.getOffset()+"    ; Guardo en la direc del atributo ");
        }

        if (siguiente != null){
            siguiente.generar();
        }
    }

}
