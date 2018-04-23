package app.actions;

import app.CommandHandler;
import app.Iu;
import app.Portfolio;

import java.util.Scanner;

import static app.actions.BuyStock.enterQty;
import static app.actions.BuyStock.enterStockName;

//Transactions Report

public class ShowUserTransactions implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) throws Exception {
        if (command == 8) {
            Portfolio portfolio = Iu.getActiveUser().getPortfolio();
            if (portfolio != null){
                String report=portfolio.getTransactionsReport();
                System.out.println(report);
            }
            else {
                System.out.println("No transactions have been made yet.");
            }
        }
    }
}
