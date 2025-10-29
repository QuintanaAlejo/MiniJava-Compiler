package TablaDeSimbolos;

import Main.Main;
import exceptions.SemanticException;
import lexical.Token;
import lexical.TokenId;

import java.util.HashMap;
import java.util.HashSet;

import static Main.Main.TS;

public class Clase {
    private Token nombre;
    private Token padre;
    private Token modificador;
    private HashMap<String, Atributo> atributos;
    private Constructor constructor;
    private HashMap<String, Metodo> metodos;
    private boolean consolidado = false;

    public Clase (Token nombre, Token mod, Token padre) {
        this.nombre = nombre;
        this.modificador = mod;
        this.padre = padre;
        this.atributos = new HashMap<>();
        this.constructor = null;
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
    public Constructor getConstructor() {
        return constructor;
    }
    public HashMap<String, Metodo> getMetodos() {
        return metodos;
    }

    public void estaBienDeclarada() throws SemanticException {
        if (padre != null){
            if (TS.existeClase(padre.getLexeme())) {
                if (hayHerenciaCircular(this)) {
                    throw new SemanticException(padre.getLexeme(), "La clase " + padre.getLexeme() + " genera herencia circular.", padre.getLinea());
                }

                Token tokenModPadre = TS.getClase(padre.getLexeme()).getModificador();
                if (tokenModPadre != null) {
                    String modPadre = tokenModPadre.getLexeme();
                    if (modPadre.equals("final") || modPadre.equals("static")) {
                        throw new SemanticException(nombre.getLexeme(), "La clase " + nombre.getLexeme() + " no puede heredar de una clase final o estatica como: " + padre.getLexeme(), this.nombre.getLinea());
                    }
                } else {
                    if (!padre.getLexeme().equals("Object")) {
                        //Si el modificador de la clase actual es abstract, no puede heredar de una clase concreta
                        if (modificador != null && modificador.getLexeme().equals("abstract")) {
                            throw new SemanticException(nombre.getLexeme(), "La clase " + nombre.getLexeme() + " es abstracta y no puede heredar de una clase concreta: " + padre.getLexeme(), this.nombre.getLinea());
                        }
                    }
                }

            } else {
                throw new SemanticException(padre.getLexeme(), "La clase " + nombre.getLexeme() + " hereda de una clase inexistente: " + padre.getLexeme(), nombre.getLinea());
            }
        }
        if (constructor != null){
            if (!constructor.getNombre().equals(getNombre())){
                throw new SemanticException(constructor.getNombre(), "El constructor " + constructor.getNombre() + " debe tener el mismo nombre que la clase " + getNombre(), constructor.getToken().getLinea());
            }
            if (modificador != null && modificador.getLexeme().equals("abstract")){
                throw new SemanticException(nombre.getLexeme(), "La clase " + nombre.getLexeme() + " es abstracta y no puede tener constructor.", constructor.getToken().getLinea());
            }
            constructor.estaBienDeclarado();
        }

        for (Atributo a : atributos.values()) {
            a.estaBienDeclarado();
        }
        for (Metodo m : metodos.values()) {
            // Chequeo que si hay un metodo abstract la clase tiene que ser abstract
            if (modificador != null){
                if(!modificador.getLexeme().equals("abstract") && m.getModificador() != null && m.getModificador().getLexeme().equals("abstract")){
                    throw new SemanticException(nombre.getLexeme(), "La clase " + nombre.getLexeme() + " debe ser abstracta ya que tiene un método abstracto: " + m.getNombre(), nombre.getLinea());
                }
            } else {
                if (m.getModificador() != null && m.getModificador().getLexeme().equals("abstract")){
                    throw new SemanticException(m.getToken().getLexeme(), "La clase " + nombre.getLexeme() + " debe ser abstracta ya que tiene un método abstracto: " + m.getNombre(), m.getToken().getLinea());
                }
                //Chequeo que si la clase no es abstracta, no tenga metodos sin cuerpo
                if (!m.tieneBloque()){
                    throw new SemanticException(m.getToken().getLexeme(), "El método " + m.getNombre() + " debe tener cuerpo ya que la clase " + nombre.getLexeme() + " no es abstracta.", m.getToken().getLinea());
                }
            }
            m.estaBienDeclarado();
        }
    }

    public void consolidar() throws SemanticException {
        if (consolidado) return;

        // Consolidar el padre primero (si existe y no es Object)
        if (padre != null && !getPadre().getLexeme().equals("Object")) {
            Clase clasePadre = TS.getClase(padre.getLexeme());
            if (clasePadre != null) {
                clasePadre.consolidar();

                // Heredar atributos
                for (Atributo a : clasePadre.getAtributos().values()) {
                    if (!this.atributos.containsKey(a.getNombre())) {
                        this.atributos.put(a.getNombre(), a);
                    } else {
                        throw new SemanticException(a.getNombre(), "La clase " + this.nombre.getLexeme() + " hereda un atributo repetido: " + a.getNombre(), this.nombre.getLinea());
                    }
                }

                // Heredar métodos (sin sobrescribir los redefinidos)
                for (Metodo mPadre : clasePadre.getMetodos().values()) {
                    Metodo mHijo = this.metodos.get(mPadre.getNombre());
                    if (mHijo == null) {
                        // Si el padre tiene un metodo asbtracto, la clase actual si o si lo debe implementar
                        if (mPadre.getModificador() != null && mPadre.getModificador().getLexeme().equals("abstract")){
                            if (this.modificador == null || !this.modificador.getLexeme().equals("abstract")) {
                                throw new SemanticException(mPadre.getNombre(), "El método abstracto " + mPadre.getNombre() + " de la clase " + clasePadre.getNombre() + " debe ser implementado en la clase concreta " + this.nombre.getLexeme() + ".", this.nombre.getLinea());
                            }
                        }
                        this.metodos.put(mPadre.getNombre(), mPadre);

                    } else {
                        // Si el metodo del padre es final o static, no puede ser redefinido
                        if (mPadre.getModificador() != null) {
                            String mod = mPadre.getModificador().getLexeme();
                            if (mod.equals("final") || mod.equals("static")) {
                                throw new SemanticException(mHijo.getNombre(), "El método " + mHijo.getNombre() + " no puede ser redefinido porque es " + mod + " en la clase padre.", mHijo.getToken().getLinea()
                                );
                            }
                        }

                        boolean padreEsStatic = mPadre.getModificador() != null && mPadre.getModificador().getTokenId().equals(TokenId.kw_static);
                        boolean hijoEsStatic  = mHijo.getModificador()  != null && mHijo.getModificador().getTokenId().equals(TokenId.kw_static);
                        if (padreEsStatic != hijoEsStatic) {
                            throw new SemanticException(mHijo.getNombre(), "El método " + mHijo.getNombre() + " no puede cambiar su naturaleza (static/no-static) respecto al padre.", mHijo.getToken().getLinea());
                        }

                        // Comparar tipo de retorno
                        String tipoPadre = mPadre.getTipoRetorno() != null ? mPadre.getTipoRetorno().getNombre() : "void";
                        String tipoHijo = mHijo.getTipoRetorno() != null ? mHijo.getTipoRetorno().getNombre() : "void";
                        if (!tipoPadre.equals(tipoHijo)) {
                            throw new SemanticException(mHijo.getNombre(), "El método " + mHijo.getNombre() + " redefine el tipo de retorno (" + tipoHijo + ") respecto al padre (" + tipoPadre + ").", mHijo.getToken().getLinea()
                            );
                        }
                        // Comparar parámetros
                        if (mPadre.getParametros().size() != mHijo.getParametros().size()) {
                            throw new SemanticException(mHijo.getNombre(), "El método " + mHijo.getNombre() + " redefine la cantidad de parámetros respecto al padre.", mHijo.getToken().getLinea()
                            );
                        }
                        var itPadre = mPadre.getParametros().values().iterator();
                        var itHijo = mHijo.getParametros().values().iterator();
                        while (itPadre.hasNext() && itHijo.hasNext()) {
                            Parametro pPadre = itPadre.next();
                            Parametro pHijo = itHijo.next();
                            if (!pPadre.getTipo().getNombre().equals(pHijo.getTipo().getNombre())) {
                                throw new SemanticException(mHijo.getNombre(), "El método " + mHijo.getNombre() + " redefine el tipo de un parámetro respecto al padre.", mHijo.getToken().getLinea()
                                );
                            }
                        }
                    }
                }
            }
        }

        consolidado = true;
    }
    public void agregarConstructor(Constructor constructor) throws SemanticException {
        if(this.constructor != null) {
            throw new SemanticException(this.nombre.getLexeme(), "La clase " + this.nombre.getLexeme() + " ya tiene constructor.", constructor.getToken().getLinea());
        }
        this.constructor = constructor;
    }

    public void agregarAtributo(Atributo atributo) throws SemanticException {
        if (atributos.putIfAbsent(atributo.getNombre(), atributo) != null) {
            throw new SemanticException(atributo.getNombre(), "Atributo repetido en la clase " + this.nombre.getLexeme(), atributo.getToken().getLinea());
        }
    }

    public void agregarMetodo(Metodo metodo) throws SemanticException {
        if(metodos.putIfAbsent(metodo.getNombre(), metodo) != null) {
            throw new SemanticException(metodo.getToken().getLexeme(), "Método repetido en la clase " + this.nombre.getLexeme(), metodo.getToken().getLinea());
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

    public void chequear() throws SemanticException {
        if (constructor != null){
            constructor.chequear();
        }
        for (Metodo m : metodos.values()) {
            Main.TS.setMetodoActual(m);
            m.chequear();
        }
    }
}
