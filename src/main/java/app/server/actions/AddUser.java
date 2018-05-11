package app.server.actions;

import app.server.CommandHandler;
import app.server.Iu;
import app.server.MyUtils;
import app.server.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Add user

public class AddUser implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws Exception {
        if (command == 16) {
            boolean isAdmin = handler.isAdmin();

            if (isAdmin) {
                addUser(handler);
            } else {
                handler.getOut().writeUTF("Wrong input.");
            }
        }
    }

    public static void addUser(Iu handler) throws IOException {
        List<User> newUserList = new ArrayList<>();
        boolean isAdmin = handler.isAdmin();

        if (isAdmin) {
            handler.getSc().nextLine();
        }
        String name = MyUtils.enterUserName(handler, true);

        if (name != null) {
            newUserList = handler.getUserList();
            newUserList.add(new User(name, 100000));
            if (isAdmin) {
                MyUtils.colorPrintYellow("Created user: " + name);
            } else {
                handler.getOut().writeUTF("Created user: " + name);
            }

        }
        handler.setUserList(newUserList);
    }
}


