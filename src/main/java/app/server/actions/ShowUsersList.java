package app.server.actions;

import app.server.CommandHandler;
import app.server.IO;
import app.server.Iu;
import app.server.User;

import java.io.IOException;
import java.util.List;

//List users

public class ShowUsersList implements CommandHandler {

    @Override
    public void handle(String command, Iu handler, IO io) throws IOException {
        if (command.equals("17")) {
            boolean isAdmin = handler.isAdmin();

            if (isAdmin) {
                showUsersList(handler, io);
            } else {
                io.println("Wrong input.");
            }
        }


    }

    public static void showUsersList(Iu handler, IO io) throws IOException {
        List<User> userList = handler.getUserList();

        StringBuilder users = new StringBuilder("Defined users: ");
        if (userList.size() > 0) {
            for (User item : userList) {
                users.append(item.getUserName()).append("\t");
            }
        } else {
            users.append("None");
        }
        io.println(users + "\n");
    }
}
