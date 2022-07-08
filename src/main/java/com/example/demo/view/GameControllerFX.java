package com.example.demo.view;

import com.example.demo.HelloApplication;
import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.tiles.Tile;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class GameControllerFX {
    @FXML
    private AnchorPane anchorPane;

    public void initialize() {
        buildMap(anchorPane);
    }

    private void buildMap(AnchorPane anchorPane) {
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 14; i++) {
                ImageView imageView = loadTile("desert");
                double x = 20 + (imageView.getFitWidth() * 3 / 2) * i / 2;
                double y = 20 + (imageView.getFitHeight() * j);
                if (i % 2 != 0) //odd columns
                    y += imageView.getFitHeight() / 2;
                imageView.setLayoutX(x);
                imageView.setLayoutY(y);
                anchorPane.getChildren().add(imageView);
            }
        }
    }

    private ImageView loadTile(String name) {
        ImageView imageView = new ImageView(new Image(HelloApplication.getResource("/com/example/demo/tiles/" + name + ".png")));
        imageView.setFitHeight(75);
        imageView.setFitWidth(120);
        return imageView;
    }
}
