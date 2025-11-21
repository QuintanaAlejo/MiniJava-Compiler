package TablaDeSimbolos.NodosAST.expresion.acceso;

import Main.Main;
import TablaDeSimbolos.NodosAST.encadenado.NodoEncadenado;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import TablaDeSimbolos.Tipos.Tipo;
import TablaDeSimbolos.Tipos.TipoReferencia;
import lexical.Token;
import lexical.TokenId;

public class NodoString extends NodoAcceso {
    private Token token;
    private NodoEncadenado encadenado;

    public NodoString(Token token) {
        this.token = token;
    }

    public void setEncadenado(NodoEncadenado encadenado) {
        this.encadenado = encadenado;
    }

    @Override
    public NodoEncadenado getEncadenado(){
        return encadenado;
    }

    @Override
    public boolean tieneEncadenado() {
        return encadenado != null;
    }

    @Override
    public Tipo chequear() {
        return new TipoReferencia(new Token(TokenId.lit_string, "String", token.getLinea()) );
    }

    @Override
    public void generar() {
        int index = Main.TS.getStringCounter();
        Main.TS.getInstructionList().add(".DATA");
        Main.TS.getInstructionList().add("str_" + index + ": DW " + token.getLexeme()+", 0");
        Main.TS.getInstructionList().add(".CODE");
        Main.TS.getInstructionList().add("PUSH "+"str_" + index);
    }
}
