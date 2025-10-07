package TablaDeSimbolos;

import exceptions.SemanticException;
import lexical.Token;

import java.util.HashMap;
import java.util.HashSet;

import static Main.Main.TS;

public class Clase {
    private Token nombre;
    private Token padre;
    private Token modificador;
    private HashMap<String, Atributo> atributos;
    private HashMap<String, Constructor> constructores;
    private HashMap<String, Metodo> metodos;

    public Clase (Token nombre, Token mod) {
        this.nombre = nombre;
        this.modificador = mod;
        this.padre = null;
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
    public Token getPadre() {
        return padre;
    }
    public Token getModificador() {
        return modificador;
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

    public void estaBienDeclarada() throws SemanticException {
        if (padre != null){
            if (!TS.existeClase(padre.getLexeme())) {
                if (hayHerenciaCircular(this)) {
                    throw new SemanticException(nombre.getLexeme(), "La clase " + nombre.getLexeme() + " genera herencia circular.", nombre.getLinea());
                }

                String modPadre = TS.getClase(padre.getLexeme()).getModificador().getLexeme();
                if (modPadre.equals("kw_final") || modPadre.equals("kw_static")) {
                    throw new SemanticException(nombre.getLexeme(), "La clase " + nombre.getLexeme() + " no puede heredar de una clase final o estatica como: " + padre.getLexeme(), this.nombre.getLinea());
                }

            } else {
                throw new SemanticException(nombre.getLexeme(), "La clase " + nombre.getLexeme() + " hereda de una clase inexistente: " + padre.getLexeme(), nombre.getLinea());
            }
        }
        for (Atributo a : atributos.values()) {
            a.estaBienDeclarado();
        }
        for (Constructor c : constructores.values()) {
            //Nombre == clase?
            //Si la clase es abstracto puede tener constructores? Creo que si
            c.estaBienDeclarado();
        }
        for (Metodo m : metodos.values()) {
            //Me fijo si es abstracto y tiene cuerpo
            //Controlo que si hay abstractos la clase tiene que ser abstracta
            //Si el tipo de retorno es una clase te fijas si existe
            m.estaBienDeclarado();
        }
    }

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

    public void setPadre(Token padre) {
        this.padre = padre;
    }

    private boolean hayHerenciaCircular(Clase origen) {
        HashSet<String> visitados = new HashSet<>();
        String actual = this.padre.getLexeme();

        while (actual != null) {
            if (actual.equals(origen.getNombre())) {
                return true; // volvió a la clase de origen
            }
            if (!visitados.add(actual)) {
                return true; // ciclo (A->B->C->B, etc.)
            }
            if (!TS.existeClase(actual)) {
                // Si en el camino falta una clase, el error lo reporta el paso 2 de arriba.
                return false;
            }
            Clase ancestro = TS.getClase(actual);

            // Si el ancestro es Object o similar, cortamos (no hay ciclo).
            // Ajustá este nombre si en tu TS usás otro literal para la raíz.
            if (ancestro.getNombre().equals("Object")) {
                return false;
            }

            actual = ancestro.getPadre().getLexeme();
        }
        return false;
    }

}
