package app.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;

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

    public void colorPrintBlue(String text) {
        System.out.println(ANSI_BLUE + text + ANSI_RESET);
    }

    public static String enterUserName(Iu handler, boolean newUser) throws IOException {
        String name;
        boolean isAdmin = handler.getActiveUser().getUserName().equals("admin");
        do {
            if (isAdmin) {
                System.out.print("Enter user name: ");
                name = handler.getSc().nextLine().trim();

                if (name.length() < 3 || name.length() > 12 || !isAlphaNumeric(name))
                    colorPrintRed("Use name with 3..12 characters and numbers.");
                else if (nameInList(name, handler.getUserList()) > -1 && newUser)
                    colorPrintRed("Name already exists!");
                else if (nameInList(name, handler.getUserList()) == -1 && !newUser)
                    colorPrintRed("Name does not exists!");
            } else {
                DataOutputStream out = handler.getOut();
                DataInputStream in = handler.getIn();
                out.writeUTF("Enter user name: ");
                name = in.readUTF().trim();

                if (name.length() < 3 || name.length() > 12 || !isAlphaNumeric(name))
                    out.writeUTF("Use name with 3..12 characters and numbers.");
                else if (nameInList(name, handler.getUserList()) > -1 && newUser)
                    out.writeUTF("Name already exists!");
                else if (nameInList(name, handler.getUserList()) == -1 && !newUser)
                    out.writeUTF("Name does not exists!");
            }

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

    public static String enterStockName(Iu handler) throws IOException {
        String name;
        boolean isAdmin = handler.isAdmin();

        if (isAdmin) {
            handler.getSc().nextLine();
            do {
                System.out.print("Enter stock name: ");
                name = handler.getSc().next().trim();
                if (name.length() < 1 || name.length() > 5 || !isAlpha(name))
                    colorPrintRed("Choose right stock name.");
                if (name.length() == 0)
                    return null;
            } while (name.length() > 5 || !isAlpha(name));
        } else {
            DataOutputStream out = handler.getOut();
            DataInputStream in = handler.getIn();
            do {
                out.writeUTF("Enter stock name: ");
                name = in.readUTF().trim();
                if (name.length() < 1 || name.length() > 5 || !isAlpha(name))
                    out.writeUTF("Choose right stock name.");
                if (name.length() == 0)
                    return null;
            } while (name.length() > 5 || !isAlpha(name));
        }

        return name.toUpperCase();
    }

    public static int enterQty(Iu handler) throws IOException {
        int qty = 0;
        boolean isAdmin = handler.isAdmin();

        if (isAdmin) {
            handler.getSc().nextLine();

            do {
                System.out.print("Enter quantity [1-1000]: ");
                try {
                    qty = handler.getSc().nextInt();
                } catch (InputMismatchException e) {
                    colorPrintRed("Wrong input: " + handler.getSc().nextLine());
                }
            } while (qty < 1 || qty > 1000);
        } else {
            DataOutputStream out = handler.getOut();
            DataInputStream in = handler.getIn();

            do {
                out.writeUTF("Enter quantity [1-1000]: ");
                try {
                    qty = Integer.valueOf(in.readUTF());
                } catch (InputMismatchException e) {
                    out.writeUTF("Wrong input. ");
                }
            } while (qty < 1 || qty > 1000);
        }

        return qty;
    }

    public static void listFiles(Iu handler) throws IOException {
        File folder = new File(".");
        File[] files = folder.listFiles();
        int i = 1;
        boolean isAdmin = handler.isAdmin();

        for (File file : files) {
            if (file.getName().endsWith(".game")) {

                if (!isAdmin) {
                    if (i++ % 4 == 0) {
                        handler.getOut().writeUTF(file.getName() + "\n");
                    } else {
                        handler.getOut().writeUTF(file.getName() + " ");
                    }
                } else {
                    System.out.printf("%15s%s", file.getName(), i++ % 4 == 0 ? "\n" : " ");
                }
            }

        }
        if (!isAdmin && (i - 1) % 4 != 0)
            handler.getOut().writeUTF("\n");
        else if (isAdmin && (i - 1) % 4 != 0) {
            System.out.println();
        }
    }

    public static String createHeader(String header) {
        return createSeparator(header.length()) + header + '\n' + createSeparator(header.length());
    }

    public static String createSeparator(int length) {
        return String.format("%0" + length + "d", 0).replace('0', '-') + '\n';
    }
}
