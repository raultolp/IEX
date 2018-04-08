package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Portfolio {

    private double availableFunds;
    private User user;

    private Map<String, Stock> portfolio;
    private List<String> symbolList;
    private List<Double> prices;  //current price
    private List<Integer> volumes; //number of stocks
    private List<Double> averagePrices; //average purchase prices (includes transaction fees) -
    //necessary also in order to take into account that
    // stocks can be bought with different prices at different times.
    private List<Double> profitsOrLosses;  //realised profit/loss from each stock in portfolio
    //("realised profit/loss" means profit loss from transactions made)
    private List<Double> unrealisedProfitsOrLosses; //"unrealised proft/loss" means profit/loss that would
    // be gained if stocks were sold at current market price
    //(does not include transaction fee)
    private List<Double> currentValuesOfPositions;  // volume*current price, i.e. current value of stock in portfolio

    //Totals:
    private double totalCurrentValueOfPositions; //sum of all totals (i.e. sum of all current positions)
    private double totalProfitOrLoss;
    private double totalUnrealisedProfitOrLoss;

    //Transaction fee:
    private double transactionFee = 0.1; // 10 cents per stock


    //Constructor - creates an empty portfolio:
    public Portfolio() {
        this.portfolio = new HashMap<>();
        this.symbolList = new ArrayList<>();
        this.prices = new ArrayList<>();
        this.volumes = new ArrayList<>();
        this.currentValuesOfPositions = new ArrayList<>();
        this.averagePrices = new ArrayList<>();
        this.profitsOrLosses = new ArrayList<>();
        this.unrealisedProfitsOrLosses= new ArrayList<>();
        this.totalCurrentValueOfPositions = 0.0;
        this.totalProfitOrLoss = 0.0;
        this.totalUnrealisedProfitOrLoss = 0.0;
        this.transactionFee = transactionFee;
        this.availableFunds=0.0;
        this.user=user;
    }


    //-----------------------------------------------
    //Buying stock:

    public void buyStock(String symbol, int volume) {

        double transactionFeeTotal = volume * transactionFee;
        double price;

        //If stock not yet included in portfolio::
        if (!portfolio.containsKey(symbol)) {
            Stock stock = new Stock(symbol);
            price = stock.getLatestPrice();  //current price

            if(checkSufficiencyOfFunds(price, volume)==false){
                throw new RuntimeException("Not enough funds!");
            }

            else{
                double averagePrice=price + transactionFee;

                portfolio.put(symbol, stock);
                symbolList.add(symbol);
                volumes.add(volume);
                prices.add(price); //current price
                currentValuesOfPositions.add(price * volume); //current value of stock in portfolio
                averagePrices.add(averagePrice);
                profitsOrLosses.add(0.0 - transactionFeeTotal);
                unrealisedProfitsOrLosses.add(volume*(price-averagePrice));

                availableFunds-=volume*(price+transactionFee);
                user.setAvailableFunds(availableFunds);
            }

        }

        //If stock already included in portfolio::
        else {
            int indexOfStock = symbolList.indexOf(symbol);
            Stock stock = portfolio.get(symbol);
            price = stock.getLatestPrice();

            if(checkSufficiencyOfFunds(price, volume)==false){
                throw new RuntimeException("Not enough funds!");
            }

            else {
                int prevVolume = volumes.get(indexOfStock);
                double prevTotal = currentValuesOfPositions.get(indexOfStock);
                double prevAveragePrice = averagePrices.get(indexOfStock);
                double prevProfitOrLoss = profitsOrLosses.get(indexOfStock);

                volumes.set(indexOfStock, prevVolume + volume);
                prices.set(indexOfStock, price); //current price
                currentValuesOfPositions.set(indexOfStock, price * (volume + prevVolume)); //current value of stock in portfolio
                profitsOrLosses.set(indexOfStock, prevProfitOrLoss - transactionFeeTotal);

                //New average purchase price calculation:
                double newAveragePrice = (prevVolume * prevAveragePrice + volume * (price + transactionFee)) / (volume + prevVolume);
                averagePrices.set(indexOfStock, newAveragePrice);  // weighted average

                //New unrealised proft/loss calculation:
                unrealisedProfitsOrLosses.set(indexOfStock, (volume + prevVolume)*(price-newAveragePrice));

                availableFunds-=volume*(price+transactionFee);
                user.setAvailableFunds(availableFunds);

            }

        }

        if(checkSufficiencyOfFunds(price, volume)==true){
            totalCurrentValueOfPositions = calculateTotal(currentValuesOfPositions);
            totalProfitOrLoss=calculateTotal(profitsOrLosses);
            totalUnrealisedProfitOrLoss=calculateTotal(unrealisedProfitsOrLosses);;
        }




        //TODO:
        //Check if user has enough money for the transaction
    }

    //-----------------------------------------------
    //Selling stock:

    //- If user tries to sell more stocks than included in portfolio, only the
    //stocks included in portfolio will be sold (negative number of stocks not allowed)
    //- If user tries to sell stock not included in portfolio, nothing happens.

    public void sellStock(String symbol, int volume) {

        if (portfolio.containsKey(symbol)) { //stock can be sold only if it is present in portfolio
            int indexOfStock = symbolList.indexOf(symbol);

            if (volumes.get(indexOfStock) < volume) { //max number of shares to be sold is their number in portfolio
                volume = volumes.get(indexOfStock); //if user tries to sell more, only the max number is sold
            }

            double transactionFeeTotal = volume * transactionFee;
            Stock stock = portfolio.get(symbol);
            double price = stock.getLatestPrice();  //current price
            int prevVolume = volumes.get(indexOfStock);
            double prevTotal = currentValuesOfPositions.get(indexOfStock);
            double prevAveragePrice = averagePrices.get(indexOfStock);
            double prevProfitOrLoss = profitsOrLosses.get(indexOfStock);

            volumes.set(indexOfStock, prevVolume - volume);
            prices.set(indexOfStock, price); //current price uuendamine
            currentValuesOfPositions.set(indexOfStock, price * (prevVolume - volume)); //current value of stock in portfolio

            //Increasing the previous profit from this stock:
            double profitFromSell=volume*(price-prevAveragePrice)-transactionFeeTotal;
            profitsOrLosses.set(indexOfStock, prevProfitOrLoss+profitFromSell);

            totalCurrentValueOfPositions = calculateTotal(currentValuesOfPositions);
            totalProfitOrLoss=calculateTotal(profitsOrLosses);
            totalUnrealisedProfitOrLoss=calculateTotal(unrealisedProfitsOrLosses);

            //average purchase price (in averagePrices) remains the same


            //New unrealised proft/loss calculation:
            unrealisedProfitsOrLosses.set(indexOfStock, (prevVolume-volume)*(price-averagePrices.get(indexOfStock)));

            //If all stocks are sold from portfolio and profit gained from the stook is zero:
            removeRedundantStock(symbol);

            availableFunds+=volume*(price-transactionFee);
            user.setAvailableFunds(availableFunds);


            //TODO:
            //Possibility for short selling could be added (allows negative number of stocks)
        }

    }

    //-----------------------------------------------
    //removing stock from portfolio:

    public void removeRedundantStock(String symbol) {
        int indexOfStock = symbolList.indexOf(symbol);
        //Should only be removed if volume in portfolio is zero and no profit/loss has been gained
        // from this stock:
        if (volumes.get(indexOfStock)==0 && profitsOrLosses.get(indexOfStock)==0.0){
            portfolio.remove(symbol);
            symbolList.remove(indexOfStock);
            prices.remove(indexOfStock);
            volumes.remove(indexOfStock);
            currentValuesOfPositions.remove(indexOfStock);
            averagePrices.remove(indexOfStock);
            profitsOrLosses.remove(indexOfStock);
            unrealisedProfitsOrLosses.remove(indexOfStock);
        }
    }

    //-----------------------------------------------

    //calculate portfolio total (sum of all current positions in stock):
    public double calculateTotal(List<Double> listToBeTotalled) {
        double total = 0.0;
        for (double t : listToBeTotalled) {
            total += t;
        }
        return total;
    }


    //-----------------------------------------------

    //update current prices of all stocks in portfolio:
    public void updatePrices() {

        for (String symbol : portfolio.keySet()) {
            int indexOfStock = symbolList.indexOf(symbol);
            Stock stock=portfolio.get(symbol);
            int volume=volumes.get(indexOfStock);
            double price = stock.getLatestPrice();

            stock.setCurrentPrice(price);
            prices.set(indexOfStock, price);
            currentValuesOfPositions.set(indexOfStock, volume*price);  //current value of position in this stock
            unrealisedProfitsOrLosses.set(indexOfStock, volume*(price-averagePrices.get(indexOfStock)));
            //profitsOrLosses does not change (because it indicates realised profit/loss from transactions)

            totalCurrentValueOfPositions = calculateTotal(currentValuesOfPositions);
            totalProfitOrLoss=calculateTotal(profitsOrLosses);
            totalUnrealisedProfitOrLoss=calculateTotal(unrealisedProfitsOrLosses);

        }
    }

    //-----------------------------------------------

    public boolean checkSufficiencyOfFunds(double price, int volume){
        if ((price+transactionFee)*volume>availableFunds){
            return false;
        }
        return true;
    }

    //-----------------------------------------------
    //GETTERS:

    //Getting stock from portfolio:
    public Stock getStock(String symbol) {
        return portfolio.get(symbol);
    }

    //Other getters:
    public Map<String, Stock> getPortfolio() {
        return portfolio;
    }

    public List<String> getSymbolList() {
        return symbolList;
    }

    public List<Double> getPrices() {
        return prices;
    }

    public List<Integer> getVolumes() {
        return volumes;
    }

    public List<Double> getAveragePrices() {
        return averagePrices;
    }

    public List<Double> getProfitsOrLosses() {
        return profitsOrLosses;
    }

    public List<Double> getUnrealisedProfitsOrLosses() {
        return unrealisedProfitsOrLosses;
    }

    public List<Double> getCurrentValuesOfPositions() {
        return currentValuesOfPositions;
    }

    public double getTotalCurrentValueOfPositions() {
        return totalCurrentValueOfPositions;
    }

    public double getTotalProfitOrLoss() {
        return totalProfitOrLoss;
    }

    public double getTotalUnrealisedProfitOrLoss() {
        return totalUnrealisedProfitOrLoss;
    }

    public void setAvailableFunds(double availableFunds) {
        this.availableFunds = availableFunds;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return
                "Stock names: \n" + symbolList.toString() + '\n' +
                        "Prices; \n" + prices.toString() + '\n' +
                        "Volumes: \n" + volumes.toString() + '\n' +
                        "Average prices: \n" + averagePrices + '\n' +
                        "Profits or losses: \n" + profitsOrLosses + '\n' +
                        "Unrealised profits or losses: \n" + unrealisedProfitsOrLosses + '\n' +
                        "Current values of positions: \n" + currentValuesOfPositions + '\n' +
                        "Total current value of positions: \n" + totalCurrentValueOfPositions + '\n' +
                        "Total profit or loss: \n" + totalProfitOrLoss + '\n' +
                        "Total unrealised profit or loss: \n" + totalUnrealisedProfitOrLoss + '\n' +
                        "Transaction fee: \n" + transactionFee + '\n';
    }

}

