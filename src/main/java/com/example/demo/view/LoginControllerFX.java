package com.example.demo.view;

import com.example.demo.HelloApplication;
import com.example.demo.controller.LoginController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class LoginControllerFX {
    @FXML
    private TextField username, password, nickname;

    @FXML
    Pane pane;

    @FXML
    private void register() {
        switch (LoginController.createNewUser(username.getText(),
                password.getText(), nickname.getText())) {
            case 0 -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("what a dumb username");
                alert.setContentText("user created successfully");
                alert.showAndWait();
            }
            case 1 -> errorMaker("Input not valid",
                    "type something dumbAss");
            case 2 -> errorMaker("Input not valid",
                    "a user with this username already exists");
            case 3 -> errorMaker("Input not valid",
                    "a user with this nickname already exists");
            case 4 -> errorMaker("Input not valid",
                    "nice joke. now please insert some real password");
        }
    }

    @FXML
    private void login() throws IOException {

        switch (LoginController.loginUser(username.getText(),
                password.getText())) {
            case 0 -> HelloApplication.sceneChanger("mainMenu.fxml");
            case 1 -> errorMaker("Input not valid",
                    "type something dumbAss");
            case 2 -> errorMaker("Input not valid",
                    "username or password is incorrect");
        }
    }


    public void errorMaker(String header, String content) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(header);
        errorAlert.setContentText(content);
        errorAlert.showAndWait();
    }
}
