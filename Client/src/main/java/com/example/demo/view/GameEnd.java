package com.example.demo.view;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.Civilization;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class GameEnd implements Initializable {

    public Pane upperMapPane;
    public ImageView background;
    private Civilization winner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        background.setImage(ImageLoader.get("treeNoLine"));
        background.setFitHeight(StageController.getScene().getHeight());
        background.setFitWidth(StageController.getScene().getHeight() * 5760 / 1080);
        Platform.runLater(this::runLater);
    }

    private void runLater()
    {
        Text text = new Text("The winner: " +
                GameController.getWinner().getUser().getNickname() +
                " with " + GameController.getWinner().getScore() + " scores");
        text.setFont(Font.font(40));
        text.setFill(Color.WHITE);
        text.setX(StageController.getStage().getWidth()*0.25);
        text.setY(StageController.getStage().getHeight()*0.10);
        upperMapPane.getChildren().add(text);

        Button button = new Button("back to main menu");
        button.setLayoutY(StageController.getStage().getHeight()*0.8);
        button.setLayoutX(StageController.getStage().getWidth()*0.05);
        upperMapPane.getChildren().add(button);
        button.setOnMouseClicked(event -> StageController.sceneChanger("mainMenu.fxml"));
    }

    public void setWinner(Civilization winner) {
        this.winner = winner;
    }
}
