package app;

import app.actions.*;

import java.io.*;
import java.util.*;

import static app.actions.SaveData.saveData;
import static app.staticData.*;

public class Iu {

    private static List<User> userList = new ArrayList<>();
    private static List<Portfolio> portfolioList = new ArrayList<>();

    private static final User admin = new User("admin", new Portfolio(), 0);
    private static User activeUser = admin;
    private static File activeGame = null;

    private static Iu handler = new Iu();
//    private Integer command;

    private final List<CommandHandler> commandHandlers;
    private static Scanner sc = new Scanner(System.in);

    public Iu() {
        this.commandHandlers = loadCommandHandlers();
    }

    public static void main(String[] args) throws Exception {
        //START PROGRAM
        System.out.println(mainTitle);

        //HANDLE COMMANDS
        handler.runInteractive(sc);

        // QUIT PROGRAM
        if (getActiveGame() != null)
            saveData(sc);
        sc.close();

        System.out.println(ANSI_YELLOW + "Bye-bye!" + ANSI_RESET);

        //Peaks kuskil ees olema .. praegu siin
        Map<String, Stock> stockMap = new HashMap<>();

        for (String symbol : availableStocks) {
            Stock stock = new Stock(symbol);
            stockMap.put(symbol, stock);

/* VANA JA KOLE SWITCH

            switch (choice) {

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


*/
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
                new showUserPortfolio(),
                new showStockList(),
                new LoadData(), new SaveData(),
                new Quit(),
                new ErrorHandler()
        );
    }

    public void runInteractive(Scanner sc) throws Exception {

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

}