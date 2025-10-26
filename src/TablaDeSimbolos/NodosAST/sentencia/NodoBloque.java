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

    public NodoBloque() {
        this.sentencias = new ArrayList<>();
        this.variablesLocales = new HashMap<>();
        this.clase = Main.TS.getClaseActual();
    }

    public void agregarSentencia(NodoSentencia sentencia) {
        this.sentencias.add(sentencia);
    }

    public void setSentencias  (ArrayList<NodoSentencia> sentencias) {
        this.sentencias = sentencias;
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

    public void agregarVariableLocal(String nombre, NodoVarLocal variable) {
        // Hacer los chequeos necesarios antes de agregar
        this.variablesLocales.put(nombre, variable);
    }

    @Override
    public void chequear() throws SemanticException {
        bloquePadre = Main.TS.getBloqueActual();
        Main.TS.setBloqueActual(this);
        for (NodoSentencia sentencia : sentencias) {
            sentencia.chequear();
        }
        Main.TS.setBloqueActual(bloquePadre);
    }
    private NodoSentencia getUltimaSentencia(){
        return sentencias.getLast();
    }
}
