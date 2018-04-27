package app;


import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;
import static app.StaticData.availableStocks;

public class User implements Comparable<User> {
    private String userName;
    private Portfolio portfolio;
    private double initialFunds;
    private Iu handler;
    private double portfolioTotalValue;

    public User(String userName, double availableFunds, Iu handler) throws IOException {
        this.userName = userName;
        this.portfolioTotalValue = availableFunds;
        this.initialFunds = availableFunds;
        this.handler = handler;

        if (userName.equals("admin")){
            this.portfolio = new Portfolio(handler.getAvailableStocks(), availableFunds, handler); // MasterPorfolio for admin
        } else {
            this.portfolio = new Portfolio(availableFunds, handler);
        }
    }

    //For initiating user from JSON:
    public User(String userName, JsonObject userObj, Iu handler) {
        this.userName = userName;
        this.portfolioTotalValue = userObj.get("portfolioTotalValue").getAsDouble();
        this.initialFunds = userObj.get("initialFunds").getAsDouble();
        this.handler = handler;

        //Initiating user portfolio:
        JsonObject portfObj = userObj.get("portfolio").getAsJsonObject();
        this.portfolio = new Portfolio(portfObj, handler);
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

    public double getAvailableFunds() {  //kas vajalik?
        return portfolio.getAvailableFunds();
    }

    public String getUserName() {
        return userName;
    }

    //TODO: (PRIORITY 1- USE THE FOLLOWING 2 GETTERS FOR DRAWING UP TOP LIST OF PLAYERS AND FINDING THE WINNER:
    public double getPortfolioTotalValue() {
        return portfolioTotalValue;
    }

    public void setPortfolioTotalValue() {
        this.portfolioTotalValue = portfolio.getTotalValueOfPortfolio();
        ;
    }

    public double getPercentageIncrease() { //increase in portfolio value from the beginning of the game
        return (portfolioTotalValue - initialFunds) / initialFunds;
    }

    @Override
    public int compareTo(User otherUser) {
        return Double.compare(otherUser.getPortfolioTotalValue(), getPortfolioTotalValue());
    }

}
