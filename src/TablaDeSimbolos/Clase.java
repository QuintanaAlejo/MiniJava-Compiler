package TablaDeSimbolos;

import exceptions.SemanticException;
import lexical.Token;

import java.util.HashMap;
import java.util.HashSet;

import static Main.Main.TS;

public class Clase {
    private Token nombre;
    private String herencia;
    private HashMap<String, Atributo> atributos;
    private HashMap<String, Constructor> constructores;
    private HashMap<String, Metodo> metodos;
    boolean herenciaCircular = false;

    public Clase (Token nombre) {
        this.nombre = nombre;
        this.herencia = null;
        this.atributos = new HashMap<>();
        this.constructores = new HashMap<>();
        this.metodos = new HashMap<>();
    }

    public String getNombre() {
        return nombre.getLexeme();
    }
    public Token getToken() {
        return nombre;
    }
    public String getHerencia() {
        return herencia;
    }
    public HashMap<String, Atributo> getAtributos() {
        return atributos;
    }
    public HashMap<String, Constructor> getConstructores() {
        return constructores;
    }
    public HashMap<String, Metodo> getMetodos() {
        return metodos;
    }

    /*
    public void estaBienDeclarada() throws SemanticException {
        if (herencia != null){
            if (TS.existeClase(herencia)){
                for (Atributo a : atributos) {
                    a.estaBienDeclarado();
                }
                for (Constructor c : constructores) {
                    c.estaBienDeclarado();
                }
                for (Metodo m : metodos) {
                    m.estaBienDeclarado(this.nombre);
                }
            } else {
                System.out.println("Error: La clase " + nombre.getLexeme() + " hereda de una clase inexistente: " + herencia);
            }
        }
    }

*/

    public void agregarConstructor(Constructor constructor) throws SemanticException {
        if(constructores.putIfAbsent(constructor.getNombre(), constructor) != null) {
            throw new SemanticException(this.nombre.getLexeme(), "Constructor repetido en la clase " + this.nombre.getLexeme(), constructor.getToken().getLinea());
        }
    }

    public void agregarAtributo(Atributo atributo) throws SemanticException {
        if (atributos.putIfAbsent(atributo.getNombre(), atributo) != null) {
            throw new SemanticException(this.nombre.getLexeme(), "Atributo repetido en la clase " + this.nombre.getLexeme(), atributo.getToken().getLinea());
        }
    }

    public void agregarMetodo(Metodo metodo) throws SemanticException {
        if(metodos.putIfAbsent(metodo.getNombre(), metodo) != null) {
            throw new SemanticException(this.nombre.getLexeme(), "Método repetido en la clase " + this.nombre.getLexeme(), metodo.getToken().getLinea());
        }
    }


}
