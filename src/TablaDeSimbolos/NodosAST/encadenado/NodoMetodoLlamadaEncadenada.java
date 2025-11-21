package TablaDeSimbolos.NodosAST.encadenado;

import Main.Main;
import TablaDeSimbolos.Clase;
import TablaDeSimbolos.Metodo;
import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.Tipos.Tipo;
import TablaDeSimbolos.Tipos.TipoPrimitivo;
import exceptions.SemanticException;
import lexical.Token;
import lexical.TokenId;

import java.util.List;

public class NodoMetodoLlamadaEncadenada extends NodoEncadenado{
    private NodoEncadenado siguiente;
    private List<NodoExpresion> argumentos;
    private Token id;
    private boolean ladoIzquierdo = false;
    private Clase anterior;

    public NodoMetodoLlamadaEncadenada(Token id){
        this.id = id;
    }

    @Override
    public void setArgumentos(List<NodoExpresion> argumentos){
        this.argumentos = argumentos;
    }

    public void setSiguiente(NodoEncadenado siguiente){
        this.siguiente = siguiente;
    }

    @Override
    public boolean terminaEnVariable() {
        return (this.siguiente != null) && this.siguiente.terminaEnVariable();
    }

    public void chequearParametros(Clase claseAnterior) throws SemanticException {
        var metodo = claseAnterior.getMetodos().get(id.getLexeme());
        if (metodo == null) {
            throw new SemanticException(id.getLexeme(), "El metodo " + id.getLexeme() + " no existe en la clase " + Main.TS.getClaseActual().getNombre(), id.getLinea());
        }
        var parametrosFormales = metodo.getParametros();
        var iteradorParametrosFormales = parametrosFormales.values().iterator();

        if (argumentos != null){
            if (argumentos.size() != parametrosFormales.size()) {
                throw new SemanticException(id.getLexeme(), "Cantidad de argumentos incorrecta en la llamada al metodo " + id.getLexeme(), id.getLinea());
            }

            for (NodoExpresion p: argumentos) {
                Tipo tipoArgumento = p.chequear();
                Tipo tipoParametroFormal = iteradorParametrosFormales.next().getTipo();

                if (!tipoArgumento.esCompatibleCon(tipoParametroFormal)) {
                    throw new SemanticException(id.getLexeme(), "Tipo de argumento incompatible para el metodo " + id.getLexeme(), id.getLinea());
                }
            }
        }
    }

    public Tipo chequear(Tipo tipoAnterior) throws SemanticException {
        if(tipoAnterior instanceof TipoPrimitivo){
            throw new SemanticException(id.getLexeme(), "No se puede encadenar a un tipo primitivo", id.getLinea());
        }

        if(tipoAnterior == null){
            throw new SemanticException(id.getLexeme(), "El metodo no tiene atributos.", id.getLinea());
        }

        anterior = Main.TS.getClase(tipoAnterior.getNombre());
        if (anterior == null) {
            throw new SemanticException(id.getLexeme(), "La clase " + tipoAnterior.getNombre() + " no existe", id.getLinea());
        }

        Metodo metodo = anterior.getMetodos().get(id.getLexeme());
        if (metodo == null) {
            throw new SemanticException(id.getLexeme(), "El metodo " + id.getLexeme() + " no existe en la clase " + anterior.getNombre(), id.getLinea());
        }

        chequearParametros(anterior);

        Tipo tipoActual = metodo.getTipoRetorno();
        if (siguiente != null) {
            return siguiente.chequear(tipoActual);
        } else {
            return tipoActual;
        }
    }

    @Override
    public void setEsLadoIzquierdo(boolean ladoIzquierdo){
        this.ladoIzquierdo = ladoIzquierdo;
    }

    @Override
    public void generar(){
        Metodo m = anterior.getMetodos().get(id.getLexeme());

        if (m != null){
            if (m.getTipoRetorno() != null && !m.getTipoRetorno().getTokenPropio().getTokenId().equals(TokenId.kw_void)){
                Main.TS.getInstructionList().add("RMEM 1");
                Main.TS.getInstructionList().add("SWAP");
            }
            for (NodoExpresion exp : argumentos){
                exp.generar();
                Main.TS.getInstructionList().add("SWAP");
            }
            Main.TS.getInstructionList().add("DUP");
            Main.TS.getInstructionList().add("LOADREF 0; Cargo VT");
            Main.TS.getInstructionList().add("LOADREF "+m.getOffset()+"; Cargo el metodo "+m.getNombre());
            Main.TS.getInstructionList().add("CALL");
        }
        if (siguiente != null){
            if (ladoIzquierdo){
                siguiente.setEsLadoIzquierdo(true);
            }
            siguiente.generar();
        }
    }
}
