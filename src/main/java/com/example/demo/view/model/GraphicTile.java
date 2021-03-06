package com.example.demo.view.model;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.controller.gameController.UnitStateController;
import com.example.demo.model.Civilization;
import com.example.demo.model.Units.Unit;
import com.example.demo.model.Units.UnitState;
import com.example.demo.model.features.Feature;
import com.example.demo.model.features.FeatureType;
import com.example.demo.model.improvements.ImprovementType;
import com.example.demo.model.resources.ResourcesTypes;
import com.example.demo.model.technologies.TechnologyType;
import com.example.demo.model.tiles.Tile;
import com.example.demo.view.GameControllerFX;
import com.example.demo.view.ImageLoader;
import com.example.demo.view.StatusBarController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.Serializable;

public class GraphicTile implements Serializable {
    private Tile tile;
    private ImageView tileImage;
    private ImageView resourceImage;
    private ImageView featureImage;
    private ImageView nonCivilianUnitImage;
    private ImageView civilianUnitImage;
    private ImageView improvementImage;
    private ImageView roadImage;
    private ImageView cityImage;
    private ImageView fogImage;
    private final ImageView[] riversImages = new ImageView[6];
    private final VBox leftPanel;
    private final Pane pane;
    private final GameControllerFX controller;

    public GraphicTile(Tile tile, Pane pane, VBox leftPanel, GameControllerFX gameControllerFX) {
        this.controller = gameControllerFX;
        this.leftPanel = leftPanel;
        this.pane = pane;
        this.tile = tile;
        loadImages(tile, pane);
        setPosition(tile.getX(), tile.getY());
    }


    private void clicked() {
        if (GameControllerFX.getSelectingTile()) {
            GameController.setSelectedTile(tile);
            GameControllerFX.publicText.setText("Destination tile:  X=" + tile.getX() + " Y=" + tile.getY());
            return;
        }
        if (GameControllerFX.isWaitingToSelectTileToBuy()) {
            GameControllerFX.buyTile(tile);
        }
        if (GameControllerFX.isWaitingToSelectTileToAttackFromCity()) {
            GameControllerFX.attackTile(tile);
        }
        //TODO: If we click on a tile this methode runs...
        leftPanel.getChildren().clear();
        GameController.setSelectedUnit(null);
        Civilization.TileCondition[][] tileConditions = GameController.getCivilizations().get(GameController.getPlayerTurn()).getTileConditions();
        if (tileConditions[tile.getX()][tile.getY()] == null) {
            leftPanel.getChildren().add(new Text("Hidden tile"));
        } else {
            Text text = new Text("Tile: " + tile.getTileType());
            Text text1 = new Text("Have Feature: " + tile.getContainedFeature());
            Text text2 = new Text("Have Resource: " + tile.getResource());
            Text text3 = new Text("gold: " + tile.getTileType().gold + "\nfood: " + tile.getTileType().food);
            Text text4 = new Text("production: " + tile.getTileType().production);
            leftPanel.getChildren().addAll(text, text1, text2, text3, text4);
        }
    }

    private void addButton(String name, boolean clearAfterClick, EventHandler<ActionEvent> func) {
        Button bottom = new Button(name);
        bottom.setOnAction(actionEvent -> {
            func.handle(actionEvent);
            if (clearAfterClick)
                leftPanel.getChildren().clear();
        });
        leftPanel.getChildren().add(bottom);
    }

