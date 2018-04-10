package app;

import java.io.*;
import java.util.*;

public class Iu {
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    private static List<User> userList = new ArrayList<>();
    private static List<Portfolio> portfolioList = new ArrayList<>();

    private static final User admin = new User("admin", new Portfolio(), 0);
    private static User activeUser = admin;
    private static File activeGame = null;


    private static final String[] availableStocks = {"AAPL", "AMZN", "CSCO", "F", "GE", "GM", "GOOG",
            "HPE", "IBM", "INTC", "JNJ", "K", "KO", "MCD", "MSFT", "NFLX", "NKE", "PEP", "PG", "SBUX",
            "TSLA", "TWTR", "V", "WMT"};

    //ORIGINAL LIST
//    private static final String[] availableStocks = {"AAPL", "AMZN", "AMD", "BA", "BABA", "BAC", "BBY", "BIDU",
//            "C", "CAT", "COST", "CRM", "CSCO", "DE", "F", "FSLR", "GE", "GM", "GME", "GOOG", "GS",
//            "HD", "HLF", "HPE", "HPQ", "HTZ", "IBM", "INTC", "JAZZ", "JCP", "JNJ", "JNPR", "JPM",
//            "K", "KO", "LMT", "LOGI", "MA", "MCD", "MMM", "MS", "MSFT", "NFLX", "NKE", "NTAP",
//            "NTNX", "NVDA", "ORCL", "P", "PEP", "PG", "QCOM", "RHT", "SBUX", "SINA", "SSYS", "STX",
//            "SYMC", "TGT", "TIF", "TRIP", "TSLA", "TWTR", "TXN", "UA", "UAL", "V", "VMW", "VNET",
//            "WDX", "WFC", "WFM", "WHR", "WMT", "X", "XONE", "YELP", "ZG"};

