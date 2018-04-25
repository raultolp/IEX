package app.actions;

import app.CommandHandler;
import app.Iu;
import app.User;

import java.util.List;

//List users

public class ShowUsersList implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) {
        if (command == 3)
            showUsersList(handler.getUserList());
    }

    public static void showUsersList(List<User> userList) {
        System.out.println("Defined users:");
        if (userList.size() > 0) {
            for (User item : userList) {
                System.out.printf("%-12s%s", item.getUserName(), (userList.indexOf(item) + 1) % 6 == 0 ? "\n" : " ");
            }
        } else
            System.out.println("None");
        System.out.println();
    }
}