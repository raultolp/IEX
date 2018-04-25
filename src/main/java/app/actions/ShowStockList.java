package app.actions;

import app.CommandHandler;
import app.Iu;

import java.util.Arrays;

//View available stock list - showStockList

public class ShowStockList implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) {
        if (command == 9) {
            showStockList(handler);
        }
    }

    public static void showStockList(Iu handler) {
        System.out.println("Avilable stocks:");
        Arrays.sort(handler.getAvailableStocks());
        int i;
        for (i = 0; i < handler.getAvailableStocks().length; i++) {
            System.out.printf("%-5s%s", handler.getAvailableStocks()[i], (i + 1) % 10 == 0 ? "\n" : " ");
        }
        if (i % 10 != 0)
            System.out.println();
    }
}