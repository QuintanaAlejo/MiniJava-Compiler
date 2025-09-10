package syntactic;

import exceptions.*;
import lexical.*;

import java.io.IOException;

import static lexical.TokenId.*;

public class SyntacticAnalyzer {
     LexicalAnalyzer lexicalAnalyzer;
     Token currentToken;
     //Firsts firsts;

     public SyntacticAnalyzer(LexicalAnalyzer lexicalAnalyzer) throws LexicalException, IOException {
          this.lexicalAnalyzer = lexicalAnalyzer;
          //firsts = new Firsts();
          nextToken();
     }

     public void match(TokenId expectedToken) throws LexicalException, IOException, SyntacticException {
          if(expectedToken.equals(currentToken.getTokenId()))
               nextToken();
          //else
               //throw new SyntacticException(expectedToken.toString(), tokenActual);
     }

     public void matchFirsts(String head) throws SyntacticException, LexicalException, IOException {
          //if(firsts.isFirst(head, currentToken))
               //nextToken();
          //else
               //throw new SyntacticException(head, tokenActual);
     }

     private void nextToken() throws LexicalException, IOException {
          currentToken = lexicalAnalyzer.getToken();
     }

     public void startAnalysis() throws LexicalException, SyntacticException, IOException{
          initial();
     }

     private void initial() throws LexicalException, SyntacticException, IOException{
          //listaClases();
          match(EOF);
     }
}
