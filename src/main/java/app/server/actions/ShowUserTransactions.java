package app.server.actions;

import app.server.*;

import java.io.DataOutputStream;
import java.io.IOException;

//Transactions Report

public class ShowUserTransactions implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler, IO io) throws IOException {

        if (command == 4) {
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
            }
            io.println("No transactions have been made yet.");
        }

    }
}
