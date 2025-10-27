package syntactic;

import Main.Main;
import TablaDeSimbolos.*;
import TablaDeSimbolos.NodosAST.encadenado.NodoEncadenado;
import TablaDeSimbolos.NodosAST.encadenado.NodoMetodoLlamadaEncadenada;
import TablaDeSimbolos.NodosAST.encadenado.NodoVariableEncadeanda;
import TablaDeSimbolos.NodosAST.expresion.*;
import TablaDeSimbolos.NodosAST.expresion.acceso.*;
import TablaDeSimbolos.NodosAST.expresion.literal.NodoBoolean;
import TablaDeSimbolos.NodosAST.expresion.literal.NodoChar;
import TablaDeSimbolos.NodosAST.expresion.literal.NodoInt;
import TablaDeSimbolos.NodosAST.expresion.literal.NodoNull;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoAcceso;
import TablaDeSimbolos.NodosAST.expresion.operandos.NodoLiteral;
import TablaDeSimbolos.NodosAST.sentencia.*;
import TablaDeSimbolos.Tipos.Tipo;
import TablaDeSimbolos.Tipos.TipoPrimitivo;
import TablaDeSimbolos.Tipos.TipoReferencia;
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
          Token mod = ModificadorOpcional();
          match(TokenId.kw_class);
          Token nombreClase = currentToken;
          match(TokenId.id_Class);
          Token padre = HerenciaOpcional();
          Clase c = new Clase(nombreClase, mod, padre);
          Main.TS.setClaseActual(c);
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

     private Token ModificadorOpcional() throws SyntacticException {
          Token mod = currentToken;
          switch (currentToken.getTokenId().toString()){
               case "kw_static" -> match(TokenId.kw_static);
               case "kw_abstract" -> match(TokenId.kw_abstract);
               case "kw_final" -> match(TokenId.kw_final);
          }
          if (mod.getLexeme().equals("static") || mod.getLexeme().equals("abstract") || mod.getLexeme().equals("final")){
               return mod;
          }
          return null;
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
               BloqueOpcional();
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
               BloqueOpcional();
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
               BloqueOpcional();
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
          c.setBloque(Bloque());
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
     private NodoBloque BloqueOpcional() throws SyntacticException {
          if(Firsts.isFirst("Bloque", currentToken)){
               NodoBloque bloque = Bloque();
               Main.TS.getMetodoActual().setBloque(bloque);
               Main.TS.getMetodoActual().setTieneBloque(true);
               return bloque;
          } else if (currentToken.getTokenId().equals(TokenId.punt_semicolon)) {
               match(TokenId.punt_semicolon);
               Main.TS.getMetodoActual().setTieneBloque(false);
               return new NodoBloqueVacio();
          } else {
               throw new SyntacticException(currentToken.getLexeme(), "BloqueOpcional", lexicalAnalyzer.getLineNumber());
          }
     }
     private NodoBloque Bloque() throws SyntacticException {
          match(TokenId.punt_openKey);
          NodoBloque bloque = new NodoBloque();
          ArrayList<NodoSentencia> sentencias = ListaSentencias();
          match(TokenId.punt_closeKey);
          bloque.setSentencias(sentencias);
          return bloque;
     }

     private ArrayList<NodoSentencia> ListaSentencias() throws SyntacticException {
          ArrayList<NodoSentencia> sentencias = new ArrayList<>();
          if(Firsts.isFirst("Sentencia", currentToken)){
               sentencias.add(Sentencia());
               sentencias.addAll(ListaSentencias());
          }
          return sentencias;
     }
     private NodoSentencia Sentencia() throws SyntacticException {
          if(currentToken.getTokenId().equals(TokenId.punt_semicolon)){
               match(TokenId.punt_semicolon);
               return new NodoSentenciaVacia();
          } else if(Firsts.isFirst("AsignacionLLamada", currentToken)){
               NodoSentencia nodo = new NodoAsignacion(currentToken, AsignacionLLamada());
               match(TokenId.punt_semicolon);
               return nodo;
          } else if(Firsts.isFirst("VarLocal", currentToken)){
               NodoVarLocal nodo = VarLocal();
               match(TokenId.punt_semicolon);
               return nodo;
          } else if(Firsts.isFirst("Return", currentToken)){
               NodoReturn nodo = Return(currentToken);
               match(TokenId.punt_semicolon);
               return nodo;
          } else if(Firsts.isFirst("If", currentToken)){
               return If();
          } else if(Firsts.isFirst("While", currentToken)){
               return While();
          } else if(Firsts.isFirst("Bloque", currentToken)){
               return Bloque();
          } else if(Firsts.isFirst("For", currentToken)){
               For(); //Logro
          } else {
               throw new SyntacticException(currentToken.getLexeme(), "Sentencia", lexicalAnalyzer.getLineNumber());
          }
          return new NodoSentenciaVacia();
     }
     private NodoExpresion AsignacionLLamada() throws SyntacticException {
          return Expresion();
     }
     private NodoVarLocal VarLocal() throws SyntacticException {
          match(TokenId.kw_var);
          Token tokenVar = currentToken;
          match(TokenId.id_MetVar);
          match(TokenId.assignment);
          NodoExpresion exp = ExpresionCompuesta(); //Aca tengo que guardar la expresion en el nodo
          return new NodoVarLocal(tokenVar, exp);
     }
     private NodoReturn Return(Token token) throws SyntacticException {
          match(TokenId.kw_return);
          NodoReturn nodo = new NodoReturn(token, ExpresionOpcional());
          if (Main.TS.getMetodoActual().getTipoRetorno() == null){
               nodo.setTipoDeRet(new TipoPrimitivo(new Token(TokenId.kw_void, "void", lexicalAnalyzer.getLineNumber())));
          } else {
               nodo.setTipoDeRet(Main.TS.getMetodoActual().getTipoRetorno());
          }
          return nodo;
     }
     private NodoExpresion ExpresionOpcional() throws SyntacticException {
          if(Firsts.isFirst("Expresion", currentToken)){
               return Expresion();
          } else {
               return new NodoExpresionVacia();
          }
     }
     private NodoIf If() throws SyntacticException {
          Token tokenIf = currentToken;
          match(TokenId.kw_if);
          match(TokenId.punt_openParenthesis);
          NodoExpresion expresion = Expresion();
          match(TokenId.punt_closeParenthesis);
          NodoSentencia sentencia = Sentencia();
          NodoSentencia senElse = Else();
          return new NodoIf(tokenIf, expresion, sentencia, senElse);
     }
     private NodoSentencia Else() throws SyntacticException {
          if(currentToken.getTokenId().equals(TokenId.kw_else)){
               match(TokenId.kw_else);
               return Sentencia();
          } else {
               return new NodoSentenciaVacia();
          }
     }
     private NodoWhile While() throws SyntacticException {
          Token tokenWhile = currentToken;
          match(TokenId.kw_while);
          match(TokenId.punt_openParenthesis);
          NodoExpresion expresion = Expresion();
          match(TokenId.punt_closeParenthesis);
          NodoSentencia sentencia = Sentencia();
          return new NodoWhile(expresion, sentencia, tokenWhile);
     }
     private void For() throws SyntacticException { //Es logro, ver despues
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
     private NodoExpresion Expresion() throws SyntacticException {
          NodoExpresion exp = ExpresionCompuesta();
          return ExpresionExtra(exp);
     }
     private NodoExpresion ExpresionExtra(NodoExpresion exp) throws SyntacticException {
         if(Firsts.isFirst("OperadorAsignacion", currentToken)){
               Token asignacion = currentToken;
               OperadorAsignacion();
               return new NodoExpresionAsignacion(exp, ExpresionCompuesta(), asignacion);
         }
         return exp;
     }
     private void OperadorAsignacion() throws SyntacticException {
          match(TokenId.assignment);
     }
     private NodoExpresion ExpresionCompuesta() throws SyntacticException {
          NodoExpresion expresionIzq = ExpresionBasica();
          return ExpresionCompuestaFinal(expresionIzq);
     }
     private NodoExpresion ExpresionCompuestaFinal(NodoExpresion expIzq) throws SyntacticException {
          if(Firsts.isFirst("OperadorBinario", currentToken)){
               NodoExpresionBinaria expBin = OperadorBinario();
               NodoExpresion expDer = ExpresionBasica();
               expBin.setIzquierda(expIzq);
               expBin.setDerecha(expDer);
               return ExpresionCompuestaFinal(expBin);
          } else if (currentToken.getTokenId().equals(TokenId.op_ternary)) {
               match(TokenId.op_ternary);
               Expresion();
               match(TokenId.punt_colon);
               Expresion();
          }
          return expIzq;
     }
     private NodoExpresionBinaria OperadorBinario() throws SyntacticException {
          Token tokenOp = currentToken;
          NodoExpresionBinaria operadorBinario = new NodoExpresionBinaria(tokenOp);
          switch (currentToken.getTokenId().toString()){
               case "op_or" -> {
                    match(TokenId.op_or);
                    return operadorBinario;
               }
               case "op_and" -> {
                    match(TokenId.op_and);
                    return operadorBinario;
               }
               case "op_equal" -> {
                    match(TokenId.op_equal);
                    return operadorBinario;
               }
               case "op_notEqual" -> {
                    match(TokenId.op_notEqual);
                    return operadorBinario;
               }
               case "op_less" -> {
                    match(TokenId.op_less);
                    return operadorBinario;
               }
               case "op_greater" -> {
                    match(TokenId.op_greater);
                    return operadorBinario;
               }
               case "op_lessOrEqual" -> {
                    match(TokenId.op_lessOrEqual);
                    return operadorBinario;
               }
               case "op_greaterOrEqual" -> {
                    match(TokenId.op_greaterOrEqual);
                    return operadorBinario;
               }
               case "op_plus" -> {
                    match(TokenId.op_plus);
                    return operadorBinario;
               }
               case "op_minus" -> {
                    match(TokenId.op_minus);
                    return operadorBinario;
               }
               case "op_multiplication" -> {
                    match(TokenId.op_multiplication);
                    return operadorBinario;
               }
               case "op_division" -> {
                    match(TokenId.op_division);
                    return operadorBinario;
               }
               case "op_module" -> {
                    match(TokenId.op_module);
                    return operadorBinario;
               }
               default -> throw new SyntacticException(currentToken.getLexeme(), "OperadorBinario", lexicalAnalyzer.getLineNumber());
          }
     }
     private NodoExpresion ExpresionBasica() throws SyntacticException {
          if(Firsts.isFirst("OperadorUnario", currentToken)){
               Token operador = OperadorUnario();
               NodoOperando operando = Operando();
               return new NodoExpresionUnaria(operador, operando);
          } else if (Firsts.isFirst("Operando", currentToken)) {
               return Operando();
          } else {
               throw new SyntacticException(currentToken.getLexeme(), "ExpresionBasica", lexicalAnalyzer.getLineNumber());
          }
     }
     private Token OperadorUnario() throws SyntacticException {
            switch (currentToken.getTokenId().toString()){
                 case "op_plus" -> {
                      Token plusToken = currentToken;
                      match(TokenId.op_plus);
                      return plusToken;
                 }
                 case "increment" -> {
                      Token incrementToken = currentToken;
                      match(TokenId.increment);
                      return incrementToken;
                 }
                 case "op_minus" -> {
                      Token minusToken = currentToken;
                      match(TokenId.op_minus);
                      return minusToken;
                 }
                 case "decrement" -> {
                      Token decrementToken = currentToken;
                      match(TokenId.decrement);
                      return decrementToken;
                 }
                 case "op_not" -> {
                      Token notToken = currentToken;
                      match(TokenId.op_not);
                      return notToken;
                 }
                 default -> throw new SyntacticException(currentToken.getLexeme(), "OperadorUnario", lexicalAnalyzer.getLineNumber());
            }
     }
     private NodoOperando Operando() throws SyntacticException {
          if(Firsts.isFirst("Primitivo", currentToken)){
                 return Primitivo();
          } else if (Firsts.isFirst("Referencia", currentToken)) {
                 return Referencia();
          } else {
                 throw new SyntacticException(currentToken.getLexeme(), "Operando", lexicalAnalyzer.getLineNumber());
          }
     }
     private NodoLiteral Primitivo() throws SyntacticException {
          switch (currentToken.getTokenId().toString()){
               case "kw_true" -> {
                    Token trueToken = currentToken;
                    match(TokenId.kw_true);
                    return new NodoBoolean(trueToken);
               }
               case "kw_false" -> {
                    Token falseToken = currentToken;
                    match(TokenId.kw_false);
                    return new NodoBoolean(falseToken);
               }
               case "lit_int" -> {
                    Token intToken = currentToken;
                    match(TokenId.lit_int);
                    return new NodoInt(intToken);
               }
               case "lit_char" -> {
                    Token charToken = currentToken;
                    match(TokenId.lit_char);
                    return new NodoChar(charToken);
               }
               case "kw_null" -> {
                    Token nullToken = currentToken;
                    match(TokenId.kw_null);
                    return new NodoNull(nullToken);
               }
               default -> throw new SyntacticException(currentToken.getLexeme(), "Primitivo", lexicalAnalyzer.getLineNumber());
          }
     }
     private NodoAcceso Referencia() throws SyntacticException {
          NodoAcceso nodoAcceso = Primario();
          nodoAcceso.setEncadenado(ReferenciaEncadenada());
          return nodoAcceso;
     }
     private NodoEncadenado ReferenciaEncadenada() throws SyntacticException {
          NodoEncadenado nodoEncadenado = null;
          if (Firsts.isFirst("ReferenciaEncadenada", currentToken)) {
               match(TokenId.punt_dot);
               Token idToken = currentToken;
               match(TokenId.id_MetVar);
               List<NodoExpresion> parametros = ElemEncadenado();
               if(parametros != null){
                    nodoEncadenado = new NodoMetodoLlamadaEncadenada(idToken);
                    nodoEncadenado.setSiguiente(ReferenciaEncadenada());
               } else {
                    nodoEncadenado = new NodoVariableEncadeanda(idToken);
                    nodoEncadenado.setSiguiente(ReferenciaEncadenada());
               }
          }
          return nodoEncadenado;
     }
     private NodoAcceso Primario() throws SyntacticException {
          if(currentToken.getTokenId().equals(TokenId.kw_this)){
               return accesoThis(currentToken);
          } else if (currentToken.getTokenId().equals(TokenId.lit_string)) {
               return litString(currentToken);
          } else if (Firsts.isFirst("AccesoVarMetodo", currentToken)) {
               return AccesoVarMetodo(currentToken);
          } else if (Firsts.isFirst("LlamadaConstructor", currentToken)) {
               return LlamadaConstructor(currentToken);
          } else if (Firsts.isFirst("LlamadaMetodoEstatico", currentToken)) {
               return LlamadaMetodoEstatico(currentToken);
          } else if (Firsts.isFirst("ExpresionParentizada", currentToken)) {
               return ExpresionParentizada(currentToken);
          } else {
               throw new SyntacticException(currentToken.getLexeme(), "Primario", lexicalAnalyzer.getLineNumber());
          }
     }
     private NodoAcceso accesoThis(Token token) throws SyntacticException {
          match(TokenId.kw_this);
          return new NodoThis(token, Main.TS.getClaseActual().getNombre());
     }
     private NodoAcceso litString(Token token) throws SyntacticException {
          match(TokenId.lit_string);
          return new NodoString(token);
     }
     private NodoAcceso AccesoVarMetodo(Token token) throws SyntacticException {
          match(TokenId.id_MetVar);
          return ArgsPosibles(token);
     }
     private NodoAcceso ArgsPosibles(Token token) throws SyntacticException {
          if(Firsts.isFirst("ArgsActuales", currentToken)){
               List<NodoExpresion> parametros = ArgsActuales();
               return new NodoLlamadaMetodo(token, parametros);
          } else{
               return new NodoVarAcceso(token);
          }
     }
     private NodoAcceso LlamadaConstructor(Token token) throws SyntacticException {
          match(TokenId.kw_new);
          Token nombreClase = currentToken;
          match(TokenId.id_Class);
          NodoLlamadaConstructor nodoLlamadaConstructor = new NodoLlamadaConstructor(nombreClase);
          nodoLlamadaConstructor.setParametros(ArgsActuales());
          return nodoLlamadaConstructor;
     }
     private NodoAcceso ExpresionParentizada(Token token) throws SyntacticException {
          match(TokenId.punt_openParenthesis);
          NodoExpresion exp = Expresion();
          match(TokenId.punt_closeParenthesis);
          return new NodoExpresionParentizada(exp);
     }
     private NodoAcceso LlamadaMetodoEstatico(Token token) throws SyntacticException {
          Token clase = currentToken;
          match(TokenId.id_Class);
          match(TokenId.punt_dot);
          Token metodo = currentToken;
          match(TokenId.id_MetVar);
          NodoLlamadaMetodoEstatico llamada = new NodoLlamadaMetodoEstatico(clase, metodo);
          llamada.setArgumentos(ArgsActuales());
          return llamada;
     }
     private List<NodoExpresion> ArgsActuales() throws SyntacticException {
          match(TokenId.punt_openParenthesis);
          List<NodoExpresion> parametrosActuales = ListaExpsOpcional();
          match(TokenId.punt_closeParenthesis);
          return parametrosActuales;
     }
     private List<NodoExpresion> ListaExpsOpcional() throws SyntacticException {
          List<NodoExpresion> parametros = new ArrayList<>();
          if(Firsts.isFirst("Expresion", currentToken)){
               NodoExpresion exp = Expresion();
               parametros = ListaExps();
               parametros.add(exp);
          }
          return parametros;
     }
     private List<NodoExpresion> ListaExps() throws SyntacticException {
          List<NodoExpresion> parametros = new ArrayList<>();
          if(currentToken.getTokenId().equals(TokenId.punt_coma)){
               match(TokenId.punt_coma);
               NodoExpresion exp = Expresion();
               parametros = ListaExps();
               parametros.add(exp);
          }
          return parametros;
     }
     private List<NodoExpresion> ElemEncadenado() throws SyntacticException {
          List<NodoExpresion> retorno = null;
          if(Firsts.isFirst("ArgsActuales", currentToken)){
               retorno = ArgsActuales();
          }
          return retorno;
     }
}
