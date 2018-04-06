package app;

public class Stock {

    private final String companyName;
    private final String ticker;
    private double curretPrice;
    private double initialPrice;
    private double finalPrice;

    public Stock(String companyName, String ticker, double price) {
        this.companyName = companyName;
        this.ticker = ticker;
        this.initialPrice = initialPrice;
    }



    public String getCompanyName() {
        return companyName;
    }

    public String getTicker() {
        return ticker;
    }


    public void setCurretPrice(double curretPrice) {
        this.curretPrice = curretPrice;
    }

    public void setInitialPrice(double initialPrice) {
        this.initialPrice = initialPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }


}
