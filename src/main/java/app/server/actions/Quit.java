package app.server.actions;

import app.server.CommandHandler;
import app.server.IO;
import app.server.Iu;

import java.io.IOException;

//Quit

public class Quit implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler, IO io) throws IOException {
        if (command == 13) {
            boolean isAdmin = handler.isAdmin();

            //app.server.actions.SaveData.saveData(handler);  //KAS VAJA ALATI SALVESTADA?

            if (isAdmin) {
                System.out.println("Quitting...");
                //handler.getDataCollector().interrupt();
                io.getSc().close();
                System.exit(0);
            } else {
                io.println("Quitting...");
                io.getIn().close();
                io.getOut().close();
            }

            handler.setRunning(false);

        }
    }
}
