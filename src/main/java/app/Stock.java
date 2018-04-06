package app;

public class Stock {

    private final String companyName;
    private final String ticker; //aktsia sümbol
    private double bid; // parim ostuhind (alumine hind)
    private double ask; // parim müügihind (ülemine hind)
    //private double curretPrice;  //hetke hind
    //private double initialPrice;
    //private double finalPrice;

    //TODO:
    //P/E, EPS, Div yield, div date
    //market cap, net income

    public Stock(String companyName, String ticker, double bid, double ask) {
        this.companyName = companyName;
        this.ticker = ticker;
        //this.initialPrice = initialPrice;
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
