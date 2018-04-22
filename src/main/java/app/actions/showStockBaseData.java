package app.actions;

import app.CommandHandler;
import app.Stock;

import java.util.Scanner;

//View stock base data

public class showStockBaseData implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) {
        if (command == 10) {
            showStockBaseData(sc);
        }
    }

    private void showStockBaseData(Scanner sc) {
        sc.nextLine();
        System.out.println("Enter stock symbol: ");
        String stockSym = sc.nextLine();

        try {
            System.out.println(new Stock(stockSym));
        } catch (Exception e) {
            System.out.println("Stock information not available.");
        }
    }

}
