package TablaDeSimbolos.NodosAST.sentencia;

import Main.Main;
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

    @Override
    public void generar(){
        int whileInicio = Main.TS.getConditionalCounter();
        Main.TS.getInstructionList().add("WHILE_START_" + whileInicio + ": NOP");
        condicion.generar();
        Main.TS.getInstructionList().add("BF WHILE_END_" + whileInicio);
        cuerpo.generar();
        Main.TS.getInstructionList().add("JUMP WHILE_START_" + whileInicio + "; Vuelta al inicio del while");
        Main.TS.getInstructionList().add("WHILE_END_" + whileInicio + ": NOP");
    }
}
