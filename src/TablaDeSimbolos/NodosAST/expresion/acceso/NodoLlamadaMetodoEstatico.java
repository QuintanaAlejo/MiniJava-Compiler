package TablaDeSimbolos.NodosAST.expresion.acceso;

import Main.Main;
import TablaDeSimbolos.Clase;
import TablaDeSimbolos.Metodo;
import TablaDeSimbolos.NodosAST.encadenado.NodoEncadenado;
import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import TablaDeSimbolos.Tipos.Tipo;
import exceptions.SemanticException;
import lexical.Token;

import java.util.List;

public class NodoLlamadaMetodoEstatico extends NodoAcceso {
    private Token clase;
    private Token id;
    private List<NodoExpresion> argumentos;
    private NodoEncadenado encadenado;

    public NodoLlamadaMetodoEstatico(Token clase, Token id) {
        this.clase = clase;
        this.id = id;
    }

    public void setArgumentos(List<NodoExpresion> argumentos) {
        this.argumentos = argumentos;
    }

    public void setEncadenado(NodoEncadenado encadenado) {
        this.encadenado = encadenado;
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

    @Override
    public Tipo chequear() throws SemanticException {
        Clase c = Main.TS.getClases().get(clase.getLexeme());
        if (c == null){
            throw new SemanticException(clase.getLexeme(), "La clase no existe", clase.getLinea());
        }

        Metodo m = c.getMetodos().get(id.getLexeme());
        if (m == null){
            throw new SemanticException(id.getLexeme(), "El metodo no existe en la clase " + clase.getLexeme(), id.getLinea());
        }

        if (!m.getModificador().getTokenId().equals(lexical.TokenId.kw_static)) {
            throw new SemanticException(id.getLexeme(), "El metodo " + id.getLexeme() + " no es estatico en la clase " + clase.getLexeme(), id.getLinea());
        }

        chequearParametros();

        if (encadenado != null) {
            return encadenado.chequear(m.getTipoRetorno());
        }
        return m.getTipoRetorno();
    }

}
