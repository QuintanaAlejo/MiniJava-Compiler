package lexical;

public enum TokenId {
    // Keywords Tokens
    kw_class,
    kw_extends,
    kw_public,
    kw_static,
    kw_void,
    kw_boolean,
    kw_char,
    kw_int,
    kw_abstract,
    kw_final,
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

    // Identifiers Tokens
    id_Class,
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
    punt_dot,
    punt_colon,

    // Operators Tokens
    op_greater,
    op_less,
    op_not,
    op_equal,
    op_greaterOrEqual,
    op_lessOrEqual,
    op_notEqual,
    op_plus,
    op_minus,
    op_multiplication,
    op_division,
    op_and,
    op_or,
    op_module,

    // Operation Tokens
    assignment,
    increment,
    decrement,

    // End of File Token
    EOF,

    // Init Token
    init
}