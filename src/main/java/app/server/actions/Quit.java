package app.server.actions;

import app.server.CommandHandler;
import app.server.Iu;

import java.io.IOException;

//Quit

public class Quit implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws IOException {
        if (command == 13) {
            boolean isAdmin = handler.isAdmin();

            //app.server.actions.SaveData.saveData(handler);  //KAS VAJA ALATI SALVESTADA?

            if (isAdmin) {
                System.out.println("Quiting...");
                //handler.getDataCollector().interrupt();
                handler.getSc().close();
            } else {
                handler.getOut().writeUTF("Quiting...");
                handler.getIn().close();
                handler.getOut().close();

            }

            handler.setRunning(false);

        }
    }
}
