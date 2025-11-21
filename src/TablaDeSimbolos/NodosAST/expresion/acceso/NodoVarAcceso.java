package TablaDeSimbolos.NodosAST.expresion.acceso;

import Main.Main;
import TablaDeSimbolos.Atributo;
import TablaDeSimbolos.NodosAST.encadenado.NodoEncadenado;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import TablaDeSimbolos.NodosAST.sentencia.NodoBloque;
import TablaDeSimbolos.NodosAST.sentencia.NodoVarLocal;
import TablaDeSimbolos.Parametro;
import TablaDeSimbolos.Tipos.Tipo;
import exceptions.SemanticException;
import lexical.Token;
import lexical.TokenId;

public class NodoVarAcceso extends NodoAcceso {
    private Token token;
    private NodoEncadenado encadenado;
    private Tipo tipoVar;
    private NodoBloque bloque;

    private Atributo atributo;
    private Parametro parametro;
    private NodoVarLocal varLocal;

    public NodoVarAcceso(Token token, NodoBloque bloqueActual) {
        this.token = token;
        this.bloque = bloqueActual;
    }

    public void setEncadenado(NodoEncadenado encadenado) {
        this.encadenado = encadenado;
    }

    public void chequearVariablesDelPadre() throws SemanticException {
        if (Main.TS.getBloqueActual().getVariablesLocales().get(token.getLexeme()) != null) {
            tipoVar =  Main.TS.getBloqueActual().getVariablesLocales().get(token.getLexeme()).getTipo();
        }
        NodoBloque bloquePadre = Main.TS.getBloqueActual().getBloquePadre();
        while (bloquePadre != null) {
            if (bloquePadre.getVariablesLocales().containsKey(token.getLexeme())) {
                tipoVar = bloquePadre.getVariablesLocales().get(token.getLexeme()).getTipo();
            }
            bloquePadre = bloquePadre.getBloquePadre();
        }
    }

    public Token getToken(){
        return token;
    }

    @Override
    public NodoEncadenado getEncadenado(){
        return encadenado;
    }

    @Override
    public boolean tieneEncadenado() {
        return encadenado != null;
    }

    @Override
    public Tipo chequear() throws SemanticException {
        if(Main.TS.getMetodoActual().getBloque() != null && Main.TS.getMetodoActual().getBloque().getVariablesLocales().get(token.getLexeme()) != null){
            tipoVar = Main.TS.getMetodoActual().getBloque().getVariablesLocales().get(token.getLexeme()).getTipo();
        }

        if(Main.TS.getMetodoActual().getParametros().get(token.getLexeme()) != null){
            tipoVar = Main.TS.getMetodoActual().getParametros().get(token.getLexeme()).getTipo();
        }

        if(Main.TS.getConstructorActual() != null && Main.TS.getConstructorActual().getParametros().get(token.getLexeme()) != null)
            tipoVar = Main.TS.getConstructorActual().getParametros().get(token.getLexeme()).getTipo();{
        }

        Atributo atr = Main.TS.getClaseActual().getAtributos().get(token.getLexeme());

        if (atr != null) {
            boolean esStatic = false;
            if (Main.TS.getMetodoActual() != null) {
                Token mod = Main.TS.getMetodoActual().getModificador();
                esStatic = (mod != null && mod.getTokenId().equals(TokenId.kw_static));
            }
            if (!esStatic) {
                atributo = atr;
                tipoVar = atr.getTipo();
            }
        }


        chequearVariablesDelPadre();

        if (tipoVar == null){
            throw new SemanticException(token.getLexeme(), "La variable " + token.getLexeme() + " no fue inicializada", token.getLinea());
        }

        if (encadenado != null){
            return encadenado.chequear(tipoVar);
        }

        return tipoVar;
    }

    @Override
    public void generar(){
        if (bloque == null){
            bloque = Main.TS.getBloqueActual();
        }
        Atributo atr = Main.TS.getClases().get(bloque.getClase().getNombre()).getAtributos().get(token.getLexeme());

        if (atr != null && bloque.getVariablesLocales().get(token.getLexeme()) == null && bloque.getMetodo() != null && bloque.getMetodo().getParametros().get(token.getLexeme()) == null){
            Main.TS.getInstructionList().add("LOAD 3; Accedo atributo");
            if (!ladoIzquierdo || encadenado != null){
                Main.TS.getInstructionList().add("LOADREF "+atr.getOffset() );
            } else {
                Main.TS.getInstructionList().add("SWAP");
                Main.TS.getInstructionList().add("STOREREF "+atr.getOffset());
            }
        } else {
            var offset = 0;
            if (bloque.getVariablesLocales().get(token.getLexeme()) != null){
                offset = bloque.getVariablesLocales().get(token.getLexeme()).getOffset();
            } else if (bloque.getMetodo() != null && bloque.getMetodo().getParametros().get(token.getLexeme()) != null){
                offset = bloque.getMetodo().getParametros().get(token.getLexeme()).getOffset();
            }
            if (!ladoIzquierdo || encadenado != null){
                Main.TS.getInstructionList().add("LOAD "+offset+"; Accedo variable local o parametro");
            } else {
                Main.TS.getInstructionList().add("STORE "+offset+"; Accedo variable local o parametro");
            }
        }
        if (encadenado != null){
            encadenado.generar();
        }
    }
}
