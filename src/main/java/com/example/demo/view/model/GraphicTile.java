package com.example.demo.view.model;

import com.example.demo.controller.NetworkController;
import com.example.demo.controller.gameController.GameController;
import com.example.demo.controller.gameController.UnitStateController;
import com.example.demo.model.City;
import com.example.demo.model.Civilization;
import com.example.demo.model.Units.CombatType;
import com.example.demo.model.Units.NonCivilian;
import com.example.demo.model.Units.Unit;
import com.example.demo.model.Units.UnitState;
import com.example.demo.model.features.Feature;
import com.example.demo.model.features.FeatureType;
import com.example.demo.model.improvements.ImprovementType;
import com.example.demo.model.resources.ResourcesTypes;
import com.example.demo.model.technologies.TechnologyType;
import com.example.demo.model.tiles.Tile;
import com.example.demo.view.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
    private ImageView civilianUnitImage;
    private ImageView improvementImage;
    private ImageView pillage;
    private ImageView roadImage;
    private ImageView cityImage;
    private ImageView fogImage;
    private ImageView citizenImage;
    private ImageView ruinsImage;
    private ImageView buildingImage;
    private final ImageView[] riversImages = new ImageView[6];
    private final VBox leftPanel;
    private final Pane pane;
    private boolean isClouded = false;
    private boolean isAroundNonClouded = false;
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
            Text text1 = new Text("Have Feature: " + tile.getContainedFeature());
            Text text2 = new Text("Have Resource: " + tile.getResource());
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
        addCommonButtons(unit);

        switch (unit.getUnitType()) {
            case SETTLER -> addSettlerButtons();
            case WORKER -> addWorkerButtons();
            default -> System.out.println("Error: military call from civilian section.");
        }
    }

    private void nonCivilianClicked(MouseEvent mouseEvent) {
        Unit unit = tile.getNonCivilian();
        GameController.setSelectedUnit(unit);
        //add move,sleep,delete buttons and some information
        addCommonButtons(unit);

        if (unit.getState() == UnitState.GARRISON || unit.getState() == UnitState.FORTIFY || unit.getState() == UnitState.FORTIFY_UNTIL_FULL_HEALTH)
            addButton("Call", true, true, event -> {
                int code = UnitStateController.unitChangeState(3);
                if (code == 2)
                    notif("fault", "This Unit is not belong to you.");
                else if (code == 0)
                    notif("Awake", "The unit awoken successfully.");
            });

        addButton("Fortify", true, true, event -> {
            int code = UnitStateController.unitChangeState(0);
            if (code == 2)
                notif("fault", "This Unit is not belong to you.");
            else if (code == 0)
                notif("Fortify", "The unit fortified successfully.");
        });

        addButton("Fortify until health", true, true, event -> {
            int code = UnitStateController.unitChangeState(1);
            if (code == 2)
                notif("fault", "This Unit is not belong to you.");
            else if (code == 0)
                notif("Fortify", "The unit fortified until full health successfully.");
        });

        addButton("Garrison", true, true, event -> {
            int code = UnitStateController.unitChangeState(2);
            if (code == 2)
                notif("fault", "This Unit is not belong to you.");
            else if (code == 0)
                notif("Garrison", "The unit garrisoned successfully.");
        });

        if (tile.getImprovement() != null && tile.getImprovement().getNeedsRepair() < 3) {
            addButton("Pillage", true, true, event -> {
                GameController.setSelectedUnit(unit);
                int code = UnitStateController.unitPillage();
                switch (code) {
                    case 4 -> notif("fault", "This unit is not belong to you!");
                    case 3 -> notif("Error", "You can not pillage your improvements.");
                    case 0 -> {
                        notif("Pillage", "Your unit pillages this improvement.");
                        gameControllerFX.renderMap();
                    }
                }
            });
        }

        if (unit.getUnitType().combatType == CombatType.SIEGE && unit.getState() != UnitState.SETUP) {
            addButton("Setup", true, true, event -> {
                int code = UnitStateController.unitSetupRanged();
                switch (code) {
                    case 2 -> notif("fault", "This unit is not belong to you!");
                    case 0 -> notif("Setup", "The unit setup successfully.");
                }
            });
        } else if (!(unit.getUnitType().combatType == CombatType.SIEGE && ((NonCivilian) unit).getFortifiedCycle() < 1) && !((NonCivilian) unit).attacked) {
            addButton("Attack", false, true, event -> {
                leftPanel.getChildren().clear();
                Text text = new Text("Click on the tile you want to attack,\n         then click move.");
                gameControllerFX.setSelectingTile(true);
                leftPanel.getChildren().addAll(text, gameControllerFX.publicText);
                addButton("Attack", true, false, event1 -> {
                    int x = GameController.getSelectedTile().getX();
                    int y = GameController.getSelectedTile().getY();
                    int code = UnitStateController.unitAttack(x, y);
                    switch (code) {
                        case 2 -> notif("fault", "This unit is not belong to you!");
                        case 7 -> notif("Setup need", "You must setup your unit before attacking!");
                        case 5 -> notif("Attack to what?", "The destination tile has no units that can be attacked.");
                        case 6 -> notif("Can not attack", "The destination tile is so far.");
                        case 8 -> notif("Attacked before", "You can not attack more in this turn.");
                        case 0 -> StageController.errorMaker("Attack", "Attacked successfully.", Alert.AlertType.INFORMATION);
                        case 9 -> notif("The Units is tired", "");
                    }
                    gameControllerFX.setSelectingTile(false);
                    gameControllerFX.renderMap();
                });

                addButton("Cancel", true, false, event1 -> {
                    gameControllerFX.setSelectingTile(false);
                });
            });
        }
    }

    private void addSettlerButtons() {
        addButton("Found City", true, true, event -> {
            int code = UnitStateController.unitFoundCity("Unnamed City");
            switch (code) {
                case 2 -> notif("fault", "This settler unit is not belong to you.");
                case 4 -> notif("Near city", "This tile is near a city. So you can not found a city here.");
                case 0 -> notif("Found City", "Your new city founded successfully.");
            }
            gameControllerFX.renderMap();
        });
    }

    private void addWorkerButtons() {
        Civilization civilization = GameController.getCivilizations().get(GameController.getPlayerTurn());
        if (tile.getRoad() == null)
            addButton("Build Road", true, true, event -> {
                UnitStateController.unitBuildRoad();
                gameControllerFX.renderMap();
            });
        if (tile.getRoad() == null && civilization.doesContainTechnology(TechnologyType.RAILROAD) == 1)
            addButton("Build Rail Road", true, true, event -> {
                UnitStateController.unitBuildRailRoad();
                gameControllerFX.renderMap();
            });
        if (tile.getRoad() != null)
            addButton("Remove Road", true, true, event -> {
                UnitStateController.unitRemoveFromTile(false);
                gameControllerFX.renderMap();
            });
        if (tile.getContainedFeature() != null) {
            FeatureType featureType = tile.getContainedFeature().getFeatureType();
            if (featureType.equals(FeatureType.JUNGLE) || featureType.equals(FeatureType.FOREST) || featureType.equals(FeatureType.SWAMP))
                addButton("Remove Feature", true, true, event -> {
                    UnitStateController.unitRemoveFromTile(true);
                    notif("Removing Feature", "Started removing feature in this tile.");
                    gameControllerFX.renderMap();
                });
        }

        //improvement repairing:
        if (tile.getImprovement() != null && tile.getImprovement().getNeedsRepair() != 0) {
            addButton("Repair Improvement", true, true, event -> {
                int code = UnitStateController.unitRepair();
                switch (code) {
                    case 2 -> notif("fault", "This unit is not belong to you.");
                    case 0 -> notif("Repairing started", "Repairing this improvement started successfully");
                    default -> notif("Error", "Can not repair.");
                }
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
                            notif("Success", "Improvement removed successfully.");
                        });
                } else
                    addButton("Build " + improvementType, true, true, event -> {
                        UnitStateController.unitBuild(improvementType);
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

    private void addCommonButtons(Unit unit) {
        leftPanel.getChildren().clear();
        Text title = new Text("Selected Unit: " + unit.getUnitType() +
            "\nMove point:" + unit.getMovementPrice() +
            "\nState: " + unit.getState() +
            "\nHealth: " + unit.getHealth() +
            "\nStrength:  A(" + round(unit.getCombatStrength(true), 1) + ")   D(" + round(unit.getCombatStrength(false), 1) + ")");
        leftPanel.getChildren().add(title);

        leftVbox = new VBox();
        scrollPane = new ScrollPane(leftVbox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        leftPanel.getChildren().add(scrollPane);

        if (unit.getMovementPrice() > 0) {
            addButton("Move", false, true, event -> {
                leftPanel.getChildren().clear();
                Text text = new Text("Click on the destination tile,\n      then click move.");
                gameControllerFX.setSelectingTile(true);
                leftPanel.getChildren().addAll(text, gameControllerFX.publicText);
                addButton("Move", true, false, event2 -> {
                    int x = GameController.getSelectedTile().getX();
                    int y = GameController.getSelectedTile().getY();
                    int code = UnitStateController.unitMoveTo(x, y);
                    switch (code) {
                        case 2 -> notif("fault", "This unit is not belong to you!");
                        case 3 -> notif("Impossible movement", "You can not move to that tile!");
                        case 4 -> notif("Movement Error", "Error in moving.");
                    }

                    gameControllerFX.setSelectingTile(false);
                    gameControllerFX.renderMap();
                });
                addButton("Cancel", true, false, event1 -> {
                    gameControllerFX.setSelectingTile(false);
                });
            });
        }

        if (GameController.getSelectedUnit().getDestinationTile() != null)
            addButton("Cancel", true, true, event -> {
                if (UnitStateController.unitCancelMission() == 0)
                    notif("Canceled successfully", "The movement Canceled successfully.");
                else
                    notif("fault", "Can not cancel.");
            });

        if (!unit.getState().equals(UnitState.SLEEP))
            addButton("Sleep", true, true, event -> {
                if (UnitStateController.unitSleep() == 0)
                    notif("Sleep", "The unit Slept successfully!");
                else
                    notif("fault", "You can not sleep this unit!");
            });

        if (unit.getState().equals(UnitState.ALERT) || unit.getState().equals(UnitState.SLEEP))
            addButton("Awake", true, true, event -> {
                if (UnitStateController.unitChangeState(3) == 0)
                    notif("Awake", "The unit awoken successfully!");
                else
                    notif("fault", "You can not awake this unit!");
            });

        if (!unit.getState().equals(UnitState.ALERT))
            addButton("Alter", true, true, event -> {
                int code = UnitStateController.unitAlert();
                switch (code) {
                    case 2 -> notif("fault", "This Unit is not belong to you.");
                    case 3 -> notif("Error", "Your unit is near a military unit of others.");
                    case 0 -> notif("Success", "Done!");
                }
            });

        addButton("Delete", true, true, event -> {
            if (UnitStateController.unitDelete(unit) != 0)
                notif("fault", "You can not delete this unit!");
            else {
                gameControllerFX.renderMap();
                notif("Delete unit", "The Unit deleted successfully.");
            }
        });
    }

    private void notif(String title, String message) {
        Notifications notifications = Notifications.create().hideAfter(Duration.seconds(5)).text(message).title(title);
        notifications.show();
    }


    public double getWidth() {
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
        for (int i = 0; i < 6; i++) {
            if (tile.getNeighbours(i) != null &&
                GameController.getCivilizations().get(GameController.getPlayerTurn()).getTileConditions()[tile.getNeighbours(i).getX()][tile.getNeighbours(i).getY()] != null) {
                isAroundNonClouded = true;
                break;
            }
        }
        Civilization.TileCondition[][] tileConditions = GameController.getCivilizations().get(GameController.getPlayerTurn()).getTileConditions();
        if (tileConditions[tile.getX()][tile.getY()] == null) {
            this.tile = tile;
            tileImage = new ImageView(ImageLoader.get("CLOUD"));
            tileImage.setFitHeight(103);
            tileImage.setFitWidth(120);
            tileImage.setOnMouseReleased(event -> clicked());
            pane.getChildren().add(tileImage);
            isClouded = true;
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
        if (!tileConditions[tile.getX()][tile.getY()].getIsClear()) {
            tile = tileConditions[tile.getX()][tile.getY()].getOpenedArea();
            fogImage = new ImageView(ImageLoader.get("fog"));
            fogImage.setFitHeight(103);
            fogImage.setFitWidth(120);
            pane.getChildren().add(fogImage);
        }
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
        for (int i = 0; i < 6; i++) {
            if (tile.isRiverWithNeighbour(i)) {
                riversImages[i] = new ImageView(ImageLoader.get("riverDown"));
                riversImages[i].setFitHeight(8);
                riversImages[i].setFitWidth(78);
                riversImages[i].setRotate(120 + 60 * i);
                pane.getChildren().add(riversImages[i]);
            }
        }
        if (tile.getRuins() != null) {
            ruinsImage = new ImageView(ImageLoader.get("ruins"));
            ruinsImage.setFitWidth(41);
            ruinsImage.setFitHeight(30);
            pane.getChildren().add(ruinsImage);

        }
    }


    public void setPosition(int j, int i) {
        x = 20 + ((getWidth() * 3 / 2) * i / 2) * (1 + 2 / (Math.sqrt(3) * 15));
        y = 20 + (getHeight() * j) * 16 / 15;
        if (i % 2 != 0) //odd columns
            y += getHeight() / 2 + getHeight() / 30;
        //tile
//        if(isClouded && !isAroundNonClouded)
//        {
//            x -=5;
//            y -= 4;
//        }
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
            cityImage.setLayoutX(x + tileImage.getFitWidth() / 2 - cityImage.getFitWidth() / 2);
            cityImage.setLayoutY(y + tileImage.getFitHeight() * 4 / 5 - cityImage.getFitHeight() / 2);
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
        nonCivilianUnitImage.setLayoutX(x + tileImage.getFitWidth() * 3.5 / 5 - nonCivilianUnitImage.getFitWidth() / 2);
        nonCivilianUnitImage.setLayoutY(y + tileImage.getFitHeight() * 3.5 / 5 - nonCivilianUnitImage.getFitHeight() / 2);
    }

    private void civilianUnitSetPosition(double x, double y) {
        civilianUnitImage.setLayoutX(x + tileImage.getFitWidth() * 1.5 / 5 - civilianUnitImage.getFitWidth() / 3);
        civilianUnitImage.setLayoutY(y + tileImage.getFitHeight() * 1.5 / 5 - civilianUnitImage.getFitHeight() / 2);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
