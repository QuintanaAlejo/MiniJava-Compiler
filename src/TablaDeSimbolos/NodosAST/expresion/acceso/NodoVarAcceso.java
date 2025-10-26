package TablaDeSimbolos.NodosAST.expresion.acceso;

import Main.Main;
import TablaDeSimbolos.NodosAST.encadenado.NodoEncadenado;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import TablaDeSimbolos.Tipos.Tipo;
import exceptions.SemanticException;
import lexical.Token;

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

    @Override
    public Tipo chequear() throws SemanticException {
        if(Main.TS.getMetodoActual().getBloque().getVariablesLocales().get(token.getLexeme()) != null){
            tipoVar = Main.TS.getMetodoActual().getBloque().getVariablesLocales().get(token.getLexeme()).getTipo();
        }

        if(Main.TS.getMetodoActual().getParametros().get(token.getLexeme()) != null){
            tipoVar = Main.TS.getMetodoActual().getParametros().get(token.getLexeme()).getTipo();
        }

        if (tipoVar == null){
            throw new SemanticException(token.getLexeme(), "La variable " + token.getLexeme() + " no fue inicializada", token.getLinea());
        }

        if (encadenado != null){
            encadenado.chequear(tipoVar);
        }
        return tipoVar;
    }
}
