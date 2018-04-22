package app;

import app.actions.*;

import java.io.*;
import java.util.*;

import static app.actions.SaveData.saveData;
import static app.actions.ShowUsersList.showUsersList;

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

    private static Iu handler = new Iu();
    private Integer command;

    private final List<CommandHandler> commandHandlers;

    public static Scanner sc = new Scanner(System.in);

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

    private static final String[] mainMenu = {"Add user",
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


    public Iu() {
        this.commandHandlers = loadCommandHandlers();
    }

    public static void main(String[] args) throws Exception {
        //START PROGRAM
        System.out.println("\n+++ BÖRSIMÄNG +++\n\nLoading stock data from web ...\n");

        //HANDLE COMMANDS
        handler.runInteractive(sc);

        // QUIT PROGRAM
        if (getActiveGame() != null)
            saveData(sc);
        sc.close();

        System.out.println(ANSI_YELLOW + "Bye-bye!" + ANSI_RESET);

        //Peaks kuskil ees olema
        Map<String, Stock> stockMap = new HashMap<>();

        for (String symbol : availableStocks) {
            Stock stock = new Stock(symbol);
            stockMap.put(symbol, stock);




           /* switch (choice) {

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
                    Portfolio portf = activeUser.getPortfolio();
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
                    System.out.println(ANSI_RED + "Wrong input, choose between 1.." + mainMenu.length + "!" + ANSI_RESET);*/
        } //switch menu options
    } //main menu endless loop
    //}

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
        commandPrompt();
    }

    public static void commandPrompt() {
        System.out.print((activeGame != null ? ANSI_BLUE + activeGame.getName() + ANSI_RESET :
                ANSI_RED + "(not saved)" + ANSI_RESET) +
                " / Active user:" + ANSI_GREEN + activeUser.getUserName() + ANSI_RESET + "> ");
    }

    private static void showUserPortfolio(Scanner sc) {
        String username;

        sc.nextLine();
        showUsersList();
        System.out.print("Enter username: ");
        username = sc.nextLine();

        if (username.length() < 3)
            username = activeUser.getUserName();

        for (User user : userList) {
            if (user.getUserName().equals(username)) {
                Portfolio portfolio = user.getPortfolio();
                System.out.print("\nAvailable cash: ");
                System.out.printf("%.2f", user.getAvailableFunds());
                System.out.println("\n" + portfolio.toString());
                System.out.print("Portfolio total value: ");
                System.out.printf("%.2f", portfolio.getTotalValue());
                System.out.println();
            }
        }
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

    private List<CommandHandler> loadCommandHandlers() {
        return Arrays.asList(
                new AddUser(), new DeleteUser(),
                new ShowUsersList(), new SetActiveUser(),
                new SellStock(), new BuyStock(),
                new LoadData(), new SaveData(),
                new Quit(),
                new ErrorHandler()
        );
    }

    public void runInteractive(Scanner sc) throws Exception {
        boolean quitProgram = false;

//        printMenu(mainMenu);

        //TODO runInteractive

        while (true) {
            printMenu(mainMenu);

            try {
                Integer command = sc.nextInt();

                for (CommandHandler commandHandler : commandHandlers) {
                    commandHandler.handle(command, sc);
                }

                //Quit
                if (command == getMainMenuSize())
                    break;

            } catch (InputMismatchException e) {
                System.out.println("Wrong input: " + sc.nextLine());
            }
        }
    }

    public static boolean isAlphaNumeric(String text) {
        return text.matches("[a-zA-Z0-9]+");
    }

    public static boolean isAlpha(String text) {
        return text.matches("[a-zA-Z]+");
    }

    public static boolean isNumeric(String text) {
        return text.matches("[0-9]+");
    }

    public static String[] getAvailableStocks() {
        return availableStocks;
    }

    public static List<User> getUserList() {
        return userList;
    }

    public static void setUserList(List<User> userList) {
        Iu.userList = userList;
    }

    public static List<Portfolio> getPortfolioList() {
        return portfolioList;
    }

    public static User getAdmin() {
        return admin;
    }

    public static User getActiveUser() {
        return activeUser;
    }

    public static void setActiveUser(User activeUser) {
        Iu.activeUser = activeUser;
    }

//    public static Scanner getSc() {
//        return sc;
//    }

    public static File getActiveGame() {
        return activeGame;
    }

    public static void setActiveGame(File activeGame) {
        Iu.activeGame = activeGame;
    }

    public static int getMainMenuSize() {
        return mainMenu.length;
    }
}