    private void civilianClicked(MouseEvent mouseEvent) {
        leftPanel.getChildren().clear();
        Unit unit = tile.getCivilian();
        GameController.setSelectedUnit(unit);
        Text title = new Text("Selected Unit: " + unit.getUnitType() + "\nUnit health: " + unit.getHealth());
        leftPanel.getChildren().add(title);

        //add move button
        addButton("Move", false, event -> {
            leftPanel.getChildren().clear();
            Text text = new Text("Click on the destination tile,\n      then click move.");
            GameControllerFX.setSelectingTile(true);
            leftPanel.getChildren().addAll(text, GameControllerFX.publicText);
            addButton("Move", true, event2 -> {
                int x = GameController.getSelectedTile().getX();
                int y = GameController.getSelectedTile().getY();
                if (!UnitStateController.unitMoveTo(x, y)) {
                    GameControllerFX.alert("Error", "Can not move to that tile");
                }
                GameControllerFX.setSelectingTile(false);
                controller.renderMap();
            });
            addButton("Cancel", true, event1 -> {
            });
        });

        if (unit.getState().equals(UnitState.AWAKE))
            addButton("Sleep", true, event -> UnitStateController.unitSleep());
        else
            addButton("Awake", true, event -> UnitStateController.unitChangeState(3));
        addButton("Delete", true, event -> {
            pane.getChildren().remove(civilianUnitImage);
            UnitStateController.unitDelete(unit);
            StatusBarController.update();
        });


        switch (unit.getUnitType()) {
            case SETTLER -> {
                addButton("Found City", true, event -> {
                    int code;
                    if ((code = UnitStateController.unitFoundCity("City")) == 0) {
                        GameController.getCivilizations().get(GameController.getPlayerTurn()).getUnits().remove(unit);
                        pane.getChildren().remove(civilianUnitImage);
                        civilianUnitImage = null;
                        controller.renderMap();
                    } else {
                        GameControllerFX.alert("Error", "Can not found a city.\ndetails - return code: " + code);
                    }
                });
            }
            case WORKER -> {
                Civilization civilization = GameController.getCivilizations().get(GameController.getPlayerTurn());
                if (tile.getRoad() == null)
                    addButton("Build Road", true, event -> UnitStateController.unitBuildRoad());
                if (tile.getRoad() == null && civilization.doesContainTechnology(TechnologyType.RAILROAD) == 1)
                    addButton("Build Rail Road", true, event -> UnitStateController.unitBuildRailRoad());
                if (tile.getRoad() != null)
                    addButton("Remove Road", true, event -> UnitStateController.unitRemoveFromTile(false));
                if (tile.getContainedFeature() != null) {
                    FeatureType featureType = tile.getContainedFeature().getFeatureType();
                    if (featureType.equals(FeatureType.JUNGLE) || featureType.equals(FeatureType.FOREST) || featureType.equals(FeatureType.SWAMP))
                        addButton("Remove Feature", true, event -> UnitStateController.unitRemoveFromTile(true));
                }
                //improvement building:
                for (ImprovementType improvementType : ImprovementType.values())
                    if (GameController.doesHaveTheRequiredTechnologyToBuildImprovement(improvementType, tile, civilization)
                        && GameController.canHaveTheImprovement(tile, improvementType))
                        addButton("Build " + improvementType, true, event -> UnitStateController.unitBuild(improvementType));
            }
        }
    }

    private void nonCivilianClicked(MouseEvent mouseEvent) {

    }

    public double getWidth() {
        return tileImage.getFitWidth();
    }

    public double getHeight() {
        return tileImage.getFitHeight();
    }

    public Tile getTile() {
        return tile;
    }

