package lexical;

public enum TokenId {
    // Keywords Tokens
    kw_class,
    kw_interface,
    kw_extends,
    kw_implements,
    kw_public,
    kw_private,
    kw_static,
    kw_void,
    kw_boolean,
    kw_char,
    kw_int,
    kw_if,
    kw_else,
    kw_while,
    kw_return,
    kw_var,
    kw_this,
    kw_new,
    kw_null,
    kw_true,
    kw_false,

    // Identifiers Tokens ??????
    id_Clase,
    id_MetVar,

    // Literals Tokens
    lit_int,
    lit_char,
    lit_string,

    // Punctuation Tokens
    punt_openParenthesis,
    punt_closeParenthesis,
    punt_openKey,
    punt_closeKey,
    punt_semicolon,
    punt_coma,
    punt_point,
    punt_colon,

    // Operators Tokens
    op_mayor,
    op_menor,
    op_negacion,
    op_igual,
    op_mayorIgual,
    op_menorIgual,
    op_distinto,
    op_suma,
    op_resta,
    op_multiplicacion,
    op_division,
    op_and,
    op_or,
    op_modulo,


    asignacion,
    incremento,
    decremento,

    EOF

}