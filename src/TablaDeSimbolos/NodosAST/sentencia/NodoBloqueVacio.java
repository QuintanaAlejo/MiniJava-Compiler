package TablaDeSimbolos.NodosAST.sentencia;

public class NodoBloqueVacio extends NodoBloque{
    public NodoBloqueVacio() {
    }

    @Override
    public void chequear() {
        // No hay nada que chequear en un bloque vacío
    }

    @Override
    public void generar(){
        // No hace nada
    }
}
