package app.server.actions;

import app.server.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static app.server.StaticData.ANSI_RESET;
import static app.server.StaticData.ANSI_YELLOW;

//Load data file - loadData, listFiles

public class LoadData implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws Exception {
        if (command == 18) {
            boolean isAdmin = handler.isAdmin();
            if (isAdmin) {
                loadData(handler);
            } else {
                handler.getOut().writeUTF("Wrong input.");
            }

        }
    }

    public void loadData(Iu handler) throws IOException {

        //Scanner sc=handler.getSc();
        File currentDir = new File(".");
        List<String> fileNames = Arrays.asList(currentDir.list());

        MyUtils.listFiles(handler);
        handler.getSc().nextLine();
        System.out.print("Enter filename: ");
        String name = handler.getSc().nextLine();

        if (!name.endsWith(".game")) {
            name += ".game";
        }

        //If file does not exist:
        if (!fileNames.contains(name)) {
            System.out.println("File does not exist.");
        }

        //If file exists: loading data from file:
        else {
            File file = new File(name);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream
                    (file), "UTF-8"))) {

                //Converting data to JSON object:
                String gameAsString = br.readLine();
                JsonParser jp = new JsonParser();
                JsonObject gameObj = jp.parse(gameAsString).getAsJsonObject(); //from String to json

                //Creating users (with their portfolios, positions and transactions):
                List<User> newUserList = new ArrayList<>();
                Portfolio masterPortfolio = handler.getMasterPortfolio();

                for (String username : gameObj.keySet()) {
                    JsonObject userObj = gameObj.get(username).getAsJsonObject();
                    User newUser = new User(username, userObj, masterPortfolio);
                    newUserList.add(newUser);
                }

                handler.setUserList(newUserList);

            } finally {
                handler.setActiveGame(file);
                System.out.println(ANSI_YELLOW + handler.getActiveGame().getName() + " file loaded." + ANSI_RESET);
            }
        }
    }
}
