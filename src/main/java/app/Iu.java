package app;

import java.util.*;

public class Iu {
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


        final String[] mainMenu = {"\n 1 - Add user",
                " 2 - List users",
                " 3 - Set active user",
                " 4 - Buy stock",
                " 5 - Sell stock",
                " 6 - View user portfolio",
                " 7 - View stock data base data",
                " 8 - View stock historical data graph",
                " 9 - View all portfolios progress graph",
                "10 - Refresh data from web",
                "11 - Save data",
                "12 - Quit"};

        List<User> userList = new ArrayList<>();
        List<Portfolio> portfolioList = new ArrayList<>();
        List<Stock> stockList = new ArrayList<>();

        User admin = new User("admin", new Portfolio(), 0);
        User active = admin;
        boolean quitProgram = false;

        Scanner sc = new Scanner(System.in);

        while(!quitProgram) {
            int choice;
            printMenu(mainMenu);
            System.out.print("Active:" + active.getUserName() + "> ");

            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Wrong input: " + sc.nextLine());
                continue;
            }

            switch (choice) {
                case 1: String name = enterUserName(sc);
                        if (name.length() < 1 || nameInList(name, userList)) {
                            System.out.println("Name already exists!");
                        } else {
                            userList.add(new User(name, new Portfolio(), 100000));
                            System.out.println("Created user: " + name);
                        }
                    break;
                case 2:
                    System.out.println("Defined users:");
                    int count = 0;
                    for (User item: userList) {
                        System.out.print(item.getUserName() + "\t" + (count++ > 5 ? "\n" : ""));
                        if (count > 5)
                            count = 0;
                    }
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
                    break;
                case 10:
                    break;
                case 11:
                    break;
                case 12:
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
        for (String item: menu) {
            System.out.println(item);
        }
    }

    private static String enterUserName(Scanner sc) {
        String name;
        do {
            System.out.print("Enter user name: ");
            name = sc.next();
        } while (name.length() == 0 || isAlphaNumeric(name));

        return name;
    }

    private static boolean nameInList(String name, List<User> userList) {
        for (User user: userList) {
            if (user.getUserName().equals(name))
                return true;
        }
        return false;
    }

    private static boolean isAlphaNumeric(String text) {
        return text.matches("^.*[^a-zA-Z0-9 ].*$");
    }
}