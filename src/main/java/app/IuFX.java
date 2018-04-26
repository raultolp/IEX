package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
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

        GridPane bottomBP = new GridPane();
        HBox buttonHBox = new HBox();
        bottomBP.setPadding(new Insets(5, 10, 5, 10));

        GridPane centerGrid = new GridPane();
        centerGrid.setVgap(8);
        centerGrid.setHgap(10);

        TextField gameTitle = new JFXTextField();
        gameTitle.setPromptText("Enter title");

        VBox centerBox = new VBox();
        centerBox.setSpacing(20);
        centerBox.getChildren().addAll(gameTitle, addListView());

        centerGrid.getChildren().add(centerBox);

        Button start = new JFXButton("Start");
        start.setStyle("-fx-background-color:'#B0C4DE'");

        Button create = new JFXButton("New game");
        create.setStyle("-fx-background-color: '#B0C4DE'");

        buttonHBox.setSpacing(10.0);
        buttonHBox.getChildren().addAll(start, create);


        bottomBP.getChildren().addAll(buttonHBox);
        borderPane.setCenter(centerGrid);
        borderPane.setBottom(bottomBP);

        Scene scene = new Scene(borderPane, 200, 400);

        //start.setOnMouseClicked(event ->);


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

    private Popup gamePopup() {
        Popup popup = new Popup();
        return popup;
    }


    public static void main(String args[]) {
        launch(args);
        System.out.println(listFilesForFolder());
    }

}
