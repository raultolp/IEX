package app;

//LightSlateGray 	#778899
//LightSlateGrey 	#778899
//LightSteelBlue 	#B0C4DE
//SeaShell 	#FFF5EE
//SteelBlue 	#4682B4

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.util.*;
import java.util.Map.Entry;


public class UserInterface extends Application {
    Portfolio portfolio;

    Object[][] data = {
            {"César Cielo", "Filho", "Brazil", "50m freestyle", 1, "21.30"},
            {"Amaury", "Leveaux", "France", "50m freestyle", 2, "21.45"}};


    public static void main(String[] args) {


        {
            launch(args);
        }
    }

    @Override
    public void start(Stage stage) {

        GridPane gridPane = new GridPane();
        /*gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);*/

        StackPane stackPane = new StackPane();

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
        // Map<String, Stock> hm = portfolio.getPortfolio();

        FlowPane flowPane = new FlowPane();
        flowPane.setPadding(new Insets(5, 0, 5, 0));
        flowPane.setVgap(4);
        flowPane.setHgap(4);
        flowPane.setPrefWrapLength(230);
        flowPane.setStyle("-fx-background-color: DAE6F3");


        /*for (String key : hm.keySet()) {
            Stock stock = hm.get(key);
            double price = stock.getLatestPrice();

        }*/

        String[] columnNames = {"Stock symbol", "Price", "Volume"
                , "Total", "Buy/sell", "Number"};


        flowPane.getChildren().addAll();

        return flowPane;
    }


}
