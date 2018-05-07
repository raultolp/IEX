package app.server.actions;

import app.server.CommandHandler;
import app.server.Iu;
import app.server.MyUtils;

import java.io.IOException;

import static app.server.StaticData.getMainMenuSize;

public class ErrorHandler implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws IOException {
        boolean isAdmin = handler.isAdmin();

        if (command < 1 || command > getMainMenuSize())
            if (isAdmin) {
                MyUtils.colorPrintRed("Wrong input, choose between 1.." + getMainMenuSize() + "!");
            } else {
                handler.getOut().writeUTF("Wrong input, choose between 1.." + getMainMenuSize() + "!");
            }

    }
}
