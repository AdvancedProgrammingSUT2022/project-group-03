package com.example.demo.view.model;

import com.example.demo.controller.NetworkController;
import com.example.demo.controller.gameController.GameController;
import com.example.demo.controller.gameController.UnitStateController;
import com.example.demo.model.City;
import com.example.demo.model.Civilization;
import com.example.demo.model.Units.*;
import com.example.demo.model.features.Feature;
import com.example.demo.model.features.FeatureType;
import com.example.demo.model.improvements.ImprovementType;
import com.example.demo.model.resources.ResourcesTypes;
import com.example.demo.model.technologies.TechnologyType;
import com.example.demo.model.tiles.Tile;
import com.example.demo.view.*;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.Serializable;

public class GraphicTile implements Serializable {
    private Tile tile;
    private ImageView tileImage;
    private ImageView resourceImage;
    private ImageView featureImage;
    private ImageView nonCivilianUnitImage;
    private HealthBar nonCivilianHealthBar;
    private ImageView civilianUnitImage;
    private HealthBar civilianHealthBar;
    private ImageView improvementImage;
    private ImageView pillage;
    private ImageView roadImage;
    private ImageView cityImage;
    private HealthBar cityHealthBar;
    private ImageView fogImage;
    private ImageView citizenImage;
    private ImageView ruinsImage;
    private ImageView buildingImage;
    private final ImageView[] riversImages = new ImageView[6];
    private final VBox leftPanel;
    private final Pane pane;
    private double x;
    private double y;

    GameControllerFX gameControllerFX;
    private VBox leftVbox;
    private ScrollPane scrollPane;

    public GraphicTile(Tile tile, Pane pane, VBox leftPanel, GameControllerFX gameControllerFX) {
        this.gameControllerFX = gameControllerFX;
        this.leftPanel = leftPanel;
        this.pane = pane;
        this.tile = tile;
        loadImages(tile, pane);
        setPosition(tile.getX(), tile.getY());
    }


    private void clicked() {
        if (gameControllerFX.getSelectingTile()) {
            GameController.setSelectedTile(tile);
            gameControllerFX.publicText.setText("Destination tile:  X=" + tile.getX() + " Y=" + tile.getY());
            gameControllerFX.cross.setLayoutX(x + 20);
            gameControllerFX.cross.setLayoutY(y + 20);
            if (!pane.getChildren().contains(gameControllerFX.cross))
                pane.getChildren().add(gameControllerFX.cross);
            return;
        }
        pane.getChildren().remove(gameControllerFX.cross);
        if (CityPanel.getButtonsProcess()[0] == 1) {
            gameControllerFX.getCityPanel().buyTile(tile);
        } else if (CityPanel.getButtonsProcess()[1] == 1) {
            gameControllerFX.getCityPanel().attackTile(tile);
        } else if (CityPanel.getButtonsProcess()[2] == 1)
            gameControllerFX.getCityPanel().assignCitizenToTile(tile);
        else if (CityPanel.getButtonsProcess()[3] == 1)
            gameControllerFX.getCityPanel().firstTileReassign(tile);
        else if (CityPanel.getButtonsProcess()[3] == 2)
            gameControllerFX.getCityPanel().secondTileReassign(tile, tile);
        else if (CityPanel.getButtonsProcess()[4] == 1)
            gameControllerFX.getCityPanel().removeCitizenFromTile(tile);
        else if (CityPanel.getButtonsProcess()[5] == 1)
            gameControllerFX.getCityPanel().buyUnitSelectTile(tile);
        else if (CityPanel.getButtonsProcess()[7] == 1)
            gameControllerFX.getCityPanel().buildBuildingSelectTile(tile);
        else gameControllerFX.getCityPanel().disableCityPanel();

        leftPanel.getChildren().clear();
        GameController.setSelectedUnit(null);

        Civilization.TileCondition[][] tileConditions = GameController.getCivilizations().get(GameController.getPlayerTurn()).getTileConditions();
        if (tileConditions[tile.getX()][tile.getY()] == null) {
            leftPanel.getChildren().add(new Text("Hidden tile"));
        } else {
            Text text = new Text("Tile: " + tile.getTileType());
            Text text1 = new Text("Feature: " + tile.getContainedFeature());
            Text text2 = new Text("Resource: " + tile.getResource());
            int gold = tile.getTileType().gold;
            int food = tile.getTileType().food;
            int production = tile.getTileType().production;
            if (tile.getContainedFeature() != null) {
                gold += tile.getContainedFeature().getFeatureType().gold;
                food += tile.getContainedFeature().getFeatureType().food;
                production += tile.getContainedFeature().getFeatureType().production;
            }
            Text text3 = new Text("gold: " + gold + "\nfood: " + food);
            Text text4 = new Text("production: " + production);
            leftPanel.getChildren().addAll(text, text1, text2, text3, text4);
        }
    }

