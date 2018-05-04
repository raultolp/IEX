package app.actions;

import app.CommandHandler;
import app.Iu;

import static app.StaticData.ANSI_RESET;
import static app.StaticData.ANSI_YELLOW;

//Quit

public class Quit implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) {
        if (command == 18) {
            System.out.println("Quiting...");
            handler.getDataCollector().interrupt();
            handler.getSc().close();
            //app.actions.SaveData.saveData(handler);  //võib-olla ei taha salvestada?

            System.out.println(ANSI_YELLOW + "Bye-bye!" + ANSI_RESET);
        }
    }
}
