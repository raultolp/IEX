package app;

import java.util.*;

public class Iu {
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) {

        //------------------------------------
        //TESTING STOCK:
//        String symbol="AAPL";
//
//        Stock aapl=new Stock("AAPL");
//
//        System.out.println(aapl.getCompanyName());
//
//        double aaplLatestPrice=aapl.getLatestPrice();
//        System.out.println("AAPL current price: " +aaplLatestPrice);
//
//        //Testing historical info for dates:
//        Map<String, Double []> aaplHistorical=aapl.getHistoricalPrices("1m");
//        Double [] aaplPriceAndVolume=aaplHistorical.get("2018-04-06");
//        double aaplPrice=aaplPriceAndVolume[0];
//        double aaplVolume=aaplPriceAndVolume[1];
//        System.out.println("AAPL price at 2018-04-06: "+aaplPrice+", volume: "+aaplVolume+" (volume in millions)");
//
//        //Testing historical info for minutes (in case of 1-day chart):
//        Map<String, Double []> aaplIntraday=aapl.getHistoricalPrices("1d");
//        Double [] aaplPriceAndVolume2=aaplIntraday.get("09:32");
//        double aaplPrice2=aaplPriceAndVolume2[0];
//        double aaplVolume2=aaplPriceAndVolume2[1];
//        System.out.println("AAPL price at 09:32: "+aaplPrice2+", volume: "+aaplVolume2+" (volume in thousands)");


        //------------------------------------

        final String[] mainMenu = {"Add user",
                "Delete user",
                "List users",
                "Set active user",
                "Buy stock",
                "Sell stock",
                "View user portfolio",
                "View available stock list",
                "View stock data base data",
                "View stock historical data graph",
                "View all portfolios progress graph",
                "Refresh data from web",
                "Load data file",
                "Save data file",
                "Quit"};

        final String[] availableStocks = {"AAPL", "AMZN", "AMD", "BA", "BABA", "BAC", "BBY", "BIDU",
                "C", "CAT", "COST", "CRM", "CSCO", "DE", "FSLR", "GE", "GM", "GME", "GOOG", "GS",
                "HD", "HLF", "HPE", "HPQ", "HTZ", "IBM", "INTC", "JAZZ", "JCP", "JNJ", "JNPR", "JPM",
                "K", "KO", "LMT", "LOGI", "MA", "MCD", "MMM", "MS", "MSFT", "NFLX", "NKE", "NTAP",
                "NTNX", "NVDA", "ORCL", "P", "PEP", "PG", "QCOM", "RHT", "SBUX", "SINA", "SSYS", "STX",
                "SYMC", "TGT", "TIF", "TRIP", "TSLA", "TWTR", "TXN", "UA", "UAL", "V", "VMW", "VNET",
                "WDX", "WFC", "WFM", "WHR", "WMT", "X", "XONE", "YELP", "ZG"};

        List<User> userList = new ArrayList<>();
        List<Portfolio> portfolioList = new ArrayList<>();
        List<Stock> stockList = new ArrayList<>();

        User admin = new User("admin", new Portfolio(), 0);
        User active = admin;
        boolean quitProgram = false;

        Scanner sc = new Scanner(System.in);

        while(!quitProgram) {
            String name;
            int index;
            int choice;

            printMenu(mainMenu);
            System.out.print("Active:" + ANSI_GREEN + active.getUserName() + ANSI_RESET + "> ");

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
                    if (name.length() < 1 || nameInList(name, userList) > -1) {
                        System.out.println("Name already exists!");
                    } else {
                        userList.add(new User(name, new Portfolio(), 100000));
                        System.out.println("Created user: " + name);
                    }
                    break;
                //Delete user
                case 2:
                    name = enterUserName(sc);
                    index = nameInList(name, userList);
                    if (index > -1) {
                        userList.remove(index);
                        System.out.println("User " + name + " has been deleted.");
                    }

                    break;
                //List users
                case 3:
                    showUsersList(userList);
                    break;
                //Set active user
                case 4:
                    showUsersList(userList);
                    name = enterUserName(sc);
                    index = nameInList(name, userList);
                    if (index > -1) {
                        active = userList.get(index);
                        System.out.println("User " + name + " is now active.");
                    }
                    break;

                //Buy stock
                case 5:
                    showStockList(availableStocks);
                    //List of available stocks
                    break;
                //Sell stock"
                case 6:
                    break;
                //View user portfolio
                case 7:
                    break;
                //View available stock list
                case 8:
                    showStockList(availableStocks);
                    break;
                //View stock data base data
                case 9:
                    break;
                //View stock historical data graph
                case 10:
                    break;
                //View all portfolios progress graph"
                case 11:
                    break;
                //Refresh data from web
                case 12:
                    break;
                //Load data file
                case 13:
                    break;
                //Save data file
                case 14:
                    break;
                //Quit
                case 15:
//                    saveData();
                    quitProgram = true;
                    System.out.println("Bye-bye!");
                    break;
                default:
                    System.out.println("Wrong input, choose between 1.." + mainMenu.length + "!");
            }
        }
    }

    private static void printMenu(String[] menu) {
        System.out.println();
        for (int i = 0; i < menu.length / 2 + menu.length % 2; i++) {
            int next =  menu.length / 2 + i + menu.length % 2;
            System.out.printf("%2d) %-30s ", i + 1, menu[i]);
            if (next < menu.length)
                System.out.printf("%2d) %-30s\n", next + 1, menu[next]);
            else
                System.out.println();
        }
    }

    private static String enterUserName(Scanner sc) {
        String name;
        do {
            System.out.print("Enter user name: ");
            name = sc.next();
            if (name.length() < 3 || name.length() > 12 || !isAlphaNumeric(name))
                System.out.println("Use name with 3..12 characters and numbers.");
            System.out.println(name.length());
        } while (name.length() < 3 || name.length() > 12 || !isAlphaNumeric(name));

        return name;
    }

    private static int nameInList(String name, List<User> userList) {
        for (User user: userList) {
            if (user.getUserName().equals(name))
                return userList.indexOf(user);
        }
        return -1;
    }

    private static void showUsersList(List<User> userList) {
        System.out.println("Defined users:");
        if (userList.size() > 0) {
            for (User item : userList) {
                System.out.printf("%-12s%s", item.getUserName(), (userList.indexOf(item) + 1) % 6 == 0 ? "\n" : " ");
            }
        } else
            System.out.println("None");
    }

    private static void showStockList(String[] stockList) {
        System.out.println("Avilable stocks:");
        int i;
        for (i = 0; i < stockList.length; i++) {
            System.out.printf("%-5s%s", stockList[i], (i + 1) % 10 == 0 ? "\n" : " ");
        }
        if ((i + 1) % 10 != 0)
            System.out.println();
    }

    private static boolean isAlphaNumeric(String text) {
        return !text.matches("^.*[^a-zA-Z0-9].*$");
    }
}