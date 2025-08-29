import exceptions.LexicalException;
import lexical.LexicalAnalizer;
import lexical.Token;
import lexical.TokenId;
import sourcemanager.SourceManager;
import sourcemanager.SourceManagerImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class Main {
    public static void main(String[] args){
        SourceManager sm = new SourceManagerImpl();
        boolean error = false;
        Token token = new Token(TokenId.EOF, "", -1);

        try {
            //sm.open("resources/conErrores/lexConErrores01.java"); // CABLE BORRAR
            sm.open("resources/sinErrores/lexSinErrores02.java"); // CABLE BORRAR
            //sm.open(args[0]);
        } catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }

        do {
            try{
                LexicalAnalizer la = new LexicalAnalizer(sm);
                token = la.getToken();
                System.out.println("(" + token.getTokenId() + ", " + token.getLexeme() + ", " + token.getLinea() + ")");
            } catch (LexicalException | IOException e){ // Revisar
                //e.printMessage();
                error = true;
            }

        } while (!Objects.equals(token.getTokenId(), TokenId.EOF));

        if (!error) {
            System.out.println("[SinErrores]");
        }
    }
}