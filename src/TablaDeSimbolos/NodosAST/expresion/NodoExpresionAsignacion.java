package TablaDeSimbolos.NodosAST.expresion;

import TablaDeSimbolos.NodosAST.encadenado.NodoEncadenado;
import TablaDeSimbolos.NodosAST.expresion.acceso.NodoExpresionParentizada;
import TablaDeSimbolos.NodosAST.expresion.acceso.NodoVarAcceso;
import TablaDeSimbolos.Tipos.Tipo;
import TablaDeSimbolos.Tipos.TipoReferencia;
import exceptions.SemanticException;
import lexical.Token;
import lexical.TokenId;
import Main.Main;

public class NodoExpresionAsignacion extends NodoExpresionCompuesta{
    private NodoExpresion izquierda;
    private NodoExpresion derecha;
    private Token token;

    public NodoExpresionAsignacion(NodoExpresion izquierda, NodoExpresion derecha, Token token) {
        this.izquierda = izquierda;
        this.derecha = derecha;
        this.token = token;
    }

    @Override
    public boolean tieneEncadenado() {
        return false;
    }

    @Override
    public Tipo chequear() throws SemanticException {
        Tipo destino = izquierda.chequear();
        Tipo origen  = derecha.chequear();

        // Parámetro o variable local tapa atributo
        if (izquierda instanceof NodoVarAcceso varAcceso && !izquierda.tieneEncadenado()) {
            String nombre = varAcceso.getToken().getLexeme();

            // Si existe un parámetro con ese nombre, usar su tipo
            var metodoActual = Main.TS.getMetodoActual();
            if (metodoActual.getParametros().containsKey(nombre)) {
                destino = metodoActual.getParametros().get(nombre).getTipo();
            }
            // Si existe una variable local con ese nombre, usar su tipo
            else if (Main.TS.getBloqueActual() != null &&
                    Main.TS.getBloqueActual().getVariablesLocales().containsKey(nombre)) {
                destino = Main.TS.getBloqueActual().getVariablesLocales().get(nombre).getTipo();
            }
        }

        if (!(izquierda instanceof NodoVarAcceso) && !izquierda.tieneEncadenado()) {
            throw new SemanticException(token.getLexeme(), "El lado izquierdo de una asignacion debe ser una variable.", token.getLinea());
        }

        if(izquierda.tieneEncadenado() && !(izquierda instanceof NodoVarAcceso)) {
            var acceso = (TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso) izquierda;
            var enc = acceso.getEncadenado();
            if (enc != null && !enc.terminaEnVariable()) {
                throw new SemanticException(token.getLexeme(), "El lado izquierdo de una asignacion debe terminar en un acceso a variable.", token.getLinea());
            }
        }

        if (derecha instanceof NodoExpresionParentizada exp) {
            if (exp.getExpresion() instanceof NodoExpresionAsignacion) {
                throw new SemanticException(token.getLexeme(), "Asignacion invalida: no se permite asignacion encadenada.", token.getLinea());
            }
        }

        // Reglas de compatibilidad
        if (destino.esCompatibleCon(origen)) {
            boolean mismoTipo = destino.getNombre().equals(origen.getNombre());
            boolean rhsEsNull = origen.getTokenPropio().getTokenId().equals(TokenId.kw_null);

            if (destino instanceof TipoReferencia && origen instanceof TipoReferencia) {
                if (!mismoTipo && !rhsEsNull && esSubtipo((TipoReferencia) destino, (TipoReferencia) origen)) {
                    throw new SemanticException(token.getLexeme(), "Asignacion invalida: no se puede asignar un valor de tipo " + origen.getNombre() + " a una variable de tipo " + destino.getNombre(), token.getLinea());
                }
            }

            return destino;
        } else {
            throw new SemanticException(token.getLexeme(), "Asignacion invalida: no se puede asignar un valor de tipo " + origen.getNombre() + " a una variable de tipo " + destino.getNombre(), token.getLinea());
        }
    }

    private boolean esSubtipo(TipoReferencia a, TipoReferencia b) {
        if (a.getNombre().equals(b.getNombre())){
            return true;
        }

        var clases = Main.TS.getClases();
        var visitados = new java.util.HashSet<String>();

        TablaDeSimbolos.Clase clase = clases.get(a.getNombre());
        while (clase != null && !visitados.contains(clase.getNombre())) {
            visitados.add(clase.getNombre());
            Token padre = clase.getPadre();
            if (padre == null) break;
            if (padre.getLexeme().equals(b.getNombre())) return true;
            clase = clases.get(padre.getLexeme());
        }
        return false;
    }

}
