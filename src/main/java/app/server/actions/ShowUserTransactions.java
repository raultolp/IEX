package app.server.actions;

import app.server.CommandHandler;
import app.server.Iu;
import app.server.MyUtils;
import app.server.Portfolio;

import java.io.DataOutputStream;
import java.io.IOException;

//Transactions Report

public class ShowUserTransactions implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws IOException {

        if (command == 4) {
            boolean isAdmin = handler.isAdmin();
            DataOutputStream out = handler.getOut();

            Portfolio portfolio;
            if (isAdmin) {
                portfolio = handler.getActiveUser().getPortfolio();
            } else {
                int userIndex = handler.getUserList().indexOf(handler.getActiveUser());
                portfolio = handler.getUserList().get(userIndex).getPortfolio();
            }

            if (portfolio != null) {
                String report = portfolio.getTransactionsReport(handler);
                if (isAdmin) {
                    System.out.println(report);
                } else {
                    out.writeUTF(report);
                }

            } else {
                if (isAdmin) {
                    MyUtils.colorPrintYellow("No transactions have been made yet.");
                } else {
                    out.writeUTF("No transactions have been made yet.");
                }
            }
        }
    }
}
