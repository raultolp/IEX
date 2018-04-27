package app.actions;

//View all portfolios progress

import app.CommandHandler;
import app.Iu;
import app.Portfolio;
import app.User;

import java.util.*;

//Top list, based on portfolio total value:

public class ShowPortfoliosProgress implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) {
        if (command == 15) {
            ShowPortfoliosProgress(handler);
        }
    }

    public void ShowPortfoliosProgress(Iu handler) {
        List<User> userList = handler.getUserList();
        for (User user : userList) {
            user.setPortfolioTotalValue();
        }

        Collections.sort(userList);

        System.out.println("\nTOP LIST:");
        System.out.println("-------------------------------------------------\n" +
                "USER\tPORTF.VALUE\t\tINCREASE IN VALUE (%)\n" +
                "-------------------------------------------------");

        for (User user : userList) {
            String name = user.getUserName();
            String portfTotalValue = String.format("%.4f", user.getPortfolioTotalValue());
            String portfValueIncrease = String.format("%.4f", user.getPercentageIncrease());
            System.out.println(name + "\t\t" + portfTotalValue + "\t\t\t" + portfValueIncrease);
        }
    }
}
