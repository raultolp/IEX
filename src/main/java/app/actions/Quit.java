package app.actions;

import app.CommandHandler;

import java.util.Scanner;

import static app.Iu.ANSI_RESET;
import static app.Iu.ANSI_YELLOW;
import static app.Iu.getActiveGame;
import static app.actions.SaveData.saveData;

public class Quit implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) throws Exception {
        if (command == 16) {
            // Praegu ei tee midagi, kuid huljem võib olla vaja asjade sulgemiseks või mida iganes
            // QUIT toimub Iu-s hetkel

//            if (getActiveGame() != null)
//                saveData(sc);
//            System.out.println(ANSI_YELLOW + "Bye-bye!" + ANSI_RESET);
//            sc.close();
//            System.exit(0);
        }
    }
}
