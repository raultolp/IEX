package app;

import app.actions.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static app.StaticData.*;

//import static app.actions.SaveData.saveData; //HETKEL EI TÖÖTA

public class IuTest {

    /***** PAHAD JA KOLEDAD *****/

    //User, Portfolio, Stocks
    private List<User> userList;
    private List<Portfolio> portfolioList;
    private Portfolio masterPortfolio;

    //Command handler
    private final List<CommandHandler> commandHandlers;
    private Scanner sc;

    //Base user and game
    private User admin;
    private User activeUser;
    private File activeGame;

    private IuTest() throws IOException {
        this.commandHandlers = loadCommandHandlers();
        boolean createMaster = createMasterPortfolio();

        //User, Portfolio, Stocks
        this.userList = new ArrayList<>();
        this.portfolioList = new ArrayList<>();
        this.masterPortfolio = null;

        // One and Only - my precious
        this.sc = new Scanner(System.in);

        //Base user and game
//        this.admin = new User("admin", 1000000); // teeme hetkel createMaster all
        this.activeUser = admin;
        this.activeGame = null;

        //Mingi kahtlase väärtusega - don't blame me, code analysis ütles
        if (!createMaster) {
            return; //peaks väljuma programmist (?)
        }
    }

    /***** MAIN START HERE *****/

    public static void main(String[] args) throws Exception {

        //Command handler
        IuTest handler = new IuTest();

        //Run IEX data collector
        Thread t2 = new Thread(new UpdatingPrices());
        t2.start();

        //START PROGRAM
        System.out.println(mainTitle);

        //HANDLE COMMANDS
        handler.runInteractive(handler.sc);

        // QUIT PROGRAM
        if (handler.activeGame != null)
            //saveData(sc); //HETKEL EI TÖÖTA
            handler.sc.close();

        System.out.println(ANSI_YELLOW + "Bye-bye!" + ANSI_RESET);

/*        //Peaks kuskil ees olema .. praegu siin
        for (String symbol : availableStocks) {
            Stock stock = new Stock(symbol);
            stockMap.put(symbol, stock);

        }*/
    }

    /***** CREATE ADMIN AND MASTER PORTFOLIO *****/

    public boolean createMasterPortfolio(){
        System.out.println("Downloading stock data from web...");
        try {
            admin = new User("admin", 1000000);
            activeUser = admin;
            masterPortfolio = admin.getPortfolio();
        } catch (IOException e) {
            System.out.println("Connection to IEX failed. Try again (Y/N)?");
            String answer = sc.nextLine().trim().toUpperCase();
            while(true) {
                if (!answer.equals("N") && !answer.equals("Y")) {
                    System.out.println("Wrong input. Try again (Y/N)?");
                } else {
                    break;
                }
            }
            if (answer.equals("N")){
                return false;
            } else {
                createMasterPortfolio();  //tries again to download data
            }
        }
        return true;
    }

    /***** MAIN MENU *****/

    private void printMenu(String[] menu) {
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

    public void commandPrompt() {
        System.out.print((activeGame != null ? ANSI_BLUE + activeGame.getName() + ANSI_RESET :
                ANSI_RED + "(not saved)" + ANSI_RESET) +
                " / Active user:" + ANSI_GREEN + activeUser.getUserName() + ANSI_RESET + "> ");
    }

    /***** MENU HANDLER COMMANDS LIST *****/

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

    /***** RUN HANDLER, RUN *****/

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

    public boolean isAlphaNumeric(String text) {
        return text.matches("[a-zA-Z0-9]+");
    }

    public boolean isAlpha(String text) {
        return text.matches("[a-zA-Z]+");
    }

    public boolean isNumeric(String text) {
        return text.matches("[0-9]+");
    }

    public String[] getAvailableStocks() {
        return availableStocks;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<Portfolio> getPortfolioList() {
        return portfolioList;
    }

    public User getAdmin() {
        return this.admin;
    }

    public User getActiveUser() {
        return this.activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }

    public File getActiveGame() {
        return this.activeGame;
    }

    public void setActiveGame(File activeGame) {
        this.activeGame = activeGame;
    }

/*    public Map<String, Stock> getStockMap() {
        return stockMap;
    }*/

    public Portfolio getMasterPortfolio(){
        return masterPortfolio;
    }

    public Scanner getSc() {
        return sc;
    }
}
