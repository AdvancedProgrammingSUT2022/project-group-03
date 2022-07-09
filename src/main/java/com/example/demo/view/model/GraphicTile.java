package com.example.demo.view.model;

import com.example.demo.HelloApplication;
import com.example.demo.model.features.Feature;
import com.example.demo.model.resources.ResourcesTypes;
import com.example.demo.model.tiles.Tile;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.Serializable;

public class GraphicTile implements Serializable {
    private final Tile tile;
    private final ImageView tileImage;
    private ImageView resourceImage;
    private ImageView featureImage;

    public GraphicTile(Tile tile, Pane pane) {
        this.tile = tile;

        //load tile
        tileImage = new ImageView(new Image(HelloApplication.getResource("/com/example/demo/tiles/" + tile.getTileType() + ".png"), 150, 0, true, true, true));
        tileImage.setFitHeight(75);
        tileImage.setFitWidth(120);
        tileImage.setOnMouseClicked(mouseEvent -> clicked());
        pane.getChildren().add(tileImage);

        //load feature
        Feature feature = tile.getContainedFeature();
        if (feature != null) {
            featureImage = new ImageView(new Image(HelloApplication.getResource("/com/example/demo/features/" + feature.getFeatureType() + ".png"), 150, 0, true, true, true));
            featureImage.setFitHeight(75);
            featureImage.setFitWidth(120);
            featureImage.setOnMouseClicked(mouseEvent -> clicked());
            pane.getChildren().add(featureImage);
        }

        //load resource
        ResourcesTypes resource = tile.getResource();
        if (resource != null) {
            resourceImage = new ImageView(new Image(HelloApplication.getResource("/com/example/demo/resources/" + resource + ".png"), 30, 0, true, true, true));
            resourceImage.setFitHeight(30);
            resourceImage.setFitWidth(30);
            resourceImage.setOnMouseClicked(mouseEvent -> clicked());
            pane.getChildren().add(resourceImage);
        }
    }

    public void setPosition(double x, double y) {
        //tile
        tileImage.setLayoutX(x);
        tileImage.setLayoutY(y);
        //feature
        if (featureImage != null) {
            featureImage.setLayoutX(x);
            featureImage.setLayoutY(y);
        }
        //resource
        if (resourceImage != null) {
            resourceImage.setLayoutX(x + 40);
            resourceImage.setLayoutY(y + 40);
        }
    }

    public double getWidth() {
        return tileImage.getFitWidth();
    }

    public double getHeight() {
        return tileImage.getFitHeight();
    }

    private void clicked() {
        //TODO: If we click on a tile this methode runs...
        System.out.println(tile.getTileType());
    }

    public Tile getTile() {
        return tile;
    }
}
