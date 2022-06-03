package com.example.demo.view;

import com.example.demo.HelloApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainControllerFX implements Initializable {
    public Button gameMenuButton;
    public Button profileMenuButton;
    public Button scoreBoardButton;
    public Button chatButton;
    public Button logoutButton;
    public ImageView background;

    @FXML
    public void gameMenu()
    {

    }
    @FXML
    public void profileMenu()
    {

    }
    @FXML
    public void scoreBoard()
    {

    }
    @FXML
    public void chat()
    {

    }
    @FXML
    public void logout() throws IOException {
        HelloApplication.sceneChanger("loginMenu.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() ->gameMenuButton.setLayoutX(HelloApplication.getScene().getWidth()/2- gameMenuButton.getWidth()/2));
        Platform.runLater(() ->profileMenuButton.setLayoutX(HelloApplication.getScene().getWidth()/2- profileMenuButton.getWidth()/2));
        Platform.runLater(() ->scoreBoardButton.setLayoutX(HelloApplication.getScene().getWidth()/2- scoreBoardButton.getWidth()/2));
        Platform.runLater(() ->chatButton.setLayoutX(HelloApplication.getScene().getWidth()/2- chatButton.getWidth()/2));
        Platform.runLater(() ->logoutButton.setLayoutX(HelloApplication.getScene().getWidth()- logoutButton.getWidth()*1.5));
        Platform.runLater(() ->background.setFitWidth(HelloApplication.getScene().getWidth()));
        Platform.runLater(() ->background.setFitHeight(HelloApplication.getScene().getHeight()));
    }
}
