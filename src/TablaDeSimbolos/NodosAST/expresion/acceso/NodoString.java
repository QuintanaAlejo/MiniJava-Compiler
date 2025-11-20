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
        String lex = token.getLexeme();
        if (lex != null && lex.length() >= 2) {
            if ((lex.startsWith("\"") && lex.endsWith("\"")) || (lex.startsWith("'") && lex.endsWith("'"))) {
                lex = lex.substring(1, lex.length() - 1);
            }
        } else {
            lex = "";
        }

        // Ver si el string contiene caracteres especiales y escaparlos
        Main.TS.getInstructionList().add("PUSH " + lex + " ; Cargo el string");
    }
}
