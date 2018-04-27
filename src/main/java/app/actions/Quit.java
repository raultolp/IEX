package app.actions;

import app.CommandHandler;
import app.Iu;

//Quit

public class Quit implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) {
        if (command == 18) {
            System.out.println("Quiting...");
            //handler.getDataCollector().interrupt(); //pandud IU-sse - et rakenduks nii Quit valimisel kui ka mistahes muul juhul

            // Praegu ei tee midagi, kuid huljem võib olla vaja asjade sulgemiseks või mida iganes
            // QUIT toimub Iu-s hetkel
        }
    }
}
