package TablaDeSimbolos.NodosAST.expresion.acceso;

import Main.Main;
import TablaDeSimbolos.Metodo;
import TablaDeSimbolos.NodosAST.encadenado.NodoEncadenado;
import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import exceptions.SemanticException;
import lexical.Token;
import TablaDeSimbolos.Tipos.Tipo;

import java.util.List;

public class NodoLlamadaMetodo extends NodoAcceso {
    private Token id;
    private List<NodoExpresion> argumentos;
    private NodoEncadenado siguiente;

    public NodoLlamadaMetodo(Token id, List<NodoExpresion> argumentos) {
        this.id = id;
        this.argumentos = argumentos;
    }

    @Override
    public void setEncadenado(NodoEncadenado encadenado) {
        this.siguiente = encadenado;
    }

    @Override
    public Tipo chequear() throws SemanticException {
        Metodo metodo = Main.TS.getClaseActual().getMetodos().get(id.getLexeme());
        if (!Main.TS.getClaseActual().getMetodos().containsKey(id.getLexeme())){
            throw new SemanticException(id.getLexeme(), "Metodo inexistente", id.getLinea());
        } else {
            chequearParametros();
        }
        if (siguiente != null) {
            return siguiente.chequear(metodo.getTipoRetorno());
        }
        return Main.TS.getClaseActual().getMetodos().get(id.getLexeme()).getTipoRetorno();
    }

    public void chequearParametros() throws SemanticException {
        var metodo = Main.TS.getClaseActual().getMetodos().get(id.getLexeme());
        var parametrosFormales = metodo.getParametros();
        var iteradorParametrosFormales = parametrosFormales.values().iterator();

        if (argumentos.size() != parametrosFormales.size()) {
            throw new SemanticException(id.getLexeme(), "Cantidad de argumentos incorrecta en la llamada al metodo " + id.getLexeme(), id.getLinea());
        }

        for (NodoExpresion p: argumentos) {
            Tipo tipoArgumento = p.chequear();
            Tipo tipoParametroFormal = iteradorParametrosFormales.next().getTipo();

            if (!tipoArgumento.esCompatibleCon(tipoParametroFormal)) {
                throw new SemanticException(id.getLexeme(), "Tipo de argumento incompatible en la posicion para el metodo " + id.getLexeme(), id.getLinea());
            }
        }
    }
}
