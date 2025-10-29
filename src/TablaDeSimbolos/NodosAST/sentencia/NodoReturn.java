package TablaDeSimbolos.NodosAST.sentencia;

import Main.Main;
import TablaDeSimbolos.Clase;
import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.NodosAST.expresion.NodoExpresionVacia;
import TablaDeSimbolos.Tipos.Tipo;
import exceptions.SemanticException;
import lexical.Token;
import lexical.TokenId;

import java.util.HashMap;

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
        if (expresion instanceof NodoExpresionVacia) {
            if(!tipoDeRet.getTokenPropio().getTokenId().equals(TokenId.kw_void)){
                throw new SemanticException(token.getLexeme(), "La funcion no es void, debe retornar un valor.", token.getLinea());
            }
            return;
        }
        if (tipoDeRet.esPrimitivo()){
            if(tipoDeRet.getTokenPropio().getTokenId().equals(TokenId.kw_void)){
                if(!expresion.chequear().getTokenPropio().getTokenId().equals(TokenId.universal)){
                    throw new SemanticException(token.getLexeme(), "La funcion es void, no debe retornar ningun valor.", token.getLinea());
                }
            } else {
                Tipo tipoExpresion = expresion.chequear();
                if (tipoExpresion != null && !tipoDeRet.esCompatibleCon(tipoExpresion)) {
                    throw new SemanticException(token.getLexeme(), "Tipo de retorno incompatible. Se esperaba: " + tipoDeRet.getNombre() + ", se obtuvo: " + tipoExpresion.getNombre(), token.getLinea());
                }
            }
        } else {
            Tipo tipoExpresion = expresion.chequear();
            HashMap<String, Clase> clases = Main.TS.getClases();
            if(tipoExpresion != null && !(esSubtipo(tipoExpresion.getNombre(), tipoDeRet.getNombre(), clases)) && !(tipoExpresion.getTokenPropio().getTokenId().equals(TokenId.kw_null))){
                throw new SemanticException(token.getLexeme(), "Tipo de retorno incompatible. Se esperaba: " + tipoDeRet.getNombre() + ", se obtuvo: " + tipoExpresion.getNombre(), token.getLinea());
            }
            if (tipoExpresion != null &&  !tipoDeRet.esCompatibleCon(tipoExpresion) && !(tipoExpresion.getTokenPropio().getTokenId().equals(TokenId.kw_null))) {
                throw new SemanticException(token.getLexeme(), "Tipo de retorno incompatible. Se esperaba: " + tipoDeRet.getNombre() + ", se obtuvo: " + tipoExpresion.getNombre(), token.getLinea());
            }
        }
    }

    private boolean esSubtipo(String nombreHijo, String nombrePadre, HashMap<String, Clase> clases) {
        if (nombreHijo.equals(nombrePadre)) return true;

        java.util.Set<String> visitados = new java.util.HashSet<>();
        Clase actual = clases.get(nombreHijo);

        while (actual != null && !visitados.contains(actual.getNombre())) {
            visitados.add(actual.getNombre());
            Token tokPadre = actual.getPadre();
            if (tokPadre == null) break;
            if (tokPadre.getLexeme().equals(nombrePadre)) return true;
            actual = clases.get(tokPadre.getLexeme());
        }
        return false;
    }
}
