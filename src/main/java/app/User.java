package app;


import java.util.List;

public class User {
    private String userName;
    private Portfolio portfolio;
    private double initialFunds;

    public User(String userName, double availableFunds) {
        this.userName = userName;
        this.portfolio=new Portfolio(availableFunds);
        this.initialFunds=availableFunds;
        //this.portfolio = portfolio;
        //this.availableFunds = availableFunds;
        //portfolio.setAvailableFunds(availableFunds);
        //portfolio.setUser(this);
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

/*    public void setAvailableFunds(double availableFunds) {  //kas vajalik?
        //this.availableFunds = availableFunds;
        portfolio.setAvailableFunds(availableFunds);
    }*/

    public double getAvailableFunds() {  //kas vajalik?
        return portfolio.getAvailableFunds();
    }

    public String getUserName() {
        return userName;
    }

    //TODO: (PRIORITY 1- USE THE FOLLOWING 2 GETTERS FOR DRAWING UP TOP LIST OF PLAYERS AND FINDING THE WINNER:
    public double getPortfolioTotalValue(){
        return portfolio.getTotalValueOfPortfolio();
    }

    public double increaseAsPercentage(){ //increase in portfolio value from the beginning of the game
        return (getPortfolioTotalValue()-initialFunds)/initialFunds;
    }
}
