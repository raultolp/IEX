package app.actions;

import app.CommandHandler;
import app.Stock;

import java.util.Scanner;

//View stock historical data

public class showStockHistoricalData implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) {
        if (command == 11) {
            showStockHistoricalData(sc);
        }
    }

    private void showStockHistoricalData(Scanner sc) {

        //TODO: add option to choose time period

        sc.nextLine();
        System.out.println("Enter stock symbol: ");
        String stockSym = sc.nextLine();

        try {
            Stock stock = new Stock(stockSym);
            System.out.println("One month: " + stock.getChange1Month() + '\n' +
                    "Three months: " + stock.getChange3Month() + '\n' +
                    "Year: " + stock.getChange1Year());
        } catch (Exception e) {
            System.out.println("Stock info not available.");
        }
    }

}
