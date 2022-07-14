package com.example.demo.view;

import com.example.demo.controller.LoginController;
import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.Civilization;
import com.example.demo.model.Map;
import com.example.demo.model.User;
import com.example.demo.model.technologies.Technology;
import com.example.demo.model.tiles.Tile;
import com.example.demo.view.model.GraphicTile;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import com.example.demo.view.InfoController.*;

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
    public VBox infoTab;
    public Text infoText;
    public int infoTabNumber = -1;
    public Button cityButton;
    @FXML
    private VBox leftPanel;
    @FXML
    private HBox statusBar;
    @FXML
    private Text status;
    @FXML
    private BorderPane root;
    @FXML
    private AnchorPane mapPane;
    @FXML
    private Pane pane;
    private double startX;
    private double startY;

    public void initialize() {
        startAFakeGame();
        //move on map
        root.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            startX = pane.getTranslateX() - mouseEvent.getScreenX();
            startY = pane.getTranslateY() - mouseEvent.getScreenY();
        });
        root.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            double x = mouseEvent.getScreenX() + startX;
            double y = mouseEvent.getScreenY() + startY;
//            if (x < 500 && x > -mapPane.getWidth() + 700)
            pane.setTranslateX(x);
//            if (y < 500 && y > -mapPane.getHeight() + 500)
            pane.setTranslateY(y);
        });

        //zoom in/out on map
        pane.setPrefHeight(Screen.getPrimary().getBounds().getHeight());
        pane.setPrefWidth(Screen.getPrimary().getBounds().getWidth());
        pane.addEventFilter(ScrollEvent.SCROLL, scrollEvent -> {
            double delta = 1.15;
            double scale = pane.getScaleY();
            scale *= (scrollEvent.getDeltaY() > 0) ? (delta) : (1 / delta);
            if ((scale > 2.5) || (scale < 0.3))
                return;
            pane.setScaleX(scale);
            pane.setScaleY(scale);
            //move the visible area according to the zoom state:
            double translateScale = (scrollEvent.getDeltaY() > 0) ? (delta) : (1 / delta);
            pane.setTranslateX(pane.getTranslateX() * translateScale);
            pane.setTranslateY(pane.getTranslateY() * translateScale);
        });
        infoButton.setOnMouseClicked(this::infoButtonClicked);
        researchesButton.setOnMouseClicked(event -> eachInfoButtonsClicked(0));
        unitsButton.setOnMouseClicked(event -> eachInfoButtonsClicked(1));
        cityButton.setOnMouseClicked(event -> eachInfoButtonsClicked(2));
        economicsButton.setOnMouseClicked(event -> eachInfoButtonsClicked(3));
        demographicsButton.setOnMouseClicked(event -> eachInfoButtonsClicked(4));
        militaryButton.setOnMouseClicked(event -> eachInfoButtonsClicked(5));
        notificationsButton.setOnMouseClicked(event -> eachInfoButtonsClicked(6));
        updateStatusBar();
        renderMap();
    }

    private void eachInfoButtonsClicked(int number) {
        if (infoTabNumber == number) {
            infoTab.setOpacity(0);
            infoTabNumber = -1;
        } else {
            switch (number) {
                case 0:
                    infoText.setText(InfoController.infoResearches());
                    break;
                case 1:
//                    infoText.setText();
                    break;
                case 2:
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
            }
            infoTab.setOpacity(1);
            infoTabNumber = number;
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

    private void updateStatusBar() {
        //TODO: init status bar once and update status bar every time
//        statusBar.getChildren().clear();
        ImageView gold = new ImageView(ImageLoader.get("gold"));
        ImageView science = new ImageView(ImageLoader.get("science"));
        ImageView happiness = new ImageView(ImageLoader.get("happiness"));
        ImageView technology = new ImageView(ImageLoader.get("technology"));

        statusBar.getChildren().add(gold);
        statusBar.getChildren().add(happiness);
        statusBar.getChildren().add(science);
        statusBar.getChildren().add(technology);
        //TODO: Make amounts of Gold, happiness, science, and technology valid.
        Civilization civilization = GameController.getCivilizations().get(GameController.getPlayerTurn());
        Text goldAmount = new Text(civilization.getGold() + "  ");
        Text happinessAmount = new Text(civilization.getHappiness() + "  ");
        Text scienceAmount = new Text("0  ");
        Technology tech = civilization.getGettingResearchedTechnology();
        Text technologyName;
        if (tech == null)
            technologyName = new Text("nothing");
        else
            technologyName = new Text(tech.getName());
        statusBar.getChildren().add(2, goldAmount);
        statusBar.getChildren().add(4, happinessAmount);
        statusBar.getChildren().add(6, scienceAmount);
        statusBar.getChildren().add(8, technologyName);
    }


    private void renderMap() {
        Map map = GameController.getMap();
        Tile[][] tiles = map.getTiles();
        for (int j = 0; j < map.getY(); j++) {
            for (int i = 0; i < map.getX(); i++) {
                GraphicTile graphicTile = new GraphicTile(tiles[i][j], mapPane, leftPanel, i, j);
                double positionX = 20 + ((graphicTile.getWidth() * 3 / 2) * i / 2) * (1 + 2 / (Math.sqrt(3) * 15));
                double positionY = 20 + (graphicTile.getHeight() * j) * 16 / 15;
                if (i % 2 != 0) //odd columns
                    positionY += graphicTile.getHeight() / 2 + graphicTile.getHeight() / 30;
                graphicTile.setPosition(positionX, positionY);
            }
        }
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
}
