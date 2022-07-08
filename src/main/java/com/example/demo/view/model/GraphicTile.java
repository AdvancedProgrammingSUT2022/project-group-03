package com.example.demo.view.model;

import com.example.demo.HelloApplication;
import com.example.demo.model.features.Feature;
import com.example.demo.model.tiles.Tile;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

import java.io.Serializable;

public class GraphicTile implements Serializable {
    private final Tile tile;
    private final ImageView tileImage;
    private ImageView featureImage;

    public GraphicTile(Tile tile, Pane pane) {
        this.tile = tile;

        tileImage = new ImageView(new Image(HelloApplication.getResource("/com/example/demo/tiles/" + tile.getTileType() + ".png"), 150, 0, true, true, true));
        tileImage.setFitHeight(75);
        tileImage.setFitWidth(120);
        tileImage.setOnMouseClicked(mouseEvent -> clicked());
        pane.getChildren().add(tileImage);

        Feature feature = tile.getContainedFeature();
        if (feature != null) {
            featureImage = new ImageView(new Image(HelloApplication.getResource("/com/example/demo/features/" + feature.getFeatureType() + ".png"), 150, 0, true, true, true));
            featureImage.setFitHeight(75);
            featureImage.setFitWidth(120);
            featureImage.setOnMouseClicked(mouseEvent -> clicked());
            pane.getChildren().add(featureImage);
        }
    }

    public void setPosition(double x, double y) {
        tileImage.setLayoutX(x);
        tileImage.setLayoutY(y);
        if (featureImage != null) {
            featureImage.setLayoutX(x);
            featureImage.setLayoutY(y);
        }
    }

    public double getWidth() {
        return tileImage.getFitWidth();
    }

    public double getHeight() {
        return tileImage.getFitHeight();
    }

    private void clicked() {
        System.out.println(tile.getTileType());
    }

    public Tile getTile() {
        return tile;
    }

    public ImageView getTileImage() {
        return tileImage;
    }

    public ImageView getFeatureImage() {
        return featureImage;
    }
}
