package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Pop extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        String nimi;
        //saab hiljem kuskilt võtta misiganes
        nimi = "AAPL";

        BorderPane borderPane = new BorderPane();

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Sth");
        yAxis.setLabel("Sth sth");
        //teeb graafiku
        final LineChart<Number, Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        lineChart.setTitle(nimi);

        XYChart.Series series = new XYChart.Series();
        series.setName(nimi);

        //paneme mingid suvalised andmed
        series.getData().add(new XYChart.Data(1, 2));
        series.getData().add(new XYChart.Data(2, 23));
        series.getData().add(new XYChart.Data(3, 15));
        series.getData().add(new XYChart.Data(4, 21));
        series.getData().add(new XYChart.Data(5, 34));
        series.getData().add(new XYChart.Data(6, 7));
        series.getData().add(new XYChart.Data(7, 2));
        series.getData().add(new XYChart.Data(8, 5));
        series.getData().add(new XYChart.Data(9, 23));

        //teine veel
        XYChart.Series series2 = new XYChart.Series();
        series2.setName(nimi + nimi);
        series2.getData().add(new XYChart.Data(9, 33));
        series2.getData().add(new XYChart.Data(8, 34));
        series2.getData().add(new XYChart.Data(7, 25));
        series2.getData().add(new XYChart.Data(6, 44));
        series2.getData().add(new XYChart.Data(5, 39));
        series2.getData().add(new XYChart.Data(4, 16));
        series2.getData().add(new XYChart.Data(1, 55));

        Scene scene = new Scene(borderPane, 750, 600);
        lineChart.getData().addAll(series, series2);

        //Button closePopup = new Button("Close");
        //closePopup.setOnAction(event -> stage.hide());

        borderPane.setCenter(lineChart);


        stage.setTitle(nimi);
        stage.setScene(scene);
        stage.show();

    }

}
