package app;

import java.util.Scanner;

//View stock list base data

public class showStockListBaseData implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) {
        if (command == 9) {
            System.out.println("Tuleb hiljem");
        }
    }
}
