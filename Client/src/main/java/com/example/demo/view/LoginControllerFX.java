package com.example.demo.view;

import com.example.demo.HelloApplication;
import com.example.demo.controller.LoginController;
import com.example.demo.controller.NetworkController;
import com.example.demo.model.User;
import com.google.gson.Gson;
import com.example.demo.controller.Music;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.demo.view.StageController.errorMaker;

public class LoginControllerFX implements Initializable {
    public Button loginButton;
    public Button registerButton;
    public ImageView background;
    @FXML
    private TextField username, nickname;
    @FXML
    private PasswordField password;

    @FXML
    private void register() {
        String response = NetworkController.send("user create" + " -u " + username.getText() + " -p " + password.getText() + " -n " +nickname.getText());
        switch ( Integer.parseInt(response)) {
            case 0 -> {
                StageController.errorMaker("what a dumb username", "user created successfully", Alert.AlertType.INFORMATION);
            }
            case 1 -> errorMaker("Input not valid",
                "type something dumbAss", Alert.AlertType.ERROR);
            case 2 -> errorMaker("Input not valid",
                "a user with this username already exists", Alert.AlertType.ERROR);
            case 3 -> errorMaker("Input not valid",
                "a user with this nickname already exists", Alert.AlertType.ERROR);
            case 4 -> errorMaker("Input not valid",
                "nice joke. now please insert some real password", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void login() {
        switch (Integer.parseInt(NetworkController.send("user login" + " -u " + username.getText() + " -p " + password.getText()))) {
            case 0 ->{
                LoginController.setLoggedUser(new Gson().fromJson(NetworkController.getResponse(true),User.class));
                NetworkController.setToken();
                StageController.sceneChanger("mainMenu.fxml");
            }
            case 1 -> errorMaker("Input not valid",
                    "type something dumbAss", Alert.AlertType.ERROR);
            case 2,3 -> errorMaker("Input not valid",
                    "username or password is incorrect", Alert.AlertType.ERROR);
            case 4 -> errorMaker("login failed",
                    "logged in already", Alert.AlertType.ERROR);

        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Music.play("menu");
        Platform.runLater(this::runLater);
    }

    public void runLater() {

//        username.setPrefWidth(150 * StageController.getStage().getWidth()/1920);
//        password.setPrefWidth(150 * StageController.getStage().getWidth()/1920);
//        nickname.setPrefWidth(150 * StageController.getStage().getWidth()/1920);
//        loginButton.setPrefWidth(43 * StageController.getStage().getWidth()/1920);
//        registerButton.setPrefWidth(57 * StageController.getStage().getWidth()/1920);

        username.setLayoutY(StageController.getStage().getHeight() * 0.476);
        password.setLayoutY(StageController.getStage().getHeight() * 0.509);
        nickname.setLayoutY(StageController.getStage().getHeight() * 0.541);
        loginButton.setLayoutY(StageController.getStage().getHeight() * 0.594);
        registerButton.setLayoutY(StageController.getStage().getHeight() * 0.631);

        username.setLayoutX(StageController.getScene().getWidth() * 0.24 - username.getWidth() / 2);
        password.setLayoutX(StageController.getScene().getWidth() * 0.24 - password.getWidth() / 2);
        nickname.setLayoutX(StageController.getScene().getWidth() * 0.24 - nickname.getWidth() / 2);
        loginButton.setLayoutX(StageController.getScene().getWidth() * 0.24 - loginButton.getWidth() / 2);
        registerButton.setLayoutX(StageController.getScene().getWidth() * 0.24 - registerButton.getWidth() / 2);
        background.setFitWidth(StageController.getScene().getWidth());
        background.setFitHeight(StageController.getScene().getHeight());
    }

    public void checkEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode().getName().equals("Enter"))
            login();
    }
}
