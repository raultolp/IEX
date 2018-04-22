package app.actions;

//View all portfolios progress

import app.CommandHandler;

import java.util.Scanner;

public class showPortfoliosProgress implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) {
        if (command == 12) {
            System.out.println("Tuleb hiljem");
        }
    }
}
