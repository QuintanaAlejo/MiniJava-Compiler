package TablaDeSimbolos.NodosAST.sentencia;

import Main.Main;
import TablaDeSimbolos.Clase;
import exceptions.SemanticException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NodoBloque extends NodoSentencia{
    private ArrayList<NodoSentencia> sentencias;
    private Map<String, NodoVarLocal> variablesLocales;
    private NodoBloque bloquePadre;
    private Clase clase;
    private boolean chequeado;

    public NodoBloque() {
        this.sentencias = new ArrayList<>();
        this.variablesLocales = new HashMap<>();
        this.clase = Main.TS.getClaseActual();
        this.chequeado = false;
    }

    public void agregarSentencia(NodoSentencia sentencia) {
        this.sentencias.add(sentencia);
    }

    public void setSentencias  (ArrayList<NodoSentencia> sentencias) {
        this.sentencias = sentencias;
    }

    public boolean isChequeado() {
        return chequeado;
    }

    public ArrayList<NodoSentencia> getSentencias() {
        return this.sentencias;
    }

    public Map<String, NodoVarLocal> getVariablesLocales() {
        return this.variablesLocales;
    }

    public NodoBloque getBloquePadre() {
        return this.bloquePadre;
    }

    public void agregarVariableLocal(String nombre, NodoVarLocal variable) throws SemanticException {
        if (!variablesLocales.containsKey(nombre)) {
            variablesLocales.put(nombre, variable);
        } else {
            throw new SemanticException(variablesLocales.get(nombre).getIdentificador().getLexeme(), "La variable local ya ha sido declarada en este bloque.", variablesLocales.get(nombre).getIdentificador().getLinea());
        }
    }

    @Override
    public void chequear() throws SemanticException {
        bloquePadre = Main.TS.getBloqueActual();
        Main.TS.setBloqueActual(this);
        chequeado = true;
        for (NodoSentencia sentencia : sentencias) {
            sentencia.chequear();
        }
        Main.TS.setBloqueActual(bloquePadre);
    }
    private NodoSentencia getUltimaSentencia(){
        return sentencias.getLast();
    }
}
