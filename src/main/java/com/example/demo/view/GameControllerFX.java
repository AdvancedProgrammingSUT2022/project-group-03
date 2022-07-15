package com.example.demo.view;

import com.example.demo.controller.LoginController;
import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.Map;
import com.example.demo.model.User;
import com.example.demo.model.tiles.Tile;
import com.example.demo.view.model.GraphicTile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class GameControllerFX {
    private static boolean selectingTile;
    public static final Text publicText = new Text("");
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
    private static GraphicTile[][] graphicMap;

    public static boolean getSelectingTile() {
        return selectingTile;
    }

    public static void setSelectingTile(boolean mode) {
        selectingTile = mode;
    }

    public void initialize() {
        startAFakeGame();
        MapMoveController mapMove = new MapMoveController(root, upperMapPane);
        StatusBarController.init(statusBar);

        renderMap();
    }


    public void renderMap() {
        mapPane.getChildren().clear();
        Map map = GameController.getMap();
        graphicMap = new GraphicTile[map.getX()][map.getY()];
        Tile[][] tiles = map.getTiles();
        for (int j = 0; j < map.getY(); j++)
            for (int i = 0; i < map.getX(); i++)
                graphicMap[i][j] = new GraphicTile(tiles[i][j], mapPane, leftPanel);
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

    public static GraphicTile[][] getGraphicMap() {
        return graphicMap;
    }

}
