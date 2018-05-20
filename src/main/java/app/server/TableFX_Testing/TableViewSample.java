package app.server.TableFX_Testing;

import app.server.AdminIO;
import app.server.Iu;
import app.server.Portfolio;
import app.server.UpdatingPrices;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class TableViewSample extends Application {

    private static TableView<TableModelMaster> table; //

    private ObservableList<TableModelMaster> data = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws InterruptedException, IOException {

        //MasterPortfolio JSON-ina:
        Iu handler = new Iu(new AdminIO());

        //RUN IEX DATA COLLECTOR AS THREAD
        Thread dataCollector = new Thread(new UpdatingPrices(handler));
        dataCollector.start();

        //MasterPortfolio as JSON (nii nagu klient selle saab):
        Portfolio masterPortfolio = handler.getMasterPortfolio();

        String masterAsString = new Gson().toJson(masterPortfolio); //selline string saadetakse Client'ile
        JsonParser jp = new JsonParser();
        JsonObject masterAsJson = jp.parse(masterAsString).getAsJsonObject();

        JsonObject portfObj = masterAsJson.get("portfolioStocks").getAsJsonObject();

        for (String symbol : portfObj.keySet()) {
            JsonObject stockObj = portfObj.get(symbol).getAsJsonObject();
            double currentPrice = stockObj.get("currentPrice").getAsDouble();
            double prevClose = stockObj.get("previousClose").getAsDouble();
            double change1Y = stockObj.get("change1Year").getAsDouble();
            double change1M = stockObj.get("change1Month").getAsDouble();
            double change3M = stockObj.get("change3Month").getAsDouble();
            double divYield = stockObj.get("dividendYield").getAsDouble();
            double eps = stockObj.get("eps").getAsDouble();
            double peRatio = stockObj.get("peRatio").getAsDouble();
            int marketCap = stockObj.get("marketCap").getAsInt();
            double shortRatio = stockObj.get("shortRatio").getAsDouble();
            TableModelMaster stockData = new TableModelMaster(symbol, currentPrice, prevClose, change1Y, change1M,
                    change3M, divYield, eps, peRatio, marketCap, shortRatio);
            data.add(stockData);
        }


        // Price update:
        Thread th = new Thread() {
            @Override
            public synchronized void run() {
                try {
                    //while (true) {
                    int i = 2; // börsivälisel ajal testimiseks
                    while (i < 10) { // börsivälisel ajal testimiseks
                        Thread.sleep(3000);

                        JsonObject priceUpdateForClients = handler.getPriceUpdateForClients();

                        for (TableModelMaster row : data) {
                            String symbol = row.getSymbol();
                            double newPrice = priceUpdateForClients.get(symbol).getAsDouble();
                            newPrice += 0.1 * i; // börsivälisel ajal testimiseks
                            row.setCurrentPrice(newPrice);
                        }
                        i++; // börsivälisel ajal testimiseks
                        //table.setItems(data2);  // pole vist vaja
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        th.start();


        // UI:
        Scene scene = new Scene(new Group());
        stage.setTitle("STOCK EXCHANGE GAME");
        stage.setWidth(900);
        stage.setHeight(400);

        Label label = new Label("AVAILABLE STOCKS");
        label.setFont(new Font("Arial", 20));

        //createAndShowGUI(label, scene, stage);
        table = new TableView<>();
        table.setEditable(true);


        TableColumn symbolCol = new TableColumn("STOCK");
        symbolCol.setMinWidth(50);
        symbolCol.setCellValueFactory(
                new PropertyValueFactory<>("symbol"));

        TableColumn priceCol = new TableColumn("PRICE");
        priceCol.setMinWidth(50);
        priceCol.setCellValueFactory(
                new PropertyValueFactory<>("currentPrice"));

        TableColumn prevCloseCol = new TableColumn("PREV.CLOSE");
        prevCloseCol.setMinWidth(50);
        prevCloseCol.setCellValueFactory(
                new PropertyValueFactory<>("prevClose"));


        TableColumn change1YCol = new TableColumn("CHANGE 1Y");
        change1YCol.setMinWidth(50);
        change1YCol.setCellValueFactory(
                new PropertyValueFactory<>("change1Y"));

        TableColumn change1MCol = new TableColumn("CHANGE 1M");
        change1MCol.setMinWidth(50);
        change1MCol.setCellValueFactory(
                new PropertyValueFactory<>("change1M"));

        TableColumn change3MCol = new TableColumn("CHANGE 3M");
        change3MCol.setMinWidth(50);
        change3MCol.setCellValueFactory(
                new PropertyValueFactory<>("change3M"));

        TableColumn divCol = new TableColumn("DIVIDEND YIELD");
        divCol.setMinWidth(50);
        divCol.setCellValueFactory(
                new PropertyValueFactory<>("divYield"));

        TableColumn epsCol = new TableColumn("EPS");
        epsCol.setMinWidth(50);
        epsCol.setCellValueFactory(
                new PropertyValueFactory<>("eps"));

        TableColumn peCol = new TableColumn("PE RATIO");
        peCol.setMinWidth(50);
        peCol.setCellValueFactory(
                new PropertyValueFactory<>("peRatio"));

        TableColumn mktCapCol = new TableColumn("MARKET CAP");
        mktCapCol.setMinWidth(50);
        mktCapCol.setCellValueFactory(
                new PropertyValueFactory<>("marketCap"));

        TableColumn shortCol = new TableColumn("SHORT RATIO");
        shortCol.setMinWidth(50);
        shortCol.setCellValueFactory(
                new PropertyValueFactory<>("shortRatio"));

        table.setItems(data);
        table.getColumns().addAll(symbolCol, priceCol, prevCloseCol, change1YCol, change1MCol, change3MCol,
                divCol, epsCol, peCol, mktCapCol, shortCol);


        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();


    }

    public ObservableList<TableModelMaster> getData() {
        return data;
    }

    public void setData(ObservableList<TableModelMaster> data) {
        this.data = data;
    }

}

