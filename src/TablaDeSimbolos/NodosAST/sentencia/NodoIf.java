package TablaDeSimbolos.NodosAST.sentencia;

import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.Tipos.Tipo;
import TablaDeSimbolos.Tipos.TipoBooleano;
import exceptions.SemanticException;
import lexical.Token;

public class NodoIf extends NodoSentencia {
    private Token tokenif;
    private NodoExpresion condicion;
    private NodoSentencia sentenciaThen;
    private NodoSentencia sentenciaElse;

    public NodoIf(Token token, NodoExpresion condicion, NodoSentencia sentenciaThen, NodoSentencia sentenciaElse) {
        this.tokenif = token;
        this.condicion = condicion;
        this.sentenciaThen = sentenciaThen;
        this.sentenciaElse = sentenciaElse;
    }

    //Getters
    public NodoExpresion getCondicion() {
        return condicion;
    }

    public NodoSentencia getSentenciaThen() {
        return sentenciaThen;
    }

    public NodoSentencia getSentenciaElse() {
        return sentenciaElse;
    }

    @Override
    public void chequear() throws SemanticException {
        Tipo tipoCondicion = condicion.chequear();
        if (!(tipoCondicion.esCompatibleCon(new TipoBooleano()))) {
            throw new SemanticException(tokenif.getLexeme(), "La condicion del IF debe ser de tipo booleano", tokenif.getLinea());
        }
        sentenciaThen.chequear();
        sentenciaElse.chequear();
    }
}
