package app.actions;

import app.CommandHandler;
import app.User;

import java.util.Scanner;

import static app.Iu.*;
import static app.actions.AddUser.enterUserName;
import static app.actions.DeleteUser.nameInList;
import static app.actions.ShowUsersList.showUsersList;

public class SetActiveUser implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) throws Exception {
        if (command == 4) {
            showUsersList();
            String name = enterUserName(sc);
            Integer index = nameInList(name);
            if (index > -1) {
                User activeUser = getUserList().get(index);
                setActiveUser(activeUser);
                System.out.println(ANSI_YELLOW + "User " + name + " is now active." + ANSI_RESET);
            }
        }
    }
}
