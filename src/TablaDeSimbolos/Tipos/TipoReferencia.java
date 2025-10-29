package TablaDeSimbolos.Tipos;

import Main.Main;
import TablaDeSimbolos.Clase;
import lexical.Token;
import exceptions.SemanticException;
import lexical.TokenId;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TipoReferencia implements Tipo {
    private Token tokenPropio;

    public TipoReferencia(Token token) {
        this.tokenPropio = token;
    }

    @Override
    public Token getTokenPropio() {
        return tokenPropio;
    }

    @Override
    public void estaBienDeclarado() throws SemanticException {
        // Consultar si la clase está declarada en la tabla de símbolos
        if (!Main.TS.existeClase(tokenPropio.getLexeme())){
            throw new SemanticException(tokenPropio.getLexeme(), "La clase del tipo de retorno no existe.", tokenPropio.getLinea());
        }
    }

    @Override
    public boolean esPrimitivo() {
        return false;
    }

    @Override
    public String getNombre() {
        return tokenPropio.getLexeme();
    }

    @Override
    public boolean esCompatibleCon(Tipo otroTipo) {
        Token otroToken = otroTipo.getTokenPropio();
        HashMap<String, Clase> clases = Main.TS.getClases();

        if (sonMismoTipo(otroToken) || esNull(otroToken)) {
            return true;
        }
        if (esNull(this.tokenPropio)) {
            return (otroTipo instanceof TipoReferencia);
        }

        if (!clases.containsKey(this.getNombre()) || !clases.containsKey(otroToken.getLexeme())) {
            return false;
        }

        if (esSubtipo(otroToken.getLexeme(), this.getNombre(), clases)) {
            return true;
        }

        if (esSubtipo(this.getNombre(), otroToken.getLexeme(), clases)) {
            return true;
        }

        return false;
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


    private boolean esNull(Token token) {
        return token.getTokenId() == TokenId.kw_null;
    }

    private boolean sonMismoTipo(Token otroToken) {
        return this.tokenPropio.getLexeme().equals(otroToken.getLexeme());
    }
}
