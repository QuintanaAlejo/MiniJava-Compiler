package TablaDeSimbolos.NodosAST.expresion.acceso;

import Main.Main;
import TablaDeSimbolos.Clase;
import TablaDeSimbolos.NodosAST.encadenado.NodoEncadenado;
import TablaDeSimbolos.NodosAST.expresion.NodoExpresion;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import TablaDeSimbolos.Tipos.Tipo;
import TablaDeSimbolos.Tipos.TipoReferencia;
import exceptions.SemanticException;
import lexical.Token;

import java.util.List;

public class NodoLlamadaConstructor extends NodoAcceso {
    private Token token;
    private List<NodoExpresion> parametros;
    private NodoEncadenado encadenado;

    public NodoLlamadaConstructor(Token token) {
        this.token = token;
    }

    public void setParametros(List<NodoExpresion> parametros) {
        this.parametros = parametros;
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
    public Tipo chequear() throws SemanticException {
        if (!Main.TS.getClases().containsKey(token.getLexeme())) {
            throw new SemanticException(token.getLexeme(), "Clase inexistente", token.getLinea());
        }

        if(encadenado==null){
            return new TipoReferencia(token);
        } else {
            return encadenado.chequear(new TipoReferencia(token));
        }
    }
}
