package TablaDeSimbolos;

import Main.Main;
import TablaDeSimbolos.NodosAST.sentencia.NodoBloque;
import lexical.Token;
import java.util.HashMap;
import java.util.LinkedHashMap;

import exceptions.SemanticException;

public class Constructor extends Metodo {
    private Token token;
    private LinkedHashMap<String, Parametro> parametros;
    private NodoBloque bloque;

    public Constructor (Token token) {
        super(token, null, null);
        this.token = token;
        this.parametros = new LinkedHashMap<>();
    }

    public HashMap<String, Parametro> getParametros() {
        return parametros;
    }

    public void estaBienDeclarado() throws SemanticException {
        for (Parametro parametro : this.parametros.values()) {
            parametro.estaBienDeclarado();
        }
    }

    public void chequear() throws SemanticException {
        if (this.bloque != null) {
            this.bloque.chequear();
        }
    }

    public String getNombre() {
        return token.getLexeme();
    }

    public Token getToken() {
        return token;
    }

    public NodoBloque getBloque() {
        return bloque;
    }

    public void setBloque(NodoBloque bloque) {
        this.bloque = bloque;
    }

    public void agregarParametro(Parametro parametro) throws SemanticException {
        if (this.parametros.containsKey(parametro.getNombre())) {
            throw new SemanticException(parametro.getNombre(), "Parámetro repetido", parametro.getToken().getLinea()); //Ver
        }
        this.parametros.put(parametro.getNombre(), parametro);
    }

    public String getLabel() {
        return "CONSTRUCTOR_"+getNombre();
    }

    public void generar(){
        int par = parametros.size() + 1;
        Main.TS.getInstructionList().add(".CODE");
        Main.TS.getInstructionList().add(getLabel() + ":");
        Main.TS.getInstructionList().add("LOADFP");
        Main.TS.getInstructionList().add("LOADSP");
        Main.TS.getInstructionList().add("STOREFP");

        if(bloque != null){
            bloque.setEsConstructor(true);
            bloque.setConstructor(this);
            bloque.generar();
            Main.TS.getInstructionList().add("FMEM " + bloque.getVariablesLocales().size()+"; Constructor");
        }

        Main.TS.getInstructionList().add("STOREFP");
        Main.TS.getInstructionList().add("RET " + par);
        Main.TS.getInstructionList().add("");
    }

    public void setParamsOffsets(){
        int initialOffset = 4;
        int valuePos = 1;
        for(Parametro p : parametros.values()){
            p.setOffset(initialOffset + parametros.size() - valuePos);
            valuePos++;
        }
    }
}
