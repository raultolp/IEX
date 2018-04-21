package app.actions;


import app.CommandHandler;
import app.Portfolio;
import app.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static app.Iu.*;
import static app.actions.DeleteUser.nameInList;

public class AddUser implements CommandHandler {
    private Scanner sc = new Scanner(System.in);

    @Override
    public void handle(Integer command) throws Exception {
        List<User> newUserList = new ArrayList<>();

        if (command == 1) {
            String name = enterUserName(sc);
            if (name != null) {
                newUserList = getUserList();
                newUserList.add(new User(name, new Portfolio(), 100000));
                System.out.println(ANSI_YELLOW + "Created user: " + name + ANSI_RESET);
            }
            setUserList(newUserList);

        }
        sc.close();
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