package app.actions;

import app.*;

import java.util.Scanner;

//View company base data

public class showCompanyData implements CommandHandler {

    Portfolio masterPortfolio = Iu.getMasterPortfolio();

    @Override
    public void handle(Integer command, Scanner sc) {
        if (command == 11) {
            showCompanyData(sc);
        }
    }

    private void showCompanyData(Scanner sc) {
        sc.nextLine();
        System.out.println("Enter stock symbol: ");
        String stockSym = sc.nextLine();

        try {
            //company base data:
            Company comp = new Company(stockSym);
            System.out.println(comp);

        } catch (Exception e) {
            System.out.println("Stock information not available.");
        }
    }

}
