package TablaDeSimbolos;

import exceptions.SemanticException;
import lexical.Token;

import java.util.HashSet;

import static Main.Main.TS;

public class Clase {
    private Token nombre;
    private String herencia;
    private HashSet<Atributo> atributos;
    private HashSet<Constructor> constructores;
    private HashSet<Metodo> metodos;
    boolean herenciaCircular = false;

    public Clase (Token nombre, String herencia) {
        this.nombre = nombre;
        this.herencia = herencia;
        this.atributos = new HashSet<>();
        this.constructores = new HashSet<>();
        this.metodos = new HashSet<>();
    }

    public String getNombre() {
        return nombre.getLexeme();
    }
    public String getHerencia() {
        return herencia;
    }
    public HashSet<Atributo> getAtributos() {
        return atributos;
    }
    public HashSet<Constructor> getConstructores() {
        return constructores;
    }
    public HashSet<Metodo> getMetodos() {
        return metodos;
    }
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
                System.out.println("Error: La clase " + nombre + " hereda de una clase inexistente: " + herencia);
            }
        }
    }

    public void agregarConstructor(Constructor constructor) {
        this.constructores.add(constructor);
    }



}
