package app.actions;

import app.CommandHandler;
import app.Iu;
import app.Portfolio;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

import static app.Iu.*;

public class BuyStock implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) throws Exception {
        if (command == 5) {
            showStockList();
//            sc.nextLine();
            String name = enterStockName(sc);

            if (Arrays.asList(Iu.getAvailableStocks()).contains(name)) {
                Integer qty = enterQty(sc);
                Portfolio portfolio = getActiveUser().getPortfolio();

                try {
                    portfolio.buyStock(name, qty);
                } catch (RuntimeException e) { //if not enough funds
                    System.out.println("Not enough funds!");
                }
            } else
                System.out.println(ANSI_RED + "This stock is not available." + ANSI_RESET);
        }
        // sc.close();
    }

    public static String enterStockName(Scanner sc) {
        String name;
        sc.nextLine();
        do {
            System.out.print("Enter stock name: ");
            name = sc.next().trim();
            if (name.length() < 1 || name.length() > 5 || !isAlpha(name))
                System.out.println(ANSI_RED + "Choose right stock name." + ANSI_RESET);
            if (name.length() == 0)
                return null;
        } while (name.length() > 5 || !isAlpha(name));

        return name.toUpperCase();
    }

    public static void showStockList() {
        System.out.println("Avilable stocks:");
        Arrays.sort(getAvailableStocks());
        int i;
        for (i = 0; i < getAvailableStocks().length; i++) {
            System.out.printf("%-5s%s", getAvailableStocks()[i], (i + 1) % 10 == 0 ? "\n" : " ");
        }
        if (i % 10 != 0)
            System.out.println();
    }

    public static int enterQty(Scanner sc) {
        int qty = 0;
        sc.nextLine();

        do {
            System.out.print("Enter quantity [1-1000]: ");
            try {
                qty = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Wrong input: " + sc.nextLine());
            }
        } while (qty < 1 || qty > 1000);

        return qty;
    }
}
