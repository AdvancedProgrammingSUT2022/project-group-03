package com.example.demo;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.Units.Unit;
import com.example.demo.view.ImageLoader;
import com.example.demo.view.InfoController;
import com.example.demo.view.StageController;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class UnitsPanel implements Initializable {

    public Pane upperMapPane;
    public ImageView background;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        background.setImage(ImageLoader.get("treeNoLine"));
        background.setFitHeight(StageController.getScene().getHeight());
        background.setFitWidth(StageController.getScene().getHeight() * 5760 / 1080);
        Platform.runLater(this::runLater);
    }
    private void runLater(){
        int i = 0;
        for (Unit unit : GameController.getCivilizations().get(GameController.getPlayerTurn()).getUnits()) {
            Text text = new Text(InfoController.printUnitInfo(unit));
            text.setLayoutX(StageController.getStage().getWidth()*0.1);
            text.setFont(Font.font(25));
            text.setLayoutY(50 + i*45);
            text.setFill(Color.WHITE);
            text.setCursor(Cursor.HAND);
            i++;
            upperMapPane.getChildren().add(text);
            text.setOnMouseClicked(event -> {
                StageController.sceneChanger("game.fxml");
            });
        }

    }
}
