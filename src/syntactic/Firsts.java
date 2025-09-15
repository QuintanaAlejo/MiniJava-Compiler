package syntactic;

import lexical.Token;
import lexical.TokenId;
import java.util.*;

/*
<Inicial> ::= <ListaClases> eof
<ListaClases> ::= <Clase> <ListaClases> | €
<Clase> ::= <ModificadorOpcional> class idClase <HerenciaOpcional> { <ListaMiembros> }
<ModificadorOpcional> ::= abstract | static | final | €
<HerenciaOpcional> ::= extends idClase | €
<ListaMiembros> ::= <Miembro> <ListaMiembros> | €
<ModificadorOpcionalNoVacio>  ::= abstract | static | final
<Miembro> ::= <Constructor>
<Miembro> ::= <ModificadorOpcionalNoVacio> <TipoMetodo> idMetVar <ArgsFormales> <BloqueOpcional>
<Miembro> ::= void idMetVar <ArgsFormales> <BloqueOpcional>
<Miembro> ::= <Tipo> idMetVar <MiembroMetodo>
<MiembroMetodo> ::= <ArgsFormales> <BloqueOpcional> | ;
<Constructor> ::= public idClase <ArgsFormales> <Bloque>
<TipoMetodo> ::= <Tipo> | void
<Tipo> := <TipoPrimitivo> | idClase
<TipoPrimitivo> ::= boolean | char | int
<ArgsFormales> ::= ( <ListaArgsFormalesOpcional> )
<ListaArgsFormalesOpcional> ::= <ListaArgsFormales> | €
<ListaArgsFormales> ::= <ArgFormal> <ArgsFormalesFinal>
<ArgsFormalesFinal> ::= , <ArgFormal> <ArgsFormalesFinal> | €
<ArgFormal> ::= <Tipo> idMetVar
<BloqueOpcional> ::= <Bloque> | ;
<Bloque> ::= { <ListaSentencias> }
<ListaSentencias> ::= <Sentencia> <ListaSentencias> | €
<Sentencia> ::=;
<Sentencia> ::= <AsignacionLlamada> ;
<Sentencia> ::= <VarLocal> ;
<Sentencia> ::= <Return> ;
<Sentencia> ::= <If>
<Sentencia> ::= <While>
<Sentencia> ::= <Bloque>
<Sentencia> ::= <For>
<AsignacionLLamada> ::= <Expresion>
<VarLocal> ::= var idMetVar = <ExpresionCompuesta>
<Return> ::= return <ExpresionOpcional>
<ExpresionOpcional> ::= <Expresion> | €
<If> ::= if (<Expresion> ) <Sentencia> <Else>
<Else> ::= else <Sentencia> | €
<While> ::= while ( <Expresion> ) <Sentencia>
<For> ::= for ( <ExpresionFor> ) <Sentencia>
<ExpresionFor> ::= <TipoPrimitivo> idMetVar : idMetVar (ForEach)
<ExpresionFor> ::= <Expresion>;<Expresion>;<Expresion>
<Expresion> ::= <ExpresionCompuesta> <ExpresionExtra>
<ExpresionExtra> ::= <OperadorAsignacion> <ExpresionCompuesta> | €
<OperadorAsignacion> := =
<ExpresionCompuesta> ::= <ExpresionBasica> <ExpresionCompuestaFinal>
<ExpresionCompuestaFinal> ::= <OperadorBinario> <ExpresionBasica> <ExpresionCompuestaFinal> | €
<OperadorBinario> := || | && | == | != | < | > | <= |>= |+ | - | * | / | %
<ExpresionBasica> ::= <OperadorUnario> <Operando>
<ExpresionBasica> ::= <Operando>
<OperadorUnario> := + | ++ | - | -- | !
<Operando> ::= <Primitvo>
<Operando> ::= <Referencia>
<Primitivo> ::= true | false | intLiteral | charLiteral | null
<Referencia> ::= <Primario> <ReferenciaEncadenada>
<ReferenciaEncadenada> ::= .idMetVar <ElemEncadenado> <ReferenciaEncadeanda>
<ReferenciaEncadenada> ::= €
<Primario> ::= this
<Primario> ::= stringLiteral
<Primario> ::= <AccesoVarMetodo>
<Primario> ::= <LlamadaConstructor>
<Primario> ::= <LlamadaMetodoEstatico>
<Primario> ::= <ExpresionParentizada>
<AccesoVarMetodo> ::= idMetVar <ArgsPosibles>
<ArgsPosibles> ::= <ArgsActuales> | €
<LlamadaConstructor> ::= new idClase <ArgsActuales>
<ExpresionParentizada> ::= ( <Expresion> )
<LlamadaMetodoEstatico> ::= idClase . idMetVar <ArgsActuales>
<ArgsActuales> ::= (<ListaExpsUpcional> )
<ListaExpsOpcional> ::= <Expresion><ListaExps> | €
<ListaExps> ::= , <Expresion><ListaExps> | €
<ElemEncadenado> ::= <ArgsActuales> | €
*/

