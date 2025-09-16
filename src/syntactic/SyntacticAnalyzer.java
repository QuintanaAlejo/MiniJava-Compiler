package syntactic;

import exceptions.*;
import lexical.*;

import java.io.IOException;

public class SyntacticAnalyzer {
     LexicalAnalyzer lexicalAnalyzer;
     Token currentToken;
     Firsts firsts;

     public SyntacticAnalyzer(LexicalAnalyzer lexicalAnalyzer){
          this.lexicalAnalyzer = lexicalAnalyzer;
          firsts = new Firsts();
          nextToken();
     }

     public void match(TokenId expectedToken) throws SyntacticException {
          if(expectedToken.equals(currentToken.getTokenId()))
               nextToken();
          else
               throw new SyntacticException(currentToken.toString(), expectedToken.toString(), lexicalAnalyzer.getLineNumber());
     }

     public void isFirsts(String head) throws SyntacticException, LexicalException, IOException {
          if(Firsts.isFirst(head, currentToken))
               nextToken();
          else
               throw new SyntacticException(currentToken.toString(), head, lexicalAnalyzer.getLineNumber());
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
          if(Firsts.isFirst("Clase", currentToken)){
               Clase();
               ListaClases();
          }
     }

     private void Clase() throws SyntacticException {
          ModificadorOpcional();
          match(TokenId.kw_class);
          match(TokenId.id_Class);
          HerenciaOpcional();
          match(TokenId.punt_openKey);
          ListaMiembros();
          match(TokenId.punt_closeKey);
     }

     private void ModificadorOpcional() throws SyntacticException {
          switch (currentToken.getTokenId().toString()){
               case "kw_public" -> match(TokenId.kw_public);
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
               BloqueOpcional();
          } else if (currentToken.getTokenId().equals(TokenId.kw_void)) {
               match(TokenId.kw_void);
               match(TokenId.id_MetVar);
               ArgsFormales();
               BloqueOpcional();
          } else {
               throw new SyntacticException(currentToken.toString(), "Miembro", lexicalAnalyzer.getLineNumber());
          }
     }
     private void ModificadorOpcionalNoVacio() throws SyntacticException {
          switch (currentToken.getTokenId().toString()){
               case "kw_public" -> match(TokenId.kw_public);
               case "kw_abstract" -> match(TokenId.kw_abstract);
               case "kw_final" -> match(TokenId.kw_final);
               default -> throw new SyntacticException(currentToken.toString(), "ModificadorOpcionalNoVacio", lexicalAnalyzer.getLineNumber());
          }
     }
     private void MiembroMetodo() throws SyntacticException {
          if (Firsts.isFirst("ArgsFormales", currentToken)) {
               ArgsFormales();
               BloqueOpcional();
          } else {
               throw new SyntacticException(currentToken.toString(), "MiembroMetodo", lexicalAnalyzer.getLineNumber()); //La tengo que tirar?
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
               throw new SyntacticException(currentToken.toString(), "TipoMetodo", lexicalAnalyzer.getLineNumber());
          }
     }
     private void Tipo() throws SyntacticException {
          if (Firsts.isFirst("TipoPrimitivo", currentToken)) {
               TipoPrimitivo();
          } else if (currentToken.getTokenId().equals(TokenId.id_Class)) {
               match(TokenId.id_Class);
          } else {
               throw new SyntacticException(currentToken.toString(), "Tipo", lexicalAnalyzer.getLineNumber());
          }
     }
     private void TipoPrimitivo() throws SyntacticException {
          switch (currentToken.getTokenId().toString()){
               case "kw_int" -> match(TokenId.kw_int);
               case "kw_char" -> match(TokenId.kw_char);
               case "kw_boolean" -> match(TokenId.kw_boolean);
               default -> throw new SyntacticException(currentToken.toString(), "TipoPrimitivo", lexicalAnalyzer.getLineNumber());
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
               throw new SyntacticException(currentToken.toString(), "BloqueOpcional", lexicalAnalyzer.getLineNumber());
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
     private void Sentencia() throws SyntacticException {}
     private void AsignacionLLamada() throws SyntacticException {}
     private void VarLocal() throws SyntacticException {}
     private void Return() throws SyntacticException {}
     private void ExpresionOpcional() throws SyntacticException {}
     private void If() throws SyntacticException {}
     private void Else() throws SyntacticException {}
     private void While() throws SyntacticException {}
     private void For() throws SyntacticException {}
     private void ExpresionFor() throws SyntacticException {}
     private void Expresion() throws SyntacticException {}
     private void ExpresionExtra() throws SyntacticException {}
     private void OperadorAsignacion() throws SyntacticException {}
     private void ExpresionCompuesta() throws SyntacticException {}
     private void ExpresionCompuestaFinal() throws SyntacticException {}
     private void OperadorBinario() throws SyntacticException {}
     private void ExpresionBasica() throws SyntacticException {}
     private void OperadorUnario() throws SyntacticException {}
     private void Operando() throws SyntacticException {}
     private void Primitivo() throws SyntacticException {}
     private void Referencia() throws SyntacticException {}
     private void ReferenciaEncadenada() throws SyntacticException {}
     private void Primario() throws SyntacticException {}
     private void AccesoVarMetodo() throws SyntacticException {}
     private void ArgsPosibles() throws SyntacticException {}
     private void LlamadaConstructor() throws SyntacticException {}
     private void ExpresionParentizada() throws SyntacticException {}
     private void LlamadaMetodoEstatico() throws SyntacticException {}
     private void ArgsActuales() throws SyntacticException {}
     private void ListaExpsOpcional() throws SyntacticException {}
     private void ListaExps() throws SyntacticException {}
     private void ElemEncadenado() throws SyntacticException {}
}
