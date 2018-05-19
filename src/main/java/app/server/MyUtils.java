package app.server;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;

import static app.server.StaticData.*;

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

    public static void colorPrintBlue(String text) {
        System.out.println(ANSI_BLUE + text + ANSI_RESET);
    }

    public static String enterUserName(Iu handler, boolean newUser, IO io) throws IOException {
        String name;
        do {
            io.print("Enter user name: ");
            name = io.getln();

            if (name.length() < 3 || name.length() > 12 || !isAlphaNumeric(name))
                colorPrintRed("Use name with 3..12 characters and numbers.");
            else if (nameInList(name, handler.getUserList()) > -1 && newUser)
                colorPrintRed("Name already exists!");
            else if (nameInList(name, handler.getUserList()) == -1 && !newUser)
                colorPrintRed("Name does not exist!");
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

    public static String enterStockName(Iu handler, IO io) throws IOException {
        String name;
        boolean tv = false;

        do {
            io.print("Enter stock name: ");
            name = io.getln().trim();
            if (name.length() < 1 || name.length() > 5 || !isAlpha(name)) {
                tv = true;
                io.println("Choose correct stock name.");
            }
        } while (tv);

        return Objects.requireNonNull(name).toUpperCase();
    }

    public static int enterQty(IO io) throws IOException {
        int qty = 0;

        do {
            io.print("Enter quantity [1-1000]: ");
            try {
                qty = Integer.parseInt(io.getln());
            } catch (InputMismatchException e) {
                io.println("Wrong input: " + ('\n'));
            }
        } while (qty < 1 || qty > 1000);
        return qty;
    }


    public static void listFiles(Iu handler, IO io) throws IOException {
        File folder = new File("Games");
        File[] files = folder.listFiles();
        // int i = 1;
        //boolean isAdmin = handler.isAdmin();

        io.println("Available files: " + '\n');
        for (File file : Objects.requireNonNull(files)) {
            if (!(file.getName().startsWith(".")))
                io.println(file.getName());
        }
        io.println("\n");
    }

    public static String createHeader(String header) {
        return createSeparator(header.length()) + header + '\n' + createSeparator(header.length());
    }

    public static String createSeparator(int length) {
        return String.format("%0" + length + "d", 0).replace('0', '-') + '\n';
    }
}

