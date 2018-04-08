package app;

//LightSlateGray 	#778899
//LightSlateGrey 	#778899
//LightSteelBlue 	#B0C4DE
//SeaShell 	#FFF5EE
//SteelBlue 	#4682B4

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.shape.Shape;
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
        borderPane.setCenter(centerGrid);
        //borderPane.setCenter(addTableView());
        borderPane.setBottom(addBottomHBox());

        addTableView();


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

    //https://stackoverflow.com/questions/38487797/javafx-populate-tableview-with-an-observablemap-that-has-a-custom-class-for-its


    private TableView<MapEntry<String, Stock>> addTableView() {

        /**stock
        price
        volume
        position total
        unrealized P/L
        Avg price paid
        realized P/L**/

        Map<String, Stock> hm = new HashMap<>();
        //Map<String, Stock> hm = portfolio.getPortfolio();
        ObservableMap<String, Stock> map = FXCollections.observableHashMap();

        ObservableList<MapEntry<String, Stock>> stocks = FXCollections.observableArrayList();

        final TableView<MapEntry<String, Stock>> table = new TableView<>(stocks);
        Stock stock1 = new Stock("AAPL");
        hm.put("AAPL", stock1);
        for (String key : hm.keySet()) {
            Stock stock = hm.get(key);
            map.put(key, stock);
            System.out.println(hm);
        }


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


    public final class MapEntry<K, V> {

        private final K key;
        private final V value;

        public MapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            // check equality only based on keys
            if (obj instanceof app.MapEntry) {
                app.MapEntry<?, ?> other = (app.MapEntry<?, ?>) obj;
                return Objects.equals(key, other.getKey());
            } else {
                return false;
            }
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

    }



}