public class Firsts {
    static Map<String, ArrayList<TokenId>> map = new HashMap<String, ArrayList<TokenId>>();
    String[] nonTerminals = {"Inicial", "ListaClases", "Clase", "ModificadorOpcional", "HerenciaOpcional", "ListaMiembros", "ModificadorOpcionalNoVacio",
            "Miembro", "MiembroMetodo", "Constructor", "TipoMetodo", "Tipo", "TipoPrimitivo", "ArgsFormales", "ListaArgsFormalesOpcional", "ListaArgsFormales",
            "ArgsFormalesFinal", "ArgFormal", "BloqueOpcional", "Bloque", "ListaSentencias", "Sentencia", "AsignacionLLamada", "VarLocal", "Return", "ExpresionOpcional",
            "If", "Else", "While", "For", "ExpresionFor", "Expresion", "ExpresionExtra", "OperadorAsignacion", "ExpresionCompuesta", "ExpresionCompuestaFinal",
            "OperadorBinario", "ExpresionBasica", "OperadorUnario", "Operando", "Primitivo", "Referencia", "ReferenciaEncadenada", "Primario", "AccesoVarMetodo",
            "ArgsPosibles", "LlamadaConstructor", "ExpresionParentizada", "LlamadaMetodoEstatico", "ArgsActuales", "ListaExpsOpcional", "ListaExps", "ElemEncadenado" };

