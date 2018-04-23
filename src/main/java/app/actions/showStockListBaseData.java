package app.actions;

import app.CommandHandler;

import java.util.Scanner;

//View stock list base data

public class showStockListBaseData implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) {
        if (command == 10) {
            System.out.println("Tuleb hiljem");
        }
    }
}
