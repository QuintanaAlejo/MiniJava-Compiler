package TablaDeSimbolos.NodosAST.sentencia;

import Main.Main;
import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.Tipos.Tipo;
import TablaDeSimbolos.Tipos.TipoNull;
import TablaDeSimbolos.Tipos.TipoPrimitivo;
import exceptions.SemanticException;
import lexical.Token;
import lexical.TokenId;

public class NodoVarLocal extends NodoSentencia{
    private Token identificador;
    private NodoExpresion expresion;
    private Tipo tipo;

    public NodoVarLocal(Token identificador, NodoExpresion expresion) {
        this.identificador = identificador;
        this.expresion = expresion;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public Tipo setTipo(Tipo tipo) {
        return this.tipo = tipo;
    }

    public void chequearVariablesDelPadre() throws SemanticException {
        NodoBloque bloquePadre = Main.TS.getBloqueActual().getBloquePadre();
        while (bloquePadre != null) {
            if (bloquePadre.getVariablesLocales().containsKey(identificador.getLexeme())) {
                throw new SemanticException(identificador.getLexeme(), "La variable local ya ha sido declarada en un bloque padre.", identificador.getLinea());
            }
            bloquePadre = bloquePadre.getBloquePadre();
        }
    }

    @Override
    public void chequear() throws SemanticException {
        tipo = expresion.chequear();
        
        if (Main.TS.getMetodoActual().getParametros().containsKey(identificador.getLexeme())) {
            throw new SemanticException(identificador.getLexeme(), "La variable local no puede tener el mismo nombre que un parámetro del método.", identificador.getLinea());
        }
        if (Main.TS.getClaseActual().getAtributos().containsKey(identificador.getLexeme())) {
            throw new SemanticException(identificador.getLexeme(), "La variable local no puede tener el mismo nombre que un atributo de la clase.", identificador.getLinea());
        }
        if (Main.TS.getBloqueActual().getVariablesLocales().containsKey(identificador.getLexeme())) {
            throw new SemanticException(identificador.getLexeme(), "La variable local ya ha sido declarada en este bloque.", identificador.getLinea());
        }
        if (tipo != null && tipo.esCompatibleCon(new TipoNull()) ) {
            throw new SemanticException(identificador.getLexeme(), "La inicialización de una variable local con 'null' no está permitida.", identificador.getLinea());
        }

        chequearVariablesDelPadre();

        Main.TS.getBloqueActual().agregarVariableLocal(identificador.getLexeme(), this);
    }
}