    private void addButton(String name, boolean clearAfterClick, Boolean scroll, EventHandler<ActionEvent> func) {

        Button button = new Button(name);
        button.setOnAction(actionEvent -> {
            func.handle(actionEvent);
            if (clearAfterClick)
                leftPanel.getChildren().clear();
        });
        if (scroll) {
            button.minWidthProperty().bind(scrollPane.widthProperty());
            leftVbox.getChildren().add(button);
        } else
            leftPanel.getChildren().add(button);
    }

    private void civilianClicked(MouseEvent mouseEvent) {
        Unit unit = tile.getCivilian();
        GameController.setSelectedUnit(unit);
        //add move,sleep,delete buttons and some information
        boolean isYours = GameController.getCurrentCivilization() == unit.getCivilization();
        addCommonButtons(unit, isYours);

        //not show control buttons if the unit is not belong to the current player
        if (!isYours)
            return;

        switch (unit.getUnitType()) {
            case SETTLER -> addSettlerButtons();
            case WORKER -> addWorkerButtons();
            default -> System.out.println("Error: military call from civilian section.");
        }
    }

    private void nonCivilianClicked(MouseEvent mouseEvent) {
        NonCivilian unit = tile.getNonCivilian();
        GameController.setSelectedUnit(unit);
        //add move,sleep,delete buttons and some information
        boolean isYours = GameController.getCurrentCivilization() == unit.getCivilization();
        addCommonButtons(unit, isYours);

        //not show control buttons if the unit is not belong to the current player
        if (!isYours)
            return;

        if (unit.getState() == UnitState.GARRISON || unit.getState() == UnitState.FORTIFY || unit.getState() == UnitState.FORTIFY_UNTIL_FULL_HEALTH)
            addButton("Call", true, true, event -> {
                int code = Integer.parseInt(NetworkController.send("state " +
                        unitToString(GameController.getSelectedUnit()) + " " + 3));
                if (code == 2)
                    notify("fault", "This Unit does not belong to you.");
                else if (code == 0)
                    notify("Awake", "The unit awoken successfully.");
            });

        if (!unit.getState().equals(UnitState.FORTIFY))
            addButton("Fortify", true, true, event -> {
                int code = Integer.parseInt(NetworkController.send("state " +
                        unitToString(GameController.getSelectedUnit()) + " " + 0));
                if (code == 2)
                    notify("fault", "This Unit does not belong to you.");
                else if (code == 0)
                    notify("Fortify", "The unit fortified successfully.");
            });

        if (!unit.getState().equals(UnitState.FORTIFY_UNTIL_FULL_HEALTH))
            addButton("Fortify until health", true, true, event -> {
                int code =  Integer.parseInt(NetworkController.send("state " +
                        unitToString(GameController.getSelectedUnit()) + " " + 1));
                if (code == 2)
                    notify("fault", "This Unit does not belong to you.");
                else if (code == 0)
                    notify("Fortify", "The unit fortified until full health successfully.");
            });

        if (!unit.getState().equals(UnitState.GARRISON))
            addButton("Garrison", true, true, event -> {
                int code =  Integer.parseInt(NetworkController.send("state " +
                        unitToString(GameController.getSelectedUnit()) + " " + 2));
                if (code == 2)
                    notify("fault", "This Unit does not belong to you.");
                else if (code == 0)
                    notify("Garrison", "The unit garrisoned successfully.");
            });

        if (tile.getImprovement() != null && tile.getImprovement().getNeedsRepair() < 3) {
            addButton("Pillage", true, true, event -> {
                GameController.setSelectedUnit(unit);
                int code = Integer.parseInt(NetworkController.send("pillage " +
                        unitToString(GameController.getSelectedUnit())));
                switch (code) {
                    case 4 -> notify("fault", "This unit does not belong to you!");
                    case 3 -> notify("Error", "You can not pillage your improvements.");
                    case 0 -> {
                        notify("Pillage", "Your unit pillages this improvement.");
                        gameControllerFX.renderMap();
                    }
                }
            });
        }

        if (unit.getUnitType().combatType == CombatType.SIEGE && unit.getState() != UnitState.SETUP) {
            addButton("Setup", true, true, event -> {
                NetworkController.send("setup " +
                        unitToString(GameController.getSelectedUnit()));
                notify("Setup", "The unit setup successfully.");
            });
        } else if (!(unit.getUnitType().combatType == CombatType.SIEGE && unit.getFortifiedCycle() < 1) && !unit.attacked) {
            addButton("Attack", false, true, event -> {
                gameControllerFX.update.pause();
                leftPanel.getChildren().clear();
                Text text = new Text("Click on the tile you want to attack,\n         then click move.");
                gameControllerFX.setSelectingTile(true);
                leftPanel.getChildren().addAll(text, gameControllerFX.publicText);
                addButton("Attack", true, false, event1 -> {
                    int x = GameController.getSelectedTile().getX();
                    int y = GameController.getSelectedTile().getY();
                    String code =NetworkController.send("unitAttack "+ x+" "+ y+" "+
                            unitToString(GameController.getSelectedUnit()));
                    if(code.length()>1) {
                        Alert alert = StageController.errorMaker("Declaring war, eh?", "By this action, you will declare war to " +
                                code + " , are you sure?", Alert.AlertType.CONFIRMATION);
                        if(alert.getResult() == ButtonType.OK){
                            code = NetworkController.send("ok");
                        }
                        else {
                            code = NetworkController.send("cancel");
                            gameControllerFX.update.play();
                            return;
                        }
                    }

                    switch (Integer.parseInt(code)) {
                        case 7 -> notify("Setup need", "You must setup your unit before attacking!");
                        case 5 -> notify("Attack to what?", "The destination tile has no units that can be attacked.");
                        case 6 -> notify("Can not attack", "The destination tile is so far.");
                        case 8 -> notify("Attacked before", "You can not attack more in this turn.");
                        case 0 -> StageController.errorMaker("Attack", "Attacked successfully.", Alert.AlertType.INFORMATION);
                        case 9 -> notify("The Units is tired", "");
                    }
                    gameControllerFX.update.play();
                    gameControllerFX.setSelectingTile(false);
                    gameControllerFX.renderMap();
                });

                addButton("Cancel", true, false, event1 -> gameControllerFX.setSelectingTile(false));
            });
        }
    }

