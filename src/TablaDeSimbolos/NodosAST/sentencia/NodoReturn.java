package TablaDeSimbolos.NodosAST.sentencia;

import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.Tipos.Tipo;
import exceptions.SemanticException;
import lexical.Token;
import lexical.TokenId;

public class NodoReturn extends NodoSentencia{
    private NodoExpresion expresion;
    private Token token;
    private Tipo tipoDeRet;

    public NodoReturn(Token token, NodoExpresion expresion) {
        this.token = token;
        this.expresion = expresion;
    }

    public void setTipoDeRet(Tipo tipoDeRet) {
        this.tipoDeRet = tipoDeRet;
    }

    @Override
    public void chequear() throws SemanticException {
        // Chequeos para el return
        if (tipoDeRet.esPrimitivo()){
            if(tipoDeRet.getTokenPropio().getTokenId().equals(TokenId.kw_void)){
                if(!expresion.chequear().getTokenPropio().getTokenId().equals(TokenId.universal)){
                    throw new SemanticException(token.getLexeme(), "La funcion es void, no debe retornar ningun valor.", token.getLinea());
                } else {
                    Tipo tipoExpresion = expresion.chequear();
                    if (tipoExpresion != null && !tipoDeRet.esCompatibleCon(tipoExpresion)) {
                        throw new SemanticException(token.getLexeme(), "Tipo de retorno incompatible. Se esperaba: " + tipoDeRet.getNombre() + ", se obtuvo: " + tipoExpresion.getNombre(), token.getLinea());
                    }
                }
            }
        } else {
            Tipo tipoExpresion = expresion.chequear();
            if (tipoExpresion != null && !tipoDeRet.esCompatibleCon(tipoExpresion)) {
                throw new SemanticException(token.getLexeme(), "Tipo de retorno incompatible. Se esperaba: " + tipoDeRet.getNombre() + ", se obtuvo: " + tipoExpresion.getNombre(), token.getLinea());
            }
        }
    }
}
