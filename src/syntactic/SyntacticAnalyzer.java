package syntactic;

import exceptions.*;
import lexical.*;

public class SyntacticAnalyzer {
     LexicalAnalyzer lexicalAnalyzer;
     Token currentToken;
     Firsts firsts;

     public SyntacticAnalyzer(LexicalAnalyzer lexicalAnalyzer){
          this.lexicalAnalyzer = lexicalAnalyzer;
          firsts = new Firsts();
     }

     public void match(TokenId expectedToken) throws SyntacticException {
          if(expectedToken.equals(currentToken.getTokenId()))
               nextToken();
          else
               throw new SyntacticException(currentToken.getLexeme(), expectedToken.toString(), lexicalAnalyzer.getLineNumber());
     }

     private void nextToken(){
          try{
               currentToken = lexicalAnalyzer.getToken();
          } catch (LexicalException e) {
               throw new RuntimeException(e);
          }
     }

     public void startAnalysis() throws SyntacticException {
          nextToken();
          Initial();
     }

     private void Initial() throws SyntacticException{
          ListaClases();
          match(TokenId.EOF);
     }

     private void ListaClases() throws SyntacticException {
          if(Firsts.isFirst("ClaseInter", currentToken)){
               ClaseInter();
               ListaClases();
          }
     }

     private void ClaseInter() throws SyntacticException {
          if(Firsts.isFirst("Clase", currentToken)){
               Clase();
          } else if (Firsts.isFirst("Interface", currentToken)) {
               Interface();
          }
     }

     private void Clase() throws SyntacticException {
          ModificadorOpcional();
          match(TokenId.kw_class);
          match(TokenId.id_Class);
          HerenciaOpcional();
          ImplementacionOpcional();
          match(TokenId.punt_openKey);
          ListaMiembros();
          match(TokenId.punt_closeKey);
     }

     private void Interface() throws SyntacticException {
          match(TokenId.kw_interface);
          match(TokenId.id_Class);
          HerenciaOpcional();
          match(TokenId.punt_openKey);
          ListaEncabezados();
          match(TokenId.punt_closeKey);
     }

     private void ImplementacionOpcional() throws SyntacticException {
          if(currentToken.getTokenId().equals(TokenId.kw_implements)){
               match(TokenId.kw_implements);
               match(TokenId.id_Class);
          }
     }

     private void ListaEncabezados() throws SyntacticException {
          if(Firsts.isFirst("Encabezado", currentToken)){
               Encabezado();
               ListaEncabezados();
          }
     }

     private void Encabezado() throws SyntacticException {
          //ModificadorOpcional();
          TipoMetodo();
          match(TokenId.id_MetVar);
          ArgsFormales();
          match(TokenId.punt_semicolon);
     }

