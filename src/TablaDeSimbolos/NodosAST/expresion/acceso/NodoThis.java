package TablaDeSimbolos.NodosAST.expresion.acceso;

import Main.Main;
import TablaDeSimbolos.NodosAST.encadenado.NodoEncadenado;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import TablaDeSimbolos.Tipos.Tipo;
import TablaDeSimbolos.Tipos.TipoReferencia;
import exceptions.SemanticException;
import lexical.Token;
import lexical.TokenId;

public class NodoThis extends NodoAcceso {
    private Token token;
    private String nombreClase;
    private NodoEncadenado encadenadoOpcional;

    public NodoThis(Token token, String nombreClase) {
        this.token = token;
        this.nombreClase = nombreClase;
    }

    public Token getToken() {
        return token;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    @Override
    public NodoEncadenado getEncadenado(){
        return encadenadoOpcional;
    }

    @Override
    public boolean tieneEncadenado() {
        return encadenadoOpcional != null;
    }

    @Override
    public Tipo chequear() throws SemanticException {
        Token modificadorMetodoActual = Main.TS.getMetodoActual().getModificador();
        if (modificadorMetodoActual != null && modificadorMetodoActual.getTokenId() == TokenId.kw_static){
            throw new SemanticException(token.getLexeme(), "No es posible usar 'this' en un metodo estatico", token.getLinea());
        }
        Tipo tipoThis = new TipoReferencia(Main.TS.getClaseActual().getToken());
        if (encadenadoOpcional == null) {
            return tipoThis;
        } else {
            return encadenadoOpcional.chequear(tipoThis);
        }
    }

    @Override
    public void setEncadenado(NodoEncadenado encadenado) {
        encadenadoOpcional = encadenado;
    }
}
