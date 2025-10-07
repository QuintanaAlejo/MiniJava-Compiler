package TablaDeSimbolos;

import lexical.Token;

import java.util.HashMap;
import exceptions.SemanticException;

public class Metodo {
    private Token nombre;
    private Tipo tipoRetorno;
    private HashMap<String, Parametro> parametros;
    private Token modificador;
    private boolean esAbstracto;

    public Metodo(Token nombre, Tipo tipoRetorno, Token modificador) {
        this.modificador = modificador;
        this.nombre = nombre;
        this.tipoRetorno = tipoRetorno;
        this.parametros = new HashMap<>();
    }

    public String getNombre() {
        return nombre.getLexeme();
    }

    public void agregarParametro(Parametro parametro) throws SemanticException {
        if (parametros.putIfAbsent(parametro.getNombre(), parametro) != null) {
            throw new SemanticException(parametro.getNombre(), "Parámetro repetido", parametro.getToken().getLinea()); //Ver
        }
    }

    public void estaBienDeclarado() throws SemanticException {
        if (this.tipoRetorno == null) { // Ver
            throw new SemanticException(this.nombre.getLexeme(), "El método no tiene un tipo de retorno declarado.", this.nombre.getLinea());
        }
        for (Parametro parametro : parametros.values()) {
            parametro.estaBienDeclarado();
            //Si esa de tipo clase tiene que existir
        }
    }

    public boolean tieneBloque() {
        return esAbstracto;
    }

    public void setTieneBloque(boolean esAbstracto) {
        this.esAbstracto = esAbstracto;
    }

    public Token getToken() {
        return nombre;
    }
}
