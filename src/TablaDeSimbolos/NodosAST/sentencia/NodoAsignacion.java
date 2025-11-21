package TablaDeSimbolos.NodosAST.sentencia;

import Main.Main;
import TablaDeSimbolos.NodosAST.encadenado.NodoMetodoLlamadaEncadenada;
import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.NodosAST.expresion.NodoExpresionAsignacion;
import TablaDeSimbolos.NodosAST.expresion.acceso.NodoLlamadaConstructor;
import TablaDeSimbolos.NodosAST.expresion.acceso.NodoLlamadaMetodo;
import TablaDeSimbolos.NodosAST.expresion.acceso.NodoLlamadaMetodoEstatico;
import TablaDeSimbolos.Tipos.Tipo;
import exceptions.SemanticException;
import lexical.Token;
import lexical.TokenId;

public class NodoAsignacion extends NodoSentencia{
    private Token token;
    private NodoExpresion expresion;
    private Tipo tipoExpresion;

    public NodoAsignacion(Token token, NodoExpresion expresion) {
        this.token = token;
        this.expresion = expresion;
    }

    @Override
    public void chequear() throws SemanticException {
        tipoExpresion = expresion.chequear();
        if (expresion instanceof NodoLlamadaConstructor){
            return;
        }
        if (!esValida() && !expresion.tieneEncadenado()) {
            throw new SemanticException(token.getLexeme(), "Expresión suelta como sentencia.", token.getLinea());
        }
    }

    private boolean esValida() {
        return (expresion instanceof NodoExpresionAsignacion ||
                expresion instanceof NodoLlamadaMetodo ||
                expresion instanceof NodoLlamadaMetodoEstatico );
    }

    @Override
    public void generar(){
        expresion.generar();
        if (tipoExpresion != null && !(tipoExpresion.getTokenPropio().getTokenId().equals(TokenId.kw_void))){
            if (!(expresion instanceof NodoExpresionAsignacion)) {
                Main.TS.getInstructionList().add("POP");
            }
        }
    }
}
