package app.actions;

import app.CommandHandler;
import app.Iu;
import app.MyUtils;

import static app.StaticData.getMainMenuSize;

public class ErrorHandler implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) {
        if (command < 1 || command > getMainMenuSize())
            MyUtils.colorPrintRed("Wrong input, choose between 1.." + getMainMenuSize() + "!");
    }
}
