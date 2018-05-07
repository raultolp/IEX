package app.server.actions;

import app.server.CommandHandler;
import app.server.Iu;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

//View available stock list - showStockList

public class ShowStockList implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws IOException {
        if (command == 5) {
            showStockList(handler);
        }
    }

    public static void showStockList(Iu handler) throws IOException {
        boolean isAdmin = handler.isAdmin();
        DataOutputStream out = handler.getOut();
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
            out.writeUTF(stocksAsString);


        }

    }
}