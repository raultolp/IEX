package app;

//LightSlateGray 	#778899
//LightSlateGrey 	#778899
//LightSteelBlue 	#B0C4DE
//SeaShell 	#FFF5EE
//SteelBlue 	#4682B4

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;


public class UserInterface extends Application {
    private Portfolio portfolio;


    public static void main(String[] args) {

        {
            launch(args);
        }
    }

    @Override
    public void start(Stage stage) {

        GridPane centerGrid = new GridPane();
        centerGrid.add(addTableView(), 0, 0);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(addTableView());
        borderPane.setBottom(addBottomHBox());


        Scene scene = new Scene(borderPane, 600, 550);


        stage.setTitle("Portfolio");
        stage.setScene(scene);
        stage.show();
    }

    private HBox addBottomHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #4682B4;");

        Button refresh = new Button("Refresh");
        refresh.setStyle("-fx-background-color: #FFF5EE");
        refresh.setPrefSize(100, 20);

        Button stockInfo = new Button("Stock info");
        stockInfo.setStyle("-fx-background-color: #FFF5EE");
        stockInfo.setPrefSize(100, 20);
        hbox.getChildren().addAll(stockInfo, refresh);

        return hbox;
    }

    private TableView<Stock> addTableView() {

       /* FlowPane flowPane = new FlowPane();
        flowPane.setPadding(new Insets(5, 0, 5, 0));
        flowPane.setVgap(4);
        flowPane.setHgap(4);
        flowPane.setPrefWrapLength(230);*/


        //Stock symbols
        TableColumn<Stock, String> symbolColumn = new TableColumn<>("Stock");
        symbolColumn.setMinWidth(100);
        symbolColumn.setCellValueFactory(new PropertyValueFactory<>("symbol"));

        //Prices
        TableColumn<Stock, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));

        ///Volume
        TableColumn<Stock, String> volumeColumn = new TableColumn<>("Volume");
        volumeColumn.setMinWidth(100);

        ///Total
        TableColumn<Stock, String> totalColumn = new TableColumn<>("Total");
        volumeColumn.setMinWidth(100);

        ///Buy/sell
        ChoiceBox<String> buySellBox = new ChoiceBox<>();
        buySellBox.getItems().addAll("Buy", "Sell");
        TableColumn<Stock, String> tradeColumn = new TableColumn<>("Buy/sell");
        volumeColumn.setMinWidth(100);

        ///Amount
        TableColumn<Stock, String> numberColumn = new TableColumn<>("Number");
        volumeColumn.setMinWidth(100);

        TableView table = new TableView<Stock>();
        table.setEditable(true);
        table.setItems(getStocksFromPortfolio());
        table.getColumns().addAll(symbolColumn, priceColumn, volumeColumn, totalColumn, tradeColumn, numberColumn);


        //flowPane.getChildren().addAll(table);

        return table;
    }

    private ObservableList<Stock> getStocksFromPortfolio() {
        //Map<String, Stock> hm = portfolio.getPortfolio();
        Stock stock1 = new Stock("AAPL");
        Map<String, Stock> hm = new HashMap<>();
        hm.put("AAPL", stock1);
      //  Portfolio portfolio = new Portfolio(hm);

        ObservableList<Stock> stocks = FXCollections.observableArrayList();

        for (String key : hm.keySet()) {
            Stock stock = hm.get(key);
            stocks.add(stock);
        }
        // System.out.println(stocks);
        return stocks;
    }


}
