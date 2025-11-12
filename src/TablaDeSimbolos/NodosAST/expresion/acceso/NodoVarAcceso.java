package TablaDeSimbolos.NodosAST.expresion.acceso;

import Main.Main;
import TablaDeSimbolos.NodosAST.encadenado.NodoEncadenado;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import TablaDeSimbolos.NodosAST.sentencia.NodoBloque;
import TablaDeSimbolos.Tipos.Tipo;
import exceptions.SemanticException;
import lexical.Token;
import lexical.TokenId;

public class NodoVarAcceso extends NodoAcceso {
    private Token token;
    private NodoEncadenado encadenado;
    private Tipo tipoVar;

    public NodoVarAcceso(Token token) {
        this.token = token;
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
        if(Main.TS.getMetodoActual().getBloque().getVariablesLocales().get(token.getLexeme()) != null){
            tipoVar = Main.TS.getMetodoActual().getBloque().getVariablesLocales().get(token.getLexeme()).getTipo();
        }

        if(Main.TS.getMetodoActual().getParametros().get(token.getLexeme()) != null){
            tipoVar = Main.TS.getMetodoActual().getParametros().get(token.getLexeme()).getTipo();
        }

        if(Main.TS.getConstructorActual() != null && Main.TS.getConstructorActual().getParametros().get(token.getLexeme()) != null)
            tipoVar = Main.TS.getConstructorActual().getParametros().get(token.getLexeme()).getTipo();{
        }

        if (Main.TS.getClaseActual().getAtributos().get(token.getLexeme()) != null){
            Token modificadorMetodoActual = Main.TS.getMetodoActual().getModificador();
            if (modificadorMetodoActual != null){
                if(!modificadorMetodoActual.getTokenId().equals(TokenId.kw_static)) {
                    tipoVar = Main.TS.getClaseActual().getAtributos().get(token.getLexeme()).getTipo();
                } else {
                    throw new SemanticException(token.getLexeme(), "No se puede acceder a un atributo de instancia desde un método estático", token.getLinea());
                }
            } else {
                tipoVar = Main.TS.getClaseActual().getAtributos().get(token.getLexeme()).getTipo();
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
}
