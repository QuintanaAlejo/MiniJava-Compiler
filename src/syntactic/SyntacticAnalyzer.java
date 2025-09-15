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
     private void HerenciaOpcional() throws SyntacticException {}
     private void ListaMiembros() throws SyntacticException {}
     private void ModificadorOpcionalNoVacio() throws SyntacticException {}
     private void Miembro() throws SyntacticException {}
     private void MiembroMetodo() throws SyntacticException {}
     private void Constructor() throws SyntacticException {}
     private void TipoMetodo() throws SyntacticException {}
     private void Tipo() throws SyntacticException {}
     private void TipoPrimitivo() throws SyntacticException {}
     private void ArgsFormales() throws SyntacticException {}
     private void ListaArgsFormalesOpcional() throws SyntacticException {}
     private void ListaArgsFormales() throws SyntacticException {}
     private void ArgsFormalesFinal() throws SyntacticException {}
     private void ArgFormal() throws SyntacticException {}
     private void BloqueOpcional() throws SyntacticException {}
     private void Bloque() throws SyntacticException {}
     private void ListaSentencias() throws SyntacticException {}
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
