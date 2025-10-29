package TablaDeSimbolos;

import TablaDeSimbolos.NodosAST.sentencia.NodoBloque;
import TablaDeSimbolos.Tipos.Tipo;
import lexical.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import exceptions.SemanticException;

public class Metodo {
    private Token nombre;
    private Tipo tipoRetorno;
    private HashMap<String, Parametro> parametros;
    private Token modificador;
    private boolean tieneBloque;
    private NodoBloque bloque;

    public Metodo(Token nombre, Tipo tipoRetorno, Token modificador) {
        this.modificador = modificador;
        this.nombre = nombre;
        this.tipoRetorno = tipoRetorno;
        this.parametros = new LinkedHashMap<>();
    }

    public String getNombre() {
        return nombre.getLexeme();
    }

    public Tipo getTipoRetorno() {
        return tipoRetorno;
    }

    public Token getModificador() {
        return modificador;
    }

    public HashMap<String, Parametro> getParametros() {
        return parametros;
    }

    public void agregarParametro(Parametro parametro) throws SemanticException {
        if (parametros.putIfAbsent(parametro.getNombre(), parametro) != null) {
            throw new SemanticException(parametro.getNombre(), "Parámetro repetido", parametro.getToken().getLinea()); //Ver
        }
    }

    public void estaBienDeclarado() throws SemanticException {
        //Es abstracto y tiene cuerpo?
        if (modificador != null && modificador.getLexeme().equals("abstract") && tieneBloque) {
            throw new SemanticException(nombre.getLexeme(), "El método " + nombre.getLexeme() + " es abstracto y no puede tener cuerpo.", nombre.getLinea());
        }
        //No es abstracto y no tiene cuerpo?
        if (modificador != null && !modificador.getLexeme().equals("abstract") && !tieneBloque) {
            throw new SemanticException(nombre.getLexeme(), "El método " + nombre.getLexeme() + " no es abstracto y debe tener cuerpo.", nombre.getLinea());
        }
        //El tipo de retorno esta bien declarado?
        if (tipoRetorno != null){
            tipoRetorno.estaBienDeclarado();
        }
        //Los parámetros están bien declarados?
        for (Parametro p : parametros.values()) {
            p.estaBienDeclarado();
        }
    }

    public void chequear() throws SemanticException{
        //Chequear el bloque si tiene
        if (tieneBloque && bloque != null && !bloque.isChequeado()){
            bloque.chequear();
        }
    }

    public boolean tieneBloque() {
        return tieneBloque;
    }

    public void setTieneBloque(boolean tieneBloque) {
        this.tieneBloque = tieneBloque;
    }

    public Token getToken() {
        return nombre;
    }

    public NodoBloque getBloque() {
        return bloque;
    }

    public void setBloque(NodoBloque bloque) {
        this.bloque = bloque;
    }
}
