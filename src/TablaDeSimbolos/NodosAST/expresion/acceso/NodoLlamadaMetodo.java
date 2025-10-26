package TablaDeSimbolos.NodosAST.expresion.acceso;

import Main.Main;
import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import exceptions.SemanticException;
import lexical.Token;
import TablaDeSimbolos.Tipos.Tipo;

import java.util.List;

public class NodoLlamadaMetodo extends NodoAcceso {
    private Token id;
    private List<NodoExpresion> argumentos;

    public NodoLlamadaMetodo(Token id, List<NodoExpresion> argumentos) {
        this.id = id;
        this.argumentos = argumentos;
    }

    @Override
    public Tipo chequear() throws SemanticException {
        if (!Main.TS.getClaseActual().getMetodos().containsKey(id.getLexeme())){
            throw new SemanticException(id.getLexeme(), "Metodo inexistente", id.getLinea());
        } else {
            chequearParametros();
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
                throw new SemanticException(id.getLexeme(), "Tipo de argumento incompatible en la posicion  para el metodo " + id.getLexeme(), id.getLinea());
            }
        }
    }
}
