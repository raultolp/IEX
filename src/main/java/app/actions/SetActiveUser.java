package app.actions;

import app.CommandHandler;
import app.User;

import java.util.Scanner;

import static app.Iu.*;
import static app.actions.AddUser.enterUserName;
import static app.actions.DeleteUser.nameInList;
import static app.actions.ShowUsersList.showUsersList;
import static app.StaticData.ANSI_RESET;
import static app.StaticData.ANSI_YELLOW;

//Set activeUser user

public class SetActiveUser implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) throws Exception {
        if (command == 4) {
            showUsersList();
            sc.nextLine();
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
