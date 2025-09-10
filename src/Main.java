import exceptions.LexicalException;
import lexical.LexicalAnalyzer;
import lexical.Token;
import lexical.TokenId;
import sourcemanager.SourceManager;
import sourcemanager.SourceManagerImpl;
import java.io.IOException;
import java.util.Objects;

public class Main {
    public static void main(String[] args){
        SourceManager sourceManager = new SourceManagerImpl();
        boolean error = false;
        Token token = new Token(TokenId.init, "", -1);
        LexicalAnalyzer lexicalAnalyzer = null;

        try {
            sourceManager.open(args[0]);
            lexicalAnalyzer = new LexicalAnalyzer(sourceManager);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        do {
            try{
                token = lexicalAnalyzer.getToken();
                System.out.println("(" + token.getTokenId() + ", " + token.getLexeme() + ", " + token.getLinea() + ")");
            } catch (LexicalException e){
                e.printMessage(sourceManager.getEntireLine());
                error = true;
            }
        } while (!Objects.equals(token.getTokenId(), TokenId.EOF));

        if (!error) {
            System.out.println("[SinErrores]");
        }
    }
}