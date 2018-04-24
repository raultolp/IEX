package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StockGraphPopup extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage, Stock stock) throws Exception {
        String nimi;
        nimi = stock.getSymbol();

        BorderPane borderPane = new BorderPane();

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Price");
        yAxis.setLabel("Time period(months)");
        //teeb graafiku
        final LineChart<Number, Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        lineChart.setTitle(nimi);

        XYChart.Series series = new XYChart.Series();
        series.setName(nimi);

        //paneme mingid suvalised andmed
        series.getData().add(new XYChart.Data(1, 2));


        //teine veel
        XYChart.Series series2 = new XYChart.Series();
        series2.setName(nimi + nimi);
        series2.getData().add(new XYChart.Data(9, 33));

        Scene scene = new Scene(borderPane, 750, 600);
        lineChart.getData().addAll(series, series2);

        //Button closePopup = new Button("Close");
        //closePopup.setOnAction(event -> stage.hide());

        borderPane.setCenter(lineChart);


        stage.setTitle(nimi);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = new BorderPane();

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Price");
        yAxis.setLabel("Time period (months)");
        //teeb graafiku
        final LineChart<Number, Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("nimi");

        XYChart.Series series = new XYChart.Series();
        series.setName("nimi");

        //paneme mingid suvalised andmed
        series.getData().add(new XYChart.Data(1, 2));


        //teine veel
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("nimi + nimi");
        series2.getData().add(new XYChart.Data(9, 33));

        Scene scene = new Scene(borderPane, 750, 600);
        lineChart.getData().addAll(series, series2);

        Button closePopup = new Button("Close");
        closePopup.setOnAction(event -> stage.hide());

        borderPane.setCenter(lineChart);


        stage.setTitle("nimi");
        stage.setScene(scene);
        stage.show();
    }
}