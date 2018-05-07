package app.client.ui;

import app.client.Stock;
import com.jfoenix.controls.JFXButton;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class UserPortfolio {
    IuFX FX;
    //User user;

    public UserPortfolio(IuFX FX) {
        this.FX = FX;
        //this.user = FX.getHandler().getActiveUser();
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

        Button exit = new JFXButton("Exit");
        exit.setStyle("-fx-background-color: #FFF5EE");
        stockInfo.setPrefSize(200, 20);


        exit.setOnAction(event -> {
            try {
                FX.getUI().buildStage().show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(centerGrid);
        borderPane.setBottom(hbox);
        borderPane.setRight(FX.getUI().getBuySellVBox());

        hbox.getChildren().addAll(stockInfo, refresh, exit);
        Scene scene = new Scene(borderPane, 750, 550);

        //stage.setTitle(user.getUserName() + "'s portfolio");
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

        //FX.getHandler().getActiveUser().getPortfolio().getPortfolioStocks().forEach(map::put); //kõik väärtused userportist tabelisse

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

        TableColumn<MapEntry<String, Stock>, String> volume = new TableColumn<>("Volume");

        TableColumn<MapEntry<String, Stock>, String> price = new TableColumn<>("Price");

        TableColumn<MapEntry<String, Stock>, String> value = new TableColumn<>("Value");

        TableColumn<MapEntry<String, Stock>, String> unrealisedPL = new TableColumn<>("Unrealized P/L");

        TableColumn<MapEntry<String, Stock>, String> realisedPL = new TableColumn<>("Realized P/L");


        table.getColumns().addAll(stockSymbols, volume, price, value, unrealisedPL, realisedPL);

        table.getColumns().addAll(stockSymbols);


        return table;
    }

/*    public void setUser(User user) {
        this.user = user;
    }*/

    public Stage getStage() {
        return buildStage();
    }
}