    private void addSettlerButtons() {
        addButton("Found City", true, true, event -> {
            int code =Integer.parseInt(NetworkController.send("foundCity "+
                    unitToString(GameController.getSelectedUnit())));
            switch (code) {
                case 4 -> notify("Near city", "This tile is near a city. So you can not found a city here.");
                case 0 -> notify("Found City", "Your new city founded successfully.");
            }
            gameControllerFX.renderMap();
        });
    }

    private void addWorkerButtons() {
        Civilization civilization = GameController.getCivilizations().get(GameController.getPlayerTurn());
        if (tile.getRoad() == null)
            addButton("Build Road", true, true, event -> {
                NetworkController.send("unitBuildRoad "+
                        unitToString(GameController.getSelectedUnit()));
                gameControllerFX.renderMap();
            });
        if (tile.getRoad() == null && civilization.doesContainTechnology(TechnologyType.RAILROAD) == 1)
            addButton("Build Rail Road", true, true, event -> {
                NetworkController.send("unitBuildRailRoad "+
                        unitToString(GameController.getSelectedUnit()));
                gameControllerFX.renderMap();
            });
        if (tile.getRoad() != null)
            addButton("Remove Road", true, true, event -> {
                NetworkController.send("unitRemoveFromTile "+
                        unitToString(GameController.getSelectedUnit())+" "+ false );

                gameControllerFX.renderMap();
            });
        if (tile.getContainedFeature() != null) {
            FeatureType featureType = tile.getContainedFeature().getFeatureType();
            if (featureType.equals(FeatureType.JUNGLE) || featureType.equals(FeatureType.FOREST) || featureType.equals(FeatureType.SWAMP))
                addButton("Remove Feature", true, true, event -> {
                    NetworkController.send("unitRemoveFromTile "+
                            unitToString(GameController.getSelectedUnit())+" "+ true );
                    notify("Removing Feature", "Started removing feature in this tile.");
                    gameControllerFX.renderMap();
                });
        }

        //improvement repairing:
        if (tile.getImprovement() != null && tile.getImprovement().getNeedsRepair() != 0) {
            addButton("Repair Improvement", true, true, event -> {
                NetworkController.send("unitRepair "+
                        unitToString(GameController.getSelectedUnit()));
                notify("Repairing started", "Repairing this improvement started successfully");
            });
        }

        //improvement building:
        for (ImprovementType improvementType : ImprovementType.values())
            if (GameController.doesHaveTheRequiredTechnologyToBuildImprovement(improvementType, tile, civilization)
                && GameController.canHaveTheImprovement(tile, improvementType))
                if (tile.getImprovement() != null && tile.getImprovement().getImprovementType().equals(improvementType)) {
                    if (improvementImage != null)
                        addButton("Remove " + improvementType, true, true, event -> {
                            tile.setImprovement(null);
                            gameControllerFX.renderMap();
                            notify("Success", "Improvement removed successfully.");
                        });
                } else if (GameController.canHaveTheImprovement(tile, improvementType))
                    addButton("Build " + improvementType, true, true, event -> {
                        System.out.println("clicked on unitBuild improvement");
                        NetworkController.send("unitBuild "+
                                unitToString(GameController.getSelectedUnit())+" " +new Gson().toJson(improvementType));
                        gameControllerFX.renderMap();
                    });
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private void addCommonButtons(Unit unit, boolean isYours) {
        leftPanel.getChildren().clear();
        Text title = new Text("Selected Unit: " + unit.getUnitType() +
            "\nMove point:" + unit.getMovementPrice() +
            "\nState: " + unit.getState() +
            "\nHealth: " + unit.getHealth() +
            "\nStrength:  A(" + round(unit.getCombatStrength(true), 1) + ")   D(" + round(unit.getCombatStrength(false), 1) + ")");
        leftPanel.getChildren().add(title);

        //not show control buttons if the unit is not belong to the current player
        if (!isYours)
            return;

        leftVbox = new VBox();
        leftVbox.setSpacing(5);
        scrollPane = new ScrollPane(leftVbox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        leftPanel.getChildren().add(scrollPane);

        if (UnitStateController.unitUpgradeCheck() == 0)
            addButton("Upgrade unit", true, true, event -> {
                NetworkController.send("upgrade "+
                                unitToString(GameController.getSelectedUnit()));
                gameControllerFX.renderMap();
                notify("Success", "Your unit upgraded successfully.");
            });

        if (unit.getMovementPrice() > 0) {
            addButton("Move", false, true, event -> {
                leftPanel.getChildren().clear();
                Text text = new Text("Click on the destination tile,\n      then click move.");
                gameControllerFX.setSelectingTile(true);
                leftPanel.getChildren().addAll(text, gameControllerFX.publicText);
                addButton("Move", true, false, event2 -> {
                    int x = GameController.getSelectedTile().getX();
                    int y = GameController.getSelectedTile().getY();
                    int code =Integer.parseInt(NetworkController.send("moveto "+ x+" "+ y+" "+
                            unitToString(GameController.getSelectedUnit())));
                    switch (code) {
                        case 3 -> notify("Impossible movement", "Units cannot move to mountains or oceans.");
                        case 4 -> notify("Movement Error", "Error in moving.");
                        case 5 -> notify("Invalid movement","Your civilians cannot move to a tile occupied by a foreign nonCivilian unit(Unless you wanna get captured)");
                    }

                    gameControllerFX.setSelectingTile(false);
                    gameControllerFX.renderMap();
                });
                addButton("Cancel", true, false, event1 -> gameControllerFX.setSelectingTile(false));
            });
        }

        if (GameController.getSelectedUnit().getDestinationTile() != null)
            addButton("Cancel", true, true, event -> {
                if (Integer.parseInt(NetworkController.send("cancel "+ tile.getX()+" "+ tile.getY()+" "+
                        unitToString(GameController.getSelectedUnit())))== 0)
                    notify("Canceled successfully", "The movement Canceled successfully.");
                else
                    notify("fault", "Can not cancel.");
            });

        if (!unit.getState().equals(UnitState.SLEEP))
            addButton("Sleep", true, true, event -> {
                NetworkController.send("sleep "+
                        unitToString(GameController.getSelectedUnit()));
                notify("Sleep", "The unit Slept successfully!");
            });

        if (unit.getState().equals(UnitState.ALERT) || unit.getState().equals(UnitState.SLEEP))
            addButton("Awake", true, true, event -> {
                if (Integer.parseInt(NetworkController.send("state " +
                        unitToString(GameController.getSelectedUnit()) + " " + 3)) == 0)
                    notify("Awake", "The unit awoken successfully!");
                else
                    notify("fault", "You can not awake this unit!");
            });

        if (!unit.getState().equals(UnitState.ALERT))
            addButton("Alter", true, true, event -> {
                int code =  Integer.parseInt(NetworkController.send("alert " +
                        unitToString(GameController.getSelectedUnit()) ));
                switch (code) {
                    case 3 -> notify("Error", "There is an alien unit nearby.");
                    case 0 -> notify("Successfully done", "the unit has been set to Alert Successfully.");
                }
            });

        addButton("Delete", true, true, event -> {
            NetworkController.send("delete " +
                    unitToString(GameController.getSelectedUnit()) );
            gameControllerFX.renderMap();
            notify("Delete unit", "The Unit deleted successfully.");
        });
    }

    private void notify(String title, String message) {
        Notifications notifications = Notifications.create().hideAfter(Duration.seconds(5)).text(message).title(title);
        notifications.show();
        GameController.getCivilizations().get(GameController.getPlayerTurn()).putNotification(title + ": " + message,GameController.getCycle());
    }


    public static double getWidth() {
        return 120;
    }

    public double getHeight() {
        return 103;
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
            tileImage.setFitHeight(getHeight() * 16 / 15);
            tileImage.setFitWidth(getWidth() * (1 + 2 / (Math.sqrt(3) * 15)));
            tileImage.setOnMouseReleased(event -> clicked());
            pane.getChildren().add(tileImage);
            return;
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

        if (!tileConditions[tile.getX()][tile.getY()].getIsClear()) {
            tile = tileConditions[tile.getX()][tile.getY()].getOpenedArea();
            fogImage = new ImageView(ImageLoader.get("fog"));
            fogImage.setFitHeight(getHeight());
            fogImage.setFitWidth(getWidth());
            fogImage.setViewOrder(-2);
            fogImage.setOnMouseReleased(event -> clicked());
            pane.getChildren().add(fogImage);
        }
        this.tile = tile;
        //load tile
        tileImage = new ImageView(ImageLoader.get(tile.getTileType().toString()));
        tileImage.setFitHeight(getHeight());
        tileImage.setFitWidth(getWidth());
        tileImage.setOnMouseReleased(event -> clicked());
        pane.getChildren().add(tileImage);
        if (!tileConditions[tile.getX()][tile.getY()].getIsClear()) {
            tile = tileConditions[tile.getX()][tile.getY()].getOpenedArea();
            fogImage = new ImageView(ImageLoader.get("fog"));
            fogImage.setFitHeight(getHeight());
            fogImage.setFitWidth(getWidth());
            pane.getChildren().add(fogImage);
        }
        //load feature
        Feature feature = tile.getContainedFeature();
        if (feature != null) {
            featureImage = new ImageView(ImageLoader.get(feature.getFeatureType().toString()));
            featureImage.setFitHeight(getHeight());
            featureImage.setFitWidth(getWidth());
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
        if (tile.getBuilding() != null && tile.getBuilding().getRemainedCost() == 0) {
            buildingImage = new ImageView(ImageLoader.get(tile.getBuilding().getBuildingType().toString()));
            buildingImage.setFitWidth(40);
            buildingImage.setFitHeight(40);
            pane.getChildren().add(buildingImage);
        }

        //load nonCivilian unit
        if (tile.getNonCivilian() != null) {
            nonCivilianUnitImage = new ImageView(ImageLoader.get(tile.getNonCivilian().getUnitType().toString()));
            nonCivilianUnitImage.setFitHeight(40);
            nonCivilianUnitImage.setFitWidth(40);
            nonCivilianUnitImage.setOnMouseClicked(this::nonCivilianClicked);
            nonCivilianUnitImage.setViewOrder(-1);
            Civilization civilization = GameController.getCivilizations().get(GameController.getPlayerTurn());
            if (!tile.getNonCivilian().getCivilization().equals(civilization))
                nonCivilianUnitImage.setOpacity(0.65);
            nonCivilianUnitImage.setCursor(Cursor.HAND);
            pane.getChildren().add(nonCivilianUnitImage);

            nonCivilianHealthBar = new HealthBar(tile.getNonCivilian(), pane);

        }

        //load civilian unit
        if (tile.getCivilian() != null) {
            civilianUnitImage = new ImageView(ImageLoader.get(tile.getCivilian().getUnitType().toString()));
            civilianUnitImage.setFitHeight(40);
            civilianUnitImage.setFitWidth(40);
            civilianUnitImage.setOnMouseClicked(this::civilianClicked);
            civilianUnitImage.setViewOrder(-1);
            Civilization civilization = GameController.getCivilizations().get(GameController.getPlayerTurn());
            if (!tile.getCivilian().getCivilization().equals(civilization))
                civilianUnitImage.setOpacity(0.65);
            civilianUnitImage.setCursor(Cursor.HAND);
            pane.getChildren().add(civilianUnitImage);

            civilianHealthBar = new HealthBar(tile.getCivilian(), pane);
        }

        //load improvements
        if (tile.getImprovement() != null && tile.getImprovement().getRemainedCost() <= 0) {
            improvementImage = new ImageView(ImageLoader.get(tile.getImprovement().getImprovementType().toString()));
            improvementImage.setFitWidth(40);
            improvementImage.setFitHeight(40);
            pane.getChildren().add(improvementImage);
            if (tile.getImprovement().getNeedsRepair() != 0) {
                pillage = new ImageView(ImageLoader.get("fire"));
                pillage.setFitWidth(20);
                pillage.setFitHeight(20);
                pane.getChildren().add(pillage);
            }
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
            Civilization civilization = GameController.getCivilizations().get(GameController.getPlayerTurn());
            if (!tile.getCity().getCivilization().equals(civilization))
                cityImage.setOpacity(0.65);
            Tile finalTile = tile;
            cityImage.setOnMouseReleased(event -> gameControllerFX.getCityPanel().cityClicked(finalTile.getCity()));
            cityImage.setCursor(Cursor.HAND);
            pane.getChildren().add(cityImage);
            cityHealthBar = new HealthBar(tile.getCity(), pane);
        }


        if (tile.getCivilization() == GameController.getCivilizations().get(GameController.getPlayerTurn())) {
            boolean isGettingWorkedOn = false;
            for (City city : tile.getCivilization().getCities()) {
                for (Tile gettingWorkedOnByCitizensTile : city.getGettingWorkedOnByCitizensTiles()) {
                    if (gettingWorkedOnByCitizensTile == tile) {
                        isGettingWorkedOn = true;
                        break;
                    }
                }
            }
            if (isGettingWorkedOn)
                citizenImage = new ImageView(ImageLoader.get("citizen"));
            else citizenImage = new ImageView(ImageLoader.get("noCitizen"));
            citizenImage.setFitWidth(20);
            citizenImage.setFitHeight(20);
            pane.getChildren().add(citizenImage);
        }

        //load rivers

        if (tile.getRuins() != null) {
            ruinsImage = new ImageView(ImageLoader.get("ruins"));
            ruinsImage.setFitWidth(41);
            ruinsImage.setFitHeight(30);
            pane.getChildren().add(ruinsImage);

        }
    }


    public void setPosition(int j, int i) {
        x = 20 + ((getWidth() * 3 / 2) * i / 2) * (1 + 2 / (Math.sqrt(3) * 15));
        Civilization.TileCondition[][] tileConditions = GameController.getCivilizations().get(GameController.getPlayerTurn()).getTileConditions();

        y = 20 + (getHeight() * j) * 16 / 15;
        if (tileConditions[tile.getX()][tile.getY()] == null) {
            x = x + getWidth() / 2 - getWidth() * (1 + 2 / (Math.sqrt(3) * 15)) / 2;
            y = y - getHeight() / 30;
        }
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
            if (pillage != null) {
                pillage.setLayoutX(x + tileImage.getFitWidth() * 1.5 / 5 - pillage.getFitWidth() / 2);
                pillage.setLayoutY(y + tileImage.getFitHeight() * 3.5 / 5 - pillage.getFitHeight() / 2);
            }
        }
        //road
        if (roadImage != null) {
            roadImage.setLayoutX(x + tileImage.getFitWidth() * 3.5 / 5 - roadImage.getFitWidth() / 2);
            roadImage.setLayoutY(y + tileImage.getFitHeight() * 3.5 / 5 - roadImage.getFitHeight() / 2);
        }
        //city
        if (cityImage != null) {
            double cityX = x + tileImage.getFitWidth() / 2 - cityImage.getFitWidth() / 2;
            double cityY = y + tileImage.getFitHeight() * 4 / 5 - cityImage.getFitHeight() / 2;
            cityImage.setLayoutX(cityX);
            cityImage.setLayoutY(cityY);
            cityHealthBar.fixFormat(cityX + cityImage.getFitWidth() / 2 - HealthBar.getWidth() / 2,
                cityY - HealthBar.getHeight());
        }
        if (fogImage != null) {
            fogImage.setLayoutX(x);
            fogImage.setLayoutY(y);
        }
        if (citizenImage != null) {
            citizenImage.setLayoutX(x + tileImage.getFitWidth() * 3.5 / 5 - citizenImage.getFitWidth() / 2);
            citizenImage.setLayoutY(y);
        }
        if (ruinsImage != null) {
            ruinsImage.setLayoutX(x + tileImage.getFitWidth() / 2 - ruinsImage.getFitWidth() / 2);
            ruinsImage.setLayoutY(y + tileImage.getFitHeight() / 2 - ruinsImage.getFitHeight() / 2);
        }
        if (buildingImage != null) {
            buildingImage.setLayoutX(x + tileImage.getFitWidth() * 0.75 - buildingImage.getFitWidth() / 2);
            buildingImage.setLayoutY(y + tileImage.getFitHeight() * 0.75 - buildingImage.getFitWidth() / 2);
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
        double unitX = x + tileImage.getFitWidth() * 3.5 / 5 - nonCivilianUnitImage.getFitWidth() / 2;
        double unitY = y + tileImage.getFitHeight() * 3.5 / 5 - nonCivilianUnitImage.getFitHeight() / 2;
        nonCivilianUnitImage.setLayoutX(unitX);
        nonCivilianUnitImage.setLayoutY(unitY);
        nonCivilianHealthBar.fixFormat(unitX + nonCivilianUnitImage.getFitWidth() / 2 - HealthBar.getWidth() / 2,
            unitY - HealthBar.getHeight());
    }

    private void civilianUnitSetPosition(double x, double y) {
        double unitX = x + tileImage.getFitWidth() * 1.5 / 5 - civilianUnitImage.getFitWidth() / 3;
        double unitY = y + tileImage.getFitHeight() * 1.5 / 5 - civilianUnitImage.getFitHeight() / 2;
        civilianUnitImage.setLayoutX(unitX);
        civilianUnitImage.setLayoutY(unitY);
        civilianHealthBar.fixFormat(unitX + civilianUnitImage.getFitWidth() / 2 - HealthBar.getWidth() / 2,
            unitY - HealthBar.getHeight());
    }
    private String unitToString(Unit unit){
        if(unit instanceof Civilian)
            return unit.getTile().getX() + " " + unit.getTile().getY() + " c" ;
        else return unit.getTile().getX() + " " + unit.getTile().getY() + " n" ;

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
