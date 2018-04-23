package app.actions;

import app.CommandHandler;

import java.util.Arrays;
import java.util.Scanner;

import static app.Iu.getAvailableStocks;

//View available stock list - showStockList

public class ShowStockList implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) {
        if (command == 9) {
            showStockList();
        }
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
}