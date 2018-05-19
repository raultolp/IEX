package app.server.actions;

import app.server.CommandHandler;
import app.server.IO;
import app.server.Iu;

import java.io.IOException;
import java.util.Arrays;

//View available stock list - showStockList

public class ShowStockList implements CommandHandler {

    @Override
    public void handle(String command, Iu handler, IO io) throws IOException {
        if (command.equals("5")) {
            showStockList(handler, io);
        }
    }

    public static void showStockList(Iu handler, IO io) throws IOException {
        boolean isAdmin = handler.isAdmin();
        Arrays.sort(handler.getAvailableStocks());
        int i;

        if (isAdmin) {
            System.out.println("Avilable stocks:");
            for (i = 0; i < handler.getAvailableStocks().length; i++) {
                System.out.printf("%-5s%s", handler.getAvailableStocks()[i], (i + 1) % 10 == 0 ? "\n" : " ");
            }
            if (i % 10 != 0)
                System.out.println();
        } else {
            String stocksAsString = "Avilable stocks: " + String.join(", ", handler.getAvailableStocks());
            io.println(stocksAsString);

        }
    }
}