package app.server;

import app.server.actions.*;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static app.server.StaticData.*;


public class Iu {

    //Command handler and game
    private final List<CommandHandler> commandHandlers;
    private final Iu masterHandler; //handler of admin
    private File activeGame;
    private boolean isRunning = true;
    private boolean acceptConnections;


    //User, Portfolio, Stocks
    private User admin;  // has masterPortfolio
    private User activeUser;
    private boolean isAdmin;
    private Portfolio masterPortfolio;
    private JsonObject priceUpdateForClients = null;  //masterPortfolio priceUpdate'i objekt- väärtustab UpdateingPrices,
    // kasutab ThreadForDataUpdates
    // I/O
    private final IO io;

    //Userlist and list of user priceUpdate threads
    private List<User> userList;
    //ArrayList<Thread> priceUpdateThreadList;
    private final Map<Thread, User> clientThreads = new HashMap<>(); //clientId (assigned when connects to server) and client update thread
    private final Map<Integer, Thread> clientIds = new HashMap<>();  //clientId is assigned when user connects (its identity
    // is not verified at this point yet, so it needs a temporary id


    /***** CONSTRUCTOR FOR ADMIN *****/

    public Iu(IO io) throws IOException {

        //Command handler and game
        this.commandHandlers = loadCommandHandlers();
        this.masterHandler = this;
        this.activeGame = null;
        this.acceptConnections = true;

        //I/O streams
        this.io = io;

        //User, Portfolio, Stocks
        this.admin = new User("admin", 1000000, availableStocks);
        this.activeUser = admin;
        this.isAdmin = true;
        this.masterPortfolio = admin.getPortfolio();


        //Userlist and list of user price update threads
        this.userList = new ArrayList<>();
        //this.priceUpdateThreadList=new ArrayList<>();

    }

    /***** CONSTRUCTOR FOR USER *****/

    //TODO delete?
    public Iu(IO io, Iu masterHandler, User user, Integer clientId) {

        //Game
        this.commandHandlers = loadCommandHandlers();
        this.masterHandler = masterHandler;
        this.activeGame = masterHandler.getActiveGame();

        //User, Portfolio, Stocks
        this.activeUser = user;
        this.isAdmin = false;
        this.masterPortfolio = masterHandler.getMasterPortfolio();  //admin's portfolio
        //for connecting users to their respective updateThreads
//        int clientId1 = clientId;
        Thread userUpdateThread = clientIds.get(clientId);
        clientThreads.put(userUpdateThread, user);

        // I/O: DataInputStream and DataOutputStream
        this.io = io;

        //Userlist - masterHandler's userlist is updated when new user is created
        //this.userList=masterHandler.getUserList(); //või siis võiks listi ainult selle ühe kliendi panna?
        this.userList = new ArrayList<>();
        userList.add(user);
        List<User> newUserList = masterHandler.getUserList();
        if (!newUserList.contains(user)) {  //lisan useri admini userlisti, kui ta ei ole veel seal
            newUserList.add(user);
            masterHandler.setUserList(newUserList);
        }
    }


    /***** MAIN MENU *****/

    private void printMenu() throws IOException {  //for admin
        if (isAdmin) {
            System.out.println();
            for (int i = 0; i < StaticData.mainMenu.length / 2 + StaticData.mainMenu.length % 2; i++) {
                int next = StaticData.mainMenu.length / 2 + i + StaticData.mainMenu.length % 2;
                System.out.printf("%2d) %-30s ", i + 1, StaticData.mainMenu[i]);
                if (next < StaticData.mainMenu.length)
                    System.out.printf("%2d) %-30s\n", next + 1, StaticData.mainMenu[next]);
                else
                    System.out.println();
            }
        } else {
            io.println("\nMENU"); // Et menüü saaks ilusti prinditud, prindib klient seda endale ise.
        }

        commandPrompt();
    }

    private void commandPrompt() throws IOException {
        if (isAdmin) {
            System.out.print((activeGame != null ? ANSI_BLUE + activeGame.getName() + ANSI_RESET :
                    ANSI_RED + "(not saved)" + ANSI_RESET) +
                    " / Active user:" + ANSI_GREEN + activeUser.getUserName() + ANSI_RESET + "> ");
        } else {
            String commandPromtAsString = "";
            if (activeGame != null) {
                commandPromtAsString += activeGame.getName() + "(not saved) / Active user:";
            }
            commandPromtAsString += ANSI_GREEN + activeUser.getUserName() + ANSI_RESET + "> ";
            io.println(commandPromtAsString);
        }
    }


    /***** MENU HANDLER COMMANDS LIST *****/

    private List<CommandHandler> loadCommandHandlers() {
        return Arrays.asList(
                new BuyStock(),
                new SellStock(),
                new ShowUserPortfolio(),
                new ShowUserTransactions(),
                new ShowStockList(),
                new ShowStockListBaseData(),
                new ShowCompanyData(),
                new ShowStockBaseData(),
                new ShowStockHistoricalData(),
                new ShowStockNews(),
                new ShowGameTopList(),
                new DeleteUser(),
                new Quit(),
                new AcceptClientConnections(),
                new StopAcceptingClientConnections(),
                new AddUser(),
                new ShowUsersList(),
                new SetActiveUser(),
                new LoadData(),
                new SaveData(),
                new ErrorHandler()
        );
    }


    /***** RUN HANDLER, RUN *****/

    public void runInteractive(IO io) throws Exception {

        while (true) {
            printMenu();

            try {
                Integer command;
                command = Integer.valueOf(io.getln());

                for (CommandHandler commandHandler : commandHandlers) {
                    commandHandler.handle(command, this, io);
                }

                //Quit
                if (!isRunning) {  // isRunning is set to "false" in Menu item "Quit"
                    break;
                }

            } catch (InputMismatchException e) {
                io.println("Wrong input.");
            }
        }
    }


    public String[] getAvailableStocks() {
        if (isAdmin) {
            return availableStocks;
        } else {
            return masterHandler.getAvailableStocks();
        }

    }

    public List<User> getUserList() {
        if (isAdmin) {
            return userList;
        } else {
            return masterHandler.getUserList();
        }
    }

    public void setUserList(List<User> userList) {
        if (this.equals(masterHandler)) {
            this.userList = userList;
        } else {
            masterHandler.setUserList(userList);
        }
    }

    public void addClientUpdateThread(Integer clientId, Thread clientThread) {
        clientIds.put(clientId, clientThread);
    }

    public Map<Thread, User> getClientThreads() {
        return clientThreads;
    }

    public JsonObject getPriceUpdateForClients() {
        return priceUpdateForClients;
    }

    public void setPriceUpdateForClients(JsonObject priceUpdateForClients) {
        this.priceUpdateForClients = priceUpdateForClients;
    }

    public User getAdmin() {
        return admin;
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

    public Portfolio getMasterPortfolio() {
        return this.masterPortfolio;
    }

    public IO getIo() {
        return io;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean doesAcceptConnections() {
        return acceptConnections;
    }

    public void setAcceptConnections(boolean acceptConnections) {
        this.acceptConnections = acceptConnections;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
