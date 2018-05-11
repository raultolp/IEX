package app.server.actions;

import app.server.CommandHandler;
import app.server.Iu;
import app.server.User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

//List users

public class ShowUsersList implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws IOException {
        if (command == 17) {
            boolean isAdmin = handler.isAdmin();

            if (isAdmin) {
                showUsersList(handler);
            } else {
                handler.getOut().writeUTF("Wrong input.");
            }
        }


    }

    public static void showUsersList(Iu handler) throws IOException {
        List<User> userList = handler.getUserList();
        boolean isAdmin = handler.isAdmin();

        if (isAdmin) {
            System.out.println("Defined users: ");
            if (userList.size() > 0) {
                for (User item : userList) {
                    System.out.printf("%-12s%s", item.getUserName(), (userList.indexOf(item) + 1) % 6 == 0 ? "\n" : " ");
                }
            } else
                System.out.println("None");
            System.out.println();
        } else {
            DataOutputStream out = handler.getOut();
            String users = "Defined users: ";
            if (userList.size() > 0) {
                for (User item : userList) {
                    users += item.getUserName() + "\t";
                }
            } else {
                users += "None";
            }
            out.writeUTF(users + "\n");
        }
    }
}