package lexical;

import java.util.HashMap;
import java.util.Map;
import static lexical.TokenId.*;

public class Keywords {
    Map<String, TokenId> map = new HashMap<String, TokenId>();

    public Keywords(){
        map.put("class", kw_class);
        map.put("extends", kw_extends);
        map.put("public", kw_public);
        map.put("static", kw_static);
        map.put("void", kw_void);
        map.put("boolean", kw_boolean);
        map.put("char", kw_char);
        map.put("int", kw_int);
        map.put("abstract", kw_abstract);
        map.put("final", kw_final);
        map.put("if", kw_if);
        map.put("else", kw_else);
        map.put("while", kw_while);
        map.put("return", kw_return);
        map.put("var", kw_var);
        map.put("this", kw_this);
        map.put("new", kw_new);
        map.put("null", kw_null);
        map.put("true", kw_true);
        map.put("false", kw_false);
    }

    public TokenId getTokenId(String token){
        return map.get(token);
    }
}
