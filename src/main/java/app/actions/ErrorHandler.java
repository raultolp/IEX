package app.actions;

import app.CommandHandler;

import java.util.Scanner;

import static app.StaticData.ANSI_RED;
import static app.StaticData.ANSI_RESET;
import static app.StaticData.getMainMenuSize;

public class ErrorHandler implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) {
        if (command < 1 || command > getMainMenuSize())
            System.out.println(ANSI_RED + "Wrong input, choose between 1.." + getMainMenuSize() + "!" + ANSI_RESET);
    }
}
