package com.example.demo.view;

import com.example.demo.controller.LoginController;
import com.example.demo.controller.gameController.CityCommandsController;
import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.City;
import com.example.demo.model.Map;
import com.example.demo.model.Units.Unit;
import com.example.demo.model.Units.UnitType;
import com.example.demo.model.User;
import com.example.demo.model.building.Building;
import com.example.demo.model.building.BuildingType;
import com.example.demo.model.tiles.Tile;
import com.example.demo.view.model.GraphicTile;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameControllerFX {
    public HBox infoBar;
    public Button infoButton;
    public Button researchesButton;
    public Button unitsButton;
    public Button economicsButton;
    public Button demographicsButton;
    public Button militaryButton;
    public Button notificationsButton;
    public ScrollPane infoTab;
    public Text infoText;
    public int infoTabNumber = -1;
    public Button cityButton;
    public ScrollPane cityPage;
    public Text cityText;
    public VBox rightPanelVBox;
    private City openedPanelCity;
    private boolean selectingTile;
    private Tile reassignOriginTile;
    public final Text publicText = new Text("");
    @FXML
    private VBox leftPanel;
    @FXML
    private HBox statusBar;
    @FXML
    private BorderPane root;
    @FXML
    private AnchorPane mapPane;
    @FXML
    private Pane upperMapPane;
    private double startX;
    private double startY;
    private static boolean isCityPanelOpen = false;
    private static boolean waitingToSelectTileToBuy = false;
    private static boolean waitingToSelectTileToAttackFromCity = false;
    private static boolean waitingToSelectTileToAssignCitizens = false;
    private static boolean waitingToSelectTileToRemoveCitizen = false;
    private static boolean waitingToSelectTileToStartProducingUnit = false;

    private static int buyUnitProcess = 0;
    private static int reAssignProcess = 0;
    private static int buildBuildingProcess = 0;
    private static Text buyTileText;
    private static Text attackTileFromCity;
    private static Text assignCitizenText;
    private static Text reAssignCitizenText;
    private static Text removeCitizenText;
    private static Text buyUnitText;
    private static Text startBuildingBuildingText;
    private static ScrollPane buyUnitPane;
    private static ScrollPane startProducingPane;
    private static ScrollPane startBuildingBuildingsPane;

    private static Tile buyUnitTile;
    private static Tile buildBuildingTile;
    private static AnchorPane cityPanelPane;
    private ArrayList<Button> cityPanelButtons = new ArrayList<>();
    private ArrayList<Text> cityPanelTexts = new ArrayList<>();

    public static boolean isWaitingToSelectTileToRemoveCitizen() {
        return waitingToSelectTileToRemoveCitizen;
    }

    public static void setWaitingToSelectTileToRemoveCitizen(boolean waitingToSelectTileToRemoveCitizen) {
        GameControllerFX.waitingToSelectTileToRemoveCitizen = waitingToSelectTileToRemoveCitizen;
    }


    public void disableCityPanel() {
        if (cityPage != null)
            cityPage.setOpacity(0);
        isCityPanelOpen = false;
        if (cityPage != null) {
            cityPage.setLayoutX(-2000);
            cityPage.setLayoutY(-2000);
        }

    }

    private void eachInfoButtonsClicked(int number) {
        if (infoTabNumber == number) {
            infoTab.setOpacity(0);
            cityPanelPane.setOpacity(0);
            infoTabNumber = -1;
            disableCityPanel();
        } else {
            infoText.setDisable(false);
            infoTab.setContent(infoText);
            switch (number) {
                case 0:
                    infoText.setText(InfoController.infoResearches());
                    break;
                case 1:
//                    infoText.setText();
                    break;
                case 2:
                    cityButtonClicked();
                    break;
                case 3:
                    infoText.setText(InfoController.infoEconomic());
                    break;
                case 4:
                    infoText.setText(InfoController.infoDemographic());
                    break;
                case 5:
                    infoText.setText(InfoController.printMilitaryOverview());
                    break;
                case 6:
                    infoText.setText(InfoController.infoNotifications(10));
                    break;
                case 7:
                    turnEveryButtonOff();
                    infoText.setText(InfoController.cityBanner(openedPanelCity));
                    break;
            }
            infoTab.setOpacity(1);
            infoTabNumber = number;
        }
    }

    private void cityButtonClicked() {
        infoText.setDisable(true);
        AnchorPane anchorPane = new AnchorPane();
        Text[] texts = new Text[GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities().size()];
        Button[] buttons = new Button[texts.length];
        for (int i = 0; i < GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities().size(); i++) {
            String string = InfoController.printCity(i);
            texts[i] = new Text(string);
            texts[i].setLayoutY((i + 1) * 45);
            anchorPane.getChildren().add(texts[i]);
            buttons[i] = new Button("open");
            buttons[i].setLayoutX(texts[i].getLayoutBounds().getMinX() + texts[i].getLayoutBounds().getWidth() + 10);
            buttons[i].setLayoutY((i + 0.5) * 45);
            int finalI = i;
            buttons[i].setOnMouseClicked(event -> cityPanel(GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities().get(finalI), false));
            anchorPane.getChildren().add(buttons[i]);
        }

        infoTab.setContent(anchorPane);
    }

    private void cityPanel(City city, boolean isRightClick) {
        if (!rightPanelVBox.getChildren().contains(cityPage) && !isRightClick) {
            rightPanelVBox.getChildren().add(cityPage);
            List<Node> nodes = new ArrayList<>(rightPanelVBox.getChildren());
            Collections.rotate(nodes.subList(rightPanelVBox.getChildren().indexOf(infoTab), rightPanelVBox.getChildren().indexOf(cityPage) + 1), -1);
            rightPanelVBox.getChildren().clear();
            rightPanelVBox.getChildren().addAll(nodes);
        }

        if (isCityPanelOpen) {
            cityPage.setOpacity(0);
            isCityPanelOpen = false;
            return;
        }
        openedPanelCity = city;
        initCityPanel();
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
        button.setOnMouseClicked(event -> eachInfoButtonsClicked(7));
        anchorPane.getChildren().add(button);

        text = new Text("Click on a tile to buy or press \"e\" if you don't want to continue buying");
        button = new Button("Buy Tile");
        selectingTileButtons(text, button, 97, anchorPane, "BUY");

        text = new Text("Click on a tile to attack or press \"e\" if you don't want to continue attacking");
        button = new Button("Attack Tile");
        selectingTileButtons(text, button, 142, anchorPane, "ATTACK");

        text = new Text("Click on a tile to assign your citizens to it or press \"e\" if you don't want to continue assigning");
        button = new Button("Assign citizen");
        selectingTileButtons(text, button, 187, anchorPane, "ASSIGN");

        text = new Text("Click on the tile you want to reassign your citizen from or press \"e\" if you don't want to continue");
        button = new Button("Reassign citizen");
        selectingTileButtons(text, button, 232, anchorPane, "REASSIGN");

        text = new Text("Click on the tile you want to remove your citizen from or press \"e\" if you don't want to continue");
        button = new Button("Remove citizen");
        selectingTileButtons(text, button, 277, anchorPane, "REMOVE");

        text = new Text("Click on the tile you want to place your unit on or press \"e\" if you don't want to continue");
        button = new Button("Buy unit");
        selectingTileButtons(text, button, 322, anchorPane, "BUY_UNIT");

        button = new Button("startProducingUnit");
        selectingTileButtons(null, button, 367, anchorPane, "START_PRODUCING_UNIT");

        text = new Text("Click on the tile you want to place your building on or press \"e\" if you don't want to continue");
        button = new Button("startBuildingBuildings");
        selectingTileButtons(text, button, 412, anchorPane, "START_BUILDING_BUILDINGS");

        cityPanelButtons = new ArrayList<>();
        cityPanelTexts = new ArrayList<>();
        for (Node child : anchorPane.getChildren()) {
            if (child instanceof Button)
                cityPanelButtons.add((Button) child);
        }
        for (Node child : anchorPane.getChildren()) {
            if (child instanceof Text)
                cityPanelTexts.add((Text) child);
        }


        cityPanelPane = anchorPane;
        cityPage.setContent(anchorPane);
        cityPage.setOpacity(1);
        isCityPanelOpen = true;
    }

    private void selectingTileButtons(Text text, Button button, double textY, AnchorPane anchorPane, String function) {
        if (text != null) {
            text.setLayoutY(textY);
            text.setOpacity(0);
        }
        button.setLayoutY(textY - 22);
        if (function.equals("BUY"))
            buyTileText = text;
        if (function.equals("ATTACK"))
            attackTileFromCity = text;
        if (function.equals("ASSIGN"))
            assignCitizenText = text;
        if (function.equals("REASSIGN"))
            reAssignCitizenText = text;
        if (function.equals("REMOVE"))
            removeCitizenText = text;
        if (function.equals("BUY_UNIT"))
            buyUnitText = text;
        if (function.equals("START_BUILDING_BUILDINGS"))
            startBuildingBuildingText = text;
        button.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().getName().equals("e") || keyEvent.getCode().getName().equals("E")) {
                if (function.equals("BUY"))
                    waitingToSelectTileToBuy = false;
                if (function.equals("ATTACK"))
                    waitingToSelectTileToAttackFromCity = false;
                if (function.equals("ASSIGN"))
                    waitingToSelectTileToAssignCitizens = false;
                if (function.equals("REASSIGN"))
                    reAssignProcess = 0;
                if (function.equals("REMOVE"))
                    waitingToSelectTileToRemoveCitizen = false;
                if (function.equals("BUY_UNIT")) {
                    buyUnitProcess = 0;
                    anchorPane.getChildren().remove(buyUnitPane);
                    cityPanelButtons.get(7).setLayoutY(367 - 22);
                }
                if (function.equals("START_PRODUCING_UNIT")) {
                    cityPanelButtons.get(8).setLayoutY(367 + 45 - 22);
                    waitingToSelectTileToStartProducingUnit = false;
                }
                if (function.equals("START_BUILDING_BUILDINGS")) {
                    buildBuildingProcess = 0;
                    anchorPane.getChildren().remove(startBuildingBuildingsPane);
                }
                assert text != null;
                text.setOpacity(0);
                turnEveryButtonOff();
            }
        });
        button.setOnMouseClicked(event -> doTileClicked(button, text, function));
        anchorPane.getChildren().add(button);
        if (text != null)
            anchorPane.getChildren().add(text);
    }


    private void doTileClicked(Button button, Text text, String function) {
        turnEveryButtonOff();
        if (text != null) {
            text.setLayoutX(button.getLayoutX() + button.getWidth() + 10);
            text.setOpacity(1);
        }
        if (function.equals("BUY"))
            waitingToSelectTileToBuy = true;
        if (function.equals("ATTACK"))
            waitingToSelectTileToAttackFromCity = true;
        if (function.equals("ASSIGN"))
            waitingToSelectTileToAssignCitizens = true;
        if (function.equals("REASSIGN"))
            reAssignProcess = 1;
        if (function.equals("REMOVE"))
            waitingToSelectTileToRemoveCitizen = true;
        if (function.equals("BUY_UNIT")) {
            buyUnitProcess = 1;
        }
        if (function.equals("START_PRODUCING_UNIT")) {
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setLayoutY(400);
            cityPanelPane.getChildren().add(scrollPane);
            startProducingPane = scrollPane;
            AnchorPane secondAnchorPane = unitsListPane(null);
//            cityPanelButtons.get(7).setLayoutY(600);
            if (startProducingPane != null)
                startProducingPane.setOpacity(1);
            scrollPane.setContent(secondAnchorPane);
            waitingToSelectTileToStartProducingUnit = true;
        }
//        if (function.equals("START_BUILDING_BUILDINGS")) {
//            ScrollPane scrollPane = new ScrollPane();
//            scrollPane.setLayoutY(457);
//            cityPanelPane.getChildren().add(scrollPane);
//            startBuildingBuildingsPane = scrollPane;
//            AnchorPane secondAnchorPane = buildingsListPane();
//            if (startBuildingBuildingsPane != null)
//                startBuildingBuildingsPane.setOpacity(1);
//            scrollPane.setContent(secondAnchorPane);
//        }
        if (function.equals("START_BUILDING_BUILDINGS")) {
            buildBuildingProcess = 1;
        }
    }

    private void infoButtonClicked(MouseEvent mouseEvent) {
        if (infoBar.getOpacity() == 1) {
            infoBar.setOpacity(0);
            infoTab.setOpacity(0);
            researchesButton.setDisable(true);
            unitsButton.setDisable(true);
            economicsButton.setDisable(true);
            demographicsButton.setDisable(true);
            militaryButton.setDisable(true);
            notificationsButton.setDisable(true);
        } else {
            infoBar.setOpacity(1);
            researchesButton.setDisable(false);
            unitsButton.setDisable(false);
            economicsButton.setDisable(false);
            demographicsButton.setDisable(false);
            militaryButton.setDisable(false);
            notificationsButton.setDisable(false);
        }
    }

    public void initialize() {
        startAFakeGame();
        MapMoveController mapMove = new MapMoveController(mapPane, upperMapPane);
        StatusBarController.init(statusBar);
        setButtonsWhenClicked();
        renderMap();
    }

    private void setButtonsWhenClicked() {
        infoButton.setOnMouseClicked(this::infoButtonClicked);
        researchesButton.setOnMouseClicked(event -> eachInfoButtonsClicked(0));
        unitsButton.setOnMouseClicked(event -> eachInfoButtonsClicked(1));
        cityButton.setOnMouseClicked(event -> eachInfoButtonsClicked(2));
        economicsButton.setOnMouseClicked(event -> eachInfoButtonsClicked(3));
        demographicsButton.setOnMouseClicked(event -> eachInfoButtonsClicked(4));
        militaryButton.setOnMouseClicked(event -> eachInfoButtonsClicked(5));
        notificationsButton.setOnMouseClicked(event -> eachInfoButtonsClicked(6));
    }

    private static GraphicTile[][] graphicMap;

    public boolean getSelectingTile() {
        return selectingTile;
    }

    public void setSelectingTile(boolean mode) {
        selectingTile = mode;
    }


    public void renderMap() {
        mapPane.getChildren().clear();
        Map map = GameController.getMap();
        graphicMap = new GraphicTile[map.getX()][map.getY()];
        Tile[][] tiles = map.getTiles();
        for (int j = 0; j < map.getY(); j++)
            for (int i = 0; i < map.getX(); i++)
                graphicMap[i][j] = new GraphicTile(tiles[i][j], mapPane, leftPanel, this);
        cityPage.setViewOrder(-2);
        mapPane.getChildren().add(cityPage);

    }

    /*
     * This methode is only for testing
     */
    private void startAFakeGame() {
        //start a fake game
        User user = new User("Sayyed", "ali", "Tayyeb");
        User user2 = new User("Sayyed2", "ali", "Tayyeb");
        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        LoginController.loginUser("u", "pap");
        Map.setX(60);
        Map.setY(90);
        GameController.startGame(users);
    }

    public static boolean isWaitingToSelectTileToBuy() {
        return waitingToSelectTileToBuy;
    }

    public static boolean isWaitingToSelectTileToAttackFromCity() {
        return waitingToSelectTileToAttackFromCity;
    }

    public static boolean isWaitingToSelectTileToAssignCitizens() {
        return waitingToSelectTileToAssignCitizens;
    }

    public static int getReAssignProcess() {
        return reAssignProcess;
    }

    public void buyTile(Tile tile) {
        switch (CityCommandsController.buyTile(tile.getX(), tile.getY(), openedPanelCity)) {
            case 0 -> StageController.errorMaker("The tile is yours", "you bought the tile successfully", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("You're dumber than a box of rocks", "the selected tile is not a neighbour to your city", Alert.AlertType.ERROR);
            case 2 -> StageController.errorMaker("You don't deserve the tile", "it's price is too high", Alert.AlertType.ERROR);
            case 3 -> StageController.errorMaker("what", "the selected tile is already a property of another civilization(or maybe even it's yours)", Alert.AlertType.ERROR);
        }
        turnEveryButtonOff();
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
    }

    public void assignCitizenToTile(Tile tile) {
        switch (CityCommandsController.assignCitizen(tile.getX(), tile.getY(), openedPanelCity)) {
            case 0 -> StageController.errorMaker("nicely done", "you assign a citizen to the selected tile successfully", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("not yours", "The selected tile is not in your city's region", Alert.AlertType.ERROR);
            case 2 -> StageController.errorMaker("not enough citizens", "great news! you have dropped the unemployment percentage of your city to 0%", Alert.AlertType.ERROR);
        }
        turnEveryButtonOff();
    }

    public static GraphicTile[][] getGraphicMap() {
        return graphicMap;
    }

    public void firstTileReassign(Tile tile) {
        reassignOriginTile = tile;
        reAssignCitizenText.setText("the originTile is: " + tile.getX() + ", " + tile.getY() + ". now select the destinationTile");
        reAssignProcess = 2;

    }

    public void secondTileReassign(Tile tile) {
        switch (CityCommandsController.reAssignCitizen(reassignOriginTile.getX(), reassignOriginTile.getY(), tile.getX(), tile.getY(), openedPanelCity)) {
            case 0 -> StageController.errorMaker("nicely done", "you reassign a citizen to the selected tile successfully", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("not yours", "the destinationTile is not in your city's region", Alert.AlertType.ERROR);
            case 3 -> StageController.errorMaker("not yours", "the originTile is not yours", Alert.AlertType.ERROR);
            case 4 -> StageController.errorMaker("not getting worked on", "the originTile is not getting worked on", Alert.AlertType.ERROR);
        }
        turnEveryButtonOff();
    }

    public void removeCitizenFromTile(Tile tile) {
        switch (CityCommandsController.removeCitizen(tile.getX(), tile.getY(), openedPanelCity)) {
            case 0 -> StageController.errorMaker("nicely done", "citizen removed from tile successfully", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("not yours", "the selected tile is not in your city's region", Alert.AlertType.ERROR);
            case 2 -> StageController.errorMaker("not getting worked on", "the selected tile is not getting worked on", Alert.AlertType.ERROR);
        }
        turnEveryButtonOff();
    }

    public void turnEveryButtonOff() {

        buyTileText.setOpacity(0);
        attackTileFromCity.setOpacity(0);
        assignCitizenText.setOpacity(0);
        removeCitizenText.setOpacity(0);
        reAssignCitizenText.setOpacity(0);
        buyUnitText.setOpacity(0);
        startBuildingBuildingText.setOpacity(0);

        waitingToSelectTileToBuy = false;
        waitingToSelectTileToAttackFromCity = false;
        waitingToSelectTileToAssignCitizens = false;
        waitingToSelectTileToRemoveCitizen = false;
        reAssignProcess = 0;
        buyUnitProcess = 0;
        buildBuildingProcess = 0;
        if (buyUnitPane != null)
            buyUnitPane.setOpacity(0);
        buyUnitTile = null;
        reassignOriginTile = null;
        buyUnitText.setText("Click on the tile you want to place your unit on or press \"e\" if you don't want to continue");
        reAssignCitizenText.setText("Click on the tile you want to reassign your citizen from or press \"e\" if you don't want to continue");
        startBuildingBuildingText.setText("Click on the tile you want to place your building on or press \"e\" if you don't want to continue");
        cityPanelButtons.get(7).setLayoutY(412 - 22 - 45);
        System.out.println(cityPanelButtons.get(7).getLayoutY());
        cityPanelButtons.get(8).setLayoutY(412 - 22);
        cityPanelPane.getChildren().remove(startBuildingBuildingsPane);
        cityPanelPane.getChildren().remove(startProducingPane);
        startProducingPane = null;
        startBuildingBuildingsPane = null;
    }

    public static int getBuyUnitProcess() {
        return buyUnitProcess;
    }

    public void buildBuildingSelectTile(Tile tile) {
        buildBuildingTile = tile;
        startBuildingBuildingText.setText("the selected tile is: " + tile.getX() + ", " + tile.getY() + ". now click on the unit type you want to start producing");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutY(457);
        cityPanelPane.getChildren().add(scrollPane);
        startBuildingBuildingsPane = scrollPane;
        AnchorPane secondAnchorPane = buildingsListPane(tile);
//        cityPanelButtons.get(8).setLayoutY(690);
        buildBuildingProcess = 2;
        if (startBuildingBuildingsPane != null)
            startBuildingBuildingsPane.setOpacity(1);
        scrollPane.setContent(secondAnchorPane);
    }

    public void buyUnitSelectTile(Tile tile) {
        buyUnitTile = tile;
        buyUnitText.setText("the selected tile is: " + tile.getX() + ", " + tile.getY() + ". now click on the unit type you want to buy");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutY(367);
        cityPanelPane.getChildren().add(scrollPane);
        buyUnitPane = scrollPane;
        AnchorPane secondAnchorPane = unitsListPane(tile);
        buyUnitProcess = 2;
        if (buyUnitPane != null)
            buyUnitPane.setOpacity(1);
        scrollPane.setContent(secondAnchorPane);
    }

    private AnchorPane unitsListPane(Tile tile) {
        AnchorPane secondAnchorPane = new AnchorPane();
        int i = 30;
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
//                text.setStyle("-fx-font: \"/com/example/demo/font/OPTIColumna.otf\";\n" +
//                        "    -fx-font-size: 5;");
                if (tile == null)
                    text.setOnMouseClicked(event -> startProducingUnit(value.toString()));
                else
                    text.setOnMouseClicked(event -> buyTheSelectedUnitType(value.toString(), tile));
                text.setCursor(Cursor.HAND);
                text.setFont(new Font(15));
//                text.setFont(ImageLoader.getFont("impactFont"));
//                Button button1 = new Button(value.toString());
//                button1.setLayoutY(i);
//                secondAnchorPane.getChildren().add(button1);
//                i += 45;
//                button1.setOnMouseClicked(event -> buyTheSelectedUnitType(button1.getText().toString(), tile));
            }
        }
        if (tile != null)
            cityPanelButtons.get(7).setLayoutY(355 + i);
        System.out.println("fuck");
        System.out.println("i: " + i);
        cityPanelButtons.get(8).setLayoutY(400 + i);
        return secondAnchorPane;
    }

    private AnchorPane buildingsListPane(Tile tile) {
        AnchorPane secondAnchorPane = new AnchorPane();
        int i = 30;
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
//                text.setStyle("-fx-font: \"/com/example/demo/font/OPTIColumna.otf\";\n" +
//                        "    -fx-font-size: 5;");
                text.setOnMouseClicked(event -> buildBuilding(value, tile));
                text.setCursor(Cursor.HAND);
                text.setFont(new Font(15));
//                text.setFont(ImageLoader.getFont("impactFont"));
//                Button button1 = new Button(value.toString());
//                button1.setLayoutY(i);
//                secondAnchorPane.getChildren().add(button1);
//                i += 45;
//                button1.setOnMouseClicked(event -> buyTheSelectedUnitType(button1.getText().toString(), tile));
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
    }

    public void buyTheSelectedUnitType(String string, Tile tile) {
        switch (CityCommandsController.buyUnit(string, tile.getX(), tile.getY())) {
            case 0 -> StageController.errorMaker("nicely done", "unit bought from tile successfully", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("not your tile", "the selected tile is not in your territory", Alert.AlertType.ERROR);
            case 2 -> StageController.errorMaker("you look poor", "you don't have enough gold", Alert.AlertType.ERROR);
            case 3 -> StageController.errorMaker("duplication", "there is already a unit on the tile you selected", Alert.AlertType.ERROR);
        }
        turnEveryButtonOff();
    }

    public void cityClicked(City city, GraphicTile graphicTile) {
        openedPanelCity = city;
        if (cityPage == null)
            initCityPanel();
        cityPage.setViewOrder(-2);
        rightPanelVBox.getChildren().remove(cityPage);
        if (!mapPane.getChildren().contains(cityPage))
            mapPane.getChildren().add(cityPage);
        cityPage.setLayoutX(graphicTile.getX());
        cityPage.setLayoutY(graphicTile.getY());
        cityPage.setOpacity(1);
        System.out.println(graphicTile.getX() + " " + graphicTile.getY());
        cityPanel(city, true);
    }

    public static int getBuildBuildingProcess() {
        return buildBuildingProcess;
    }

    public void buildBuilding(BuildingType buildingType, Tile tile) {
        switch (CityCommandsController.buildBuilding(buildingType, tile)) {
            case 0 -> StageController.errorMaker("nicely done", "building's building's started", Alert.AlertType.INFORMATION);
            case 3 -> StageController.errorMaker("duplication", "your city already has this building", Alert.AlertType.ERROR);
            case 4 -> StageController.errorMaker("prerequisites not satisfied", "you don't have the prerequisite buildings", Alert.AlertType.ERROR);
            case 6 -> StageController.errorMaker("no river found", "you need to have rivers in your city to build this building", Alert.AlertType.ERROR);
            case 7 -> StageController.errorMaker("no river or lake found", "you need to have rivers or lakes in your city to build this building", Alert.AlertType.ERROR);
            case 8 -> StageController.errorMaker("you cannot place you building over there", "the windmill cannot be build on a hill", Alert.AlertType.ERROR);
            case 9 -> StageController.errorMaker("you cannot place you building over there", "a building cannot be placed on an ocean or a mountain", Alert.AlertType.ERROR);
            case 10 -> StageController.errorMaker("no resources?", "you don't have the prerequisite resources", Alert.AlertType.ERROR);
        }
        turnEveryButtonOff();
    }
}
