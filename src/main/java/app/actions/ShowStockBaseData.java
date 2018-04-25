package app.actions;

import app.*;

import java.util.Scanner;

//View stock base data

public class ShowStockBaseData implements CommandHandler {

    Portfolio masterPortfolio= Iu.getMasterPortfolio();

    @Override
    public void handle(Integer command, Scanner sc) {
        if (command == 12) {
            showStockBaseData(sc);
        }
    }

    private void showStockBaseData(Scanner sc) {
        sc.nextLine();
        System.out.println("Enter stock symbol: ");
        String stockSym = sc.nextLine();

        try {
            //stock fundamentals:
            System.out.println(masterPortfolio.getStock(stockSym));


        } catch (Exception e) {
            System.out.println("Stock information not available.");
        }
    }

}
