package com.example.demo.view;

import com.example.demo.controller.LoginController;
import com.example.demo.controller.gameController.CityCommandsController;
import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.City;
import com.example.demo.model.Map;
import com.example.demo.model.User;
import com.example.demo.model.tiles.Tile;
import com.example.demo.view.cheat.Cheat;
import com.example.demo.view.model.GraphicTile;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;

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
    private static City openedPanelCity;
    private static boolean selectingTile;
    public static final Text publicText = new Text("");
    public Button nextButton;
    @FXML
    private HBox cheatBar;
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
    private boolean isCityPanelOpen = false;
    private static boolean waitingToSelectTileToBuy = false;
    private static boolean waitingToSelectTileToAttackFromCity = false;
    private static Text buyTileText;
    private static Text attackTileFromCity;

    public void initialize() {
        startAFakeGame();
        new Cheat(root, cheatBar,this);
        new MapMoveController(root, upperMapPane);
        StatusBarController.init(statusBar);
        addInfoButtons();
        renderMap();
    }

    private void eachInfoButtonsClicked(int number) {
        if (infoTabNumber == number) {
            infoTab.setOpacity(0);
            infoTabNumber = -1;
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
            buttons[i].setOnMouseClicked(event -> cityPanel(GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities().get(finalI)));
            anchorPane.getChildren().add(buttons[i]);
        }
        infoTab.setContent(anchorPane);
    }

    private void cityPanel(City city) {
        if (isCityPanelOpen) {
            cityPage.setOpacity(0);
            isCityPanelOpen = false;
            return;
        }
        openedPanelCity = city;
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
        text.setLayoutY(97);
        text.setOpacity(0);
        button = new Button("Buy Tile");
        button.setLayoutY(75);
        Button finalButton = button;
        buyTileText = text;
        Text finalText = text;
        button.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().getName().equals("e") || keyEvent.getCode().getName().equals("E")) {
                waitingToSelectTileToBuy = false;
                finalText.setOpacity(0);
            }
        });
        button.setOnMouseClicked(event -> buyTileClicked(finalButton, finalText));
        anchorPane.getChildren().add(button);
        anchorPane.getChildren().add(text);

        text = new Text("Click on a tile to attack or press \"e\" if you don't want to continue attacking");
        text.setLayoutY(142);
        text.setOpacity(0);
        button = new Button("Attack Tile");
        button.setLayoutY(120);
        Button finalButton1 = button;
        attackTileFromCity = text;
        Text finalText1 = text;
        button.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().getName().equals("e") || keyEvent.getCode().getName().equals("E")) {
                waitingToSelectTileToAttackFromCity = false;
                finalText1.setOpacity(0);
            }
        });
        button.setOnMouseClicked(event -> attackTileClicked(finalButton1, finalText1));
        anchorPane.getChildren().add(button);
        anchorPane.getChildren().add(text);

        cityPage.setContent(anchorPane);
        cityPage.setOpacity(1);
        isCityPanelOpen = true;
    }


    private void attackTileClicked(Button button, Text text) {
        text.setLayoutX(button.getLayoutX() + button.getWidth() + 10);
        text.setOpacity(1);
        System.out.println(text.getOpacity());
        waitingToSelectTileToAttackFromCity = true;
    }

    private void buyTileClicked(Button button, Text text) {
        text.setLayoutX(button.getLayoutX() + button.getWidth() + 10);
        text.setOpacity(1);
        waitingToSelectTileToBuy = true;
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

    private void addInfoButtons() {
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

    public static boolean getSelectingTile() {
        return selectingTile;
    }

    public static void setSelectingTile(boolean mode) {
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

    public static void buyTile(Tile tile) {
        switch (CityCommandsController.buyTile(tile.getX(), tile.getY(), openedPanelCity)) {
            case 0 -> StageController.errorMaker("The tile is yours", "you bought the tile successfully", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("You're dumber than a box of rocks", "the selected tile is not a neighbour to your city", Alert.AlertType.ERROR);
            case 2 -> StageController.errorMaker("You don't deserve the tile", "it's price is too high", Alert.AlertType.ERROR);
            case 3 -> StageController.errorMaker("what", "the selected tile is already a property of another civilization(or maybe even it's yours)", Alert.AlertType.ERROR);
        }
        waitingToSelectTileToBuy = false;
        buyTileText.setOpacity(0);
    }

    public static void attackTile(Tile tile) {
        switch (CityCommandsController.cityAttack(tile.getX(), tile.getY(), openedPanelCity)) {
            case 0 -> StageController.errorMaker("nicely done", "you attacked successfully", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("self-harm is haram", "the selected tile is your city's main tile", Alert.AlertType.ERROR);
            case 2 -> StageController.errorMaker("attack what", "there are no nonCivilians over there", Alert.AlertType.ERROR);
            case 3 -> StageController.errorMaker("bruh", "you selected one of your own units", Alert.AlertType.ERROR);
            case 4 -> StageController.errorMaker("no ranges?", "the selected tile is not in your range", Alert.AlertType.ERROR);
        }
    }

    public static GraphicTile[][] getGraphicMap() {
        return graphicMap;
    }

    public void nextTurn(ActionEvent actionEvent) {
        GameController.nextTurnIfYouCan();
        renderMap();
    }
}
