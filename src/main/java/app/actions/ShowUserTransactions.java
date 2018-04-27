package app.actions;

import app.CommandHandler;
import app.Iu;
import app.MyUtils;
import app.Portfolio;

//Transactions Report

public class ShowUserTransactions implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) {
        if (command == 8) {
            Portfolio portfolio = handler.getActiveUser().getPortfolio();
            if (portfolio != null) {
                String report = portfolio.getTransactionsReport();
                System.out.println(report);
            } else {
                MyUtils.colorPrintYellow("No transactions have been made yet.");
            }
        }
    }
}
