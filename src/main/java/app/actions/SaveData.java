/*
package app.actions;

import app.CommandHandler;
import app.Iu;
import app.User;

import java.io.*;
import java.util.Scanner;

import static app.StaticData.ANSI_RESET;
import static app.StaticData.ANSI_YELLOW;

//Save data file - saveData

public class SaveData implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws Exception {
        if (command == 17) {
            saveData(handler);
        }
    }

    public static void saveData(Iu handler) throws IOException {
        File file;
        handler.getSc().nextLine();

        if (handler.getActiveGame() == null) {
            System.out.print("Enter filename to save data: ");
            String filename = handler.getSc().nextLine();

            if (!filename.endsWith(".game"))
                filename += ".game";

            file = new File(filename);
        } else {
            file = handler.getActiveGame();
        }

        file.createNewFile();

        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        for (User user : handler.getUserList()) {
            writer.write(user.getPortfolio().toStringForFile());
        }
        writer.close();
        handler.setActiveGame(file);
        System.out.println(ANSI_YELLOW + handler.getActiveGame().getName() + " file saved." + ANSI_RESET);
    }
}
*/
