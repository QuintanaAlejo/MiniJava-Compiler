package TablaDeSimbolos;

import Main.Main;
import TablaDeSimbolos.NodosAST.sentencia.NodoBloque;
import TablaDeSimbolos.Tipos.TipoPrimitivo;
import TablaDeSimbolos.Tipos.TipoReferencia;
import exceptions.SemanticException;
import lexical.Token;
import lexical.TokenId;

import java.util.ArrayList;
import java.util.HashMap;

public class TablaDeSimbolos {
    private HashMap<String, Clase> clases;
    private HashMap<String, Interfaz> interfaces;
    private ArrayList<NodoBloque> pilaDeBloques;

    private Clase claseActual;
    private Metodo metodoActual;
    private Constructor constructorActual;
    private NodoBloque bloqueActual;

    private ArrayList<String> instructionList;

    public TablaDeSimbolos() {
        this.clases = new HashMap<String, Clase>();
        this.interfaces = new HashMap<String, Interfaz>();

        this.claseActual = null;
        this.metodoActual = null;
        this.constructorActual = null;
        this.instructionList = new ArrayList<>();
        //Clase Object
        Clase object = new Clase(new Token(TokenId.id_Class, "Object", 0), null, null);
        //Metodo static void debugPrint(int i) - Ver si el retorno es null o void
        Metodo debugPrint = new Metodo(new Token(TokenId.id_MetVar, "debugPrint", 0), null, new Token(TokenId.kw_static, "static", 0));
        debugPrint.setTieneBloque(true);
        debugPrint.setClase(object.getNombre());
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
        read.setClase(system.getNombre());
        system.getMetodos().put(read.getNombre(), read);
        //Metodo static void printB(boolean b)
        Metodo printB = new Metodo(new Token(TokenId.id_MetVar, "printB", 0), null, new Token(TokenId.kw_static, "static", 0));
        printB.setTieneBloque(true);
        printB.setClase(system.getNombre());
        system.getMetodos().put(printB.getNombre(), printB);
        Parametro b = new Parametro(new Token(TokenId.id_MetVar, "b", 0), new TipoPrimitivo(new Token(TokenId.kw_boolean, "boolean", 0)));
        printB.getParametros().put(b.getNombre(), b);
        //Metodo static void printC(char c)
        Metodo printC = new Metodo(new Token(TokenId.id_MetVar, "printC", 0), null, new Token(TokenId.kw_static, "static", 0));
        printC.setTieneBloque(true);
        printC.setClase(system.getNombre());
        system.getMetodos().put(printC.getNombre(), printC);
        Parametro c = new Parametro(new Token(TokenId.id_MetVar, "c", 0), new TipoPrimitivo(new Token(TokenId.kw_char, "char", 0)));
        printC.getParametros().put(c.getNombre(), c);
        //Metodo static void printI(int i)
        Metodo printI = new Metodo(new Token(TokenId.id_MetVar, "printI", 0), null, new Token(TokenId.kw_static, "static", 0));
        printI.setTieneBloque(true);
        printI.setClase(system.getNombre());
        system.getMetodos().put(printI.getNombre(), printI);
        Parametro i2 = new Parametro(new Token(TokenId.id_MetVar, "i", 0), new TipoPrimitivo(new Token(TokenId.kw_int, "int", 0)));
        printI.getParametros().put(i2.getNombre(), i2);
        //Metodo static void printS(String s)
        Metodo printS = new Metodo(new Token(TokenId.id_MetVar, "printS", 0), null, new Token(TokenId.kw_static, "static", 0));
        printS.setTieneBloque(true);
        printS.setClase(system.getNombre());
        system.getMetodos().put(printS.getNombre(), printS);
        Parametro s = new Parametro(new Token(TokenId.id_MetVar, "s", 0), new TipoReferencia(new Token(TokenId.id_Class, "String", 0)));
        printS.getParametros().put(s.getNombre(), s);
        //Metodo static void println()
        Metodo println = new Metodo(new Token(TokenId.id_MetVar, "println", 0), null, new Token(TokenId.kw_static, "static", 0));
        println.setTieneBloque(true);
        println.setClase(system.getNombre());
        system.getMetodos().put(println.getNombre(), println);
        //Metodo static void printBln(boolean b)
        Metodo printBln = new Metodo(new Token(TokenId.id_MetVar, "printBln", 0), null, new Token(TokenId.kw_static, "static", 0));
        printBln.setTieneBloque(true);
        printBln.setClase(system.getNombre());
        system.getMetodos().put(printBln.getNombre(), printBln);
        Parametro b2 = new Parametro(new Token(TokenId.id_MetVar, "b", 0), new TipoPrimitivo(new Token(TokenId.kw_boolean, "boolean", 0)));
        printBln.getParametros().put(b2.getNombre(), b2);
        //Metodo static void printCln(char c)
        Metodo printCln = new Metodo(new Token(TokenId.id_MetVar, "printCln", 0), null, new Token(TokenId.kw_static, "static", 0));
        printCln.setTieneBloque(true);
        printCln.setClase(system.getNombre());
        system.getMetodos().put(printCln.getNombre(), printCln);
        Parametro c2 = new Parametro(new Token(TokenId.id_MetVar, "c", 0), new TipoPrimitivo(new Token(TokenId.kw_char, "char", 0)));
        printCln.getParametros().put(c2.getNombre(), c2);
        //Metodo static void printIln(int i)
        Metodo printIln = new Metodo(new Token(TokenId.id_MetVar, "printIln", 0), null, new Token(TokenId.kw_static, "static", 0));
        printIln.setTieneBloque(true);
        printIln.setClase(system.getNombre());
        system.getMetodos().put(printIln.getNombre(), printIln);
        Parametro i3 = new Parametro(new Token(TokenId.id_MetVar, "i", 0), new TipoPrimitivo(new Token(TokenId.kw_int, "int", 0)));
        printIln.getParametros().put(i3.getNombre(), i3);
        //Metodo static void printSln(String s)
        Metodo printSln = new Metodo(new Token(TokenId.id_MetVar, "printSln", 0), null, new Token(TokenId.kw_static, "static", 0));
        printSln.setTieneBloque(true);
        printSln.setClase(system.getNombre());
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

    public void chequear() throws SemanticException {
        for (Clase c : clases.values()) {
            Main.TS.setClaseActual(c);
            c.chequear();
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

    public ArrayList<String> getInstructionList(){
        return instructionList;
    }

    public void generar(){
        generarInicial();
        generarHeap();
        generarClasesPred();

        for(Clase c : clases.values()){
            if (!c.getNombre().equals("Object") && !c.getNombre().equals("System") && !c.getNombre().equals("String")){
                c.generar();
            }
        }
    }

    public void generarInicial(){
        instructionList.add(".CODE");
        instructionList.add("PUSH simple_heap_init");
        instructionList.add("CALL");
        instructionList.add("PUSH Init_main");
        instructionList.add("CALL");
        instructionList.add("HALT");
    }

    public void generarHeap(){
        instructionList.add("simple_heap_init: RET 0 ; Retorna inmediatamente");
        instructionList.add("");
        instructionList.add("simple_malloc: LOADFP");
        instructionList.add("");
        instructionList.add("LOADSP");
        instructionList.add("STOREFP");
        instructionList.add("LOADHL");
        instructionList.add("DUP");
        instructionList.add("PUSH 1");
        instructionList.add("ADD");
        instructionList.add("STORE 4");
        instructionList.add("LOAD 3");
        instructionList.add("ADD");
        instructionList.add("STOREHL");
        instructionList.add("STOREFP");
        instructionList.add("RET 1");
    }

    public void generarClasesPred(){
        //Object class
        //static void debugPrint(int i)
        instructionList.add("; Clase Object");
        instructionList.add("Object_debugPrint:");
        instructionList.add("LOADFP");
        instructionList.add("LOADSP");
        instructionList.add("STOREFP");
        instructionList.add("LOAD 3");
        instructionList.add("IPRINT");
        instructionList.add("STOREFP");
        instructionList.add("RET 1");

        //System class
        //static int read()
        instructionList.add("; Clase System");
        instructionList.add("System_read:");
        instructionList.add("LOADFP");
        instructionList.add("LOADSP");
        instructionList.add("STOREFP");
        instructionList.add("READ");
        instructionList.add("STORE 3");
        instructionList.add("STOREFP");
        instructionList.add("RET 0");

        //static void printB(boolean b)
        instructionList.add("System_printB:");
        instructionList.add("LOADFP");
        instructionList.add("LOADSP");
        instructionList.add("STOREFP");
        instructionList.add("LOAD 3");
        instructionList.add("BPRINT");
        instructionList.add("STOREFP");
        instructionList.add("RET 1");

        //static void printC(char c)
        instructionList.add("System_printC:");
        instructionList.add("LOADFP");
        instructionList.add("LOADSP");
        instructionList.add("STOREFP");
        instructionList.add("LOAD 3");
        instructionList.add("CPRINT");
        instructionList.add("STOREFP");
        instructionList.add("RET 1");

        //static void printI(int i)
        instructionList.add("System_printI:");
        instructionList.add("LOADFP");
        instructionList.add("LOADSP");
        instructionList.add("STOREFP");
        instructionList.add("LOAD 3");
        instructionList.add("IPRINT");
        instructionList.add("STOREFP");
        instructionList.add("RET 1");

        //static void printS(String s)
        instructionList.add("System_printS:");
        instructionList.add("LOADFP");
        instructionList.add("LOADSP");
        instructionList.add("STOREFP");
        instructionList.add("LOAD 3");
        instructionList.add("SPRINT");
        instructionList.add("STOREFP");
        instructionList.add("RET 1");

        //static void println()
        instructionList.add("System_println:");
        instructionList.add("LOADFP");
        instructionList.add("LOADSP");
        instructionList.add("STOREFP");
        instructionList.add("PRNLN");
        instructionList.add("STOREFP");
        instructionList.add("RET 0");

        //static void printBln(boolean b)
        instructionList.add("System_printBln:");
        instructionList.add("LOADFP");
        instructionList.add("LOADSP");
        instructionList.add("STOREFP");
        instructionList.add("LOAD 3");
        instructionList.add("BPRINT");
        instructionList.add("PRNLN");
        instructionList.add("STOREFP");
        instructionList.add("RET 1");

        //static void printCln(char c)
        instructionList.add("System_printCln:");
        instructionList.add("LOADFP");
        instructionList.add("LOADSP");
        instructionList.add("STOREFP");
        instructionList.add("LOAD 3");
        instructionList.add("CPRINT");
        instructionList.add("PRNLN");
        instructionList.add("STOREFP");
        instructionList.add("RET 1");

        //static void printIln(int i)
        instructionList.add("System_printIln:");
        instructionList.add("LOADFP");
        instructionList.add("LOADSP");
        instructionList.add("STOREFP");
        instructionList.add("LOAD 3");
        instructionList.add("IPRINT");
        instructionList.add("PRNLN");
        instructionList.add("STOREFP");
        instructionList.add("RET 1");

        //static void printSln(String s)
        instructionList.add("System_printSln:");
        instructionList.add("LOADFP");
        instructionList.add("LOADSP");
        instructionList.add("STOREFP");
        instructionList.add("LOAD 3");
        instructionList.add("SPRINT");
        instructionList.add("PRNLN");
        instructionList.add("STOREFP");
        instructionList.add("RET 1");
    }

    public void agregarInstruccion(String inst){
        instructionList.add(inst);
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

    public HashMap<String, Clase> getClases() {
        return clases;
    }

    public void insertarClase(Clase clase) throws SemanticException {
        if (this.clases.containsKey(clase.getNombre())) {
            throw new SemanticException(clase.getNombre(), "Clase repetida: " + clase.getNombre(), clase.getToken().getLinea());
        } else {
            this.clases.put(clase.getNombre(), clase);
        }
    }

    public void setBloqueActual(NodoBloque bloqueActual) {
        this.bloqueActual = bloqueActual;
    }

    public NodoBloque getBloqueActual() {
        return bloqueActual;
    }
}
