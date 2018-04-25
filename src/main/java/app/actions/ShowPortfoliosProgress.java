package app.actions;

//View all portfolios progress

import app.CommandHandler;
import app.Iu;

//TODO Priority 1

public class ShowPortfoliosProgress implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) {
        if (command == 15) {
            System.out.println("Tuleb hiljem");
        }
    }
}
