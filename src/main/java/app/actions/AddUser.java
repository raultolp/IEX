package app.actions;

import app.CommandHandler;
import app.Portfolio;
import app.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static app.Iu.*;
import static app.actions.DeleteUser.nameInList;
import static app.staticData.ANSI_RED;
import static app.staticData.ANSI_RESET;
import static app.staticData.ANSI_YELLOW;

//Add user - AddUser, enterUserName

public class AddUser implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) throws Exception {
        List<User> newUserList = new ArrayList<>();

        if (command == 1) {
            sc.nextLine();
            String name = enterUserName(sc);
            if (name != null) {
                newUserList = getUserList();
                newUserList.add(new User(name, new Portfolio(), 100000));
                System.out.println(ANSI_YELLOW + "Created user: " + name + ANSI_RESET);
            }
            setUserList(newUserList);
        }
    }

    public static String enterUserName(Scanner sc) {
        String name;
        do {
            System.out.print("Enter user name: ");
            name = sc.nextLine().trim();
            if (name.length() < 3 || name.length() > 12 || !isAlphaNumeric(name))
                System.out.println(ANSI_RED + "Use name with 3..12 characters and numbers." + ANSI_RESET);
            else if (nameInList(name) > -1) {
                System.out.println(ANSI_RED + "Name already exists!" + ANSI_RESET);
            }
            if (name.length() == 0)
                return null;
        } while (name.length() < 3 || name.length() > 12 || !isAlphaNumeric(name));

        return name;
    }
}