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
        StageController.sceneChanger("gameEntryMenu.fxml");
    }
    @FXML
    public void profileMenu()
    {
        AssetsController.openLeadersAvatars();
        StageController.sceneChanger("profileMenu.fxml");
    }
    @FXML
    public void scoreBoard()
    {

    }
    @FXML
    public void chat()
    {
        StageController.sceneChanger("chat.fxml");
    }
    @FXML
    public void logout() {
        StageController.sceneChanger("loginMenu.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(this::runLater);
    }

    public void runLater()
    {

//        gameMenuButton.setPrefWidth(81 * StageController.getStage().getWidth()/1920);
//        profileMenuButton.setPrefWidth(84 * StageController.getStage().getWidth()/1920);
//        scoreBoardButton.setPrefWidth(77 * StageController.getStage().getWidth()/1920);
//        chatButton.setPrefWidth(41 * StageController.getStage().getWidth()/1920);
//        logoutButton.setPrefWidth(54 * StageController.getStage().getWidth()/1920);

        gameMenuButton.setLayoutY(StageController.getStage().getHeight()*0.19);
        profileMenuButton.setLayoutY(StageController.getStage().getHeight()*0.22);
        scoreBoardButton.setLayoutY(StageController.getStage().getHeight()*0.25);
        chatButton.setLayoutY(StageController.getStage().getHeight()*0.28);
        logoutButton.setLayoutY(StageController.getStage().getHeight()*0.012);

        gameMenuButton.setLayoutX(StageController.getScene().getWidth()*0.16- gameMenuButton.getWidth()/2);
        profileMenuButton.setLayoutX(StageController.getScene().getWidth()*0.16- profileMenuButton.getWidth()/2);
        scoreBoardButton.setLayoutX(StageController.getScene().getWidth()*0.16- scoreBoardButton.getWidth()/2);
        chatButton.setLayoutX(StageController.getScene().getWidth()*0.16- chatButton.getWidth()/2);
        logoutButton.setLayoutX(StageController.getScene().getWidth()- logoutButton.getWidth()*1.5);
        background.setFitWidth(StageController.getScene().getWidth());
        background.setFitHeight(StageController.getScene().getHeight());
    }
}
