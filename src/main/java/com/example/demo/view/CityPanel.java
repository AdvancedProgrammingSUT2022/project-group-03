package com.example.demo.view;

import com.example.demo.controller.gameController.CityCommandsController;
import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.City;
import com.example.demo.model.Units.Unit;
import com.example.demo.model.Units.UnitType;
import com.example.demo.model.building.Building;
import com.example.demo.model.building.BuildingType;
import com.example.demo.model.tiles.Tile;
import com.example.demo.view.model.GraphicTile;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CityPanel {
    private final GameControllerFX gameControllerFX;

    public CityPanel(GameControllerFX gameControllerFX) {
        this.gameControllerFX = gameControllerFX;
    }

    private static ScrollPane buyUnitPane;
    private static ScrollPane startProducingPane;
    private static ScrollPane startBuildingBuildingsPane;
    private static AnchorPane cityPanelPane;
    private ArrayList<Button> cityPanelButtons = new ArrayList<>();
    private Tile reassignOriginTile;
    private City openedPanelCity;
    private static final Text[] cityTexts = new Text[8];
    private static final int[] buttonsProcess = new int[9];

    public void buildBuilding(BuildingType buildingType, Tile tile) {
        switch (CityCommandsController.buildBuilding(buildingType, tile, openedPanelCity)) {
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
        }
        turnEveryButtonOff();
        gameControllerFX.renderMap();
    }


    public void turnEveryButtonOff() {
        for (Text cityText : cityTexts)
            if (cityText != null)
                cityText.setOpacity(0);
        Arrays.fill(buttonsProcess, 0);
        if (buyUnitPane != null)
            buyUnitPane.setOpacity(0);
        reassignOriginTile = null;
        cityTexts[5].setText("Click on the tile you want to place your unit on or press \"e\" if you don't want to continue");
        cityTexts[3].setText("Click on the tile you want to reassign your citizen from or press \"e\" if you don't want to continue");
        cityTexts[7].setText("Click on the tile you want to place your building on or press \"e\" if you don't want to continue");
        cityPanelButtons.get(7).setLayoutY(97 + 6 * 45 - 22);
        System.out.println(cityPanelButtons.get(7).getLayoutY());
        cityPanelButtons.get(8).setLayoutY(97 + 7 * 45 - 22);
        cityPanelPane.getChildren().remove(startBuildingBuildingsPane);
        cityPanelPane.getChildren().remove(startProducingPane);
        startProducingPane = null;
        startBuildingBuildingsPane = null;
    }


    private AnchorPane buildingsListPane(Tile tile) {
        AnchorPane secondAnchorPane = new AnchorPane();
        int i = 0;
        for (BuildingType value : BuildingType.values()) {
            if (GameController.getCivilizations().get(GameController.getPlayerTurn()).doesContainTechnology(value.technologyType) == 1) {

                String textString = value + ": " + "cost: " + value.getCost();
                textString = textString + " | cycles to complete: ";
                Building building = openedPanelCity.findHalfBuiltBuildings(value);
                if (building == null)
                    textString = textString + openedPanelCity.cyclesToComplete(value.getCost());
                else textString = textString + openedPanelCity.cyclesToComplete(building.getRemainedCost());
                System.out.println(textString);
                Label text = new Label(textString);
                text.setLayoutY(i);
                secondAnchorPane.getChildren().add(text);
                i += 45;
                text.setOnMouseClicked(event -> buildBuilding(value, tile));
                text.setCursor(Cursor.HAND);
                text.setFont(new Font(15));
            }
        }
        return secondAnchorPane;
    }


    private void startProducingUnit(String string) {
        switch (GameController.startProducingUnit(string)) {
            case 0 -> StageController.errorMaker("nicely done", "the selected production started successfully", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("resources required", "you don't have the required resources", Alert.AlertType.ERROR);
        }
        turnEveryButtonOff();
        gameControllerFX.renderMap();
    }


    public void buyTheSelectedUnitType(String string, Tile tile) {
        switch (CityCommandsController.buyUnit(string, tile.getX(), tile.getY())) {
            case 0 -> StageController.errorMaker("nicely done", "unit bought from tile successfully", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("not your tile", "the selected tile is not in your territory", Alert.AlertType.ERROR);
            case 2 -> StageController.errorMaker("you look poor", "you don't have enough gold", Alert.AlertType.ERROR);
            case 3 -> StageController.errorMaker("duplication", "there is already a unit on the tile you selected", Alert.AlertType.ERROR);
        }
        turnEveryButtonOff();
        gameControllerFX.renderMap();
    }


    public void cityClicked(City city, GraphicTile graphicTile) {
        openedPanelCity = city;
        if (gameControllerFX.getCityPage() == null)
            initCityPanel();
        gameControllerFX.getCityPage().setViewOrder(-2);
        gameControllerFX.getRightPanelVBox().getChildren().remove(gameControllerFX.getCityPage());
        if (!gameControllerFX.getMapPane().getChildren().contains(gameControllerFX.getCityPage()))
            gameControllerFX.getMapPane().getChildren().add(gameControllerFX.getCityPage());
        gameControllerFX.getCityPage().setLayoutX(graphicTile.getX());
        gameControllerFX.getCityPage().setLayoutY(graphicTile.getY());
        gameControllerFX.getCityPage().setOpacity(1);
        System.out.println(graphicTile.getX() + " " + graphicTile.getY());
        cityPanel(city, true);
    }


    private AnchorPane unitsListPane(Tile tile) {
        AnchorPane secondAnchorPane = new AnchorPane();
        int i = 0;
        for (UnitType value : UnitType.values()) {
            if (GameController.getCivilizations().get(GameController.getPlayerTurn()).doesContainTechnology(value.technologyRequired) == 1) {

                String textString = value + ": " + "cost: " + value.getCost();
                if (tile == null) {
                    textString = textString + " | cycles to complete: ";
                    Unit unit = openedPanelCity.findHalfProducedUnit(value);
                    if (unit == null)
                        textString = textString + openedPanelCity.cyclesToComplete(value.getCost());
                    else textString = textString + openedPanelCity.cyclesToComplete(unit.getRemainedCost());
                }
                Label text = new Label(textString);
                text.setLayoutY(i);
                secondAnchorPane.getChildren().add(text);
                i += 45;
                if (tile == null)
                    text.setOnMouseClicked(event -> startProducingUnit(value.toString()));
                else
                    text.setOnMouseClicked(event -> buyTheSelectedUnitType(value.toString(), tile));
                text.setCursor(Cursor.HAND);
                text.setFont(new Font(15));
            }
        }
        if (tile != null)
            cityPanelButtons.get(7).setLayoutY(97 + 45 * 6 + i);
        cityPanelButtons.get(8).setLayoutY(97 + 45 * 7 + i);
        return secondAnchorPane;
    }


    public void buyUnitSelectTile(Tile tile) {
        cityTexts[5].setText("the selected tile is: " + tile.getX() + ", " + tile.getY() + ". now click on the unit type you want to buy");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutY(367);
        cityPanelPane.getChildren().add(scrollPane);
        buyUnitPane = scrollPane;
        AnchorPane secondAnchorPane = unitsListPane(tile);
        buttonsProcess[5] = 2;
        if (buyUnitPane != null)
            buyUnitPane.setOpacity(1);
        scrollPane.setContent(secondAnchorPane);
    }

    public void removeCitizenFromTile(Tile tile) {
        switch (CityCommandsController.removeCitizen(tile.getX(), tile.getY(), openedPanelCity)) {
            case 0 -> StageController.errorMaker("nicely done", "citizen removed from tile successfully", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("not yours", "the selected tile is not in your city's region", Alert.AlertType.ERROR);
            case 2 -> StageController.errorMaker("not getting worked on", "the selected tile is not getting worked on", Alert.AlertType.ERROR);
        }
        turnEveryButtonOff();
        gameControllerFX.renderMap();
    }

    public void buildBuildingSelectTile(Tile tile) {
        cityTexts[7].setText("the selected tile is: " + tile.getX() + ", " + tile.getY() + ". now click on the unit type you want to start producing");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutY(97 + 45 * 8);
        cityPanelPane.getChildren().add(scrollPane);
        startBuildingBuildingsPane = scrollPane;
        AnchorPane secondAnchorPane = buildingsListPane(tile);
//        cityPanelButtons.get(8).setLayoutY(690);
        buttonsProcess[7] = 2;
        if (startBuildingBuildingsPane != null)
            startBuildingBuildingsPane.setOpacity(1);
        scrollPane.setContent(secondAnchorPane);
    }

    public void firstTileReassign(Tile tile) {
        reassignOriginTile = tile;
        cityTexts[3].setText("the originTile is: " + tile.getX() + ", " + tile.getY() + ". now select the destinationTile");
        buttonsProcess[3] = 2;

    }

    public void secondTileReassign(Tile tile) {
        switch (CityCommandsController.reAssignCitizen(reassignOriginTile.getX(), reassignOriginTile.getY(), tile.getX(), tile.getY(), openedPanelCity)) {
            case 0 -> StageController.errorMaker("nicely done", "you reassign a citizen to the selected tile successfully", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("not yours", "the destinationTile is not in your city's region", Alert.AlertType.ERROR);
            case 3 -> StageController.errorMaker("not yours", "the originTile is not yours", Alert.AlertType.ERROR);
            case 4 -> StageController.errorMaker("not getting worked on", "the originTile is not getting worked on", Alert.AlertType.ERROR);
        }
        turnEveryButtonOff();
        gameControllerFX.renderMap();
    }


    public void buyTile(Tile tile) {
        switch (CityCommandsController.buyTile(tile.getX(), tile.getY(), openedPanelCity)) {
            case 0 -> StageController.errorMaker("The tile is yours", "you bought the tile successfully", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("You're dumber than a box of rocks", "the selected tile is not a neighbour to your city", Alert.AlertType.ERROR);
            case 2 -> StageController.errorMaker("You don't deserve the tile", "it's price is too high", Alert.AlertType.ERROR);
            case 3 -> StageController.errorMaker("what", "the selected tile is already a property of another civilization(or maybe even it's yours)", Alert.AlertType.ERROR);
        }
        turnEveryButtonOff();
        gameControllerFX.renderMap();
    }

    public void attackTile(Tile tile) {
        switch (CityCommandsController.cityAttack(tile.getX(), tile.getY(), openedPanelCity)) {
            case 0 -> StageController.errorMaker("nicely done", "you attacked successfully", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("self-harm is haram", "the selected tile is your own city's main tile", Alert.AlertType.ERROR);
            case 2 -> StageController.errorMaker("attack what", "there are no nonCivilians over there", Alert.AlertType.ERROR);
            case 3 -> StageController.errorMaker("bruh", "you selected one of your own units", Alert.AlertType.ERROR);
            case 4 -> StageController.errorMaker("no ranges?", "the selected tile is not in your range", Alert.AlertType.ERROR);
        }
        turnEveryButtonOff();
        gameControllerFX.renderMap();
    }

    public void assignCitizenToTile(Tile tile) {
        switch (CityCommandsController.assignCitizen(tile.getX(), tile.getY(), openedPanelCity)) {
            case 0 -> StageController.errorMaker("nicely done", "you assign a citizen to the selected tile successfully", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("not yours", "The selected tile is not in your city's region", Alert.AlertType.ERROR);
            case 2 -> StageController.errorMaker("not enough citizens", "great news! you have dropped the unemployment percentage of your city to 0%", Alert.AlertType.ERROR);
        }
        turnEveryButtonOff();
        gameControllerFX.renderMap();
    }

    private void initCityPanel() {
        AnchorPane anchorPane = new AnchorPane();
        Text text = new Text("City name: " + openedPanelCity.getName() +
                " | population:" + openedPanelCity.getPopulation() +
                " | (Unemployed) citizens: " + openedPanelCity.getCitizen());
        text.setLayoutY(15);
        anchorPane.getChildren().add(text);

        Button button = new Button("Show Banner");
        button.setLayoutY(30);
        button.setOnMouseClicked(event -> gameControllerFX.eachInfoButtonsClicked(7));
        anchorPane.getChildren().add(button);


        text = new Text("Click on a tile to buy or press \"e\" if you don't want to continue buying");
        button = new Button("Buy Tile");
        selectingTileButtons(text, button, anchorPane, 0);

        text = new Text("Click on a tile to attack or press \"e\" if you don't want to continue attacking");
        button = new Button("Attack Tile");
        selectingTileButtons(text, button, anchorPane, 1);

        text = new Text("Click on a tile to assign your citizens to it or press \"e\" if you don't want to continue assigning");
        button = new Button("Assign citizen");
        selectingTileButtons(text, button, anchorPane, 2);

        text = new Text("Click on the tile you want to reassign your citizen from or press \"e\" if you don't want to continue");
        button = new Button("Reassign citizen");
        selectingTileButtons(text, button, anchorPane, 3);

        text = new Text("Click on the tile you want to remove your citizen from or press \"e\" if you don't want to continue");
        button = new Button("Remove citizen");
        selectingTileButtons(text, button, anchorPane, 4);

        text = new Text("Click on the tile you want to place your unit on or press \"e\" if you don't want to continue");
        button = new Button("Buy unit");
        selectingTileButtons(text, button, anchorPane, 5);

        button = new Button("startProducingUnit");
        selectingTileButtons(null, button, anchorPane, 6);

        text = new Text("Click on the tile you want to place your building on or press \"e\" if you don't want to continue");
        button = new Button("startBuildingBuildings");
        selectingTileButtons(text, button, anchorPane, 7);

        cityPanelButtons = new ArrayList<>();
        ArrayList<Text> cityPanelTexts = new ArrayList<>();
        for (Node child : anchorPane.getChildren()) {
            if (child instanceof Button)
                cityPanelButtons.add((Button) child);
        }
        for (Node child : anchorPane.getChildren()) {
            if (child instanceof Text)
                cityPanelTexts.add((Text) child);
        }

        cityPanelPane = anchorPane;
        gameControllerFX.getCityPage().setContent(anchorPane);
        gameControllerFX.getCityPage().setOpacity(1);
        buttonsProcess[8] = 1;
    }


    private void selectingTileButtons(Text text, Button button, AnchorPane anchorPane, int textNumber) {
        double textY = 97 + textNumber * 45;
        if (text != null) {
            text.setLayoutY(textY);
            text.setOpacity(0);
        }
        button.setLayoutY(textY - 22);
        if (textNumber != 6)
            cityTexts[textNumber] = text;
        button.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().getName().equals("e") || keyEvent.getCode().getName().equals("E")) {
                buttonsProcess[textNumber] = 0;
                if (textNumber == 5) {
                    anchorPane.getChildren().remove(buyUnitPane);
                    cityPanelButtons.get(7).setLayoutY(97 + 45 * 5);
                }
                if (textNumber == 6)
                    cityPanelButtons.get(8).setLayoutY(97 + 45 * 6);
                if (textNumber == 7)
                    anchorPane.getChildren().remove(startBuildingBuildingsPane);
                assert text != null;
                text.setOpacity(0);
                turnEveryButtonOff();
            }
        });
        button.setOnMouseClicked(event -> doTileClicked(button, text, textNumber));
        anchorPane.getChildren().add(button);
        if (text != null)
            anchorPane.getChildren().add(text);
    }

    void cityPanel(City city, boolean isRightClick) {
        if (!gameControllerFX.getRightPanelVBox().getChildren().contains(gameControllerFX.getCityPage()) && !isRightClick) {
            gameControllerFX.getRightPanelVBox().getChildren().add(gameControllerFX.getCityPage());
            List<Node> nodes = new ArrayList<>(gameControllerFX.getRightPanelVBox().getChildren());
            Collections.rotate(nodes.subList(gameControllerFX.getRightPanelVBox().getChildren().indexOf(gameControllerFX.getInfoTab()), gameControllerFX.getRightPanelVBox().getChildren().indexOf(gameControllerFX.getCityPage()) + 1), -1);
            gameControllerFX.getRightPanelVBox().getChildren().clear();
            gameControllerFX.getRightPanelVBox().getChildren().addAll(nodes);
        }

        if (buttonsProcess[8] == 1) {
            gameControllerFX.getCityPage().setOpacity(0);
            buttonsProcess[8] = 0;
            return;
        }
        openedPanelCity = city;
        initCityPanel();
    }


    private void doTileClicked(Button button, Text text, int textNumber) {
        turnEveryButtonOff();
        if (text != null) {
            text.setLayoutX(button.getLayoutX() + button.getWidth() + 10);
            text.setOpacity(1);
        }
        buttonsProcess[textNumber] = 1;
        if (textNumber == 6) {
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setLayoutY(400);
            cityPanelPane.getChildren().add(scrollPane);
            startProducingPane = scrollPane;
            AnchorPane secondAnchorPane = unitsListPane(null);
            if (startProducingPane != null)
                startProducingPane.setOpacity(1);
            scrollPane.setContent(secondAnchorPane);
        }
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