     private void ModificadorOpcional() throws SyntacticException {
          switch (currentToken.getTokenId().toString()){
               case "kw_static" -> match(TokenId.kw_static);
               case "kw_abstract" -> match(TokenId.kw_abstract);
               case "kw_final" -> match(TokenId.kw_final);
          }
     }
     private void HerenciaOpcional() throws SyntacticException {
          if(currentToken.getTokenId().equals(TokenId.kw_extends)){
                 match(TokenId.kw_extends);
                 match(TokenId.id_Class);
          }
     }
     private void ListaMiembros() throws SyntacticException {
          if(Firsts.isFirst("Miembro", currentToken)){
                 Miembro();
                 ListaMiembros();
          }
     }
     private void Miembro() throws SyntacticException {
          if (Firsts.isFirst("Constructor", currentToken)) {
               Constructor();
          } else if (Firsts.isFirst("ModificadorOpcionalNoVacio", currentToken)) {
               ModificadorOpcionalNoVacio();
               TipoMetodo();
               match(TokenId.id_MetVar);
               ArgsFormales();
               BloqueOpcional();
          } else if (Firsts.isFirst("Tipo", currentToken)) {
               Tipo();
               match(TokenId.id_MetVar);
               MetodoODeclaracion();
          } else if (currentToken.getTokenId().equals(TokenId.kw_void)) {
               match(TokenId.kw_void);
               match(TokenId.id_MetVar);
               ArgsFormales();
               BloqueOpcional();
          } else {
               throw new SyntacticException(currentToken.getLexeme(), "Miembro", lexicalAnalyzer.getLineNumber());
          }
     }
     private void ModificadorOpcionalNoVacio() throws SyntacticException {
          switch (currentToken.getTokenId().toString()){
               case "kw_abstract" -> match(TokenId.kw_abstract);
               case "kw_static" -> match(TokenId.kw_static);
               case "kw_final" -> match(TokenId.kw_final);
               default -> throw new SyntacticException(currentToken.getLexeme(), "ModificadorOpcionalNoVacio", lexicalAnalyzer.getLineNumber());
          }
     }
     private void MetodoODeclaracion() throws SyntacticException {
          if(currentToken.getTokenId().equals(TokenId.assignment)){
               match(TokenId.assignment);
               ExpresionCompuesta();
               match(TokenId.punt_semicolon);
          } else if (Firsts.isFirst("Metodo", currentToken)) {
               Metodo();
          } else {
               throw new SyntacticException(currentToken.getLexeme(), "MetodoODeclaracion", lexicalAnalyzer.getLineNumber());
          }
     }
     private void Metodo() throws SyntacticException {
          if (Firsts.isFirst("ArgsFormales", currentToken)) {
               ArgsFormales();
               BloqueOpcional();
          } else if (currentToken.getTokenId().equals(TokenId.punt_semicolon)) {
               match(TokenId.punt_semicolon);
          } else{
                throw new SyntacticException(currentToken.getLexeme(), "Metodo", lexicalAnalyzer.getLineNumber());
          }
     }
     private void Constructor() throws SyntacticException {
          match(TokenId.kw_public);
          match(TokenId.id_Class);
          ArgsFormales();
          Bloque();
     }
     private void TipoMetodo() throws SyntacticException {
          if (Firsts.isFirst("Tipo", currentToken)) {
               Tipo();
          } else if (currentToken.getTokenId().equals(TokenId.kw_void)) {
               match(TokenId.kw_void);
          } else {
               throw new SyntacticException(currentToken.getLexeme(), "TipoMetodo", lexicalAnalyzer.getLineNumber());
          }
     }
     private void Tipo() throws SyntacticException {
          if (Firsts.isFirst("TipoPrimitivo", currentToken)) {
               TipoPrimitivo();
          } else if (currentToken.getTokenId().equals(TokenId.id_Class)) {
               match(TokenId.id_Class);
          } else {
               throw new SyntacticException(currentToken.getLexeme(), "Tipo", lexicalAnalyzer.getLineNumber());
          }
     }
     private void TipoPrimitivo() throws SyntacticException {
          switch (currentToken.getTokenId().toString()){
               case "kw_int" -> match(TokenId.kw_int);
               case "kw_char" -> match(TokenId.kw_char);
               case "kw_boolean" -> match(TokenId.kw_boolean);
               default -> throw new SyntacticException(currentToken.getLexeme(), "TipoPrimitivo", lexicalAnalyzer.getLineNumber());
          }
     }
     private void ArgsFormales() throws SyntacticException {
          match(TokenId.punt_openParenthesis);
          ListaArgsFormalesOpcional();
          match(TokenId.punt_closeParenthesis);
     }
     private void ListaArgsFormalesOpcional() throws SyntacticException {
          if(Firsts.isFirst("ListaArgsFormales", currentToken)){
               ListaArgsFormales();
          }
     }
     private void ListaArgsFormales() throws SyntacticException {
          ArgFormal();
          ArgsFormalesFinal();
     }
     private void ArgsFormalesFinal() throws SyntacticException {
          if(currentToken.getTokenId().equals(TokenId.punt_coma)){
               match(TokenId.punt_coma);
               ArgFormal();
               ArgsFormalesFinal();
          }
     }
     private void ArgFormal() throws SyntacticException {
          Tipo();
          match(TokenId.id_MetVar);
     }
     private void BloqueOpcional() throws SyntacticException {
          if(Firsts.isFirst("Bloque", currentToken)){
               Bloque();
          } else if (currentToken.getTokenId().equals(TokenId.punt_semicolon)) {
               match(TokenId.punt_semicolon);
          } else {
               throw new SyntacticException(currentToken.getLexeme(), "BloqueOpcional", lexicalAnalyzer.getLineNumber());
          }
     }
     private void Bloque() throws SyntacticException {
          match(TokenId.punt_openKey);
          ListaSentencias();
          match(TokenId.punt_closeKey);
     }
     private void ListaSentencias() throws SyntacticException {
          if(Firsts.isFirst("Sentencia", currentToken)){
               Sentencia();
               ListaSentencias();
          }
     }
     private void Sentencia() throws SyntacticException {
          if(currentToken.getTokenId().equals(TokenId.punt_semicolon)){
               match(TokenId.punt_semicolon);
          } else if(Firsts.isFirst("AsignacionLLamada", currentToken)){
               AsignacionLLamada();
               match(TokenId.punt_semicolon);
          } else if(Firsts.isFirst("VarLocal", currentToken)){
               VarLocal();
               match(TokenId.punt_semicolon);
          } else if(Firsts.isFirst("Return", currentToken)){
               Return();
               match(TokenId.punt_semicolon);
          } else if(Firsts.isFirst("If", currentToken)){
               If();
          } else if(Firsts.isFirst("While", currentToken)){
               While();
          } else if(Firsts.isFirst("Bloque", currentToken)){
               Bloque();
          } else if(Firsts.isFirst("For", currentToken)){
               For();
          } else {
               throw new SyntacticException(currentToken.getLexeme(), "Sentencia", lexicalAnalyzer.getLineNumber());
          }
     }
     private void AsignacionLLamada() throws SyntacticException {
          Expresion();
     }
     private void VarLocal() throws SyntacticException {
          match(TokenId.kw_var);
          match(TokenId.id_MetVar);
          match(TokenId.assignment);
          ExpresionCompuesta();
     }
     private void Return() throws SyntacticException {
          match(TokenId.kw_return);
          ExpresionOpcional();
     }
     private void ExpresionOpcional() throws SyntacticException {
          if(Firsts.isFirst("Expresion", currentToken)){
                 Expresion();
          }
     }
     private void If() throws SyntacticException {
          match(TokenId.kw_if);
          match(TokenId.punt_openParenthesis);
          Expresion();
          match(TokenId.punt_closeParenthesis);
          Sentencia();
          Else();
     }
     private void Else() throws SyntacticException {
          if(currentToken.getTokenId().equals(TokenId.kw_else)){
               match(TokenId.kw_else);
               Sentencia();
          }
     }
     private void While() throws SyntacticException {
          match(TokenId.kw_while);
          match(TokenId.punt_openParenthesis);
          Expresion();
          match(TokenId.punt_closeParenthesis);
          Sentencia();
     }
     private void For() throws SyntacticException {
          match(TokenId.kw_for);
          match(TokenId.punt_openParenthesis);
          ExpresionFor();
          match(TokenId.punt_closeParenthesis);
          Sentencia();
     }
     private void ExpresionFor() throws SyntacticException {
          if(currentToken.getTokenId().equals(TokenId.kw_var)){
               match(TokenId.kw_var);
               match(TokenId.id_MetVar);
               ForTipo();
          } else if (Firsts.isFirst("Expresion", currentToken)) {
               Expresion();
               ForNormal();
          } else {
               throw new SyntacticException(currentToken.getLexeme(), "ExpresionFor", lexicalAnalyzer.getLineNumber());
          }
     }
     private void ForTipo() throws SyntacticException {
          if(currentToken.getTokenId().equals(TokenId.assignment)){
               match(TokenId.assignment);
               ExpresionCompuesta();
               ForNormal();
          } else if (Firsts.isFirst("ForEach", currentToken)) {
               ForEach();
          } else {
               throw new SyntacticException(currentToken.getLexeme(), "ForTipo", lexicalAnalyzer.getLineNumber());
          }
     }
     private void ForEach() throws SyntacticException {
          match(TokenId.punt_colon);
          Referencia();
     }
     private void ForNormal() throws SyntacticException {
          match(TokenId.punt_semicolon);
          Expresion();
          match(TokenId.punt_semicolon);
          Expresion();
     }
     private void Expresion() throws SyntacticException {
          ExpresionCompuesta();
          ExpresionExtra();
     }
     private void ExpresionExtra() throws SyntacticException {
          if(Firsts.isFirst("OperadorAsignacion", currentToken)){
               OperadorAsignacion();
               ExpresionCompuesta();
          }
     }
     private void OperadorAsignacion() throws SyntacticException {
          match(TokenId.assignment);
     }
     private void ExpresionCompuesta() throws SyntacticException {
          ExpresionBasica();
          ExpresionCompuestaFinal();
     }
     private void ExpresionCompuestaFinal() throws SyntacticException {
          if(Firsts.isFirst("OperadorBinario", currentToken)){
               OperadorBinario();
               ExpresionBasica();
               ExpresionCompuestaFinal();
          } else if (currentToken.getTokenId().equals(TokenId.op_ternary)) {
               match(TokenId.op_ternary);
               Expresion();
               match(TokenId.punt_colon);
               Expresion();
          }
     }
     private void OperadorBinario() throws SyntacticException {
          switch (currentToken.getTokenId().toString()){
               case "op_or" -> match(TokenId.op_or);
               case "op_and" -> match(TokenId.op_and);
               case "op_equal" -> match(TokenId.op_equal);
               case "op_notEqual" -> match(TokenId.op_notEqual);
               case "op_less" -> match(TokenId.op_less);
               case "op_greater" -> match(TokenId.op_greater);
               case "op_lessOrEqual" -> match(TokenId.op_lessOrEqual);
               case "op_greaterOrEqual" -> match(TokenId.op_greaterOrEqual);
               case "op_plus" -> match(TokenId.op_plus);
               case "op_minus" -> match(TokenId.op_minus);
               case "op_multiplication" -> match(TokenId.op_multiplication);
               case "op_division" -> match(TokenId.op_division);
               case "op_module" -> match(TokenId.op_module);
               default -> throw new SyntacticException(currentToken.getLexeme(), "OperadorBinario", lexicalAnalyzer.getLineNumber());
          }
     }
     private void ExpresionBasica() throws SyntacticException {
          if(Firsts.isFirst("OperadorUnario", currentToken)){
               OperadorUnario();
               Operando();
          } else if (Firsts.isFirst("Operando", currentToken)) {
               Operando();
          } else {
               throw new SyntacticException(currentToken.getLexeme(), "ExpresionBasica", lexicalAnalyzer.getLineNumber());
          }
     }
     private void OperadorUnario() throws SyntacticException {
            switch (currentToken.getTokenId().toString()){
                 case "op_plus" -> match(TokenId.op_plus);
                 case "increment" -> match(TokenId.increment);
                 case "op_minus" -> match(TokenId.op_minus);
                 case "decrement" -> match(TokenId.decrement);
                 case "op_not" -> match(TokenId.op_not);
                 default -> throw new SyntacticException(currentToken.getLexeme(), "OperadorUnario", lexicalAnalyzer.getLineNumber());
            }
     }
     private void Operando() throws SyntacticException {
          if(Firsts.isFirst("Primitivo", currentToken)){
                 Primitivo();
          } else if (Firsts.isFirst("Referencia", currentToken)) {
                 Referencia();
          } else {
                 throw new SyntacticException(currentToken.getLexeme(), "Operando", lexicalAnalyzer.getLineNumber());
          }
     }
     private void Primitivo() throws SyntacticException {
          switch (currentToken.getTokenId().toString()){
               case "kw_true" -> match(TokenId.kw_true);
               case "kw_false" -> match(TokenId.kw_false);
               case "lit_int" -> match(TokenId.lit_int);
               case "lit_char" -> match(TokenId.lit_char);
               case "kw_null" -> match(TokenId.kw_null);
               default -> throw new SyntacticException(currentToken.getLexeme(), "Primitivo", lexicalAnalyzer.getLineNumber());
          }
     }
     private void Referencia() throws SyntacticException {
          Primario();
          ReferenciaEncadenada();
     }
     private void ReferenciaEncadenada() throws SyntacticException {
          if (Firsts.isFirst("ReferenciaEncadenada", currentToken)) {
               match(TokenId.punt_dot);
               match(TokenId.id_MetVar);
               ElemEncadenado();
               ReferenciaEncadenada();
          }
     }
     private void Primario() throws SyntacticException {
          if(currentToken.getTokenId().equals(TokenId.kw_this)){
               match(TokenId.kw_this);
          } else if (currentToken.getTokenId().equals(TokenId.lit_string)) {
               match(TokenId.lit_string);
          } else if (Firsts.isFirst("AccesoVarMetodo", currentToken)) {
               AccesoVarMetodo();
          } else if (Firsts.isFirst("LlamadaConstructor", currentToken)) {
               LlamadaConstructor();
          } else if (Firsts.isFirst("LlamadaMetodoEstatico", currentToken)) {
               LlamadaMetodoEstatico();
          } else if (Firsts.isFirst("ExpresionParentizada", currentToken)) {
               ExpresionParentizada();
          } else {
               throw new SyntacticException(currentToken.getLexeme(), "Primario", lexicalAnalyzer.getLineNumber());
          }
     }
     private void AccesoVarMetodo() throws SyntacticException {
          match(TokenId.id_MetVar);
          ArgsPosibles();
     }
     private void ArgsPosibles() throws SyntacticException {
          if(Firsts.isFirst("ArgsActuales", currentToken)){
               ArgsActuales();
          }
     }
     private void LlamadaConstructor() throws SyntacticException {
          match(TokenId.kw_new);
          match(TokenId.id_Class);
          ArgsActuales();
     }
     private void ExpresionParentizada() throws SyntacticException {
          match(TokenId.punt_openParenthesis);
          Expresion();
          match(TokenId.punt_closeParenthesis);
     }
     private void LlamadaMetodoEstatico() throws SyntacticException {
          match(TokenId.id_Class);
          match(TokenId.punt_dot);
          match(TokenId.id_MetVar);
          ArgsActuales();
     }
     private void ArgsActuales() throws SyntacticException {
          match(TokenId.punt_openParenthesis);
          ListaExpsOpcional();
          match(TokenId.punt_closeParenthesis);
     }
     private void ListaExpsOpcional() throws SyntacticException {
          if(Firsts.isFirst("Expresion", currentToken)){
               Expresion();
               ListaExps();
          }
     }
     private void ListaExps() throws SyntacticException {
          if(currentToken.getTokenId().equals(TokenId.punt_coma)){
               match(TokenId.punt_coma);
               Expresion();
               ListaExps();
          }
     }
     private void ElemEncadenado() throws SyntacticException {
          if(Firsts.isFirst("ArgsActuales", currentToken)){
               ArgsActuales();
          }
     }
}
