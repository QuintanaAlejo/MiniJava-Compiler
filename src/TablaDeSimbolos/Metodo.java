package TablaDeSimbolos;

import Main.Main;
import TablaDeSimbolos.NodosAST.sentencia.NodoBloque;
import TablaDeSimbolos.Tipos.Tipo;
import lexical.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import exceptions.SemanticException;
import lexical.TokenId;

public class Metodo {
    private Token nombre;
    private Tipo tipoRetorno;
    private HashMap<String, Parametro> parametros;
    private Token modificador;
    private boolean tieneBloque;
    private NodoBloque bloque;
    private int offset;
    private String label;
    private String clase;

    public Metodo(Token nombre, Tipo tipoRetorno, Token modificador) {
        this.modificador = modificador;
        this.nombre = nombre;
        this.tipoRetorno = tipoRetorno;
        this.parametros = new LinkedHashMap<>();
    }

    public String getNombre() {
        return nombre.getLexeme();
    }

    public Tipo getTipoRetorno() {
        return tipoRetorno;
    }

    public Token getModificador() {
        return modificador;
    }

    public HashMap<String, Parametro> getParametros() {
        return parametros;
    }

    public void agregarParametro(Parametro parametro) throws SemanticException {
        if (parametros.putIfAbsent(parametro.getNombre(), parametro) != null) {
            throw new SemanticException(parametro.getNombre(), "Parámetro repetido", parametro.getToken().getLinea()); //Ver
        }
    }

    public String getClase(){
        return clase;
    }

    public void setClase(String nombreClase){
        clase = nombreClase;
    }

    public void estaBienDeclarado() throws SemanticException {
        //Es abstracto y tiene cuerpo?
        if (modificador != null && modificador.getLexeme().equals("abstract") && tieneBloque) {
            throw new SemanticException(nombre.getLexeme(), "El método " + nombre.getLexeme() + " es abstracto y no puede tener cuerpo.", nombre.getLinea());
        }
        //No es abstracto y no tiene cuerpo?
        if (modificador != null && !modificador.getLexeme().equals("abstract") && !tieneBloque) {
            throw new SemanticException(nombre.getLexeme(), "El método " + nombre.getLexeme() + " no es abstracto y debe tener cuerpo.", nombre.getLinea());
        }
        //El tipo de retorno esta bien declarado?
        if (tipoRetorno != null){
            tipoRetorno.estaBienDeclarado();
        }
        //Los parámetros están bien declarados?
        for (Parametro p : parametros.values()) {
            p.estaBienDeclarado();
        }
    }

    public void chequear() throws SemanticException{
        //Chequear el bloque si tiene
        if (tieneBloque && bloque != null && !bloque.isChequeado()){
            bloque.chequear();
        }
    }

    public boolean tieneBloque() {
        return tieneBloque;
    }

    public void setTieneBloque(boolean tieneBloque) {
        this.tieneBloque = tieneBloque;
    }

    public Token getToken() {
        return nombre;
    }

    public NodoBloque getBloque() {
        return bloque;
    }

    public void setBloque(NodoBloque bloque) {
        this.bloque = bloque;
    }

    /* Necesito meterle a los metodos la clase que los contiene
    public String getLabel() {
        if(label == null && ownerClass != null) {
            label = ownerClass.getName() + "_" + idToken.getLexeme();
        }
        return label;
    }
     */

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public void generar(){
        Main.TS.getInstructionList().add(".CODE");
        if(getNombre().equals("main") && esEstatico()){
            Main.TS.getInstructionList().add("main:");
        } else if (clase != null){
            Main.TS.getInstructionList().add(clase + "_" + getNombre() + ":");
        }
        Main.TS.getInstructionList().add("LOADFP");
        Main.TS.getInstructionList().add("LOADSP");
        Main.TS.getInstructionList().add("STOREFP");

        if (bloque != null){
            bloque.generar();
        }

        if (tipoRetorno != null && tipoRetorno.getTokenPropio().getTokenId().equals(TokenId.kw_void)){
            int cantVarsLocales = 0;
            int memoriaNecesaria = esEstatico() ? parametros.size() : parametros.size() + 1;

            if (bloque != null && bloque.getVariablesLocales() != null){
                cantVarsLocales = bloque.getVariablesLocales().size();
            }

            if (cantVarsLocales > 0){
                Main.TS.getInstructionList().add("FMEM " + cantVarsLocales);
            }

            Main.TS.getInstructionList().add("STOREFP");
            Main.TS.getInstructionList().add("RET " + memoriaNecesaria);
        }
    }

    private boolean esEstatico(){
        return getModificador().getTokenId().equals(TokenId.kw_static);
    }
}
