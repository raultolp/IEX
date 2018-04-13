package app;

public class User {
    private String userName;
    private Portfolio portfolio;
    private double availableFunds;

    public User(String userName, Portfolio portfolio, double availableFunds) {
        this.userName = userName;
        this.portfolio = portfolio;
        this.availableFunds = availableFunds;
        portfolio.setAvailableFunds(availableFunds);
        portfolio.setUser(this);
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setAvailableFunds(double availableFunds) {
        this.availableFunds = availableFunds;
    }

    public double getAvailableFunds() {
        return availableFunds;
    }

    public String getUserName() {
        return userName;
    }
}
