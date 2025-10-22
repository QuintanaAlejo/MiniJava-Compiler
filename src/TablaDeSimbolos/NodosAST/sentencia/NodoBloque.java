package TablaDeSimbolos.NodosAST.sentencia;

import exceptions.SemanticException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NodoBloque extends NodoSentencia{
    private ArrayList<NodoSentencia> sentencias;
    private Map<String, NodoVarLocal> variablesLocales;

    public NodoBloque() {
        this.sentencias = new ArrayList<>();
        this.variablesLocales = new HashMap<>();

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

    public void agregarVariableLocal(String nombre, NodoVarLocal variable) {
        // Hacer los chequeos necesarios antes de agregar
        this.variablesLocales.put(nombre, variable);
    }

    @Override
    public void chequear() throws SemanticException {
        if (!chequeado) {
            for (NodoSentencia sentencia : sentencias) {
                sentencia.chequear();
            }
            chequeado = true;
        }
    }

    @Override
    public void generar() {
        if (!generado) {
            for (NodoSentencia sentencia : sentencias) {
                sentencia.generar();
            }
            generado = true;
        }
    }


    private NodoSentencia getUltimaSentencia(){
        return sentencias.getLast();
    }
}
