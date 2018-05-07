package app.server.actions;

import app.server.CommandHandler;
import app.server.Iu;

import java.io.IOException;

//View stock list base data

//TODO Priority 1

public class ShowStockListBaseData implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws IOException {
        if (command == 6) {
            boolean isAdmin = handler.isAdmin();
            if (isAdmin) {
                System.out.println("Tuleb hiljem");
            } else {
                handler.getOut().writeUTF("Tuleb hiljem");
            }

        }
    }
}
