package TablaDeSimbolos;

/*
Una tabla de símbolos es una estructura central del compilador que actúa como un gran diccionario donde se va guardando toda la información de las entidades declaradas en el programa,
como clases, atributos, métodos, parámetros o constructores. Su construcción comienza durante el análisis sintáctico, a medida que se reconocen las distintas declaraciones,
y sirve como base para el análisis semántico, ya que permite verificar que cada nombre usado en el programa corresponde efectivamente a una entidad válida, que no existan repeticiones
indebidas, que los tipos declarados estén definidos, que no haya herencia circular y que las sobreescrituras de métodos respeten la signatura original.
En lenguajes como MiniJava, la tabla de símbolos suele tener una estructura jerárquica: en el nivel global se almacenan las clases, cada clase contiene sus propios atributos y métodos,
y cada metodo o constructor mantiene la información de sus parámetros. De este modo, la tabla refleja el ambiente de declaración del programa, facilita la detección de errores semánticos
y sienta las bases para etapas posteriores como la verificación de sentencias o la generación de código.
 */

import exceptions.SemanticException;
import lexical.Token;

import java.util.HashMap;
import java.util.HashSet;

public class TablaDeSimbolos {
    private HashMap<String, Clase> clases;
    private HashMap<String, Interfaz> interfaces;

    private Clase claseActual;
    private Metodo metodoActual;
    private Atributo atributoActual;
    private Constructor constructorActual;

    public TablaDeSimbolos() {
        this.clases = new HashMap<String, Clase>();
        this.interfaces = new HashMap<String, Interfaz>();

        this.claseActual = null;
        this.metodoActual = null;
        this.atributoActual = null;
        this.constructorActual = null;
        //Agregar todas las clases predefinidas
    }

    public void estaBienDeclarada() throws SemanticException {
        for (Clase c : clases.values()) {
            c.estaBienDeclarada();
        }
    }

    public boolean existeClase(String nombre) {
        for (String c : clases.keySet()) {
            if (c.equals(nombre)) {
                return true;
            }
        }
        return false;
    }

    //Getters y Setters de los actuales
    public Clase getClaseActual() {
        return claseActual;
    }
    public void setClaseActual(Clase claseActual) {
        this.claseActual = claseActual;
    }

    public Metodo getMetodoActual() {
        return metodoActual;
    }
    public void setMetodoActual(Metodo metodoActual) {
        this.metodoActual = metodoActual;
    }

    public Atributo getAtributoActual() {
        return atributoActual;
    }
    public void setAtributoActual(Atributo atributoActual) {
        this.atributoActual = atributoActual;
    }

    public Constructor getConstructorActual() {
        return constructorActual;
    }
    public void setConstructorActual(Constructor constructorActual) {
        this.constructorActual = constructorActual;
    }

    public Clase getClase(String nombre) {
        return this.clases.get(nombre);
    }

    public void insertarClase(Clase clase) throws SemanticException {
        if (this.clases.containsKey(clase.getNombre())) {
            throw new SemanticException(clase.getNombre(), "Clase repetida: " + clase.getNombre(), clase.getToken().getLinea());
        } else {
            this.clases.put(clase.getNombre(), clase);
        }
    }
}
