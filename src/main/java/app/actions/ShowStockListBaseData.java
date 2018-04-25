package app.actions;

import app.CommandHandler;
import app.Iu;

//View stock list base data

//TODO Priority 1

public class ShowStockListBaseData implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) {
        if (command == 10) {
            System.out.println("Tuleb hiljem");
        }
    }
}
