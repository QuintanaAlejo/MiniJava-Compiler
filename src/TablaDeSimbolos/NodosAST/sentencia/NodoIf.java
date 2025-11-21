package TablaDeSimbolos.NodosAST.sentencia;

import Main.Main;
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

    @Override
    public void generar(){
        int ifInicio = Main.TS.getConditionalCounter();
        condicion.generar();
        if (sentenciaElse == null) {
            Main.TS.getInstructionList().add("BF IF_END_" + ifInicio + "; Salto si la condicion es falsa");
            sentenciaThen.generar();
            Main.TS.getInstructionList().add("IF_END_" + ifInicio + ": NOP");
        } else {
            int elseInicio = Main.TS.getConditionalCounter();
            Main.TS.getInstructionList().add("BF ELSE_START_" + elseInicio + "; Salto si la condicion es falsa");
            sentenciaThen.generar();
            Main.TS.getInstructionList().add("JUMP IF_END_" + ifInicio + "; Salto al final del IF");
            Main.TS.getInstructionList().add("ELSE_START_" + elseInicio + ": NOP");
            sentenciaElse.generar();
            Main.TS.getInstructionList().add("IF_END_" + ifInicio + ": NOP");
        }
    }
}
