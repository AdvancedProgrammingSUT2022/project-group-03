package com.example.demo.view;

import com.example.demo.HelloApplication;
import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.User;
import com.example.demo.model.features.Feature;
import com.example.demo.model.features.FeatureType;
import com.example.demo.model.tiles.Tile;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class GameControllerFX {
    @FXML
    private AnchorPane anchorPane;

    public void initialize() {
        startAFakeGame();
        buildMap(anchorPane);
    }

    private void buildMap(AnchorPane anchorPane) {
        Tile[][] map = GameController.getMap().getTiles();
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 14; i++) {
                ImageView tileImage = loadTile(map[i][j].getTileType().toString(), Tile.class);
                double positionX = 20 + (tileImage.getFitWidth() * 3 / 2) * i / 2;
                double positionY = 20 + (tileImage.getFitHeight() * j);
                if (i % 2 != 0) //odd columns
                    positionY += tileImage.getFitHeight() / 2;
                tileImage.setLayoutX(positionX);
                tileImage.setLayoutY(positionY);
                anchorPane.getChildren().add(tileImage);
                Feature feature = map[i][j].getContainedFeature();
                if (feature != null) {
                    ImageView featureImage = loadTile(feature.getFeatureType().toString(), Feature.class);
                    featureImage.setLayoutX(positionX);
                    featureImage.setLayoutY(positionY);
                    anchorPane.getChildren().add(featureImage);
                }
            }
        }
    }

    private ImageView loadTile(String name, Object type) {
        ImageView imageView;
        if (type.equals(Tile.class))
            imageView = new ImageView(new Image(HelloApplication.getResource("/com/example/demo/tiles/" + name + ".png")));
        else if (type.equals(Feature.class))
            imageView = new ImageView(new Image(HelloApplication.getResource("/com/example/demo/features/" + name + ".png")));
        else
            throw new RuntimeException("Not allowed type in 'loadTile'.");
        imageView.setFitHeight(75);
        imageView.setFitWidth(120);
        return imageView;
    }


    /*
     * This methode is only for testing
     */
    private void startAFakeGame() {
        User user = new User("Sayyed", "ali", "Tayyeb");
        User user2 = new User("Sayyed2", "ali", "Tayyeb");
        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        GameController.startGame(users);
    }
}
