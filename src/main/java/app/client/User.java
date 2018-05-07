package app.client;


import com.google.gson.JsonObject;

public class User implements Comparable<User> {
    private String userName;
    private Portfolio portfolio;
    private double initialFunds;
    private double portfolioTotalValue;

/*    //Creating normal users:
    public User(String userName, double availableFunds) throws IOException {
        this.userName = userName;
        this.portfolioTotalValue = availableFunds;
        this.initialFunds = availableFunds;
        this.portfolio = new Portfolio(availableFunds);

        //Portfolio for normal users:
        if (!userName.equals("admin")) {
            this.portfolio = new Portfolio(availableFunds);
        }
    }*/


    //For initiating user from JSON:
    public User(String userName, JsonObject userObj, Portfolio masterPortfolio) {
        this.userName = userName;
        this.portfolioTotalValue = userObj.get("portfolioTotalValue").getAsDouble();
        this.initialFunds = userObj.get("initialFunds").getAsDouble();

        //Initiating user portfolio:
        JsonObject portfObj = userObj.get("portfolio").getAsJsonObject();
        this.portfolio = new Portfolio(portfObj, masterPortfolio);
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

    public double getPortfolioTotalValue() {
        return portfolioTotalValue;
    }

    public void setPortfolioTotalValue() {
        this.portfolioTotalValue = portfolio.getTotalValueOfPortfolio();
    }

    public double getPercentageIncrease() { //increase in portfolio value from the beginning of the game (%)
        return ((portfolioTotalValue - initialFunds) / initialFunds) * 100;
    }

    public double getValueIncrease() { //increase in portfolio value from the beginning of the game (USD)
        return portfolioTotalValue - initialFunds;
    }

    @Override
    public int compareTo(User otherUser) {
        return Double.compare(otherUser.getPortfolioTotalValue(), getPortfolioTotalValue());
    }

}