    public static void main(String[] args) throws IOException {

        final String[] mainMenu = {"Add user",
                "Delete user",
                "List users",
                "Set active user",
                "Buy stock",
                "Sell stock",
                "View user portfolio",
                "View available stock list",
                "View stock list base data",
                "View stock base data",
                "View stock historical data",
                "View all portfolios performance",
                "Refresh data from web",
                "Load data file",
                "Save data file",
                "Quit"};

        System.out.println("\n+++ BÖRSIMÄNG +++\n\nLoading stock data from web ...\n");

        Map<String, Stock> stockMap = new HashMap<>();

        for (String symbol : availableStocks) {
            Stock stock = new Stock(symbol);
            stockMap.put(symbol, stock);
        }

        boolean quitProgram = false;

        while (!quitProgram) {
            String name;
            int index;
            int choice;
            int qty;
            Scanner sc = new Scanner(System.in);

            printMenu(mainMenu);
            System.out.print((activeGame != null ? ANSI_BLUE + activeGame.getName() + ANSI_RESET :
                    ANSI_RED + "(not saved)" + ANSI_RESET) +
                    " / Active user:" + ANSI_GREEN + activeUser.getUserName() + ANSI_RESET + "> ");

            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Wrong input: " + sc.nextLine());
                continue;
            }

            switch (choice) {

                //Add user
                case 1:
                    name = enterUserName(sc);
                    if (name != null) {
                        userList.add(new User(name, new Portfolio(), 100000));
                        System.out.println(ANSI_YELLOW + "Created user: " + name + ANSI_RESET);
                    }
                    break;

                //Delete user
                case 2:
                    name = enterUserName(sc);
                    index = nameInList(name);
                    if (index > -1) {
                        userList.remove(index);
                        System.out.println(ANSI_YELLOW + "User " + name + " has been deleted." + ANSI_RESET);
                    }
                    break;

                //List users
                case 3:
                    showUsersList();
                    break;

                //Set activeUser user
                case 4:
                    showUsersList();
                    name = enterUserName(sc);
                    index = nameInList(name);
                    if (index > -1) {
                        activeUser = userList.get(index);
                        System.out.println(ANSI_YELLOW + "User " + name + " is now active." + ANSI_RESET);
                    }
                    break;

                //Buy stock
                case 5:
                    showStockList();
                    name = enterStockName(sc);

                    if (Arrays.asList(availableStocks).contains(name)) {
                        qty = enterQty(sc);
                        Portfolio portfolio = activeUser.getPortfolio();

                        try {
                            portfolio.buyStock(name, qty);
                        } catch (RuntimeException e) { //if not enough funds
                            System.out.println("Not enough funds!");
                        }
                    } else
                        System.out.println(ANSI_RED + "This stock is not available." + ANSI_RESET);
                    break;

                //Sell stock
                case 6:
                    Portfolio portfolio = activeUser.getPortfolio();
                    if (portfolio != null)
                        portfolio.toString();
                    name = enterStockName(sc);

                    if (portfolio.getSymbolList().contains(name)) {
                        qty = enterQty(sc);
                        portfolio.sellStock(name, qty);
                    } else {
                        System.out.println("Stock not included in portfolio!");
                    }
                    break;

                //View user portfolio
                case 7:
                    showUserPortfolio(sc);
                    break;

                //View available stock list
                case 8:
                    showStockList();
                    break;
                //View stock list base data
                case 9:
                    System.out.println("Tuleb hiljem");
                    break;

                //View stock base data
                case 10:
                    showStockBaseData(sc);
                    break;

                //View stock historical data
                case 11:
                    showStockHistoricalData(sc);
                    break;

                //View all portfolios progress
                case 12:
                    System.out.println("Tuleb hiljem");
                    break;

                //Refresh data from web (refreshes stock prices in stockMap)
                case 13:
                    for (String symbol : stockMap.keySet()) {
                        Stock stock = stockMap.get(symbol);
                        double price = stock.getLatestPrice();
                        stock.setCurrentPrice(price);
                    }
                    Portfolio portf=activeUser.getPortfolio();
                    portf.updatePrices();
                    break;

                //Load data file
                case 14:
                    loadData(sc);
                    break;

                //Save data file
                case 15:
                    saveData(sc);
                    break;

                //Quit
                case 16:
                    if (activeGame != null)
                        saveData(sc);
                    quitProgram = true;
                    System.out.println(ANSI_YELLOW + "Bye-bye!" + ANSI_RESET);
                    sc.close();
                    break;

                default:
                    System.out.println(ANSI_RED + "Wrong input, choose between 1.." + mainMenu.length + "!" + ANSI_RESET);
            } //switch menu options
        } //main menu endless loop
    }

    private static void printMenu(String[] menu) {
        System.out.println();
        for (int i = 0; i < menu.length / 2 + menu.length % 2; i++) {
            int next = menu.length / 2 + i + menu.length % 2;
            System.out.printf("%2d) %-30s ", i + 1, menu[i]);
            if (next < menu.length)
                System.out.printf("%2d) %-30s\n", next + 1, menu[next]);
            else
                System.out.println();
        }
    }

    private static String enterUserName(Scanner sc) {
        String name;
        sc.nextLine();
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

    private static String enterStockName(Scanner sc) {
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

    private static int enterQty(Scanner sc) {
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

    private static int nameInList(String name) {
        for (User user : userList) {
            if (user.getUserName().equals(name))
                return userList.indexOf(user);
        }
        return -1;
    }

    private static void showUsersList() {
        System.out.println("Defined users:");
        if (userList.size() > 0) {
            for (User item : userList) {
                System.out.printf("%-12s%s", item.getUserName(), (userList.indexOf(item) + 1) % 6 == 0 ? "\n" : " ");
            }
        } else
            System.out.println("None");
        System.out.println();
    }

    private static void showStockList() {
        System.out.println("Avilable stocks:");
        Arrays.sort(availableStocks);
        int i;
        for (i = 0; i < availableStocks.length; i++) {
            System.out.printf("%-5s%s", availableStocks[i], (i + 1) % 10 == 0 ? "\n" : " ");
        }
        if (i % 10 != 0)
            System.out.println();
    }

    private static void saveData(Scanner sc) throws IOException {

        File file;
        sc.nextLine();

        if (activeGame == null) {
            System.out.print("Enter filename to save data: ");
            String filename = sc.nextLine();

            if (!filename.endsWith(".game"))
                filename += ".game";

            file = new File(filename);
        } else {
            file = activeGame;
        }

        file.createNewFile();

        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        for (User user : userList) {
            writer.write(user.getPortfolio().toStringForFile());
        }
        writer.close();
        activeGame = file;
        System.out.println(ANSI_YELLOW + activeGame.getName() + " file saved." + ANSI_RESET);
    }

    public static void loadData(Scanner sc) throws IOException {
        double availableFunds = 0.0;
        double transactionFee = 0.1;
        double totalUnrealisedProfitOrLoss = 0.0;
        double totalProfitOrLoss = 0.0;
        double totalCurrentValueOfPositions = 0.0;
        List<Double> unrealisedProfitsLosses = new ArrayList<>();

        listFiles();
        sc.nextLine();

        System.out.print("Enter filename: ");
        String filename = sc.nextLine();

        if (!filename.endsWith(".game"))
            filename += ".game";

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
                activeGame = file;
                System.out.println(ANSI_YELLOW + activeGame.getName() + " file loaded." + ANSI_RESET);
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

    private static void showUserPortfolio(Scanner sc) {

        String username;
        User userToView;

        if (activeUser.equals("admin")){
            userToView=activeUser;
            sc.nextLine();
            showUsersList();
            System.out.print("Enter username: ");
            username = sc.nextLine();

            if (!userList.contains(username)) {
                return;
            }

            for(User user : userList) {
                if (user.getUserName().equals(username)) {
                    userToView=user;
                }
            }
        } else {
            userToView=activeUser;
        }

        Portfolio portfolio = userToView.getPortfolio();
        System.out.print("\nAvailable cash: ");
        System.out.printf("%.2f", userToView.getAvailableFunds());
        System.out.println("\n"+portfolio.toString());
        System.out.print("Portfolio total value: ");
        System.out.printf("%.2f", portfolio.getTotalValue());
        System.out.println();
    }


    private static void showStockBaseData(Scanner sc) {

        sc.nextLine();
        System.out.println("Enter stock symbol: ");
        String stockSym = sc.nextLine();

        try {
            System.out.println(new Stock(stockSym));
        } catch (Exception e) {
            System.out.println("Stock information not available.");
        }
    }

    private static void showStockHistoricalData(Scanner sc) {

        //TODO: add option to choose time period

        sc.nextLine();
        System.out.println("Enter stock symbol: ");
        String stockSym = sc.nextLine();

        try {
            Stock stock = new Stock(stockSym);
            System.out.println("One month: " + stock.getChange1Month() + '\n' +
                    "Three months: " + stock.getChange3Month() + '\n' +
                    "Year: " + stock.getChange1Year());
        } catch (Exception e) {
            System.out.println("Stock info not available.");
        }
    }


    private static boolean isAlphaNumeric(String text) {
        return text.matches("[a-zA-Z0-9]+");
    }

    private static boolean isAlpha(String text) {
        return text.matches("[a-zA-Z]+");
    }

    private static boolean isNumeric(String text) {
        return text.matches("[0-9]+");
    }
}