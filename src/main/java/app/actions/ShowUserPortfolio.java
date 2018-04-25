package app.actions;

import app.CommandHandler;
import app.Iu;
import app.Portfolio;
import app.User;

import static app.actions.ShowUsersList.showUsersList;

//View user portfolio - showUserPortfolio

public class ShowUserPortfolio implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) {
        if (command == 7) {
            showUserPortfolio(handler);
        }
    }

    private static void showUserPortfolio(Iu handler) {
        String username;

        handler.getSc().nextLine();
        showUsersList(handler.getUserList());
        System.out.print("Enter username: ");
        username = handler.getSc().nextLine();

        if (username.length() < 3)
            username = handler.getActiveUser().getUserName();

        for (User user : handler.getUserList()) {
            if (user.getUserName().equals(username)) {
                Portfolio portfolio = user.getPortfolio();
                System.out.print("\nAvailable cash: ");
                System.out.printf("%.2f", user.getAvailableFunds());
                System.out.println("\n" + portfolio.toString());
                System.out.print("Portfolio total value: ");
                System.out.printf("%.2f", portfolio.getTotalValueOfPortfolio());
                System.out.println();
            }
        }
    }

}
