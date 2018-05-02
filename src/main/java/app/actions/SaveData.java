package app.actions;

import app.CommandHandler;
import app.Iu;
import app.User;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.List;
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
        List<User> userlist = handler.getUserList();

        //Determining file name:
        if (handler.getActiveGame() == null) {
            System.out.print("Enter filename to save data: ");
            String filename = handler.getSc().nextLine();

            if (!filename.endsWith(".game"))
                filename += ".game";

            file = new File(filename);
        } else {
            file = handler.getActiveGame();
        }

        //Converting userlist, with all users, their portfolios, positions and
        // transactions into one JSON object:
        JsonObject gameObj = new JsonObject();  //kõik kasutajad
        for (User user : userlist) {
            JsonObject userObj = user.covertToJson();
            String username = user.getUserName();
            gameObj.add(username, userObj);
        }

        //Converting the JSON Object to String for saving to file:
        String gameAsString = gameObj.toString();

        //Saving users' data to file:
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        writer.write(gameAsString);
        writer.close();
        handler.setActiveGame(file);
        System.out.println(ANSI_YELLOW + handler.getActiveGame().getName() + " file saved." + ANSI_RESET);
    }
}
