package app.actions;

//View all portfolios progress

import app.CommandHandler;

import java.util.Scanner;

//TODO Priority 1

public class ShowPortfoliosProgress implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) {
        if (command == 15) {
            System.out.println("Tuleb hiljem");
        }
    }
}
