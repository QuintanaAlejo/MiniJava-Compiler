package TablaDeSimbolos.NodosAST.expresion.acceso;

import Main.Main;
import TablaDeSimbolos.Clase;
import TablaDeSimbolos.Metodo;
import TablaDeSimbolos.NodosAST.encadenado.NodoEncadenado;
import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import TablaDeSimbolos.Tipos.TipoNull;
import exceptions.SemanticException;
import lexical.Token;
import TablaDeSimbolos.Tipos.Tipo;
import lexical.TokenId;

import java.util.HashMap;
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
    public NodoEncadenado getEncadenado(){
        return siguiente;
    }

    @Override
    public void setEncadenado(NodoEncadenado encadenado) {
        this.siguiente = encadenado;
    }

    @Override
    public boolean tieneEncadenado() {
        return siguiente != null;
    }

    @Override
    public Tipo chequear() throws SemanticException {
        Metodo metodo = Main.TS.getClaseActual().getMetodos().get(id.getLexeme());

        if (!Main.TS.getClaseActual().getMetodos().containsKey(id.getLexeme())){
            throw new SemanticException(id.getLexeme(), "Metodo inexistente", id.getLinea());
        } else {
            chequearParametros();
        }

        Token modActual = Main.TS.getMetodoActual().getModificador();
        if (modActual != null && !modActual.getTokenId().equals(TokenId.kw_static) && metodo.getModificador().getTokenId().equals(TokenId.kw_static)){
            throw new SemanticException(id.getLexeme(), "No se puede llamar a un metodo de instancia desde un contexto estatico", id.getLinea());
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
        HashMap<String, Clase> clases = Main.TS.getClases();

        if (argumentos.size() != parametrosFormales.size()) {
            throw new SemanticException(id.getLexeme(), "Cantidad de argumentos incorrecta en la llamada al metodo " + id.getLexeme(), id.getLinea());
        }

        for (NodoExpresion p : argumentos) {
            Tipo tipoArgumento = p.chequear();
            Tipo tipoParametroFormal = iteradorParametrosFormales.next().getTipo();

            // 🔹 Si el argumento es null, solo usamos esCompatibleCon
            if (tipoArgumento instanceof TipoNull) {
                if (!tipoArgumento.esCompatibleCon(tipoParametroFormal)) {
                    throw new SemanticException(id.getLexeme(),
                            "Tipo de argumento incompatible para el metodo " + id.getLexeme(), id.getLinea());
                }
                continue;
            }

            // 🔹 Para el resto de tipos, primero verificamos subtipo, luego compatibilidad
            if (!esSubtipo(tipoArgumento.getTokenPropio().getLexeme(),
                    tipoParametroFormal.getTokenPropio().getLexeme(),
                    clases)
                    && !tipoArgumento.esCompatibleCon(tipoParametroFormal)) {
                throw new SemanticException(id.getLexeme(),
                        "Tipo de argumento incompatible para el metodo " + id.getLexeme(), id.getLinea());
            }
        }

    }

    private boolean esSubtipo(String nombreHijo, String nombrePadre, HashMap<String, Clase> clases) {
        if (nombreHijo.equals(nombrePadre)) return true;

        java.util.Set<String> visitados = new java.util.HashSet<>();
        Clase actual = clases.get(nombreHijo);

        while (actual != null && !visitados.contains(actual.getNombre())) {
            visitados.add(actual.getNombre());
            Token tokPadre = actual.getPadre();
            if (tokPadre == null) break;
            if (tokPadre.getLexeme().equals(nombrePadre)) return true;
            actual = clases.get(tokPadre.getLexeme());
        }
        return false;
    }
}
