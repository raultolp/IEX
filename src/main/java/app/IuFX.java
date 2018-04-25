package app;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Objects;

import java.util.Set;

//LightSlateGray 	#778899
//LightSlateGrey 	#778899
//LightSteelBlue 	#B0C4DE
//SeaShell 	#FFF5EE
//SteelBlue 	#4682B4

public class IuFX extends Application {

    @Override

    public void start(Stage stage) {
        BorderPane borderPane = new BorderPane();

        BorderPane bottomBP = new BorderPane();
        bottomBP.setPadding(new Insets(5, 5, 5, 5));

        GridPane centerGrid = new GridPane();
        centerGrid.setVgap(8);
        centerGrid.setHgap(10);


        centerGrid.getChildren().addAll(addListView());

        Button start = new JFXButton("Start");
        start.setStyle("-fx-background-color:'#B0C4DE'");
        bottomBP.setCenter(start);


        borderPane.setCenter(centerGrid);
        borderPane.setBottom(bottomBP);

        Scene scene = new Scene(borderPane, 200, 400);

       // start.setOnMouseClicked(event ->);


        stage.setTitle("Start");
        stage.setScene(scene);
        stage.show();
    }

    private JFXListView<String> addListView() {
        JFXListView<String> gameList = new JFXListView<>();
        ObservableList<String> items = FXCollections.observableArrayList(
                listFilesForFolder());
        gameList.setItems(items);

        gameList.cellFactoryProperty();

        return gameList;
    }

    //võtab kõik failid kaustast nimega Games
    public static Set<String> listFilesForFolder() {
        File gameFolder = new File("Games");
        Set<String> games = new HashSet<>();

        for (final File fileEntry : Objects.requireNonNull(gameFolder.listFiles())) {
            if (!fileEntry.getName().startsWith("."))
                games.add(fileEntry.getName());
        }
        return games;
    }


    public static void main(String args[]) {
        launch(args);
        System.out.println(listFilesForFolder());
    }

}
