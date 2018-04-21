package app.actions;

import app.CommandHandler;
import app.User;

import java.util.Scanner;

import static app.Iu.*;
import static app.actions.AddUser.enterUserName;

public class DeleteUser implements CommandHandler {
    //private Scanner sc = new Scanner(System.in);
    Scanner sc = getSc();

    @Override
    public void handle(Integer command) throws Exception {
        if (command == 2) {
            String name = enterUserName(sc);
            Integer index = nameInList(name);
            if (index > -1) {
                getUserList().remove(index); //TODO ???
                System.out.println(ANSI_YELLOW + "User " + name + " has been deleted." + ANSI_RESET);
            }
        }
        //sc.close();
    }


    public static int nameInList(String name) {
        for (User user : getUserList()) {
            if (user.getUserName().equals(name))
                return getUserList().indexOf(user);
        }
        return -1;
    }
}