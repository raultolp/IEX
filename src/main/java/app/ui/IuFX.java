package app.ui;

import app.Iu;
import app.Portfolio;
import app.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

//LightSlateGray 	#778899
//LightSlateGrey 	#778899
//LightSteelBlue 	#B0C4DE
//SeaShell 	#FFF5EE
//SteelBlue 	#4682B4

public class IuFX extends Application {
    private Iu iu = new Iu();
    private Iu handler;
    private CreateUser CU;
    private UserInterface UI;
    private UserPortfolio UP;
    private String selectedGame = "";
    private StockGraphPopup graphPopup;
    private ListView<String> savedGames = addListView();
    private Portfolio userPortfolio;
    //private List<User> userList = handler.getUserList();
    private String gameTitle;

    public IuFX() throws Exception {
        this.UI = new UserInterface(this);
        this.CU = new CreateUser(this);
        this.UP = new UserPortfolio(this);
        this.handler = iu;
        this.userPortfolio = handler.getActiveUser().getPortfolio();

    }


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
        centerBox.getChildren().addAll(gameTitle, savedGames);

        centerGrid.getChildren().add(centerBox);

        Button start = new JFXButton("Start");
        start.setStyle("-fx-background-color:'#B0C4DE'");

       Button create = new JFXButton("New game");
        create.setStyle("-fx-background-color: '#B0C4DE'");

        buttonHBox.setSpacing(10.0);
        buttonHBox.getChildren().addAll(start);


        bottomBP.getChildren().addAll(buttonHBox);
        borderPane.setCenter(centerGrid);
        borderPane.setBottom(bottomBP);

        Scene scene = new Scene(borderPane, 200, 400);


        start.setOnMouseClicked(event -> {
                    try {
                        fxLoadData();
                        System.out.println(handler.getUserList());
                        UI.getStage().setTitle("Master portfolio");
                        UI.getStage().show();
                        stage.hide();
                        System.out.println("Start " + selectedGame);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("@ start");
                    }

                }
        );

        create.setOnAction(event -> {
            try {
                if (selectedGame == null || !gameTitle.getText().equals("")) {
                    saveData(gameTitle.getText());
                } else fxLoadData();
                UI.getStage().show();
                stage.hide();
                //start game
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("@ create");
            }
        });


        stage.setTitle("Start");
        stage.setScene(scene);
        stage.show();
    }

    private JFXListView<String> addListView() {
        JFXListView<String> gameList = new JFXListView<>();
        final ObservableList<String> items = FXCollections.observableArrayList(
                listFilesForFolder());
        gameList.setItems(items);

        gameList.cellFactoryProperty();

        gameList.setOnMouseClicked(event -> selectedGame = gameList.getSelectionModel().getSelectedItem());

        return gameList;
    }


    //võtab kõik failid kaustast nimega Games
    public static Set<String> listFilesForFolder() {
        File gameFolder = new File("Games");
        Set<String> games = new HashSet<>();

        for (final File fileEntry : Objects.requireNonNull(gameFolder.listFiles())) {
            if (!fileEntry.getName().startsWith("."))
                games.add(fileEntry.getName());
            //System.out.println(fileEntry.getName());
        }
        return games;
    }

    public void saveData(String filename) throws IOException {
        File directory = new File("Games");
        if (!directory.exists()) {
            directory.mkdir();
        }
        try {
            File file = new File("Games" + "/" + filename);
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            file = new File(filename);
            Iu iu = new Iu();

            for (User user : iu.getUserList()) {
                writer.write(user.getPortfolio().toString());
            }
            writer.close();
            iu.setActiveGame(file);
        } catch (IOException io) {
            io.printStackTrace();
            System.exit(1);
        }
    }

    public void fxLoadData() throws IOException {
        Set<String> fileNames = listFilesForFolder();
        System.out.println(fileNames);
        List<User> newUserList = new ArrayList<>();
        //If file does not exist:
        if (!fileNames.contains(selectedGame)) {
            saveData(selectedGame);
        }

        //If file exists: loading data from file:
        else {
            File file = new File(selectedGame);
            try ( BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream
                    (selectedGame), "UTF-8")) ) {

                //Converting data to JSON object:
                String gameAsString = br.readLine();
                JsonParser jp = new JsonParser();
                JsonObject gameObj = jp.parse(gameAsString).getAsJsonObject(); //from String to json

                //Creating users (with their portfolios, positions and transactions):


                for (String username : gameObj.keySet()) {
                    JsonObject userObj = gameObj.get(username).getAsJsonObject();
                    User newUser = new User(username, userObj, handler);
                    newUserList.add(newUser);
                }
                handler.setUserList(newUserList);
            } finally {
                handler.setActiveGame(file);
            }
        }

    }

    public CreateUser getCU() {
        return CU;
    }

    public Iu getHandler() {
        return iu;
    }

    public UserInterface getUI() {
        return UI;
    }

    public UserPortfolio getUP() {
        return UP;
    }

    public Portfolio getUserPortfolio() {
        return userPortfolio;
    }

    public String getSelectedGame() {
        return selectedGame;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public static void main(String args[]) {
        launch(args);
        System.out.println(listFilesForFolder());
    }

}