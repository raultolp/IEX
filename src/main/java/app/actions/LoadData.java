/*
package app.actions;

import app.CommandHandler;
import app.Portfolio;
import app.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static app.Iu.*;
import static app.StaticData.ANSI_RESET;
import static app.StaticData.ANSI_YELLOW;

//Load data file - loadData, listFiles

public class LoadData implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) throws Exception {
        if (command == 16) {
            loadData(sc);
        }
    }

    public static void loadData(Scanner sc) throws IOException {
        double availableFunds = 0.0;
        double transactionFee = 0.1;
        double totalUnrealisedProfitOrLoss = 0.0;
        double totalProfitOrLoss = 0.0;
        double totalCurrentValueOfPositions = 0.0;
        List<User> userList = getUserList();
        List<Portfolio> portfolioList = getPortfolioList();

        listFiles();
        sc.nextLine();

        System.out.print("Enter filename: ");
        String filename = sc.nextLine();

        // if (!filename.endsWith(".game"))
        //   filename += ".game";

        File file = new File(filename);

        if (file.exists()) {
            try ( BufferedReader br = new BufferedReader(new FileReader(file)) ) {
                userList.clear();
                portfolioList.clear();
                String line;
                //reads all lines from file and adds users and portfolios to their respective arraylists

                while ((line = br.readLine()) != null) {

                    String[] elements = line.split(";");

                    String[] syms = elements[2].split(",");
                    List<String> symbols = new ArrayList<>();
                    for (String sym : syms) {
                        if (!(sym.equals("[]")))
                            symbols.add(sym.replaceAll("[\\W]", ""));
                        else symbols.add("");
                    }

                    String[] price = elements[3].split(",");
                    List<Double> prices = new ArrayList<>();
                    for (String pri : price) {
                        if (!(pri.equals("[]")))
                            prices.add(Double.parseDouble(pri.replaceAll("[\\W]", "")));
                    }

                    String[] vols = elements[4].split(",");
                    List<Integer> volumes = new ArrayList<>();
                    for (String number : vols) {
                        if (!(number.equals("[]")))
                            volumes.add(Integer.parseInt(number.replaceAll("[\\W]", "")));
                    }

                    String[] avgPrc = elements[5].split(",");
                    List<Double> averagePrices = new ArrayList<>();
                    for (String avg : avgPrc) {
                        if (!(avg.equals("[]")))
                            averagePrices.add(Double.parseDouble(avg.replaceAll("[\\W]", "")));
                    }

                    String[] profLoss = elements[6].split(",");
                    List<Double> profitsOrLosses = new ArrayList<>();
                    for (String profL : profLoss) {
                        if (!(profL.equals("[]")))
                            profitsOrLosses.add(Double.parseDouble(profL.replaceAll("[\\W]", "")));
                    }

                    String[] unreals = elements[7].split(",");
                    List<Double> unrealisedProfitsOrLosses = new ArrayList<>();
                    for (String pl : unreals) {
                        if (!(pl.equals("[]")))
                            unrealisedProfitsOrLosses.add(Double.parseDouble(pl.replaceAll("[\\W]", "")));
                    }

                    String[] currs = elements[8].split(",");
                    List<Double> currentValuesOfPositions = new ArrayList<>();
                    for (String cr : currs) {
                        if (!(cr.equals("[]")))
                            currentValuesOfPositions.add(Double.parseDouble(cr.replaceAll("[\\W]", "")));
                        else
                            currentValuesOfPositions.add(0.0);
                    }

                    if (!(elements[1].equals("[]")))
                        availableFunds = Double.parseDouble(elements[1]);

                    if (!(elements[9].equals("[]")))
                        totalCurrentValueOfPositions = Double.parseDouble(elements[9]);

                    if (!(elements[10].equals("[]")))
                        totalProfitOrLoss = Double.parseDouble(elements[10]);

                    if (!(elements[11].equals("[]")))
                        totalUnrealisedProfitOrLoss = Double.parseDouble(elements[11]);

                    if (!(elements[12].equals("[]")))
                        transactionFee = Double.parseDouble(elements[12]);


                    User user = new User(elements[0], new Portfolio(), Double.parseDouble(elements[1]));


                    Portfolio port = new Portfolio(availableFunds, user,
                            symbols, prices, volumes, averagePrices, profitsOrLosses, unrealisedProfitsOrLosses,
                            currentValuesOfPositions, totalCurrentValueOfPositions, totalProfitOrLoss,
                            totalUnrealisedProfitOrLoss, transactionFee);

                    User newUser = new User(elements[0], port, Double.parseDouble(elements[1]));

                    portfolioList.add(port);
                    userList.add(newUser);
                }
            } finally {
                setActiveGame(file);
                System.out.println(ANSI_YELLOW + getActiveGame().getName() + " file loaded." + ANSI_RESET);
            }
        }
    }

    private static void listFiles() {
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
}
*/
