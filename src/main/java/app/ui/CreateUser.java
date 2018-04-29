package app.ui;

import app.Iu;
import app.MyUtils;
import app.User;
import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CreateUser {
    IuFX FX;
    Scene scene;

    public CreateUser(IuFX iuFX) throws Exception {
        this.scene = buildScene();
        this.FX = iuFX;
    }
//https://www.callicoder.com/javafx-registration-form-gui-tutorial/

    public Scene buildScene() throws Exception {

        GridPane gridPane = registrationFormPane();

        addUIControls(gridPane); //add username fields etc

        Scene scene = new Scene(gridPane, 450, 200);

        return scene;
    }

    private GridPane registrationFormPane() {
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();

        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.CENTER);

        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap between columns
        gridPane.setHgap(10);

        // Set the vertical gap between rows
        gridPane.setVgap(10);

        // Add Column Constraints

        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);

        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200, 200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
    }

    private void addUIControls(GridPane gridPane) {
        // Add Header
        Label headerLabel = new Label("Registration Form");
        headerLabel.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD, 24));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

        // Add Name Label
        Label username = new Label("Username: ");
        username.setFont(Font.font("Times new roman", FontWeight.MEDIUM, 16));
        gridPane.add(username, 0, 1);

        // Add Name Text Field
        TextField nameField = new TextField();
        nameField.setPrefHeight(40);
        gridPane.add(nameField, 1, 1);


        // Add Submit Button
        Button submitButton = new JFXButton("Submit");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 4, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0, 20, 0));

        //SUBMIT HANDLER
        submitButton.setOnAction(e -> {
            if (nameField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your name");
            }

            try {
                fxAddUser(nameField.getText());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Registration Successful!", "Welcome " + nameField.getText());
        });

    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }


    private void fxAddUser(String name) throws IOException {

        List<User> newUserList = new ArrayList<>(FX.getHandler().getUserList());
        newUserList.add(new User(name, 100000, FX.getHandler()));
        MyUtils.colorPrintYellow("Created user: " + name);

        FX.getHandler().setUserList(newUserList);
    }


    public Scene getScene() {
        return scene;
    }
}

