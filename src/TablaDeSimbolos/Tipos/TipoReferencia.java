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

        if (!clases.containsKey(this.getNombre()) || !clases.containsKey(otroToken.getLexeme())) {
            return false;
        }

        if (sonMismoTipo(otroToken) || esNull(otroToken)) {
            return true;
        }

        Set<String> visitados = new HashSet<>();
        Clase claseActual = clases.get(otroToken.getLexeme());

        while (claseActual != null && !(visitados.contains(claseActual.getNombre()))){
            visitados.add(claseActual.getNombre());
            Token tokenPadre = claseActual.getPadre();

            if (tokenPadre == null) {
                break;
            }

            if (sonMismoTipo(tokenPadre)) {
                return true;
            }

            claseActual = clases.get(tokenPadre.getLexeme());
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