    private void loadImages(Tile tile, Pane pane) {
        //load clouds
        Civilization.TileCondition[][] tileConditions = GameController.getCivilizations().get(GameController.getPlayerTurn()).getTileConditions();
        if (tileConditions[tile.getX()][tile.getY()] == null) {
            this.tile = tile;
            tileImage = new ImageView(ImageLoader.get("CLOUD"));
            tileImage.setFitHeight(103);
            tileImage.setFitWidth(120);
            tileImage.setOnMouseReleased(event -> clicked());
            pane.getChildren().add(tileImage);
            return;
        }
        if (!tileConditions[tile.getX()][tile.getY()].getIsClear()) {
            tile = tileConditions[tile.getX()][tile.getY()].getOpenedArea();
            fogImage = new ImageView(ImageLoader.get("fog"));
            fogImage.setFitHeight(103);
            fogImage.setFitWidth(120);
            fogImage.setViewOrder(-2);
            fogImage.setOnMouseReleased(event -> clicked());
            pane.getChildren().add(fogImage);
        }
        this.tile = tile;
        //load tile
        tileImage = new ImageView(ImageLoader.get(tile.getTileType().toString()));
        tileImage.setFitHeight(103);
        tileImage.setFitWidth(120);
        tileImage.setOnMouseReleased(event -> clicked());
        pane.getChildren().add(tileImage);

        //load feature
        Feature feature = tile.getContainedFeature();
        if (feature != null) {
            featureImage = new ImageView(ImageLoader.get(feature.getFeatureType().toString()));
            featureImage.setFitHeight(103);
            featureImage.setFitWidth(120);
            featureImage.setOnMouseReleased(event -> clicked());
            pane.getChildren().add(featureImage);
        }

        //load resource
        ResourcesTypes resource = tile.getResource();
        if (resource != null) {
            resourceImage = new ImageView(ImageLoader.get(resource.toString()));
            resourceImage.setFitHeight(17);
            resourceImage.setFitWidth(17);
            resourceImage.setOnMouseReleased(event -> clicked());
            pane.getChildren().add(resourceImage);
        }

        //load nonCivilian unit
        if (tile.getNonCivilian() != null) {
            nonCivilianUnitImage = new ImageView(ImageLoader.get(tile.getNonCivilian().getUnitType().toString()));
            nonCivilianUnitImage.setFitHeight(80);
            nonCivilianUnitImage.setFitWidth(80);
            nonCivilianUnitImage.setOnMouseClicked(this::nonCivilianClicked);
            nonCivilianUnitImage.setViewOrder(-1);
            pane.getChildren().add(nonCivilianUnitImage);
        }

        //load civilian unit
        if (tile.getCivilian() != null) {
            civilianUnitImage = new ImageView(ImageLoader.get(tile.getCivilian().getUnitType().toString()));
            civilianUnitImage.setFitHeight(80);
            civilianUnitImage.setFitWidth(80);
            civilianUnitImage.setOnMouseClicked(this::civilianClicked);
            civilianUnitImage.setViewOrder(-1);
            pane.getChildren().add(civilianUnitImage);
        }

        //load improvements
        if (tile.getImprovement() != null && tile.getImprovement().getRemainedCost() <= 0) {
            improvementImage = new ImageView(ImageLoader.get(tile.getImprovement().getImprovementType().toString()));
            improvementImage.setFitWidth(40);
            improvementImage.setFitHeight(40);
            //set on mouse clicked ?
            pane.getChildren().add(improvementImage);
        }

        //load roads
        if (tile.getRoad() != null) {
            roadImage = new ImageView(ImageLoader.get(tile.getRoad().getImprovementType().toString()));
            roadImage.setFitWidth(40);
            roadImage.setFitHeight(40);
            pane.getChildren().add(roadImage);
        }

        //load cities
        if (tile.getCity() != null) {
            cityImage = new ImageView(ImageLoader.get("city"));
            cityImage.setFitHeight(40);
            cityImage.setFitWidth(40);
            pane.getChildren().add(cityImage);
        }

        //load rivers
        for (int i = 0; i < 6; i++) {
            if (tile.isRiverWithNeighbour(i)) {
                riversImages[i] = new ImageView(ImageLoader.get("riverDown"));
                riversImages[i].setFitHeight(8);
                riversImages[i].setFitWidth(78);
                riversImages[i].setRotate(120 + 60 * i);
                pane.getChildren().add(riversImages[i]);
            }
        }
    }

