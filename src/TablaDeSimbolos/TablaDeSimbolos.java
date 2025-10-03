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

import java.util.HashMap;

public class TablaDeSimbolos {
    private HashMap<String, Clase> clases;
    private HashMap<String, Interfaz> interfaces;

    private Clase claseActual;
    private Metodo metodoActual;
    private Atributo atributoActual;

    public TablaDeSimbolos() {
        this.clases = new HashMap<String, Clase>();
        this.interfaces = new HashMap<String, Interfaz>();

        this.claseActual = null;
        this.metodoActual = null;
        this.atributoActual = null;
    }
}
