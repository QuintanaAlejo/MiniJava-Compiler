package TablaDeSimbolos.NodosAST.expresion.literal;

import Main.Main;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoLiteral;
import TablaDeSimbolos.Tipos.Tipo;
import TablaDeSimbolos.Tipos.TipoBooleano;
import lexical.Token;

public class NodoBoolean extends NodoLiteral {
    private Token token;

    public NodoBoolean(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public boolean tieneEncadenado() {
        return false;
    }

    @Override
    public Tipo chequear() {
        return new TipoBooleano();
    }

    @Override
    public void generar(){
        if (token.getLexeme().equals("true")) {
            Main.TS.getInstructionList().add("PUSH 1");
        } else {
            Main.TS.getInstructionList().add("PUSH 0");
        }
    }

}
