
package app.ui;

//LightSlateGray 	#778899
//LightSlateGrey 	#778899
//LightSteelBlue 	#B0C4DE
//SeaShell 	#FFF5EE
//SteelBlue 	#4682B4

import app.Iu;
import app.Portfolio;
import app.Stock;
import app.User;
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
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserInterface {
    private Iu handler;
    private IuFX FX;
    private Portfolio portfolio;
    private List<User> userList;
    private String selectedUser;
    private Scene scene;
    private Stage stage;
    private User user;


    public UserInterface(IuFX FX) throws Exception {
        this.handler = FX.getHandler();
        this.portfolio = this.handler.getMasterPortfolio();
        this.FX = FX;
        this.userList = this.handler.getUserList();
    }


    public Stage buildStage() throws Exception {

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

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(centerGrid);
        borderPane.setBottom(addBottomHBox());
        borderPane.setRight(addRightSideVBox());
        borderPane.setStyle("-fx-background-color: #4682B4");


        hbox.getChildren().addAll(stockInfo, refresh);
        Scene scene = new Scene(borderPane, 750, 550);
        addTableView();

        stage.setTitle("Master portfolio");
        stage.setScene(scene);

        return stage;
    }


    private HBox addBottomHBox() {
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

        Button userPort = new JFXButton("My portfolio");
        userPort.setStyle("-fx-background-color: #FFF5EE");
        userPort.setPrefSize(100, 20);


        //praegu ei tööta ja ei peagi
        refresh.setOnMouseClicked(event -> {
            /**try {
             handler.runInteractive(FX.getHandler(), 1111);
             } catch (Exception e) {
             e.printStackTrace();
             }*/
        });

        stockInfo.setOnMouseClicked(event -> {
            try {//TODO get selected stock and display graph
                handler.runInteractive(FX.getHandler(), 12);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        hbox.getChildren().addAll(stockInfo, refresh, userPort);


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

        //STOCK, VOLUME, PRICE, VALUE, UNREALIZED P/L, REALIZED P/L

        //TODO property value factories

        TableColumn<MapEntry<String, Stock>, String> stockSymbols = new TableColumn<>("Stock");
        stockSymbols.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getKey()));

        TableColumn<MapEntry<String, Stock>, String> volume = new TableColumn<>("Volume");

        TableColumn<MapEntry<String, Stock>, String> price = new TableColumn<>("Price");

        TableColumn<MapEntry<String, Stock>, String> value = new TableColumn<>("Value");

        TableColumn<MapEntry<String, Stock>, String> unrealisedPL = new TableColumn<>("Unrealized P/L");

        TableColumn<MapEntry<String, Stock>, String> realisedPL = new TableColumn<>("Realized P/L");


        table.getColumns().addAll(stockSymbols, volume, price, value, unrealisedPL, realisedPL);


        return table;
    }

    //OSTU-MÜÜGI KAST
    private VBox addBuySellVBox() {

        VBox buySellVB = new VBox();
        buySellVB.setPadding(new Insets(150, 12, 15, 12));
        buySellVB.setSpacing(20);
        buySellVB.setStyle("-fx-background-color: #4682B4");

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

    private VBox addRightSideVBox() {
        VBox rightBox = new VBox();
        rightBox.setPadding(new Insets(150, 12, 15, 12));
        rightBox.setSpacing(20);
        rightBox.setStyle("-fx-background-color: #4682B4");

        Button createUser = new JFXButton("Create user");
        createUser.setStyle("-fx-background-color: #FFF5EE");
        createUser.setPrefSize(200, 20);

        Button openUserPortfolio = new JFXButton("Open portfolio");
        openUserPortfolio.setStyle("-fx-background-color: #FFF5EE");
        openUserPortfolio.setPrefSize(200, 20);

        //kasutajad menüüsse
        MenuButton selectUser = new SplitMenuButton();
        //if (!(userList.isEmpty()))
        for (User user : handler.getUserList()) {
            System.out.println("sthsth");
            MenuItem item = new MenuItem(user.getUserName());
            selectUser.getItems().add(item);
            //kui menu item valitud, siis muudab activeUseri
            item.setOnAction(a -> {
                //TODO aktiivne kasutaja peab jääma aktiivseks
                FX.getHandler().setActiveUser(user);
                System.out.println(FX.getHandler().getActiveUser().getUserName());
            });
        }
        selectUser.setStyle("-fx-background-color: #4682B4");
        selectUser.setPrefSize(200, 20);

        openUserPortfolio.setOnMouseClicked(event -> {
         //   FX.getUP().getStage().show();
            //hide UI
        });


        createUser.setOnAction(event -> {
            try {
                Stage stage1 = new Stage();
                stage1.setScene(FX.getCU().getScene());
                stage1.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        rightBox.getChildren().addAll(selectUser, createUser, openUserPortfolio);

        return rightBox;
    }

    public VBox getBuySellVBox() {
        return addBuySellVBox();
    }


    public Stage getStage() throws Exception {
        return buildStage();
    }
}
