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
    private ImageView nonCivilianUnitImage;
    private ImageView civilianUnitImage;

    public GraphicTile(Tile tile, Pane pane) {
        this.tile = tile;

        //load tile
        tileImage = new ImageView(ImageLoader.get(tile.getTileType().toString()));
        tileImage.setFitHeight(103);
        tileImage.setFitWidth(120);
        tileImage.setOnMouseReleased(this::clicked);
        pane.getChildren().add(tileImage);

        //load feature
        Feature feature = tile.getContainedFeature();
        if (feature != null) {
            featureImage = new ImageView(ImageLoader.get(feature.getFeatureType().toString()));
            featureImage.setFitHeight(103);
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
        if(tile.getNonCivilian()!=null)
        {
            nonCivilianUnitImage = new ImageView(ImageLoader.get(tile.getNonCivilian().getUnitType().toString()));
            nonCivilianUnitImage.setFitHeight(50);
            nonCivilianUnitImage.setFitWidth(50);
            nonCivilianUnitImage.setOnMouseClicked(this::clicked);
            civilianUnitImage.setViewOrder(-1);
            pane.getChildren().add(nonCivilianUnitImage);
        }
        if(tile.getCivilian()!=null)
        {
            civilianUnitImage = new ImageView(ImageLoader.get(tile.getCivilian().getUnitType().toString()));
            civilianUnitImage.setFitHeight(40);
            civilianUnitImage.setFitWidth(40);
            civilianUnitImage.setOnMouseClicked(this::clicked);
            civilianUnitImage.setViewOrder(-1);
            pane.getChildren().add(civilianUnitImage);
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
            resourceImage.setLayoutX(x + tileImage.getFitWidth()/2 - resourceImage.getFitWidth()/2);
            resourceImage.setLayoutY(y + 50);
        }
        if(nonCivilianUnitImage !=null)
        {
            nonCivilianUnitImage.setLayoutX(x + tileImage.getFitWidth()*3.5/5 - nonCivilianUnitImage.getFitWidth()/2);
            nonCivilianUnitImage.setLayoutY(y+tileImage.getFitHeight()*3.5/5 - nonCivilianUnitImage.getFitHeight()/2);
        }
        if(civilianUnitImage !=null)
        {
            civilianUnitImage.setLayoutX(x + tileImage.getFitWidth()*1.5/5 - civilianUnitImage.getFitWidth()/2);
            civilianUnitImage.setLayoutY(y+tileImage.getFitHeight()*1.5/5 - civilianUnitImage.getFitHeight()/2);
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
