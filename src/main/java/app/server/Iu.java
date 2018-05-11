package app.server;

import app.server.actions.*;
import com.google.gson.JsonObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static app.server.StaticData.*;


public class Iu {

    //Command handler and game
    private final List<CommandHandler> commandHandlers;
    private Iu masterHandler; //handler of admin
    private File activeGame;
    private boolean isRunning = true;
    private boolean acceptConnections;

    //User, Portfolio, Stocks
    private User admin;  // has masterPortfolio
    private User activeUser;
    private boolean isAdmin;
    private Portfolio masterPortfolio;
    private int clientId;  //for connecting users to their respective updateThreads
    private JsonObject priceUpdateForClients = null;  //masterPortfolio priceUpdate'i objekt- väärtustab UpdateingPrices,
    // kasutab ThreadForDataUpdates
    // I/O
    private Scanner sc;
    private DataInputStream in;
    private DataOutputStream out;

    //Userlist and list of user priceUpdate threads
    private List<User> userList;
    //ArrayList<Thread> priceUpdateThreadList;
    Map<Thread, User> clientThreads = new HashMap<>(); //clientId (assigned when connects to server) and client update thread
    Map<Integer, Thread> clientIds = new HashMap<>();  //clientId is assigned when user connects (its identity
    // is not verified at this point yet, so it needs a temporary id


    /***** CONSTRUCTOR FOR ADMIN *****/

    public Iu() throws IOException {

        //Command handler and game
        this.commandHandlers = loadCommandHandlers();
        this.masterHandler = this;
        this.activeGame = null;
        this.acceptConnections = true;

        //User, Portfolio, Stocks
        this.admin = new User("admin", 1000000, availableStocks);
        this.activeUser = admin;
        this.isAdmin = true;
        this.masterPortfolio = admin.getPortfolio();

        // One and Only - my precious
        this.sc = new Scanner(System.in);

        //Userlist and list of user price update threads
        this.userList = new ArrayList<>();
        //this.priceUpdateThreadList=new ArrayList<>();

    }

    /***** CONSTRUCTOR FOR USER *****/

    public Iu(DataInputStream in, DataOutputStream out, Iu masterHandler, User user, Integer clientId) throws IOException {

        //Game
        this.commandHandlers = loadCommandHandlers();
        this.masterHandler = masterHandler;
        this.activeGame = masterHandler.getActiveGame();

        //User, Portfolio, Stocks
        this.activeUser = user;
        this.isAdmin = false;
        this.masterPortfolio = masterHandler.getMasterPortfolio();  //admin's portfolio
        this.clientId = clientId;
        Thread userUpdateThread = clientIds.get(clientId);
        clientThreads.put(userUpdateThread, user);

        // I/O: DataInputStream and DataOutputStream
        this.in = in;
        this.out = out;

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

    private void printMenu(String[] menu) throws IOException {  //for admin
        if (isAdmin) {
            System.out.println();
            for (int i = 0; i < menu.length / 2 + menu.length % 2; i++) {
                int next = menu.length / 2 + i + menu.length % 2;
                System.out.printf("%2d) %-30s ", i + 1, menu[i]);
                if (next < menu.length)
                    System.out.printf("%2d) %-30s\n", next + 1, menu[next]);
                else
                    System.out.println();
            }
        } else {
            out.writeUTF("\nMENU"); // Et menüü saaks ilusti prinditud, prindib klient seda endale ise.
        }

        commandPrompt();
    }

    public void commandPrompt() throws IOException {
        if (isAdmin) {
            System.out.print((activeGame != null ? ANSI_BLUE + activeGame.getName() + ANSI_RESET :
                    ANSI_RED + "(not saved)" + ANSI_RESET) +
                    " / Active user:" + ANSI_GREEN + activeUser.getUserName() + ANSI_RESET + "> ");
        } else {
            String commandPromtAsString = "";
            if (activeGame != null) {
                commandPromtAsString += activeGame.getName() + "(not saved)";
            }
            commandPromtAsString += " / Active user:" + activeUser.getUserName() + "> ";
            out.writeUTF(commandPromtAsString);
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

    public void runInteractive(Iu handler) throws Exception {

        //TODO runInteractive

        while (true) {
            printMenu(mainMenu);

            try {
                Integer command;
                if (isAdmin) {
                    command = sc.nextInt();
                } else {
                    command = Integer.valueOf(in.readUTF());
                }

                for (CommandHandler commandHandler : commandHandlers) {
                    commandHandler.handle(command, handler);
                }

                //Quit
                if (!isRunning) {  // isRunning is set to "false" in Menu item "Quit"
                    break;
                }

            } catch (InputMismatchException e) {
                if (isAdmin) {
                    System.out.println("Wrong input: " + sc.nextLine());

                } else {
                    out.writeUTF("Wrong input.");
                }
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

    public Scanner getSc() {
        return this.sc;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public DataInputStream getIn() {
        return in;
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
