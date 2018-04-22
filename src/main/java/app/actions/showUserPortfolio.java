package app.actions;

import app.CommandHandler;
import app.Portfolio;
import app.User;

import java.util.Scanner;

import static app.Iu.getActiveUser;
import static app.Iu.getUserList;
import static app.actions.ShowUsersList.showUsersList;

//View user portfolio - showUserPortfolio

public class showUserPortfolio implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) {
        if (command == 7) {
            showUserPortfolio(sc);
        }
    }

    private static void showUserPortfolio(Scanner sc) {
        String username;

        sc.nextLine();
        showUsersList();
        System.out.print("Enter username: ");
        username = sc.nextLine();

        if (username.length() < 3)
            username = getActiveUser().getUserName();

        for (User user : getUserList()) {
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
