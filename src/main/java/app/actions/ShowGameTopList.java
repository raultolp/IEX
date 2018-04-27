package app.actions;

//View all portfolios progress

import app.CommandHandler;
import app.Iu;
import app.Portfolio;
import app.User;

import java.util.*;

//Top list, based on portfolio total value:

public class ShowGameTopList implements CommandHandler {

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
                "RANK USER\t\tPORTF.VALUE\t\t\tINCREASE IN VALUE\n" +
                "-------------------------------------------------");

        for (int i = 1; i < userList.size() + 1; i++) {
            User user = userList.get(i - 1);
            String name = user.getUserName();
            String portfTotalValue = String.format("%.2f", user.getPortfolioTotalValue());
            String portValueIncrease = String.format("%.2f", user.getValueIncrease());
            String portfValueIncreasePercent = String.format("%.4f", user.getPercentageIncrease());
            System.out.println(i + ". " + name + " \t\t" + portfTotalValue + " USD\t\t " +
                    portValueIncrease + " USD (" + portfValueIncreasePercent + "%)");
        }
    }
}
