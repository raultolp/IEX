package app.actions;

import app.CommandHandler;
import app.Iu;
import app.Portfolio;
import app.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import app.MyUtils;

import static app.StaticData.ANSI_RESET;
import static app.StaticData.ANSI_YELLOW;

//Load data file - loadData, listFiles

public class LoadData implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws Exception {
        if (command == 16) {
            loadData(handler);
        }
    }

    public void loadData(Iu handler) throws IOException {

        //Scanner sc=handler.getSc();
        File currentDir = new File(".");
        List<String> fileNames = Arrays.asList(currentDir.list());

        MyUtils.listFiles();

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
                for (String username : gameObj.keySet()) {
                    JsonObject userObj = gameObj.get(username).getAsJsonObject();
                    User newUser = new User(username, userObj, handler);
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
