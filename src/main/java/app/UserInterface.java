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

        GridPane gridPane = new GridPane();

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(addFlowPane());
        borderPane.setBottom(addBottomHBox());


        Scene scene = new Scene(borderPane, 600, 350);


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

    private FlowPane addFlowPane() {

        FlowPane flowPane = new FlowPane();
        flowPane.setPadding(new Insets(5, 0, 5, 0));
        flowPane.setVgap(4);
        flowPane.setHgap(4);
        flowPane.setPrefWrapLength(230);
        flowPane.setStyle("-fx-background-color: DAE6F3");


        String[] columnNames = {"Stock symbol", "Price", "Volume"
                , "Total", "Buy/sell", "Number"};


        flowPane.getChildren().addAll();

        return flowPane;
    }

    public ObservableList<Stock> getProduct() {
        Map<String, Stock> hm = portfolio.getPortfolio();

        ObservableList<Stock> stocks = FXCollections.observableArrayList();

        for (String key : hm.keySet()) {
            Stock stock = hm.get(key);
            stocks.add(stock);
        }
        return stocks;
    }


}
