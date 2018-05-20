package app.server.actions;

import app.server.CommandHandler;
import app.server.IO;
import app.server.Iu;

import java.io.IOException;

import static app.server.MyUtils.createHeader;
import static app.server.StaticData.ANSI_GREEN;
import static app.server.StaticData.ANSI_RESET;
import static app.server.StaticData.gameInstructions;

public class Help implements CommandHandler {

    @Override
    public void handle(String command, Iu handler, IO io) throws IOException {
        if (command.equals("13")) {
            io.println(ANSI_GREEN + createHeader("Instructions") + ANSI_RESET);
            io.println(gameInstructions);
        }
    }
}