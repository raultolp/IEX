package app.server.TableFX_Testing;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

class TableModelMaster {

    private final SimpleStringProperty symbol;
    private SimpleDoubleProperty currentPrice;
    private SimpleDoubleProperty prevClose;
    private SimpleDoubleProperty change1Y;
    private SimpleDoubleProperty change1M;
    private SimpleDoubleProperty change3M;
    private SimpleDoubleProperty divYield;
    private SimpleDoubleProperty eps;
    private SimpleDoubleProperty peRatio;
    private SimpleIntegerProperty marketCap;
    private SimpleDoubleProperty shortRatio;


    public TableModelMaster(String symbol, double currentPrice, double prevClose, double change1Y,
                            double change1M, double change3M, double divYield, double eps,
                            double peRatio, int marketCap, double shortRatio) {
        this.symbol = new SimpleStringProperty(symbol);
        this.currentPrice = new SimpleDoubleProperty(currentPrice);
        this.prevClose = new SimpleDoubleProperty(prevClose);
        this.change1Y = new SimpleDoubleProperty(change1Y);
        this.change1M = new SimpleDoubleProperty(change1M);
        this.change3M = new SimpleDoubleProperty(change3M);
        this.divYield = new SimpleDoubleProperty(divYield);
        this.eps = new SimpleDoubleProperty(eps);
        this.peRatio = new SimpleDoubleProperty(peRatio);
        this.marketCap = new SimpleIntegerProperty(marketCap);
        this.shortRatio = new SimpleDoubleProperty(shortRatio);
    }


    public String getSymbol() {
        return symbol.get();
    }

    public SimpleStringProperty symbolProperty() {
        return symbol;
    }

    public double getCurrentPrice() {
        return currentPrice.get();
    }

    public SimpleDoubleProperty currentPriceProperty() {
        return currentPrice;
    }

    public double getPrevClose() {
        return prevClose.get();
    }

    public SimpleDoubleProperty prevCloseProperty() {
        return prevClose;
    }

    public double getChange1Y() {
        return change1Y.get();
    }

    public SimpleDoubleProperty change1YProperty() {
        return change1Y;
    }

    public double getChange1M() {
        return change1M.get();
    }

    public SimpleDoubleProperty change1MProperty() {
        return change1M;
    }

    public double getChange3M() {
        return change3M.get();
    }

    public SimpleDoubleProperty change3MProperty() {
        return change3M;
    }

    public double getDivYield() {
        return divYield.get();
    }

    public SimpleDoubleProperty divYieldProperty() {
        return divYield;
    }

    public double getEps() {
        return eps.get();
    }

    public SimpleDoubleProperty epsProperty() {
        return eps;
    }

    public double getPeRatio() {
        return peRatio.get();
    }

    public SimpleDoubleProperty peRatioProperty() {
        return peRatio;
    }

    public int getMarketCap() {
        return marketCap.get();
    }

    public SimpleIntegerProperty marketCapProperty() {
        return marketCap;
    }

    public double getShortRatio() {
        return shortRatio.get();
    }

    public SimpleDoubleProperty shortRatioProperty() {
        return shortRatio;
    }

    public void setSymbol(String symbol) {
        this.symbol.set(symbol);
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice.set(currentPrice);
    }

    public void setPrevClose(double prevClose) {
        this.prevClose.set(prevClose);
    }

    public void setChange1Y(double change1Y) {
        this.change1Y.set(change1Y);
    }

    public void setChange1M(double change1M) {
        this.change1M.set(change1M);
    }

    public void setChange3M(double change3M) {
        this.change3M.set(change3M);
    }

    public void setDivYield(double divYield) {
        this.divYield.set(divYield);
    }

    public void setEps(double eps) {
        this.eps.set(eps);
    }

    public void setPeRatio(double peRatio) {
        this.peRatio.set(peRatio);
    }

    public void setMarketCap(int marketCap) {
        this.marketCap.set(marketCap);
    }

    public void setShortRatio(double shortRatio) {
        this.shortRatio.set(shortRatio);
    }
}
