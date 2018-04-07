package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Portfolio {

    //private List<Stock> portfolio= new ArrayList<>();
    private Map<String, Stock>portfolio=new HashMap<>();

    public void addStock(Stock stock){
        portfolio.put(stock.getTicker(), stock);
    }

    public Stock getStock(String ticker){
        return portfolio.get(ticker);
    }

/*    public double calculateTotal(){

    }*/

    public Map<String, Stock> getPortfolio() {
        return portfolio;
    }

}

