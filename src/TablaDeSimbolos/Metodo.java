package TablaDeSimbolos;

import lexical.Token;
import java.util.HashSet;
import exceptions.SemanticException;

public class Metodo {
    private Token nombre;
    private Tipo tipoRetorno;
    private HashSet<Parametro> parametros;

    public Metodo(Token nombre, Tipo tipoRetorno) {
        this.nombre = nombre;
        this.tipoRetorno = tipoRetorno;
        this.parametros = new HashSet<>();
    }

    public String getNombre() {
        return nombre.getLexeme();
    }

    public void agregarParametro(Parametro parametro) throws SemanticException {
        for (Parametro p : parametros) {
            if (p.getNombre().equals(parametro.getNombre())) {
                throw new SemanticException(this.nombre.getLexeme(), "Parámetro repetido en el método " + this.nombre.getLexeme(), parametro.getToken().getLinea());
            } else {
                this.parametros.add(parametro);
            }
        }
    }

    public void estaBienDeclarado(Token claseActual) throws SemanticException {
        if (this.tipoRetorno == null) { // Ver
            throw new SemanticException(this.nombre.getLexeme(), "El método no tiene un tipo de retorno declarado.", this.nombre.getLinea());
        }
        for (Parametro parametro : this.parametros) {
            parametro.estaBienDeclarado();
        }
    }
}
