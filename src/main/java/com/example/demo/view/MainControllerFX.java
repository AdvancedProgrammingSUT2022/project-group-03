package com.example.demo.view;

import com.example.demo.HelloApplication;
import com.example.demo.controller.NetworkController;
import com.example.demo.controller.Music;
import com.example.demo.controller.gameController.GameController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainControllerFX implements Initializable {
    public Button gameMenuButton;
    public Button profileMenuButton;
    public Button scoreBoardButton;
    public Button chatButton;
    public Button logoutButton;
    public ImageView background;
    public Button resumeButton;

    @FXML
    public void gameMenu() {
        if (GameController.getMap() != null) {
            Alert alert = new Alert(Alert.AlertType.NONE, "You have a paused game and starting a new game deletes the paused game. Do you like to save your paused game?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.initOwner(StageController.getStage());
            alert.setTitle("Paused game");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() == ButtonType.CANCEL)
                return;
            if (result.get() == ButtonType.YES) {
                SavingHandler.save(true);
            }
            GameController.setMap(null);
            StageController.sceneChanger("gameEntryMenu.fxml");
        } else {
            StageController.sceneChanger("gameEntryMenu.fxml");
        }
    }

    @FXML
    public void profileMenu()
    {
        NetworkController.send("menu enter profile");
        AssetsController.openLeadersAvatars();
        StageController.sceneChanger("profileMenu.fxml");
    }
    @FXML
    public void scoreBoard()
    {
            NetworkController.send("menu enter profile");
            AssetsController.openLeadersAvatars();
            StageController.sceneChanger("scoreboard.fxml");
    }

    @FXML
    public void chat() {
        StageController.sceneChanger("chat.fxml");
    }

    @FXML
    public void logout() {
        if (GameController.getMap() != null) {
            Alert alert = new Alert(Alert.AlertType.NONE, "You have a paused game and logging out deletes this game. Do you like to save your game?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.initOwner(StageController.getStage());
            alert.setTitle("Paused game");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() == ButtonType.CANCEL)
                return;
            if (result.get() == ButtonType.YES) {
                SavingHandler.save(true);
            }
            GameController.setMap(null);
            NetworkController.send("menu exit");
            NetworkController.deleteToken();
            StageController.sceneChanger("loginMenu.fxml");
        } else {
            NetworkController.send("menu exit");
            NetworkController.deleteToken();
            StageController.sceneChanger("loginMenu.fxml");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (GameController.getMap() != null) {
            resumeButton.setDisable(false);
        }
        Platform.runLater(this::runLater);
    }

    public void runLater() {

//        gameMenuButton.setPrefWidth(81 * StageController.getStage().getWidth()/1920);
//        profileMenuButton.setPrefWidth(84 * StageController.getStage().getWidth()/1920);
//        scoreBoardButton.setPrefWidth(77 * StageController.getStage().getWidth()/1920);
//        chatButton.setPrefWidth(41 * StageController.getStage().getWidth()/1920);
//        logoutButton.setPrefWidth(54 * StageController.getStage().getWidth()/1920);

        gameMenuButton.setLayoutY(StageController.getStage().getHeight() * 0.19);
        profileMenuButton.setLayoutY(StageController.getStage().getHeight() * 0.22);
        scoreBoardButton.setLayoutY(StageController.getStage().getHeight() * 0.25);
        chatButton.setLayoutY(StageController.getStage().getHeight() * 0.28);
        resumeButton.setLayoutY(StageController.getStage().getHeight() * 0.31);
        logoutButton.setLayoutY(StageController.getStage().getHeight() * 0.012);

        gameMenuButton.setLayoutX(StageController.getScene().getWidth() * 0.16 - gameMenuButton.getWidth() / 2);
        profileMenuButton.setLayoutX(StageController.getScene().getWidth() * 0.16 - profileMenuButton.getWidth() / 2);
        scoreBoardButton.setLayoutX(StageController.getScene().getWidth() * 0.16 - scoreBoardButton.getWidth() / 2);
        chatButton.setLayoutX(StageController.getScene().getWidth() * 0.16 - chatButton.getWidth() / 2);
        resumeButton.setLayoutX(StageController.getScene().getWidth() * 0.16 - resumeButton.getWidth() / 2);
        logoutButton.setLayoutX(StageController.getScene().getWidth() - logoutButton.getWidth() * 1.5);
        background.setFitWidth(StageController.getScene().getWidth());
        background.setFitHeight(StageController.getScene().getHeight());
    }

    public void resume() {
        StageController.sceneChanger("game.fxml");
        Music.play("game");
    }
}