    public Firsts () {
        for (String nt : nonTerminals) {
            map.put(nt, new ArrayList<TokenId>());
        }

        map.get("Inicial").addAll(map.get("ListaClases"));

        map.get("ListaClases").addAll(map.get("Clase"));

        map.get("Clase").addAll(map.get("ModificadorOpcional"));
        map.get("Clase").add(TokenId.kw_class);

        map.get("ModificadorOpcional").addAll(Arrays.asList(TokenId.kw_abstract, TokenId.kw_static, TokenId.kw_final));

        map.get("HerenciaOpcional").add(TokenId.kw_extends);

        map.get("ListaMiembros").addAll(map.get("Miembro"));

        map.get("ModificadorOpcionalNoVacio").addAll(Arrays.asList(TokenId.kw_abstract, TokenId.kw_static, TokenId.kw_final));

        map.get("Miembro").addAll(map.get("Constructor"));
        map.get("Miembro").addAll(map.get("ModificadorOpcionalNoVacio"));
        map.get("Miembro").add(TokenId.kw_void);
        map.get("Miembro").addAll(map.get("Tipo"));
        map.get("MiembroMetodo").addAll(map.get("ArgsFormales"));
        map.get("MiembroMetodo").add(TokenId.punt_semicolon);

        map.get("Constructor").add(TokenId.kw_public);

        map.get("TipoMetodo").addAll(map.get("Tipo"));
        map.get("TipoMetodo").add(TokenId.kw_void);

        map.get("Tipo").addAll(map.get("TipoPrimitivo"));
        map.get("Tipo").add(TokenId.id_Class);

        map.get("TipoPrimitivo").addAll(Arrays.asList(TokenId.kw_boolean, TokenId.kw_char, TokenId.kw_int));

        map.get("ArgsFormales").add(TokenId.punt_openParenthesis);

        map.get("ListaArgsFormalesOpcional").addAll(map.get("ListaArgsFormales"));

        map.get("ListaArgsFormales").addAll(map.get("ArgFormal"));
        map.get("ArgsFormalesFinal").add(TokenId.punt_coma);

        map.get("ArgFormal").addAll(map.get("Tipo"));

        map.get("BloqueOpcional").addAll(map.get("Bloque"));
        map.get("BloqueOpcional").add(TokenId.punt_semicolon);

        map.get("Bloque").add(TokenId.punt_openKey);

        map.get("ListaSentencias").addAll(map.get("Sentencia"));

        map.get("Sentencia").add(TokenId.punt_semicolon);
        map.get("Sentencia").addAll(map.get("AsignacionLLamada"));
        map.get("Sentencia").addAll(map.get("VarLocal"));
        map.get("Sentencia").addAll(map.get("Return"));
        map.get("Sentencia").addAll(map.get("If"));
        map.get("Sentencia").addAll(map.get("While"));
        map.get("Sentencia").addAll(map.get("Bloque"));
        map.get("Sentencia").addAll(map.get("For"));

        map.get("AsignacionLLamada").addAll(map.get("Expresion"));

        map.get("VarLocal").add(TokenId.kw_var);

        map.get("Return").add(TokenId.kw_return);

        map.get("ExpresionOpcional").addAll(map.get("Expresion"));

        map.get("If").add(TokenId.kw_if);
        map.get("Else").add(TokenId.kw_else);

        map.get("While").add(TokenId.kw_while);

        map.get("For").add(TokenId.kw_for);
        map.get("ExpresionFor").addAll(map.get("TipoPrimitivo"));
        map.get("ExpresionFor").addAll(map.get("Expresion"));

        map.get("Expresion").addAll(map.get("ExpresionCompuesta"));
        map.get("ExpresionExtra").addAll(map.get("OperadorAsignacion"));

        map.get("OperadorAsignacion").add(TokenId.assignment);

        map.get("ExpresionCompuesta").addAll(map.get("ExpresionBasica"));
        map.get("ExpresionCompuestaFinal").addAll(map.get("OperadorBinario"));

        map.get("OperadorBinario").addAll(Arrays.asList(TokenId.op_or, TokenId.op_and, TokenId.op_equal, TokenId.op_notEqual,
                TokenId.op_less, TokenId.op_greater, TokenId.op_lessOrEqual, TokenId.op_greaterOrEqual,
                TokenId.op_plus, TokenId.op_minus, TokenId.op_multiplication, TokenId.op_division, TokenId.op_module));

        map.get("ExpresionBasica").addAll(map.get("OperadorUnario"));
        map.get("ExpresionBasica").addAll(map.get("Operando"));

        map.get("OperadorUnario").addAll(Arrays.asList(TokenId.op_plus, TokenId.increment, TokenId.op_minus, TokenId.decrement, TokenId.op_not));

        map.get("Operando").addAll(map.get("Primitivo"));
        map.get("Operando").addAll(map.get("Referencia"));

        map.get("Primitivo").addAll(Arrays.asList(TokenId.kw_true, TokenId.kw_false, TokenId.lit_int, TokenId.lit_char, TokenId.kw_null));

        map.get("Referencia").addAll(map.get("Primario"));

        map.get("ReferenciaEncadenada").add(TokenId.punt_dot);

        map.get("Primario").add(TokenId.kw_this);
        map.get("Primario").add(TokenId.lit_string);
        map.get("Primario").addAll(map.get("AccesoVarMetodo"));
        map.get("Primario").addAll(map.get("LlamadaConstructor"));
        map.get("Primario").addAll(map.get("LlamadaMetodoEstatico"));
        map.get("Primario").addAll(map.get("ExpresionParentizada"));

        map.get("AccesoVarMetodo").add(TokenId.id_MetVar);

        map.get("ArgsPosibles").addAll(map.get("ArgsActuales"));

        map.get("LlamadaConstructor").add(TokenId.kw_new);

        map.get("ExpresionParentizada").add(TokenId.punt_openParenthesis);

        map.get("LlamadaMetodoEstatico").add(TokenId.id_Class);

        map.get("ArgsActuales").add(TokenId.punt_openParenthesis);

        map.get("ListaExpsOpcional").addAll(map.get("Expresion"));

        map.get("ListaExps").add(TokenId.punt_coma);

        map.get("ElemEncadenado").addAll(map.get("ArgsActuales"));

    }

    public static boolean isFirst(String head, Token token) {
        if (map.containsKey(head)) {
            return map.get(head).contains(token.getTokenId());
        }
        return false;
    }
}
