package app.actions;

import app.CommandHandler;
import app.Iu;
import app.Portfolio;
import app.User;

import java.util.Collections;
import java.util.List;

import static app.actions.ShowUsersList.showUsersList;

//View active user's portfolio - showUserPortfolio

public class ShowUserPortfolio implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) {
        if (command == 7) {
            showUserPortfolio(handler);
        }
    }

    private static void showUserPortfolio(Iu handler) {
        User user = handler.getActiveUser();
        String username = user.getUserName();

        //Info from Portfolio:
        System.out.println("\n" + username.toUpperCase() + "'s PORTFOLIO: ");
        System.out.println(user.getPortfolio());

        //Info from User:
        String portValueIncrease = String.format("%.2f", user.getValueIncrease());
        String portfValueIncreasePercent = String.format("%.4f", user.getPercentageIncrease());
        System.out.println("PORTFOLIO'S TOTAL INCREASE IN VALUE (REALIZED + UNREALIZED PROFIT): " +
                portValueIncrease + " USD (" + portfValueIncreasePercent + "%)");

        //Getting user ranking in TopList:
        List<User> userList = handler.getUserList();
        Collections.sort(userList);
        int ranking = userList.indexOf(user) + 1;
        System.out.println(username + "'s ranking in game: " + ranking);


/*        if (username.length() < 3)
            username = handler.getActiveUser().getUserName();

        for (User user : handler.getUserList()) {
            if (user.getUserName().equals(username)) {
                Portfolio portfolio = user.getPortfolio();
                System.out.print("\nAvailable cash: ");
                System.out.printf("%.2f", user.getAvailableFunds());
                //System.out.println("\n" + portfolio.toString());
                System.out.print("Portfolio total value: ");
                System.out.printf("%.2f", portfolio.getTotalValueOfPortfolio());
                System.out.println();
            }
        }*/
    }

}
