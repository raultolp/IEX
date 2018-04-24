package app.actions;

import app.CommandHandler;
import app.Iu;
import java.util.Scanner;

//Quit

public class Quit implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) {
        if (command == 18) {
            System.out.println("Quiting...");
            app.Iu.t2.interrupt();

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
