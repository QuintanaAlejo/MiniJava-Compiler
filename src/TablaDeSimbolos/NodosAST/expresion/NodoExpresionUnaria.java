package TablaDeSimbolos.NodosAST.expresion;

import Main.Main;
import TablaDeSimbolos.Tipos.Tipo;
import TablaDeSimbolos.Tipos.TipoBooleano;
import TablaDeSimbolos.Tipos.TipoInt;
import exceptions.SemanticException;
import lexical.Token;

public class NodoExpresionUnaria extends NodoExpresionCompuesta{
    private Token operador;
    private NodoOperando operando;

    public NodoExpresionUnaria(Token operador, NodoOperando operando) {
        this.operador = operador;
        this.operando = operando;
    }

    // Getters y setters
    public Token getOperador() {
        return operador;
    }

    public NodoOperando getOperando() {
        return operando;
    }

    @Override
    public boolean tieneEncadenado() {
        return false;
    }

    @Override
    public Tipo chequear() throws SemanticException {
        Tipo tipoOperando = operando.chequear();

        switch (operador.getTokenId()){
            case op_minus:
            case op_plus:
            case increment:
            case decrement:
                if (tipoOperando.esCompatibleCon(new TipoInt())){
                    return new TipoInt();
                } else{
                    throw new SemanticException(operador.getLexeme(), "Operador unario incompatible con el tipo del operando.", operador.getLinea());
                }
            case op_not:
                if (tipoOperando.esCompatibleCon(new TipoBooleano())){
                    return new TipoBooleano();
                } else{
                    throw new SemanticException(operador.getLexeme(), "Operador unario incompatible con el tipo del operando.", operador.getLinea());
                }
        }

        throw new SemanticException(operador.getLexeme(), "Operador unario incompatible con el tipo del operando.", operador.getLinea());
    }

    @Override
    public void generar(){
        operando.generar();
        switch (operador.getLexeme()){
            case "++":
                Main.TS.getInstructionList().add("PUSH 1");
                Main.TS.getInstructionList().add("ADD");
                break;
            case "--":
                Main.TS.getInstructionList().add("PUSH 1");
                Main.TS.getInstructionList().add("SUB");
                break;
            case "+":
                // No hace nada
                break;
            case "-":
                Main.TS.getInstructionList().add("NEG");
                break;
            case "!":
                Main.TS.getInstructionList().add("NOT");
                break;
        }
    }
}
