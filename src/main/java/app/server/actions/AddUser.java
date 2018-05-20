package app.server.actions;

import app.server.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Add user

public class AddUser implements CommandHandler {

    @Override
    public void handle(String command, Iu handler, IO io) throws Exception {
        if (command .equals("17")) {
            boolean isAdmin = handler.isAdmin();
            if (isAdmin) {
                addUser(handler, io);
            } else {
                io.println("Wrong input.");
            }
        }
    }

    private static void addUser(Iu handler, IO io) throws IOException {
        List<User> newUserList = new ArrayList<>();

        String name = MyUtils.enterUserName(handler, true, io);

        if (name != null) {
            newUserList = handler.getUserList();
            newUserList.add(new User(name, 100000));
            io.println("Created user: " + name);
        }

        handler.setUserList(newUserList);
    }
}


