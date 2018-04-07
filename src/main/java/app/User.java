package app;

public class User {
    private String userName;
    private Portfolio portfolio;
    private double cashmoneybiatcheees;

    public User(String userName, Portfolio portfolio, double cashmoneybiatcheees) {
        this.userName = userName;
        this.portfolio = portfolio;
        this.cashmoneybiatcheees = cashmoneybiatcheees;
    }

    public void setCashmoneybiatcheees(double cashmoneybiatcheees) {
        this.cashmoneybiatcheees = cashmoneybiatcheees;
    }

    public String getUserName() {
        return userName;
    }
}
