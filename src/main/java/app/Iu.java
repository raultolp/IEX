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
                " 2 - Delete user",
                " 3 - List users",
                " 4 - Set active user",
                " 5 - Buy stock",
                " 6 - Sell stock",
                " 7 - View user portfolio",
                " 8 - View stock data base data",
                " 9 - View stock historical data graph",
                "10 - View all portfolios progress graph",
                "11 - Refresh data from web",
                "12 - Load data file",
                "13 - Save data file",
                "14 - Quit"};

        List<User> userList = new ArrayList<>();
        List<Portfolio> portfolioList = new ArrayList<>();
        List<Stock> stockList = new ArrayList<>();

        User admin = new User("admin", new Portfolio(), 0);
        User active = admin;
        boolean quitProgram = false;

        Scanner sc = new Scanner(System.in);

        while(!quitProgram) {
            int choice;
            String name;

            printMenu(mainMenu);
            System.out.print("Active:" + active.getUserName() + "> ");

            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Wrong input: " + sc.nextLine());
                continue;
            }

            switch (choice) {
                // 1 - Add user
                case 1:
                    name = enterUserName(sc);
                    if (name.length() < 1 || nameInList(name, userList) > -1) {
                        System.out.println("Name already exists!");
                    } else {
                        userList.add(new User(name, new Portfolio(), 100000));
                        System.out.println("Created user: " + name);
                    }
                    break;
                //2 - Delete user
                case 2:
                    name = enterUserName(sc);
                    int index = nameInList(name, userList);
                    if (index > -1) {
                        userList.remove(index);
                        System.out.println("User " + name + " has been deleted.");
                    }

                    break;
                //3 - List users
                case 3:
                    System.out.println("Defined users:");
                    int count = 0;
                    for (User item: userList) {
                        System.out.printf("%-12s%s", item.getUserName(), (count++ > 5 ? "\n" : " "));
                        if (count > 6)
                            count = 0;
                    }
                    break;
                //4 - Set active user
                case 4:
                    break;
                //5 - Buy stock
                case 5:
                    break;
                //6 - Sell stock"
                case 6:
                    break;
                //7 - View user portfolio
                case 7:
                    break;
                //8 - View stock data base data
                case 8:
                    break;
                //9 - View stock historical data graph
                case 9:
                    break;
                //10 - View all portfolios progress graph"
                case 10:
                    break;
                //11 - Refresh data from web
                case 11:
                    break;
                //12 - Load data file
                case 12:
                    break;
                //13 - Save data file
                case 13:
                    break;
                //14 - Quit
                case 14:
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
            if (name.length() < 3 || name.length() > 12 || !isAlphaNumeric(name))
                System.out.println("Use name with 3..12 characters and numbers.");
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

    private static boolean isAlphaNumeric(String text) {
        return !text.matches("^.*[^a-zA-Z0-9].*$");
    }
}