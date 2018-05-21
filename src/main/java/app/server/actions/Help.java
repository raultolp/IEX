package app.server.actions;

import app.server.CommandHandler;
import app.server.IO;
import app.server.Iu;

import java.io.IOException;

import static app.server.MyUtils.createHeader;
import static app.server.MyUtils.textGreen;
import static app.server.StaticData.*;

// Help + external resources web links

public class Help implements CommandHandler {

    @Override
    public void handle(String command, Iu handler, IO io) throws IOException {
        if (command.equals("13")) {
            io.println(textGreen(createHeader("Instructions")));
            io.println(gameInstructions);

            io.println(textGreen("External resources"));
            io.println(externalResources);
        }
    }
}
