package app.server.actions;

import app.server.CommandHandler;
import app.server.IO;
import app.server.Iu;
import app.server.MyUtils;

import static app.server.StaticData.getMainMenuSize;


public class ErrorHandler implements CommandHandler {

    @Override
    public void handle(String command, Iu handler, IO io) throws Exception {
        if (MyUtils.isAlpha(command) || Integer.parseInt(command) < 1 || Integer.parseInt(command) > getMainMenuSize()) {
            io.println("Wrong input, choose between 1.." + getMainMenuSize() + "!");
        }
    }

}
