package app.server.actions;

import app.server.CommandHandler;
import app.server.IO;
import app.server.Iu;

import java.io.IOException;

//View stock list base data

//TODO Priority 1

public class ShowStockListBaseData implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler, IO io) throws IOException {
        if (command == 6) {
            io.println("Tuleb hiljem");
        }
    }
}
