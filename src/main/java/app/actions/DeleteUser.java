package app.actions;

import app.CommandHandler;
import app.User;

import java.util.Scanner;

import static app.Iu.*;
import static app.actions.AddUser.enterUserName;
import static app.staticData.ANSI_RESET;
import static app.staticData.ANSI_YELLOW;

//Delete user - DeleteUser, nameInList

public class DeleteUser implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) throws Exception {
        if (command == 2) {
            sc.nextLine();
            String name = enterUserName(sc);
            Integer index = nameInList(name);
            if (index > -1) {
                getUserList().remove(index); //TODO ???
                System.out.println(ANSI_YELLOW + "User " + name + " has been deleted." + ANSI_RESET);
            }
        }
    }

    public static int nameInList(String name) {
        for (User user : getUserList()) {
            if (user.getUserName().equals(name))
                return getUserList().indexOf(user);
        }
        return -1;
    }
}