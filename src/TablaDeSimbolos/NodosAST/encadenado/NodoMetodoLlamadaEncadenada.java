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

    public NodoMetodoLlamadaEncadenada(Token id){
        this.id = id;
    }

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

        Clase anterior = Main.TS.getClase(tipoAnterior.getNombre());
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
}
