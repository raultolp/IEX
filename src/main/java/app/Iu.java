package app;

import app.actions.*;

import java.io.*;
import java.util.*;

//import static app.actions.SaveData.saveData; //HETKEL EI TÖÖTA
import static app.StaticData.*;

public class Iu {
    //User, Portfolio, Stocks
    private static List<User> userList = new ArrayList<>();
    private static List<Portfolio> portfolioList = new ArrayList<>();
    private static Map<String, Stock> stockMap = new HashMap<>();

    //Base user and game
    private static final User admin = new User("admin", 100000);
    private static User activeUser = admin;
    private static File activeGame = null;

    //Command handler
    private static Iu handler = new Iu();
    private final List<CommandHandler> commandHandlers;
    //    private Integer command;
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
            //saveData(sc); //HETKEL EI TÖÖTA
            sc.close();

        System.out.println(ANSI_YELLOW + "Bye-bye!" + ANSI_RESET);

        //Peaks kuskil ees olema .. praegu siin
        for (String symbol : availableStocks) {
            Stock stock = new Stock(symbol);
            stockMap.put(symbol, stock);

        }
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
        commandPrompt();
    }

    public static void commandPrompt() {
        System.out.print((activeGame != null ? ANSI_BLUE + activeGame.getName() + ANSI_RESET :
                ANSI_RED + "(not saved)" + ANSI_RESET) +
                " / Active user:" + ANSI_GREEN + activeUser.getUserName() + ANSI_RESET + "> ");
    }

    private List<CommandHandler> loadCommandHandlers() {
        return Arrays.asList(
                new AddUser(),
                new DeleteUser(),
                new ShowUsersList(),
                new SetActiveUser(),
                new SellStock(),
                new BuyStock(),
                new ShowUserPortfolio(),
                new ShowUserTransactions(),
                new ShowStockList(),
                new ShowStockListBaseData(),
                new ShowStockBaseData(),
                new ShowStockHistoricalData(),
                new ShowStockNews(),
                new ShowPortfoliosProgress(),
/*                new RefreshDataFromWeb(),  //HETKEL EI TÖÖTA  NEED KOLM
                new LoadData(),
                new SaveData(),*/
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


    public static File getActiveGame() {
        return activeGame;
    }

    public static void setActiveGame(File activeGame) {
        Iu.activeGame = activeGame;
    }

    public static Map<String, Stock> getStockMap() {
        return stockMap;
    }
}
