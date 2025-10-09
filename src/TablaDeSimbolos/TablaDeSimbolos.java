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
import lexical.TokenId;

import java.util.HashMap;

public class TablaDeSimbolos {
    private HashMap<String, Clase> clases;
    private HashMap<String, Interfaz> interfaces;

    private Clase claseActual;
    private Metodo metodoActual;
    private Constructor constructorActual;

    public TablaDeSimbolos() {
        this.clases = new HashMap<String, Clase>();
        this.interfaces = new HashMap<String, Interfaz>();

        this.claseActual = null;
        this.metodoActual = null;
        this.constructorActual = null;
        //Clase Object
        Clase object = new Clase(new Token(TokenId.id_Class, "Object", 0), null, null);
        //Metodo static void debugPrint(int i) - Ver si el retorno es null o void
        Metodo debugPrint = new Metodo(new Token(TokenId.id_MetVar, "debugPrint", 0), null, new Token(TokenId.kw_static, "static", 0));
        debugPrint.setTieneBloque(true);
        object.getMetodos().put(debugPrint.getNombre(), debugPrint);
        Parametro i = new Parametro(new Token(TokenId.id_MetVar, "i", 0), new TipoPrimitivo(new Token(TokenId.kw_int, "int", 0)));
        debugPrint.getParametros().put(i.getNombre(), i);

        clases.put(object.getNombre(), object);

        //Clase String
        Clase string = new Clase(new Token(TokenId.id_Class, "String", 0), null, object.getToken());
        clases.put(string.getNombre(), string);

        //Clase System
        Clase system = new Clase(new Token(TokenId.id_Class, "System", 0), null, object.getToken());
        //Metodo static int read()
        Metodo read = new Metodo(new Token(TokenId.id_MetVar, "read", 0), new TipoPrimitivo(new Token(TokenId.kw_int, "int", 0)), new Token(TokenId.kw_static, "static", 0));
        read.setTieneBloque(true);
        system.getMetodos().put(read.getNombre(), read);
        //Metodo static void printB(boolean b)
        Metodo printB = new Metodo(new Token(TokenId.id_MetVar, "printB", 0), null, new Token(TokenId.kw_static, "static", 0));
        printB.setTieneBloque(true);
        system.getMetodos().put(printB.getNombre(), printB);
        Parametro b = new Parametro(new Token(TokenId.id_MetVar, "b", 0), new TipoPrimitivo(new Token(TokenId.kw_boolean, "boolean", 0)));
        printB.getParametros().put(b.getNombre(), b);
        //Metodo static void printC(char c)
        Metodo printC = new Metodo(new Token(TokenId.id_MetVar, "printC", 0), null, new Token(TokenId.kw_static, "static", 0));
        printC.setTieneBloque(true);
        system.getMetodos().put(printC.getNombre(), printC);
        Parametro c = new Parametro(new Token(TokenId.id_MetVar, "c", 0), new TipoPrimitivo(new Token(TokenId.kw_char, "char", 0)));
        printC.getParametros().put(c.getNombre(), c);
        //Metodo static void printI(int i)
        Metodo printI = new Metodo(new Token(TokenId.id_MetVar, "printI", 0), null, new Token(TokenId.kw_static, "static", 0));
        printI.setTieneBloque(true);
        system.getMetodos().put(printI.getNombre(), printI);
        Parametro i2 = new Parametro(new Token(TokenId.id_MetVar, "i", 0), new TipoPrimitivo(new Token(TokenId.kw_int, "int", 0)));
        printI.getParametros().put(i2.getNombre(), i2);
        //Metodo static void printS(String s)
        Metodo printS = new Metodo(new Token(TokenId.id_MetVar, "printS", 0), null, new Token(TokenId.kw_static, "static", 0));
        printS.setTieneBloque(true);
        system.getMetodos().put(printS.getNombre(), printS);
        Parametro s = new Parametro(new Token(TokenId.id_MetVar, "s", 0), new TipoReferencia(new Token(TokenId.id_Class, "String", 0)));
        //Metodo static void println()
        Metodo println = new Metodo(new Token(TokenId.id_MetVar, "println", 0), null, new Token(TokenId.kw_static, "static", 0));
        println.setTieneBloque(true);
        system.getMetodos().put(println.getNombre(), println);
        //Metodo static void printBln(boolean b)
        Metodo printBln = new Metodo(new Token(TokenId.id_MetVar, "printBln", 0), null, new Token(TokenId.kw_static, "static", 0));
        printBln.setTieneBloque(true);
        system.getMetodos().put(printBln.getNombre(), printBln);
        Parametro b2 = new Parametro(new Token(TokenId.id_MetVar, "b", 0), new TipoPrimitivo(new Token(TokenId.kw_boolean, "boolean", 0)));
        printBln.getParametros().put(b2.getNombre(), b2);
        //Metodo static void printCln(char c)
        Metodo printCln = new Metodo(new Token(TokenId.id_MetVar, "printCln", 0), null, new Token(TokenId.kw_static, "static", 0));
        printCln.setTieneBloque(true);
        system.getMetodos().put(printCln.getNombre(), printCln);
        Parametro c2 = new Parametro(new Token(TokenId.id_MetVar, "c", 0), new TipoPrimitivo(new Token(TokenId.kw_char, "char", 0)));
        printCln.getParametros().put(c2.getNombre(), c2);
        //Metodo static void printIln(int i)
        Metodo printIln = new Metodo(new Token(TokenId.id_MetVar, "printIln", 0), null, new Token(TokenId.kw_static, "static", 0));
        printIln.setTieneBloque(true);
        system.getMetodos().put(printIln.getNombre(), printIln);
        Parametro i3 = new Parametro(new Token(TokenId.id_MetVar, "i", 0), new TipoPrimitivo(new Token(TokenId.kw_int, "int", 0)));
        printIln.getParametros().put(i3.getNombre(), i3);
        //Metodo static void printSln(String s)
        Metodo printSln = new Metodo(new Token(TokenId.id_MetVar, "printSln", 0), null, new Token(TokenId.kw_static, "static", 0));
        printSln.setTieneBloque(true);
        system.getMetodos().put(printSln.getNombre(), printSln);
        Parametro s2 = new Parametro(new Token(TokenId.id_MetVar, "s", 0), new TipoReferencia(new Token(TokenId.id_Class, "String", 0)));
        printSln.getParametros().put(s2.getNombre(), s2);

        clases.put(system.getNombre(), system);
    }

    public void estaBienDeclarada() throws SemanticException {
        for (Clase c : clases.values()) {
            c.estaBienDeclarada();
        }
    }

    public void consolidar() throws SemanticException {
        for (Clase c : clases.values()) {
            c.consolidar();
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
