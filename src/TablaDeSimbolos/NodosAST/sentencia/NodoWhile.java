package TablaDeSimbolos.NodosAST.sentencia;

import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.Tipos.Tipo;
import TablaDeSimbolos.Tipos.TipoBooleano;
import exceptions.SemanticException;
import lexical.Token;

public class NodoWhile extends NodoSentencia{
    private NodoExpresion condicion;
    private NodoSentencia cuerpo;
    private Token tokenWhile;

    public NodoWhile(NodoExpresion condicion, NodoSentencia cuerpo, Token tokenWhile) {
        this.condicion = condicion;
        this.cuerpo = cuerpo;
        this.tokenWhile = tokenWhile;
    }

    public NodoExpresion getCondicion() {
        return condicion;
    }

    public NodoSentencia getCuerpo() {
        return cuerpo;
    }

    public Token getTokenWhile() {
        return tokenWhile;
    }

    @Override
    public void chequear() throws SemanticException {
        Tipo tipoCondicion = condicion.chequear();
        if (tipoCondicion != null && tipoCondicion.esCompatibleCon(new TipoBooleano())){
            cuerpo.chequear();
        } else {
            throw new SemanticException(tokenWhile.getLexeme(), "La condición del while debe ser de tipo booleano.", tokenWhile.getLinea());
        }
    }
}
