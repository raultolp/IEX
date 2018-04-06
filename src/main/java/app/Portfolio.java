package app;

import java.util.List;

public class Portfolio {
    private List<Stock> stocks;

    public Portfolio(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public void addStock(Stock stock) {
        stocks.add(stock);
    }
}
