package app.server.actions;

import app.server.CommandHandler;
import app.server.Iu;
import app.server.User;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.List;

import static app.server.StaticData.ANSI_RESET;
import static app.server.StaticData.ANSI_YELLOW;

//Save data file - saveData

public class SaveData implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws Exception {
        if (command == 19) {
            boolean isAdmin = handler.isAdmin();
            if (isAdmin) {
                saveData(handler);
            } else {
                handler.getOut().writeUTF("Wrong input.");
            }
        }
    }

    public static void saveData(Iu handler) throws IOException {
        File file;
        List<User> userlist = handler.getUserList();

        handler.getSc().nextLine();

        //Determining file name:
        if (handler.getActiveGame() == null) {
            System.out.print("Enter filename to save data: ");
            String filename = handler.getSc().nextLine();

            if (!filename.endsWith(".game")) {
                filename += ".game";
            }
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
