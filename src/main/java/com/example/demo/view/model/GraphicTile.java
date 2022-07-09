package com.example.demo.view.model;

import com.example.demo.model.Units.Unit;
import com.example.demo.model.features.Feature;
import com.example.demo.model.resources.ResourcesTypes;
import com.example.demo.model.tiles.Tile;
import com.example.demo.view.ImageLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.Serializable;

public class GraphicTile implements Serializable {
    private final Tile tile;
    private final ImageView tileImage;
    private ImageView resourceImage;
    private ImageView featureImage;
    private ImageView civilianImage;
    private ImageView militaryImage;


    public GraphicTile(Tile tile, Pane pane) {
        this.tile = tile;

        //load tile
        tileImage = new ImageView(ImageLoader.get(tile.getTileType().toString()));
        tileImage.setFitHeight(75);
        tileImage.setFitWidth(120);
        tileImage.setOnMouseReleased(this::clicked);
        pane.getChildren().add(tileImage);

        //load feature
        Feature feature = tile.getContainedFeature();
        if (feature != null) {
            featureImage = new ImageView(ImageLoader.get(feature.getFeatureType().toString()));
            featureImage.setFitHeight(75);
            featureImage.setFitWidth(120);
            featureImage.setOnMouseReleased(this::clicked);
            pane.getChildren().add(featureImage);
        }

        //load resource
        ResourcesTypes resource = tile.getResource();
        if (resource != null) {
            resourceImage = new ImageView(ImageLoader.get(resource.toString()));
            resourceImage.setFitHeight(17);
            resourceImage.setFitWidth(17);
            resourceImage.setOnMouseReleased(this::clicked);
            pane.getChildren().add(resourceImage);
        }

        //load civilian units
        Unit civilian = tile.getCivilian();
        if (civilian != null) {
            civilianImage = new ImageView(ImageLoader.get(civilian.getName()));
            civilianImage.setFitHeight(80);
            civilianImage.setFitWidth(80);
            civilianImage.setOnMouseReleased(this::clicked);
            civilianImage.setViewOrder(-1);
            pane.getChildren().add(civilianImage);
        }

        //load military units
        Unit military = tile.getNonCivilian();
        if (military != null) {
            militaryImage = new ImageView(ImageLoader.get(military.getName()));
            militaryImage.setFitHeight(80);
            militaryImage.setFitWidth(80);
            militaryImage.setOnMouseReleased(this::clicked);
            militaryImage.setViewOrder(-1);
            pane.getChildren().add(militaryImage);
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
            resourceImage.setLayoutY(y + 50);
        }
        //civilian unit
        if (civilianImage != null) {
            civilianImage.setLayoutX(x + 50);
            civilianImage.setLayoutY(y + 60);
        }
        //military unit
        if (militaryImage != null) {
            militaryImage.setLayoutX(x - 10);
            militaryImage.setLayoutY(y + 60);
        }
    }

    public double getWidth() {
        return tileImage.getFitWidth();
    }

    public double getHeight() {
        return tileImage.getFitHeight();
    }

    private void clicked(MouseEvent mouseEvent) {
        //TODO: If we click on a tile this methode runs...
        System.out.println(tile.getTileType());
    }

    public Tile getTile() {
        return tile;
    }
}
