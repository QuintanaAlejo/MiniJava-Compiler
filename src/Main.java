import exceptions.LexicalException;
import lexical.LexicalAnalizer;
import lexical.Token;
import lexical.TokenId;
import sourcemanager.SourceManager;
import sourcemanager.SourceManagerImpl;
import java.io.IOException;
import java.util.Objects;

public class Main {
    public static void main(String[] args){
        SourceManager sm = new SourceManagerImpl();
        boolean error = false;
        Token token = new Token(TokenId.init, "", -1);
        LexicalAnalizer la = null;

        try {
            //sm.open("resources/withErrors/lexConErrores02.java"); // CABLE BORRAR
            //sm.open("resources/sinErrores/lexSinErrores02.java"); // CABLE BORRAR
            sm.open(args[0]);
            la = new LexicalAnalizer(sm);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        do {
            try{
                token = la.getToken();
                System.out.println("(" + token.getTokenId() + ", " + token.getLexeme() + ", " + token.getLinea() + ")");
            } catch (LexicalException e){
                e.printMessage(sm.getEntireLine());
                error = true;
            }
        } while (!Objects.equals(token.getTokenId(), TokenId.EOF));

        if (!error) {
            System.out.println("[SinErrores]");
        }
    }
}