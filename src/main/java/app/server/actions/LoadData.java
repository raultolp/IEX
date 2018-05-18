package app.server.actions;

import app.server.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static app.server.StaticData.ANSI_RESET;
import static app.server.StaticData.ANSI_YELLOW;

//Load data file - loadData, listFiles

public class LoadData implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler, IO io) throws Exception {
        if (command == 19) {
            boolean isAdmin = handler.isAdmin();
            if (isAdmin) {
                loadData(handler, io);
            } else {
                io.println("Wrong input.");
            }

        }
    }

    public void loadData(Iu handler, IO io) throws IOException {

        //Scanner sc=handler.getSc();
        File currentDir = new File(".");
        List<String> fileNames = Arrays.asList(Objects.requireNonNull(currentDir.list()));

        MyUtils.listFiles(handler, io);
        io.getln();
        System.out.print("Enter filename: ");
        String name = io.getln();

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
                io.println(ANSI_YELLOW + handler.getActiveGame().getName() + " file loaded." + ANSI_RESET);
            }
        }
    }
}
