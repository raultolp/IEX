package app.actions;

import app.CommandHandler;
import app.Company;
import app.Stock;
import java.util.Scanner;

//View stock base data

public class ShowStockBaseData implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) {
        if (command == 11) {
            showStockBaseData(sc);
        }
    }

    private void showStockBaseData(Scanner sc) {
        sc.nextLine();
        System.out.println("Enter stock symbol: ");
        String stockSym = sc.nextLine();

        try {
            //company base data:
            Company comp = new Company(stockSym);
            System.out.println(comp);

            //stock fundamentals:
            System.out.println(new Stock(stockSym)); //uusi stocke poleks vaja teha! (tähendab veebist info laadimist)

        } catch (Exception e) {
            System.out.println("Stock information not available.");
        }
    }

}
