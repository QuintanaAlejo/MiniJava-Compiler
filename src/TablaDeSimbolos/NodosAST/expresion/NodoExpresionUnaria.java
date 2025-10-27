package TablaDeSimbolos.NodosAST.expresion;

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
}
