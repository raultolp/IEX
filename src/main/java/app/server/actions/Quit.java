package app.server.actions;

import app.server.CommandHandler;
import app.server.IO;
import app.server.Iu;

import java.io.IOException;

//QUIT

public class Quit implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler, IO io) throws IOException {
        if (command == 13) {
            boolean isAdmin = handler.isAdmin();

            if (isAdmin) {
                System.out.println("Quitting...");
                //handler.getDataCollector().interrupt();
                SaveData.saveData(handler, io);
                io.close();
                System.exit(0);
            } else {
                io.println("Quitting...");
                io.close();
            }

            handler.setRunning(false);

        }
    }
}
