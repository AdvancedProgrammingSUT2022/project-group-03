package com.example.demo.view;

import com.example.demo.controller.gameController.CityCommandsController;
import com.example.demo.controller.gameController.GameController;
import com.example.demo.controller.gameController.UnitStateController;
import com.example.demo.model.City;
import com.example.demo.model.Units.Unit;
import com.example.demo.model.Units.UnitType;
import com.example.demo.model.building.Building;
import com.example.demo.model.building.BuildingType;
import com.example.demo.model.tiles.Tile;
import com.example.demo.view.model.GraphicTile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CityPanel {
    private final GameControllerFX gameControllerFX;
    private final VBox leftPanel;

    public CityPanel(GameControllerFX gameControllerFX, VBox leftPanel) {
        this.gameControllerFX = gameControllerFX;
        this.leftPanel = leftPanel;
    }

    private static ScrollPane buyUnitPane;
    private static ScrollPane startProducingPane;
    private static ScrollPane startBuildingBuildingsPane;
    private static AnchorPane cityPanelPane;
    private ArrayList<Button> cityPanelButtons = new ArrayList<>();
    private City openedPanelCity;
    private static final Text[] cityTexts = new Text[8];
    private static final int[] buttonsProcess = new int[9];

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void cityClicked(City city) {
//        MapMoveController.showTile(GameControllerFX.getGraphicMap()[city.getMainTile().getX()][city.getMainTile().getY()]);
        GameController.setSelectedCity(city);
        openedPanelCity = city;
        leftPanel.getChildren().clear();
        Text text = new Text("City name: " + openedPanelCity.getName() +
                "\nHealth: " + openedPanelCity.getHP() +
                "\nStrength:  A(" + round(openedPanelCity.getCombatStrength(true), 1) + ")   D(" + round(openedPanelCity.getCombatStrength(false), 1) + ")" +
                "\npopulation:" + openedPanelCity.getPopulation() +
                "\nUnemployed citizens: " + openedPanelCity.getCitizen());
        leftPanel.getChildren().add(text);
        if (openedPanelCity.getProduct() != null) {
            text = new Text("product: " +
                    openedPanelCity.getProduct().getName() + " " +
                    openedPanelCity.cyclesToComplete(openedPanelCity.getProduct().getRemainedCost()) + "T");
            leftPanel.getChildren().add(text);
        }
        Button button = new Button("Show Banner");
        button.setLayoutY(30);
        button.setOnMouseClicked(event -> gameControllerFX.eachInfoButtonsClicked(7));
        leftPanel.getChildren().add(button);

        addConfirmButton("Buy tile", "Click on the tile you want to buy\nthen click OK.", event -> {
            buyTile(GameController.getSelectedTile());
        });

        addConfirmButton("Attack tile", "Click on the tile you want to attack\nthen click OK.", event -> {
            attackTile(GameController.getSelectedTile());
        });

        addConfirmButton("Assign citizen", "Click on the tile you want to assign\nthen click OK.", event -> {
            assignCitizenToTile(GameController.getSelectedTile());
        });

        addConfirmButton("Remove citizen", "Click on the tile you want to remove\nthen click OK.", event -> {
            removeCitizenFromTile(GameController.getSelectedTile());
        });

        addConfirmButton("Reassign citizen", "Click on the tile you want to reassign\nthen click OK.", event -> {
            Tile firstTile = GameController.getSelectedTile();
            String description = "the origin tile is: " + firstTile.getX() + ", " + firstTile.getY() + ".\nnow select the destination tile";
            runSelectingMode(description, actionEvent -> secondTileReassign(firstTile, GameController.getSelectedTile()), event);
        });

        Button button1 = new Button("Produce a unit");
        leftPanel.getChildren().add(button1);
        button1.setOnAction(actionEvent -> {
            leftPanel.getChildren().clear();
            leftPanel.getChildren().add(new Text("Select which unit you want to make:"));
            addUnitsButtons(false);
        });

        addConfirmButton("Build a building", "Select which Tile you want\nto place your building\nthen click OK.", event -> {
            Text text1 = new Text("Now select which building\n     you want to build:");
            leftPanel.getChildren().clear();
            leftPanel.getChildren().add(text1);
            addBuildingsButtons(false);
        });

        button1 = new Button("Buy a unit");
        leftPanel.getChildren().add(button1);
        button1.setOnAction(actionEvent -> {
            Text text1 = new Text("Select which unit you want to buy:");
            leftPanel.getChildren().clear();
            leftPanel.getChildren().add(text1);
            addUnitsButtons(true);
        });

        addConfirmButton("Buy a building", "Select which Tile you want\nto place your building\nthen click OK.", event -> {
            Text text1 = new Text("Now select which building\n     you want to build:");
            leftPanel.getChildren().clear();
            leftPanel.getChildren().add(text1);
            addBuildingsButtons(true);
        });
    }

    private void addBuildingsButtons(boolean buy) {
        VBox vBox = new VBox();
        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        leftPanel.getChildren().add(scrollPane);
        UP:
        for (BuildingType type : BuildingType.values()) {

            for (Building building : openedPanelCity.getBuildings()) {
                if (building.getBuildingType().equals(type))
                    continue UP;
            }

            if (type == BuildingType.STOCK_EXCHANGE) {
                if (openedPanelCity.findBuilding(BuildingType.BANK) == null &&
                        openedPanelCity.findBuilding(BuildingType.SATRAPS_COURT) == null)
                    continue;
            } else {
                for (BuildingType type2 : BuildingType.prerequisites.get(type))
                    if (openedPanelCity.findBuilding(type2) == null)
                        continue UP;
            }

            if (GameController.getCivilizations().get(GameController.getPlayerTurn()).doesContainTechnology(type.technologyType) == 1) {
                String textString = type + " " + type.getCost() + "$ ";
                Building building = openedPanelCity.findHalfBuiltBuildings(type);
                if (!buy) {
                    if (building == null)
                        textString = textString + openedPanelCity.cyclesToComplete(type.getCost()) + "T";
                    else textString = textString + openedPanelCity.cyclesToComplete(building.getRemainedCost()) + "T";
                }
                Button button = new Button(textString);
                button.minWidthProperty().bind(scrollPane.widthProperty());
                vBox.getChildren().add(button);

                if (buy) {
                    button.setOnAction(actionEvent -> {
                        buildBuilding(type, GameController.getSelectedTile(), true);
                        leftPanel.getChildren().clear();
                        gameControllerFX.renderMap();
                    });
                } else
                    button.setOnMouseClicked(event -> {
                        buildBuilding(type, GameController.getSelectedTile(), false);
                        leftPanel.getChildren().clear();
                        gameControllerFX.renderMap();
                    });
            }
        }
    }


    private void addUnitsButtons(boolean buy) {
        VBox vBox = new VBox();
        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        leftPanel.getChildren().add(scrollPane);

        for (UnitType unitType : UnitType.values()) {
            if (GameController.getCivilizations().get(GameController.getPlayerTurn()).doesContainTechnology(unitType.technologyRequired) == 1) {
                if (!buy && openedPanelCity.getProduct() != null && openedPanelCity.getProduct() instanceof Unit && ((Unit) openedPanelCity.getProduct()).getUnitType() == unitType)
                    continue;
                String textString = unitType + ": ";

                Unit unit = openedPanelCity.findHalfProducedUnit(unitType);
                if (buy)
                    textString = textString + unitType.getCost() + "$";
                else {
                    int cycles = openedPanelCity.cyclesToComplete(unitType.getCost());
                    if (unit != null)
                        cycles = openedPanelCity.cyclesToComplete(unit.getRemainedCost());
                    if (cycles>=12345) {
                        textString = textString + "Never, Production=0";
                    } else {
                        textString = textString + cycles + " Cycles";
                    }
                }
                Button button = new Button(textString);
                button.minWidthProperty().bind(scrollPane.widthProperty());
                vBox.getChildren().add(button);
                if (buy)
                    button.setOnAction(actionEvent -> {
                        leftPanel.getChildren().clear();
                        String description = "Click on the tile you want to put\nyour new unit, then click OK.";
                        runSelectingMode(description, actionEvent1 -> buyTheSelectedUnitType(unitType.toString(), GameController.getSelectedTile()), actionEvent);
                    });
                else
                    button.setOnAction(event -> {
                        startProducingUnit(unitType.toString());
                        leftPanel.getChildren().clear();
                    });
            }
        }
    }

    private void addConfirmButton(String buttonName, String description, EventHandler<ActionEvent> func) {
        Button button = new Button(buttonName);
        leftPanel.getChildren().add(button);
        button.setOnAction(actionEvent -> runSelectingMode(description, func, actionEvent));
    }

    private void runSelectingMode(String description, EventHandler<ActionEvent> OKFunction, ActionEvent actionEvent) {
        gameControllerFX.setSelectingTile(true);
        leftPanel.getChildren().clear();
        leftPanel.getChildren().add(new Text(description));
        Button ok = new Button("OK");
        Button cancel = new Button("CANCEL");
        leftPanel.getChildren().addAll(ok, cancel);
        ok.setOnAction(actionEvent1 -> {
            gameControllerFX.setSelectingTile(false);
            leftPanel.getChildren().clear();
            OKFunction.handle(actionEvent);
        });
        cancel.setOnAction(actionEvent1 -> {
            gameControllerFX.setSelectingTile(false);
            leftPanel.getChildren().clear();
        });
    }

    public void buyUnitSelectTile(Tile tile) {
        cityTexts[5].setText("the selected tile is: " + tile.getX() + ", " + tile.getY() + ". now click on the unit type you want to buy");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutY(367);
        cityPanelPane.getChildren().add(scrollPane);
        buyUnitPane = scrollPane;
//        AnchorPane secondAnchorPane = addProducingUnitsButtons(tile);
        buttonsProcess[5] = 2;
        if (buyUnitPane != null)
            buyUnitPane.setOpacity(1);
//        scrollPane.setContent(secondAnchorPane);
    }

    public void buildBuildingSelectTile(Tile tile) {
        cityTexts[7].setText("the selected tile is: " + tile.getX() + ", " + tile.getY() + ". now click on the unit type you want to start producing");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutY(97 + 45 * 8);
        cityPanelPane.getChildren().add(scrollPane);
        startBuildingBuildingsPane = scrollPane;
//        AnchorPane secondAnchorPane = buildingsListPane(tile);
        buttonsProcess[7] = 2;
        if (startBuildingBuildingsPane != null)
            startBuildingBuildingsPane.setOpacity(1);
//        scrollPane.setContent(secondAnchorPane);
    }

    public void firstTileReassign(Tile tile) {
        Text text = new Text("the origin tile is: " + tile.getX() + ", " + tile.getY() + ".\nnow select the destination tile");
        leftPanel.getChildren().add(text);
    }

    public void buildBuilding(BuildingType buildingType, Tile tile, boolean buy) {
        switch (CityCommandsController.buildBuilding(buildingType, tile, openedPanelCity, buy)) {
            case 0 -> StageController.errorMaker("nicely done", "building's building's started", Alert.AlertType.INFORMATION);
            case 3 -> StageController.errorMaker("duplication", "your city already has this building", Alert.AlertType.ERROR);
            case 4 -> StageController.errorMaker("prerequisites not satisfied", "you don't have the prerequisite buildings", Alert.AlertType.ERROR);
            case 6 -> StageController.errorMaker("no river found", "you need to have rivers in your city to build this building", Alert.AlertType.ERROR);
            case 7 -> StageController.errorMaker("no river or lake found", "you need to have rivers or lakes in your city to build this building", Alert.AlertType.ERROR);
            case 8 -> StageController.errorMaker("you cannot place you building over there", "the windmill cannot be build on a hill", Alert.AlertType.ERROR);
            case 9 -> StageController.errorMaker("you cannot place you building over there", "a building cannot be placed on an ocean or a mountain", Alert.AlertType.ERROR);
            case 10 -> StageController.errorMaker("no resources?", "you don't have the prerequisite resources", Alert.AlertType.ERROR);
            case 11 -> StageController.errorMaker("no money?", "you don't have enough gold", Alert.AlertType.ERROR);
            case 12 -> StageController.errorMaker("not this tile", "the selected tile already has a building on it", Alert.AlertType.ERROR);
            case 15 -> StageController.errorMaker("not this tile", "the selected tile is not in your city area.", Alert.AlertType.ERROR);
            case 20 -> StageController.errorMaker("nicely done", "You purchased this building successfully.", Alert.AlertType.INFORMATION);
        }
        gameControllerFX.renderMap();
    }

    public void removeCitizenFromTile(Tile tile) {
        switch (CityCommandsController.removeCitizen(tile.getX(), tile.getY(), openedPanelCity)) {
            case 0 -> StageController.errorMaker("nicely done", "citizen removed from tile successfully", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("not yours", "the selected tile is not in your city's region", Alert.AlertType.ERROR);
            case 2 -> StageController.errorMaker("not getting worked on", "the selected tile is not getting worked on", Alert.AlertType.ERROR);
        }
        gameControllerFX.renderMap();
    }

    private void startProducingUnit(String string) {
        if (GameController.startProducingUnit(string) == 5) {
            StageController.errorMaker("resources required", "you don't have the required resources", Alert.AlertType.ERROR);
        }

        gameControllerFX.renderMap();
    }

    public void secondTileReassign(Tile firstTile, Tile secondTile) {
        switch (CityCommandsController.reAssignCitizen(firstTile.getX(), firstTile.getY(), secondTile.getX(), secondTile.getY(), openedPanelCity)) {
            case 0 -> StageController.errorMaker("nicely done", "you reassign a citizen to the selected tile successfully", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("not yours", "the destinationTile is not in your city's region", Alert.AlertType.ERROR);
            case 3 -> StageController.errorMaker("not yours", "the originTile is not yours", Alert.AlertType.ERROR);
            case 4 -> StageController.errorMaker("not getting worked on", "the originTile is not getting worked on", Alert.AlertType.ERROR);
        }
        gameControllerFX.renderMap();
    }


    public void buyTile(Tile tile) {
        switch (CityCommandsController.buyTile(tile.getX(), tile.getY(), openedPanelCity)) {
            case 0 -> StageController.errorMaker("The tile is yours", "you bought the tile successfully", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("You're dumber than a box of rocks", "the selected tile is not a neighbour to your city", Alert.AlertType.ERROR);
            case 2 -> StageController.errorMaker("You don't deserve the tile", "it's price is too high", Alert.AlertType.ERROR);
            case 3 -> StageController.errorMaker("what", "the selected tile is already a property of another civilization(or maybe even it's yours)", Alert.AlertType.ERROR);
        }
        gameControllerFX.renderMap();
    }

    public void attackTile(Tile tile) {

        switch (CityCommandsController.cityAttack(tile.getX(), tile.getY(), openedPanelCity)) {
            case 0 -> StageController.errorMaker("nicely done", "you attacked successfully", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("self-harm is haram", "the selected tile is your own city's main tile", Alert.AlertType.ERROR);
            case 2 -> StageController.errorMaker("attack what", "there are no nonCivilians over there", Alert.AlertType.ERROR);
            case 3 -> StageController.errorMaker("bruh", "you selected one of your own units", Alert.AlertType.ERROR);
            case 4 -> StageController.errorMaker("no ranges?", "the selected tile is not in your range", Alert.AlertType.ERROR);
            case 5 -> StageController.errorMaker("you look greedy", "you can't attack more than once in one cycle", Alert.AlertType.ERROR);
        }
        gameControllerFX.renderMap();
    }

    public void assignCitizenToTile(Tile tile) {
        switch (CityCommandsController.assignCitizen(tile.getX(), tile.getY(), openedPanelCity)) {
            case 0 -> StageController.errorMaker("nicely done", "you assign a citizen to the selected tile successfully", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("not yours", "The selected tile is not in your city's region", Alert.AlertType.ERROR);
            case 2 -> StageController.errorMaker("not enough citizens", "great news! you have dropped the unemployment percentage of your city to 0%", Alert.AlertType.ERROR);
        }
        gameControllerFX.renderMap();
    }

    public void buyTheSelectedUnitType(String string, Tile tile) {
        switch (CityCommandsController.buyUnit(string, tile.getX(), tile.getY())) {
            case 0 -> StageController.errorMaker("nicely done", "unit bought from tile successfully", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("not your tile", "the selected tile is not in your territory", Alert.AlertType.ERROR);
            case 2 -> StageController.errorMaker("you look poor", "you don't have enough gold", Alert.AlertType.ERROR);
            case 3 -> StageController.errorMaker("duplication", "there is already a unit on the tile you selected", Alert.AlertType.ERROR);
        }
        gameControllerFX.renderMap();
    }

    public void disableCityPanel() {
        if (gameControllerFX.getCityPage() != null)
            gameControllerFX.getCityPage().setOpacity(0);
        buttonsProcess[8] = 0;
        if (gameControllerFX.getCityPage() != null) {
            gameControllerFX.getCityPage().setLayoutX(-2000);
            gameControllerFX.getCityPage().setLayoutY(-2000);
        }
    }


    public static int[] getButtonsProcess() {
        return buttonsProcess;
    }

    public static AnchorPane getCityPanelPane() {
        return cityPanelPane;
    }

    public City getOpenedPanelCity() {
        return openedPanelCity;
    }
}
