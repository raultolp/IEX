package app;

import java.io.File;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static app.StaticData.*;

public final class MyUtils {

    public static boolean isAlphaNumeric(String text) {
        return text.matches("[a-zA-Z0-9]+");
    }

    public static boolean isAlpha(String text) {
        return text.matches("[a-zA-Z]+");
    }

    public boolean isNumeric(String text) {
        return text.matches("[0-9]+");
    }

    public static void colorPrintYellow(String text) {
        System.out.println(ANSI_YELLOW + text + ANSI_RESET);
    }

    public static void colorPrintRed(String text) {
        System.out.println(ANSI_RED + text + ANSI_RESET);
    }

    public void colorPrintGreen(String text) {
        System.out.println(ANSI_RED + text + ANSI_RESET);
    }

    public void colorPrintBlue(String text) {
        System.out.println(ANSI_BLUE + text + ANSI_RESET);
    }

    public static String enterUserName(Iu handler, boolean newUser) {
        String name;
        do {
            System.out.print("Enter user name: ");
            name = handler.getSc().nextLine().trim();

            if (name.length() < 3 || name.length() > 12 || !isAlphaNumeric(name))
                colorPrintRed("Use name with 3..12 characters and numbers.");
            else if (nameInList(name, handler.getUserList()) > -1 && newUser)
                colorPrintRed("Name already exists!");
            else if (nameInList(name, handler.getUserList()) == -1 && !newUser)
                colorPrintRed("Name does not exists!");

            if (name.length() == 0)
                return null;
        } while (name.length() < 3 || name.length() > 12 || !isAlphaNumeric(name));

        return name;
    }

    public static int nameInList(String name, List<User> userList) {
        for (User user : userList) {
            if (user.getUserName().equals(name))
                return userList.indexOf(user);
        }
        return -1;
    }

    public static String enterStockName(Scanner sc) {
        String name;
        sc.nextLine();
        do {
            System.out.print("Enter stock name: ");
            name = sc.next().trim();
            if (name.length() < 1 || name.length() > 5 || !isAlpha(name))
                colorPrintRed("Choose right stock name.");
            if (name.length() == 0)
                return null;
        } while (name.length() > 5 || !isAlpha(name));

        return name.toUpperCase();
    }

    public static int enterQty(Scanner sc) {
        int qty = 0;
        sc.nextLine();

        do {
            System.out.print("Enter quantity [1-1000]: ");
            try {
                qty = sc.nextInt();
            } catch (InputMismatchException e) {
                colorPrintRed("Wrong input: " + sc.nextLine());
            }
        } while (qty < 1 || qty > 1000);

        return qty;
    }

    public static void listFiles() {
        File folder = new File(".");
        File[] files = folder.listFiles();
        int i = 1;
        for (File file : files) {
            if (file.getName().endsWith(".game"))
                System.out.printf("%15s%s", file.getName(), i++ % 4 == 0 ? "\n" : " ");
        }
        if ((i - 1) % 4 != 0)
            System.out.println();
    }

    public static String createHeader(String header) {
        return createSeparator(header.length()) + header + '\n' + createSeparator(header.length());
    }

    public static String createSeparator(int length) {
        return String.format("%0" + length + "d", 0).replace('0', '-') + '\n';
    }
}
