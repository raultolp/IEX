package app.actions;

import app.CommandHandler;
import app.Iu;
import app.User;

import java.util.Scanner;

public class ShowUsersList implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) throws Exception {
        if (command == 3)
            showUsersList();
    }

    public static void showUsersList() {
        System.out.println("Defined users:");
        if (Iu.getUserList().size() > 0) {
            for (User item : Iu.getUserList()) {
                System.out.printf("%-12s%s", item.getUserName(), (Iu.getUserList().indexOf(item) + 1) % 6 == 0 ? "\n" : " ");
            }
        } else
            System.out.println("None");
        System.out.println();
    }
}