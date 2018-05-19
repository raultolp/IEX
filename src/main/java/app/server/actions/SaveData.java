package app.server.actions;

import app.server.CommandHandler;
import app.server.IO;
import app.server.Iu;
import app.server.User;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.List;

import static app.server.MyUtils.colorPrintRed;
import static app.server.StaticData.ANSI_RESET;
import static app.server.StaticData.ANSI_YELLOW;

//Save data file - saveData

public class SaveData implements CommandHandler {

    @Override
    public void handle(String command, Iu handler, IO io) throws Exception {
        if (command.equals("20")) {
            boolean isAdmin = handler.isAdmin();
            if (isAdmin) {
                saveData(handler, io);
            } else {
                io.println("Wrong input.");
            }
        }
    }

    public static void saveData(Iu handler, IO io) throws IOException {
        File file;
        List<User> userlist = handler.getUserList();
        String filename;

        File directory = new File("Games");
        if (!directory.exists())
            if (!directory.mkdir())
                colorPrintRed("Creating " + directory.getName() + " directory failed!");

        //Determining file name:
        if (handler.getActiveGame() == null) {
            System.out.print("Enter filename to save data: ");
            filename = io.getln();
        } else {
            filename = handler.getActiveGame().getName();
        }
        if (filename.length() < 2)
            filename = "newSavedGame";
        if (!filename.endsWith(".game"))
            filename += ".game";

        file = new File(directory, filename);

        //Converting userlist, with all users, their portfolios, positions and
        // transactions into one JSON object:
        JsonObject gameObj = new JsonObject();  //kõik kasutajad
        for (
                User user : userlist)

        {
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
        System.out.println(ANSI_YELLOW + handler.getActiveGame().

                getName() + " file saved." + ANSI_RESET);
    }
}
