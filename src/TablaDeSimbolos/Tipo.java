package TablaDeSimbolos;

import exceptions.SemanticException;
import lexical.Token;

public interface Tipo {
    Token getToken();
    String getNombre();
    void estaBienDeclarado() throws SemanticException;
    //void setToken(Token token);
    //void setNombre(String nombre);
}
