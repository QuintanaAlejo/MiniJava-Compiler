package syntactic;

import Main.Main;
import TablaDeSimbolos.*;
import exceptions.*;
import lexical.*;

import java.util.ArrayList;
import java.util.List;

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

     public void startAnalysis() throws SyntacticException, SemanticException {
          nextToken();
          Initial();
     }

     private void Initial() throws SyntacticException, SemanticException {
          ListaClases();
          match(TokenId.EOF);
     }

     private void ListaClases() throws SyntacticException, SemanticException {
          if(Firsts.isFirst("ClaseInter", currentToken)){
               ClaseInter();
               ListaClases();
          }
     }

     private void ClaseInter() throws SyntacticException, SemanticException {
          if(Firsts.isFirst("Clase", currentToken)){
               Clase();
          } else if (Firsts.isFirst("Interface", currentToken)) {
               Interface();
          }
     }

     private void Clase() throws SyntacticException, SemanticException {
          ModificadorOpcional();
          match(TokenId.kw_class);
          Token nombreClase = currentToken;
          match(TokenId.id_Class);
          Clase c = new Clase(nombreClase);
          Main.TS.setClaseActual(c);
          Token padre = HerenciaOpcional();
          ImplementacionOpcional();
          match(TokenId.punt_openKey);
          ListaMiembros();
          match(TokenId.punt_closeKey);
          Main.TS.insertarClase(c);
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
     private Token HerenciaOpcional() throws SyntacticException {
          Token nombrePadre = null;
          if(currentToken.getTokenId().equals(TokenId.kw_extends)){
                 match(TokenId.kw_extends);
                 nombrePadre = currentToken;
                 match(TokenId.id_Class);
          } else {
                nombrePadre = new Token(TokenId.id_Class, "Object", lexicalAnalyzer.getLineNumber());
          }
          return nombrePadre;
     }
     private void ListaMiembros() throws SyntacticException, SemanticException {
          if(Firsts.isFirst("Miembro", currentToken)){
                 Miembro();
                 ListaMiembros();
          }
     }
     private void Miembro() throws SyntacticException, SemanticException {
          if (Firsts.isFirst("Constructor", currentToken)) {
               Constructor();
          } else if (Firsts.isFirst("ModificadorOpcionalNoVacio", currentToken)) {
               Token mod = ModificadorOpcionalNoVacio();
               Tipo t = TipoMetodo();
               Token nombre = currentToken;
               match(TokenId.id_MetVar);
               Metodo m = new Metodo(nombre, t, mod);
               Main.TS.setMetodoActual(m);
               List<Parametro> args = ArgsFormales();
               for(Parametro p : args){
                    Main.TS.getMetodoActual().agregarParametro(p);
               }
               Main.TS.getMetodoActual().setTieneBloque(BloqueOpcional());
               Main.TS.getClaseActual().agregarMetodo(m);
          } else if (Firsts.isFirst("Tipo", currentToken)) {
               Tipo t = Tipo();
               Token nombre = currentToken;
               match(TokenId.id_MetVar);
               MetodoODeclaracion(t, nombre);
          } else if (currentToken.getTokenId().equals(TokenId.kw_void)) {
               match(TokenId.kw_void);
               Token nombre = currentToken;
               match(TokenId.id_MetVar);
               Metodo m = new Metodo(nombre, null, null);
               Main.TS.setMetodoActual(m);
               List<Parametro> args = ArgsFormales();
               for(Parametro p : args){
                    Main.TS.getMetodoActual().agregarParametro(p);
               }
               Main.TS.getMetodoActual().setTieneBloque(BloqueOpcional());
               Main.TS.getClaseActual().agregarMetodo(m);
          } else {
               throw new SyntacticException(currentToken.getLexeme(), "Miembro", lexicalAnalyzer.getLineNumber());
          }
     }
     private Token ModificadorOpcionalNoVacio() throws SyntacticException {
          Token mod = currentToken;
          switch (currentToken.getTokenId().toString()){
               case "kw_abstract" -> match(TokenId.kw_abstract);
               case "kw_static" -> match(TokenId.kw_static);
               case "kw_final" -> match(TokenId.kw_final);
               default -> throw new SyntacticException(currentToken.getLexeme(), "ModificadorOpcionalNoVacio", lexicalAnalyzer.getLineNumber());
          }
          return mod;
     }
     private void MetodoODeclaracion(Tipo t, Token nombre) throws SyntacticException, SemanticException {
          if(currentToken.getTokenId().equals(TokenId.assignment)){
               Atributo a = new Atributo(nombre, t);
               match(TokenId.assignment);
               ExpresionCompuesta();
               match(TokenId.punt_semicolon);
               Main.TS.getClaseActual().agregarAtributo(a);
          } else if (Firsts.isFirst("Metodo", currentToken)) {
               Metodo(t, nombre);
          } else {
               throw new SyntacticException(currentToken.getLexeme(), "MetodoODeclaracion", lexicalAnalyzer.getLineNumber());
          }
     }
     private void Metodo(Tipo t, Token nombre) throws SyntacticException, SemanticException {
          if (Firsts.isFirst("ArgsFormales", currentToken)) {
               Metodo m = new Metodo(nombre, t, null);
               Main.TS.setMetodoActual(m);
               List<Parametro> args = ArgsFormales();
               for (Parametro p : args) {
                    Main.TS.getMetodoActual().agregarParametro(p);
               }
               Main.TS.getMetodoActual().setTieneBloque(BloqueOpcional());
               Main.TS.getClaseActual().agregarMetodo(m);
          } else if (currentToken.getTokenId().equals(TokenId.punt_semicolon)) {
               Atributo a = new Atributo(nombre, t);
               match(TokenId.punt_semicolon);
               Main.TS.getClaseActual().agregarAtributo(a);
          } else{
                throw new SyntacticException(currentToken.getLexeme(), "Metodo", lexicalAnalyzer.getLineNumber());
          }
     }
     private void Constructor() throws SyntacticException, SemanticException {
          match(TokenId.kw_public);
          Token nombre = currentToken;
          match(TokenId.id_Class);
          Constructor c = new Constructor(nombre);
          Main.TS.setConstructorActual(c);
          List<Parametro> args = ArgsFormales();
          for (Parametro p : args) {
               Main.TS.getConstructorActual().agregarParametro(p);
          }
          Bloque();
          Main.TS.getClaseActual().agregarConstructor(c);
     }
     private Tipo TipoMetodo() throws SyntacticException {
          Tipo t = null;
          if (Firsts.isFirst("Tipo", currentToken)) {
               t = Tipo();
          } else if (currentToken.getTokenId().equals(TokenId.kw_void)) {
               match(TokenId.kw_void);
          } else {
               throw new SyntacticException(currentToken.getLexeme(), "TipoMetodo", lexicalAnalyzer.getLineNumber());
          }
          return t;
     }
     private Tipo Tipo() throws SyntacticException {
          Tipo t = null;
          Token nombre;
          if (Firsts.isFirst("TipoPrimitivo", currentToken)) {
               nombre = currentToken;
               TipoPrimitivo();
               t = new TipoPrimitivo(nombre);
          } else if (currentToken.getTokenId().equals(TokenId.id_Class)) {
               nombre = currentToken;
               match(TokenId.id_Class);
               t = new TipoReferencia(nombre);
          } else {
               throw new SyntacticException(currentToken.getLexeme(), "Tipo", lexicalAnalyzer.getLineNumber());
          }
          return t;
     }
     private void TipoPrimitivo() throws SyntacticException {
          switch (currentToken.getTokenId().toString()){
               case "kw_int" -> match(TokenId.kw_int);
               case "kw_char" -> match(TokenId.kw_char);
               case "kw_boolean" -> match(TokenId.kw_boolean);
               default -> throw new SyntacticException(currentToken.getLexeme(), "TipoPrimitivo", lexicalAnalyzer.getLineNumber());
          }
     }
     private List<Parametro> ArgsFormales() throws SyntacticException {
          match(TokenId.punt_openParenthesis);
          List<Parametro> args = ListaArgsFormalesOpcional();
          match(TokenId.punt_closeParenthesis);
          return args;
     }
     private List<Parametro> ListaArgsFormalesOpcional() throws SyntacticException {
          List<Parametro> args = new ArrayList<>();
          if(Firsts.isFirst("ListaArgsFormales", currentToken)){
               args = ListaArgsFormales();
          }
          return args;
     }
     private List<Parametro> ListaArgsFormales() throws SyntacticException {
          List<Parametro> args = new ArrayList<>();
          args.addLast(ArgFormal());
          args.addAll(ArgsFormalesFinal());
          return args;
     }
     private List<Parametro> ArgsFormalesFinal() throws SyntacticException {
          List<Parametro> args = new ArrayList<>();
          if(currentToken.getTokenId().equals(TokenId.punt_coma)){
               match(TokenId.punt_coma);
               args.addLast(ArgFormal());
               args.addAll(ArgsFormalesFinal());
          }
          return args;
     }
     private Parametro ArgFormal() throws SyntacticException {
          Parametro p = null;
          Tipo t = Tipo();
          Token nombre = currentToken;
          match(TokenId.id_MetVar);
          p = new Parametro(nombre, t);
          return p;
     }
     private boolean BloqueOpcional() throws SyntacticException {
          if(Firsts.isFirst("Bloque", currentToken)){
               Bloque();
               return true;
          } else if (currentToken.getTokenId().equals(TokenId.punt_semicolon)) {
               match(TokenId.punt_semicolon);
               return false;
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
