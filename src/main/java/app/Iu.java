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

    public static void main(String[] args) throws IOException {

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
        //TESTING PORTFOLIO:
        Portfolio portfell = new Portfolio();
        User pets = new User("Peeter", portfell, 1000000); //testing Pets
        portfell.buyStock("CAT", 200);
        portfell.buyStock("AAPL", 1);
        System.out.println("Total profit: " + portfell.getTotalProfitOrLoss());
        System.out.println("AAPL CEO: " + portfell.getPortfolio().get("AAPL").getCEO());


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
                "View stock historical data",
                "View all portfolios performance",
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


        List<Portfolio> portfolioList = new ArrayList<>();
        List<Stock> stockList = new ArrayList<>();


        User admin = new User("admin", new Portfolio(), 0);
        User active = admin;
        boolean quitProgram = false;

        userList.add(pets); //et saaks midagi kirjutada


        while (!quitProgram) {
            String name;
            int index;
            int choice;
            int qty;
            Scanner sc = new Scanner(System.in);


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
                    if (name != null && nameInList(name, userList) > -1) {
                        System.out.println(ANSI_RED + "Name already exists!" + ANSI_RESET);
                    } else if (name != null) {
                        userList.add(new User(name, new Portfolio(), 100000));
                        System.out.println(ANSI_YELLOW + "Created user: " + name + ANSI_RESET);
                    }
                    break;

                //Delete user
                case 2:
                    name = enterUserName(sc);
                    index = nameInList(name, userList);
                    if (index > -1) {
                        userList.remove(index);
                        System.out.println(ANSI_YELLOW + "User " + name + " has been deleted." + ANSI_RESET);
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
                        System.out.println(ANSI_YELLOW + "User " + name + " is now active." + ANSI_RESET);
                    }
                    break;

                //Buy stock
                case 5:
                    showStockList(availableStocks);
                    name = enterStockName(sc);

                    if (Arrays.asList(availableStocks).contains(name)) {
                        qty = enterQty(sc);
                        // kontrolli kas on piisavalt raha koos teenustasudega
                        // kui on, lisa portfelli ja võta raha maha
//                        active.getPortfolio().lisaAktsia(name, qty, date);
                    } else
                        System.out.println(ANSI_RED + "This stock is not available." + ANSI_RESET);
                    break;

                //Sell stock"
                case 6:
                    if (active.getPortfolio() != null)
                        active.getPortfolio().toString();

                    name = enterStockName(sc);

                    // sell name(qty)

                    System.out.println("Tuleb hiljem");
                    break;

                //View user portfolio
                case 7:
                    System.out.println("Tuleb hiljem");
                    break;

                //View available stock list
                case 8:
                    showStockList(availableStocks);
                    break;

                //View stock data base data
                case 9:
                    System.out.println("Tuleb hiljem");
                    break;

                //View stock historical data graph
                case 10:
                    System.out.println("Tuleb hiljem");
                    break;

                //View all portfolios progress graph"
                case 11:
                    System.out.println("Tuleb hiljem");
                    break;

                //Refresh data from web
                case 12:
                    System.out.println("Tuleb hiljem");
                    break;

                //Load data file
                case 13:
                    loadData(sc);
                    break;

                //Save data file
                case 14:
                    saveData(sc);
                    break;

                //Quit
                case 15:
                    //saveData(sc);
                    quitProgram = true;
                    System.out.println(ANSI_YELLOW + "Bye-bye!" + ANSI_RESET);
                    sc.close();
                    break;

                default:
                    System.out.println(ANSI_RED + "Wrong input, choose between 1.." + mainMenu.length + "!" + ANSI_RESET);
            } //switch menu options
//            sc.close();
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

    private static int nameInList(String name, List<User> userList) {
        for (User user : userList) {
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
        System.out.println();
    }

    private static void showStockList(String[] stockList) {
        System.out.println("Avilable stocks:");
        Arrays.sort(stockList);
        int i;
        for (i = 0; i < stockList.length; i++) {
            System.out.printf("%-5s%s", stockList[i], (i + 1) % 10 == 0 ? "\n" : " ");
        }
        if ((i + 1) % 10 != 0)
            System.out.println();
    }

    private static void saveData(Scanner sc) throws IOException {

        sc.nextLine();

        System.out.print("Enter filename: ");
        String filename = sc.nextLine();

        File file = new File(filename);
        file.createNewFile();

        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        for (User user : userList) {
            writer.write(user.getPortfolio().toStringForFile());
        }
        writer.close();

    }


    public static void loadData(Scanner sc) throws IOException {
        sc.nextLine();

        System.out.print("Enter filename: ");
        String filename = sc.nextLine();

        File file = new File(filename);

        if (file.exists()) {
            try ( BufferedReader br = new BufferedReader(new FileReader(file)) ) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] elements = line.split(";");

                    System.out.println(Arrays.toString(elements));

                    String[] syms = elements[3].split(",");
                    List<String> symbols = new ArrayList<>();
                    for (String sym : syms) {
                        symbols.add(sym.trim());
                    }

                    String[] price = elements[4].split(",");
                    List<Double> prices = new ArrayList<>();
                    for (String pri : price) {
                        prices.add(Double.parseDouble(pri));
                    }

                    String[] vols = elements[4].split(",");
                    List<Double> volumes = new ArrayList<>();
                    for (String number : vols) {
                        volumes.add(Double.parseDouble(number));
                    }

                    String[] avgPrc = elements[5].split(",");
                    List<Integer> averagePrices = new ArrayList<>();
                    for (String avg : avgPrc) {
                        averagePrices.add(Integer.parseInt(avg));
                    }

                    String[] profLoss = elements[6].split(",");
                    List<Double> profitsOrLosses = new ArrayList<>();
                    for (String profL : profLoss) {
                        profitsOrLosses.add(Double.parseDouble(profL));
                    }

                    String[] unreals = elements[7].split(",");
                    List<Double> unrealisedProfitsOrLosses = new ArrayList<>();
                    for (String pl : unreals) {
                        unrealisedProfitsOrLosses.add(Double.parseDouble(pl));
                    }

                    String[] currs = elements[8].split(",");
                    List<Double> currentValuesOfPositions = new ArrayList<>();
                    for (String cr : currs) {
                        currentValuesOfPositions.add(Double.parseDouble(cr));
                    }
                   /* Portfolio port = new Portfolio(Double.parseDouble(elements[1]),
                            new User(elements[0], new Portfolio(), Double.parseDouble(elements[1])),
                                    symbols, prices, volumes, averagePrices, profitsOrLosses, unrealisedProfitsOrLosses,
                                    currentValuesOfPositions, Double.parseDouble(elements[9]), Double.parseDouble(elements[10]),
                            Double.parseDouble(elements[11]));
                }*/
                }
            }
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