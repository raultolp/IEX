package app.server;


import com.google.gson.JsonObject;

import java.io.IOException;

public class User implements Comparable<User> {
    private final String userName;
    private Portfolio portfolio;
    private final double initialFunds;
    private double portfolioTotalValue;

    //Creating normal users:
    public User(String userName, double availableFunds) {
        this.userName = userName;
        this.portfolioTotalValue = availableFunds;
        this.initialFunds = availableFunds;
        this.portfolio = new Portfolio(availableFunds);

        //Portfolio for normal users:
        if (!userName.equals("admin")) {
            this.portfolio = new Portfolio(availableFunds);
        }
    }

    //Creating admin:
    public User(String userName, double availableFunds, String[] availableStocks) throws IOException {
        this(userName, availableFunds);  //uses other constructor (for creating normal users)

        //Portfolio for admin:
        this.portfolio = new Portfolio(availableStocks, availableFunds); // MasterPorfolio for admin
    }

    //For initiating user from JSON:
    public User(String userName, JsonObject userObj, Portfolio masterPortfolio) {
        this.userName = userName;
        this.portfolioTotalValue = userObj.get("portfolioTotalValue").getAsDouble();
        this.initialFunds = userObj.get("initialFunds").getAsDouble();

        //Initiating user portfolio:
        JsonObject portfObj = userObj.get("portfolio").getAsJsonObject();
        this.portfolio = new Portfolio(portfObj, masterPortfolio);
    }


    public JsonObject covertToJson() {
        JsonObject userfObj = new JsonObject();
        userfObj.addProperty("initialFunds", initialFunds);
        userfObj.addProperty("portfolioTotalValue", portfolioTotalValue);

        //Adding user portfolio:
        JsonObject portfObj = portfolio.covertToJson();
        userfObj.add("portfolio", portfObj);

        return userfObj;
    }


    public Portfolio getPortfolio() {
        return portfolio;
    }

// --Commented out by Inspection START (19.05.2018 6:48):
//    public double getAvailableFunds() {  //kas vajalik?
//        return portfolio.getAvailableFunds();
//    }
// --Commented out by Inspection STOP (19.05.2018 6:48)

    public String getUserName() {
        return userName;
    }

    public double getPortfolioTotalValue() {
        return portfolioTotalValue;
    }

    public void setPortfolioTotalValue() {
        this.portfolioTotalValue = portfolio.getTotalValueOfPortfolio();
    }

    public double getPercentageIncrease() { //increase in portfolio value from the beginning of the game (%)
        return (getValueIncrease() / initialFunds) * 100;
    }

    public double getValueIncrease() { //increase in portfolio value from the beginning of the game (USD)
        return portfolio.getProfit() + portfolio.getUnrealisedProfit();
    }

    @Override
    public int compareTo(User otherUser) {
        return Double.compare(otherUser.getPortfolioTotalValue(), getPortfolioTotalValue());
    }

}
