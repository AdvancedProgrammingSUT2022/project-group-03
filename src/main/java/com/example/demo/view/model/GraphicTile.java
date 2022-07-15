package com.example.demo.view.model;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.controller.gameController.UnitStateController;
import com.example.demo.model.Civilization;
import com.example.demo.model.Units.Unit;
import com.example.demo.model.features.Feature;
import com.example.demo.model.resources.ResourcesTypes;
import com.example.demo.model.tiles.Tile;
import com.example.demo.view.GameControllerFX;
import com.example.demo.view.ImageLoader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.Serializable;
public class GraphicTile implements Serializable {
    private final Tile tile;
    private final ImageView tileImage;
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

    public GraphicTile(Tile tile, Pane pane, VBox leftPanel, int x, int y) {
        this.pane = pane;
        this.leftPanel = leftPanel;
        Civilization.TileCondition[][] tileConditions = GameController.getCivilizations().get(GameController.getPlayerTurn()).getTileConditions();
        if (tileConditions[tile.getX()][tile.getY()] == null) {
            this.tile = tile;
            tileImage = new ImageView(ImageLoader.get("CLOUD"));
            tileImage.setFitHeight(103);
            tileImage.setFitWidth(120);
            pane.getChildren().add(tileImage);
            return;
        }
        if(!tileConditions[tile.getX()][tile.getY()].getIsClear())
        {
            tile=tileConditions[tile.getX()][tile.getY()].getOpenedArea();
            fogImage = new ImageView(ImageLoader.get("fog"));
            fogImage.setFitHeight(103);
            fogImage.setFitWidth(120);
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
        if(tile.getImprovement()!=null && tile.getImprovement().getRemainedCost()<=0)
        {
            improvementImage = new ImageView(ImageLoader.get(tile.getImprovement().getImprovementType().toString()));
            improvementImage.setFitWidth(40);
            improvementImage.setFitHeight(40);
            //set on mouse clicked ?
            pane.getChildren().add(improvementImage);
        }
        if(tile.getRoad()!=null)
        {
            roadImage = new ImageView(ImageLoader.get(tile.getRoad().getImprovementType().toString()));
            roadImage.setFitWidth(40);
            roadImage.setFitHeight(40);
            pane.getChildren().add(roadImage);
        }

        if(tile.getCity()!=null)
        {
            cityImage = new ImageView(ImageLoader.get("city"));
            cityImage.setFitHeight(40);
            cityImage.setFitWidth(40);
            pane.getChildren().add(cityImage);
        }

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

    private void civilianClicked(MouseEvent mouseEvent) {
        leftPanel.getChildren().clear();
        Unit unit = tile.getCivilian();
        GameController.setSelectedUnit(unit);
        Text title = new Text("Selected Unit: " + unit.getUnitType());
        leftPanel.getChildren().add(title);
        Button move = new Button("Move");
        Button sleep = new Button("Sleep");
        Button remove = new Button("Remove");
        leftPanel.getChildren().addAll(move, sleep, remove);

        remove.setOnAction(event -> {

        });

        switch (unit.getUnitType()) {
            case SETTLER -> {
                Button foundCity = new Button("Found City");
                leftPanel.getChildren().add(foundCity);
                foundCity.setOnAction(event -> {
                    UnitStateController.unitFoundCity("City");
                    pane.getChildren().remove(civilianUnitImage);
                    civilianUnitImage = null;
                });
            }
            case WORKER -> {
                //TODO:
                Button buildRoad = new Button("Build Road");
                Button buildRailRoad = new Button("Build Rail Road");
                leftPanel.getChildren().addAll(buildRoad, buildRailRoad);
            }
        }
    }

    private void nonCivilianClicked(MouseEvent mouseEvent) {

    }

    private void clicked() {
        //TODO: If we click on a tile this methode runs...
        if(GameControllerFX.isWaitingToSelectTileToBuy())
        {
            GameControllerFX.buyTile(tile);
        }
        if(GameControllerFX.isWaitingToSelectTileToAttackFromCity())
        {
            GameControllerFX.attackTile(tile);
        }
        leftPanel.getChildren().clear();
        GameController.setSelectedUnit(null);
        Text text = new Text("Tile: " + tile.getTileType());
        Text text1 = new Text("Have Resource: " + tile.getResource());
        leftPanel.getChildren().addAll(text, text1);
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
            resourceImage.setLayoutX(x + tileImage.getFitWidth() / 2 - resourceImage.getFitWidth() / 2);
            resourceImage.setLayoutY(y + 50);
        }
        //nonCivilian Unit
        if (nonCivilianUnitImage != null) {
            nonCivilianUnitImage.setLayoutX(x + tileImage.getFitWidth() * 3.5 / 5 - nonCivilianUnitImage.getFitWidth() / 2);
            nonCivilianUnitImage.setLayoutY(y + tileImage.getFitHeight() * 3.5 / 5 - nonCivilianUnitImage.getFitHeight() / 2);
        }
        //civilian Unit
        if (civilianUnitImage != null) {
            civilianUnitImage.setLayoutX(x + tileImage.getFitWidth() * 1.5 / 5 - civilianUnitImage.getFitWidth() / 2);
            civilianUnitImage.setLayoutY(y + tileImage.getFitHeight() * 1.5 / 5 - civilianUnitImage.getFitHeight() / 2);
        }

        if(improvementImage!=null)
        {
            improvementImage.setLayoutX(x + tileImage.getFitWidth()*1.5/5 - improvementImage.getFitWidth()/2);
            improvementImage.setLayoutY(y + tileImage.getFitHeight()*3.5/5 - improvementImage.getFitHeight()/2);
        }

        if(roadImage!=null)
        {
            roadImage.setLayoutX(x+tileImage.getFitWidth()*3.5/5 - roadImage.getFitWidth()/2);
            roadImage.setLayoutY(y+tileImage.getFitHeight()*3.5/5 - roadImage.getFitHeight()/2);
        }
        if(cityImage!=null)
        {
            cityImage.setLayoutX(x+tileImage.getFitWidth()/2 - cityImage.getFitWidth()/2);
            cityImage.setLayoutY(y+tileImage.getFitHeight()*4/5 - cityImage.getFitHeight()/2);
        }
        if(fogImage!=null)
        {
            fogImage.setLayoutX(x);
            fogImage.setLayoutY(y);
        }

        for (int i = 0; i < 6; i++) {
            if (riversImages[i] != null) {
//                double edge= Math.sqrt(tileImage.getFitHeight()*tileImage.getFitHeight()+ tileImage.getFitWidth()*tileImage.getFitWidth())/15;
                switch (i) {
                    case 0 -> {
                        riversImages[i].setLayoutX(x + tileImage.getFitWidth() / 8 - riversImages[i].getFitWidth() / 2 - riversImages[i].getFitHeight() / 2 +1);
                        riversImages[i].setLayoutY(y - riversImages[i].getFitHeight() / 2 + tileImage.getFitHeight() / 4-2);
                    }
                    case 1 -> {
                        riversImages[i].setLayoutX(x + tileImage.getFitWidth() / 2 - riversImages[i].getFitWidth() / 2);
                        riversImages[i].setLayoutY(y - riversImages[i].getFitHeight());
                    }
                    case 2 -> {
                        riversImages[i].setLayoutX(x + tileImage.getFitWidth() *7/ 8 - riversImages[i].getFitWidth() / 2 + riversImages[i].getFitHeight() / 2);
                        riversImages[i].setLayoutY(y - riversImages[i].getFitHeight() / 2 + tileImage.getFitHeight() / 4-1);
                    }
                    case 3 -> {
                        riversImages[i].setLayoutX(x + tileImage.getFitWidth() *7/ 8 - riversImages[i].getFitWidth() / 2 + riversImages[i].getFitHeight() / 2);
                        riversImages[i].setLayoutY(y + tileImage.getFitHeight()/2 + tileImage.getFitHeight() / 4  - riversImages[i].getFitHeight() / 2+2);
                    }
                    case 4 -> {
                        riversImages[i].setLayoutX(x + tileImage.getFitWidth() / 2 - riversImages[i].getFitWidth() / 2);
                        riversImages[i].setLayoutY(y + tileImage.getFitHeight());
                    }
                    case 5 -> {
                        riversImages[i].setLayoutX(x + tileImage.getFitWidth() / 8 - riversImages[i].getFitWidth() / 2 - riversImages[i].getFitHeight() / 2+1);
                        riversImages[i].setLayoutY(y + tileImage.getFitHeight()/2 + tileImage.getFitHeight() / 4  - riversImages[i].getFitHeight() / 2+2);
                    }
                }
                System.out.println(riversImages[i].getFitWidth());
            }
        }
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
}
