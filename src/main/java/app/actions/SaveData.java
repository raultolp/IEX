package app.actions;

import app.CommandHandler;
import app.User;

import java.io.*;
import java.util.Scanner;

import static app.Iu.*;

public class SaveData implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) throws Exception {
        if (command == 15) {
            saveData(sc);
        }
    }

    public static void saveData(Scanner sc) throws IOException {
        File file;
        sc.nextLine();

        if (getActiveGame() == null) {
            System.out.print("Enter filename to save data: ");
            String filename = sc.nextLine();

            if (!filename.endsWith(".game"))
                filename += ".game";

            file = new File(filename);
        } else {
            file = getActiveGame();
        }

        file.createNewFile();

        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        for (User user : getUserList()) {
            writer.write(user.getPortfolio().toStringForFile());
        }
        writer.close();
        setActiveGame(file);
        System.out.println(ANSI_YELLOW + getActiveGame().getName() + " file saved." + ANSI_RESET);
    }
}