    public void setPosition(int i, int j) {
        double x = 20 + ((getWidth() * 3 / 2) * i / 2) * (1 + 2 / (Math.sqrt(3) * 15));
        double y = 20 + (getHeight() * j) * 16 / 15;
        if (i % 2 != 0) //odd columns
            y += getHeight() / 2 + getHeight() / 30;
        //tile
        tileImage.setLayoutX(x);
        tileImage.setLayoutY(y);
        //cloud fog
        //feature
        if (featureImage != null) {
            featureImage.setLayoutX(x);
            featureImage.setLayoutY(y);
        }
        //resource
        if (resourceImage != null) {
            resourceImage.setLayoutX(x + tileImage.getFitWidth() / 2 - resourceImage.getFitWidth() / 2);
            resourceImage.setLayoutY(y + 50);
        }
        //nonCivilian Unit
        if (nonCivilianUnitImage != null) {
            nonCivilianUnitSetPosition(x, y);
        }
        //civilian Unit
        if (civilianUnitImage != null) {
            civilianUnitSetPosition(x, y);
        }
        //improvement
        if (improvementImage != null) {
            improvementImage.setLayoutX(x + tileImage.getFitWidth() * 1.5 / 5 - improvementImage.getFitWidth() / 2);
            improvementImage.setLayoutY(y + tileImage.getFitHeight() * 3.5 / 5 - improvementImage.getFitHeight() / 2);
        }
        //road
        if (roadImage != null) {
            roadImage.setLayoutX(x + tileImage.getFitWidth() * 3.5 / 5 - roadImage.getFitWidth() / 2);
            roadImage.setLayoutY(y + tileImage.getFitHeight() * 3.5 / 5 - roadImage.getFitHeight() / 2);
        }
        //city
        if (cityImage != null) {
            cityImage.setLayoutX(x + tileImage.getFitWidth() / 2 - cityImage.getFitWidth() / 2);
            cityImage.setLayoutY(y + tileImage.getFitHeight() * 4 / 5 - cityImage.getFitHeight() / 2);
        }
        if (fogImage != null) {
            fogImage.setLayoutX(x);
            fogImage.setLayoutY(y);
        }
        //rivers
        for (int k = 0; k < 6; k++) {
            if (riversImages[k] != null) {
                switch (k) {
                    case 0 -> {
                        riversImages[k].setLayoutX(x + tileImage.getFitWidth() / 8 - riversImages[k].getFitWidth() / 2 - riversImages[k].getFitHeight() / 2 + 1);
                        riversImages[k].setLayoutY(y - riversImages[k].getFitHeight() / 2 + tileImage.getFitHeight() / 4 - 2);
                    }
                    case 1 -> {
                        riversImages[k].setLayoutX(x + tileImage.getFitWidth() / 2 - riversImages[k].getFitWidth() / 2);
                        riversImages[k].setLayoutY(y - riversImages[k].getFitHeight());
                    }
                    case 2 -> {
                        riversImages[k].setLayoutX(x + tileImage.getFitWidth() * 7 / 8 - riversImages[k].getFitWidth() / 2 + riversImages[k].getFitHeight() / 2);
                        riversImages[k].setLayoutY(y - riversImages[k].getFitHeight() / 2 + tileImage.getFitHeight() / 4 - 1);
                    }
                    case 3 -> {
                        riversImages[k].setLayoutX(x + tileImage.getFitWidth() * 7 / 8 - riversImages[k].getFitWidth() / 2 + riversImages[k].getFitHeight() / 2);
                        riversImages[k].setLayoutY(y + tileImage.getFitHeight() / 2 + tileImage.getFitHeight() / 4 - riversImages[k].getFitHeight() / 2 + 2);
                    }
                    case 4 -> {
                        riversImages[k].setLayoutX(x + tileImage.getFitWidth() / 2 - riversImages[k].getFitWidth() / 2);
                        riversImages[k].setLayoutY(y + tileImage.getFitHeight());
                    }
                    case 5 -> {
                        riversImages[k].setLayoutX(x + tileImage.getFitWidth() / 8 - riversImages[k].getFitWidth() / 2 - riversImages[k].getFitHeight() / 2 + 1);
                        riversImages[k].setLayoutY(y + tileImage.getFitHeight() / 2 + tileImage.getFitHeight() / 4 - riversImages[k].getFitHeight() / 2 + 2);
                    }
                }
            }
        }
    }

    private void nonCivilianUnitSetPosition(double x, double y) {
        nonCivilianUnitImage.setLayoutX(x + tileImage.getFitWidth() * 3.5 / 5 - nonCivilianUnitImage.getFitWidth() / 2);
        nonCivilianUnitImage.setLayoutY(y + tileImage.getFitHeight() * 3.5 / 5 - nonCivilianUnitImage.getFitHeight() / 2);
    }

    private void civilianUnitSetPosition(double x, double y) {
        civilianUnitImage.setLayoutX(x + tileImage.getFitWidth() * 1.5 / 5 - civilianUnitImage.getFitWidth() / 3);
        civilianUnitImage.setLayoutY(y + tileImage.getFitHeight() * 1.5 / 5 - civilianUnitImage.getFitHeight() / 2);
    }
}
