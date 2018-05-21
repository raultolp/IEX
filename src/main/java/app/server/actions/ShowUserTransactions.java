package app.server.actions;

import app.server.*;

import java.io.IOException;

import static app.server.MyUtils.textRed;

//Transactions Report

public class ShowUserTransactions implements CommandHandler {

    @Override
    public void handle(String command, Iu handler, IO io) throws IOException {

        if (command.equals("4")) {
            boolean isAdmin = handler.isAdmin();

            Portfolio portfolio;
            if (isAdmin) {
                portfolio = handler.getActiveUser().getPortfolio();
            } else {
                int userIndex = handler.getUserList().indexOf(handler.getActiveUser());
                portfolio = handler.getUserList().get(userIndex).getPortfolio();
            }

            if (portfolio != null) {
                String report = portfolio.getTransactionsReport(handler);
                io.println(report);
            } else {
                io.println(textRed("No transactions have been made yet."));
            }
        }

    }
}
