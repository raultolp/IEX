package app;


import java.io.IOException;
import java.util.List;
import static app.StaticData.availableStocks;

public class User {
    private String userName;
    private Portfolio portfolio;
    private double initialFunds;
    private Iu handler;

    public User(String userName, double availableFunds, Iu handler) throws IOException {
        this.userName = userName;
        this.initialFunds = availableFunds;

        if (userName.equals("admin")){
            this.portfolio=new Portfolio(availableStocks, availableFunds, handler); // MasterPorfolio for admin
        } else {
            this.portfolio = new Portfolio(availableFunds, handler);
        }
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
        return portfolio.getTotalValueOfPortfolio();
    }

    public double increaseAsPercentage() { //increase in portfolio value from the beginning of the game
        return (getPortfolioTotalValue() - initialFunds) / initialFunds;
    }
}
