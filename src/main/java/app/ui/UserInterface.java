
package app.ui;

//LightSlateGray 	#778899
//LightSlateGrey 	#778899
//LightSteelBlue 	#B0C4DE
//SeaShell 	#FFF5EE
//SteelBlue 	#4682B4

import app.Iu;
import app.Portfolio;
import app.Stock;
import com.jfoenix.controls.JFXButton;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserInterface extends Stage {
    private Iu handler = new Iu();
    private IuFX FX;
    private Portfolio portfolio = handler.getMasterPortfolio();
    private Scene scene;
    private Stage stage;

    public UserInterface(IuFX FX) throws Exception {
        this.stage = buildStage();
        this.FX = FX;
    }


    public Stage buildStage() {

        Stage stage = new Stage();


        GridPane centerGrid = new GridPane();
        centerGrid.setVgap(8);
        centerGrid.setHgap(10);
        centerGrid.add(addTableView(), 0, 0);

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #4682B4;");

        Button refresh = new JFXButton("Refresh");
        refresh.setStyle("-fx-background-color: #FFF5EE");
        refresh.setPrefSize(100, 20);

        Button stockInfo = new JFXButton("Stock info");
        stockInfo.setStyle("-fx-background-color: #FFF5EE");
        stockInfo.setPrefSize(100, 20);

        Button clickMe = new JFXButton("Create user");
        clickMe.setStyle("-fx-background-color: #FFF5EE");
        stockInfo.setPrefSize(200, 20);


        clickMe.setOnAction(event -> {
            try {
                Stage stage1 = new Stage();
                stage1.setScene(FX.getCU().getScene());
                stage1.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(centerGrid);
        borderPane.setBottom(hbox);
        borderPane.setRight(addBuySellVBox());

        hbox.getChildren().addAll(stockInfo, refresh, clickMe);
        Scene scene = new Scene(borderPane, 750, 550);
        addTableView();

        stage.setTitle("Start");
        stage.setScene(scene);

        return stage;
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


    //https://stackoverflow.com/questions/38487797/javafx-populate-tableview-with-an-observablemap-that-has-a-custom-class-for-its

    private TableView<MapEntry<String, Stock>> addTableView() {

        ObservableMap<String, Stock> map = FXCollections.observableHashMap();

        portfolio.getPortfolioStocks().forEach(map::put); //kõik väärtused masterportfoliost tabelisse

        ObservableList<MapEntry<String, Stock>> stocks = FXCollections.observableArrayList();
        for (String key : map.keySet()) {
            stocks.add(new MapEntry<>(key, map.get(key)));
        }

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

        table.getColumns().addAll(stockSymbols);


        return table;
    }

    private VBox addBuySellVBox() {

        VBox buySellVB = new VBox();
        buySellVB.setPadding(new Insets(150, 12, 15, 12));
        buySellVB.setSpacing(20);
        buySellVB.setStyle("-fx-background-color: #4682B4;");

        final TextField stockSymbol = new TextField();
        stockSymbol.setPromptText("Enter stock symbol");

        final TextField stockVolume = new TextField();
        stockVolume.setPromptText("Enter volume of stocks");

        Button buy = new Button("Buy");
        buy.setStyle("-fx-background-color: #FFF5EE");
        buy.setOnAction((ActionEvent e) -> {
            try {
                if (stockSymbol.getText() != null && stockVolume.getText() != null) {
                    portfolio.buyStock(stockSymbol.getText(), Integer.parseInt(stockVolume.getText()));
                    stockSymbol.clear();
                    stockVolume.clear();
                }

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        Button sell = new Button("Sell");
        sell.setStyle("-fx-background-color: #FFF5EE");

        sell.setOnAction(e -> {
            try {
                if (stockSymbol.getText() != null && stockVolume.getText() != null) {
                    portfolio.sellStock(stockSymbol.getText(), Integer.parseInt(stockVolume.getText()));
                    stockSymbol.clear();
                    stockVolume.clear();
                }

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });


        buySellVB.getChildren().addAll(stockSymbol, stockVolume, buy, sell);


        return buySellVB;
    }

    public Stage getStage() {
        return stage;
    }
}
