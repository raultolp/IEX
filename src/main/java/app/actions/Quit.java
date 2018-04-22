package app.actions;

import app.CommandHandler;

import java.util.Scanner;

public class Quit implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) {
        if (command == 16) {
            System.out.println("Quiting...");
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
