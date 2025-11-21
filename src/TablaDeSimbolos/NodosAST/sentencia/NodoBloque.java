package TablaDeSimbolos.NodosAST.sentencia;

import Main.Main;
import TablaDeSimbolos.Clase;
import TablaDeSimbolos.Metodo;
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
    private Metodo metodo;

    public NodoBloque() {
        this.sentencias = new ArrayList<>();
        this.variablesLocales = new HashMap<>();
        this.clase = Main.TS.getClaseActual();
        this.metodo = Main.TS.getMetodoActual();
        this.chequeado = false;
    }

    public void agregarSentencia(NodoSentencia sentencia) {
        this.sentencias.add(sentencia);
    }

    public Clase getClase() {
        return this.clase;
    }

    public Metodo getMetodo() {
        return this.metodo;
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

    public void generar(){
        setLocalVarsOffset();
        bloquePadre = Main.TS.getBloqueActual();
        Main.TS.setBloqueActual(this);
        for (NodoSentencia s : sentencias){
            s.generar();
        }
        Main.TS.setBloqueActual(bloquePadre);
    }

    public void setLocalVarsOffset() {
        int offset = 0;
        if(bloquePadre != null) {
            offset = bloquePadre.getLastLocalVarOffset();
        }
        for(NodoVarLocal localVar : variablesLocales.values()) {
            if(offset != 0) {
                localVar.setOffset(offset);
                offset--;
            }
        }
    }

    public int getLastLocalVarOffset(){
        int ret = 0;
        if(!variablesLocales.isEmpty()) {
            for(NodoVarLocal localVarNode : variablesLocales.values()) {
                if(localVarNode.getOffset() < ret) {
                    ret =  localVarNode.getOffset();
                }
            }
        }
        return ret;
    }

}
