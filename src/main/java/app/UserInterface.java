/*
package app;

//LightSlateGray 	#778899
//LightSlateGrey 	#778899
//LightSteelBlue 	#B0C4DE
//SeaShell 	#FFF5EE
//SteelBlue 	#4682B4

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Popup;
import javafx.stage.Stage;

import javax.sound.sampled.Port;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class UserInterface extends Application {
    private Portfolio portfolio;
    Stock selectedStock;
    Stage stage;

    private User user = new User("Pedro", new Portfolio(0.1), 10000);
    User user1 = new User("Peeter", new Portfolio(0.2), 1000000); //testing Pets


    public static void main(String[] args) {

        {
            launch(args);
        }
    }

    @Override
    public void start(Stage stage) {

        GridPane centerGrid = new GridPane();
        centerGrid.setVgap(8);
        centerGrid.setHgap(10);
        centerGrid.add(addTableView(), 0, 0);

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

        Button clickMe = new Button("Click me for a popup");
        clickMe.setStyle("-fx-background-color: #FFF5EE");
        stockInfo.setPrefSize(200, 20);

        Button closePopup = new Button("Close");

        Popup popup = new Popup();
        popup.getContent().add(closePopup);


        closePopup.setOnAction(event -> popup.hide());


        clickMe.setOnAction(event -> {
            System.out.printf("popup");
            popup.show(stage);
        });


        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(centerGrid);
        borderPane.setBottom(hbox);
        borderPane.setRight(addBuySellVBox());


        hbox.getChildren().addAll(stockInfo, refresh, clickMe);


        addTableView();


        Scene scene = new Scene(borderPane, 750, 550);


        stage.setTitle("Portfolio");
        stage.setScene(scene);
        stage.show();
    }

   */
/* private HBox addBottomHBox() {
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

        Button clickMe = new Button("Click me for a popup");
        clickMe.setStyle("-fx-background-color: #FFF5EE");
        stockInfo.setPrefSize(200, 20);
        Popup popup = new Popup();

        clickMe.setOnAction(event -> popup.show(stage));

        hbox.getChildren().addAll(stockInfo, refresh, clickMe);


        return hbox;
    }*//*


    //https://stackoverflow.com/questions/38487797/javafx-populate-tableview-with-an-observablemap-that-has-a-custom-class-for-its


    private TableView<MapEntry<String, Stock>> addTableView() {
        ObservableMap<String, Stock> map = FXCollections.observableHashMap();

        ObservableList<MapEntry<String, Stock>> stocks = FXCollections.observableArrayList();

        final TableView<MapEntry<String, Stock>> table = new TableView<>(stocks);

        map.addListener((MapChangeListener.Change<? extends String, ? extends Stock> change) -> {
            boolean removed = change.wasRemoved();
            if (removed != change.wasAdded()) {
                if (removed) {
                    // no put for existing key, remove pair completely
                    stocks.remove(new MapEntry<>(change.getKey(), (Stock) null));
                } else {
                    // add new entry
                    stocks.add(new MapEntry<>(change.getKey(), change.getValueAdded()));
                }
            } else {
                // replace existing entry
                MapEntry<String, Stock> entry = new MapEntry<>(change.getKey(), change.getValueAdded());

                int index = stocks.indexOf(entry);
                stocks.set(index, entry);
            }
        });

        TableColumn<MapEntry<String, Stock>, String> stockSymbols = new TableColumn<>("Stock");
        stockSymbols.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getKey()));

        TableColumn<MapEntry<String, Stock>, String> price = new TableColumn<>("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("Prices"));

        TableColumn<MapEntry<String, Stock>, String> volume = new TableColumn<>("Volume");
        price.setCellValueFactory(new PropertyValueFactory<>("Volumes"));

        TableColumn<MapEntry<String, Stock>, String> positionTotal = new TableColumn<>("Position total");
        positionTotal.setCellValueFactory(new PropertyValueFactory<>("CurrentValuesOfPositions"));

        TableColumn<MapEntry<String, Stock>, String> unrealised = new TableColumn<>("Unrealised P/L");
        positionTotal.setCellValueFactory(new PropertyValueFactory<>("UnrealisedProfitsOrLosses"));

        TableColumn<MapEntry<String, Stock>, String> avgPaid = new TableColumn<>("AVG price paid");
        avgPaid.setCellValueFactory(new PropertyValueFactory<>("AveragePrices"));

        TableColumn<MapEntry<String, Stock>, String> realised = new TableColumn<>("Realised P/L");
        realised.setCellValueFactory(new PropertyValueFactory<>("ProfitsOrLosses"));


        table.getColumns().addAll(stockSymbols, price, volume, positionTotal, unrealised, avgPaid, realised);


        return table;
    }

    private VBox addBuySellVBox() {
        String stockSym = "";

        VBox buySellVB = new VBox();
        buySellVB.setPadding(new Insets(150, 12, 15, 12));
        buySellVB.setSpacing(20);
        buySellVB.setStyle("-fx-background-color: #4682B4;");

        final TextField stockSymbol = new TextField();
        stockSymbol.setPromptText("Enter stock symbol");

        final TextField stockVolume = new TextField();
        stockVolume.setPromptText("Enter volume of stocks");


        stockSymbol.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!(newValue.equals(""))) { //TODO: check if symbol in available stocks
                System.out.println(newValue);
                //selectedStock = new Stock(newValue);

            }
        });

        stockVolume.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!(newValue.equals(""))) {
                System.out.println(newValue);
            }
        });
        Button buy = new Button("Buy");
        buy.setStyle("-fx-background-color: #FFF5EE");
        buy.setOnAction((ActionEvent e) -> {
            try {
                if (stockSymbol.getText() != null && stockVolume.getText() != null)
                    user.getPortfolio().buyStock(stockSymbol.getText(), Integer.parseInt(stockVolume.getText()));
                stockSymbol.clear();
                stockVolume.clear();

            } catch (Exception e1) {
                System.out.printf("Fuck");
            }
        });

        Button sell = new Button("Sell");
        sell.setStyle("-fx-background-color: #FFF5EE");

        sell.setOnAction(e -> {
            try {
                user.getPortfolio().sellStock(stockSym, Integer.parseInt(stockVolume.getText()));
                stockSymbol.clear();
                stockVolume.clear();

            } catch (Exception e1) {
                e1.printStackTrace();
            }


        });


        buySellVB.getChildren().addAll(stockSymbol, stockVolume, buy, sell);


        return buySellVB;
    }
}

*/
