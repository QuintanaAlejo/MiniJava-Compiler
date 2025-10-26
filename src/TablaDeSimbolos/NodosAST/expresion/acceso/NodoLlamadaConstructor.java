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
    private Clase clase;
    private List<NodoExpresion> parametros;
    private NodoEncadenado encadenado;

    public NodoLlamadaConstructor(Token token) {
        this.token = token;
        this.clase = Main.TS.getClaseActual();
    }

    public void setParametros(List<NodoExpresion> parametros) {
        this.parametros = parametros;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public void setEncadenado(NodoEncadenado encadenado) {
        this.encadenado = encadenado;
    }

    @Override
    public Tipo chequear() throws SemanticException {
        if (!Main.TS.getClases().containsKey(clase.getNombre())){
            throw new SemanticException(clase.getNombre(), "Clase inexistente", token.getLinea());
        }

        if(encadenado==null){
            return new TipoReferencia(clase.getToken());
        } else {
            return encadenado.chequear(new TipoReferencia(clase.getToken()));
        }
    }
}
