package app.actions;

import app.CommandHandler;

import java.util.Scanner;

import static app.Iu.getMainMenuSize;

public class ErrorHandler implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) throws Exception {
        if (command < 1 || command > 16)
            System.out.println("Wrong input! Choose between 1.." + getMainMenuSize());
    }
}
