package com.example.demo.view;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.Map;
import com.example.demo.model.User;
import com.example.demo.model.tiles.Tile;
import com.example.demo.view.model.GraphicTile;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class GameControllerFX {
    @FXML
    private Pane pane;
    @FXML
    private AnchorPane anchorPane;
    double startX;
    double startY;

    public void initialize() {

        startAFakeGame();

        //move on map
        pane.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            startX = pane.getTranslateX() - mouseEvent.getScreenX();
            startY = pane.getTranslateY() - mouseEvent.getScreenY();
        });

        //move on map
        pane.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            pane.setTranslateX(mouseEvent.getScreenX() + startX);
            pane.setTranslateY(mouseEvent.getScreenY() + startY);
        });

        //zoom in/out on map
        pane.addEventFilter(ScrollEvent.SCROLL, scrollEvent -> {
            double delta = 1.15;
            double scale = pane.getScaleY();
            scale *= (scrollEvent.getDeltaY() > 0) ? (delta) : (1 / delta);
            scale = Math.min(scale, 2.5);
            scale = Math.max(scale, 0.4);
            pane.setScaleX(scale);
            pane.setScaleY(scale);
            //move the visible area according to the zoom state:
            if (0.4 < scale && scale < 2.5) {
                double translateScale = (scrollEvent.getDeltaY() > 0) ? (delta) : (1 / delta);
                pane.setTranslateX(pane.getTranslateX() * translateScale);
                pane.setTranslateY(pane.getTranslateY() * translateScale);
            }
        });

        renderMap(anchorPane);
    }

    private void renderMap(AnchorPane anchorPane) {
        Map map = GameController.getMap();
        Tile[][] tiles = map.getTiles();
        for (int j = 0; j < map.getY(); j++) {
            for (int i = 0; i < map.getX(); i++) {
                GraphicTile graphicTile = new GraphicTile(tiles[i][j], anchorPane);
                double positionX = 20 + (graphicTile.getWidth() * 3 / 2) * i / 2;
                double positionY = 20 + (graphicTile.getHeight() * j);
                if (i % 2 != 0) //odd columns
                    positionY += graphicTile.getHeight() / 2;
                graphicTile.setPosition(positionX, positionY);
            }
        }
    }


    /*
     * This methode is only for testing
     */
    private void startAFakeGame() {
        User user = new User("Sayyed", "ali", "Tayyeb");
        User user2 = new User("Sayyed2", "ali", "Tayyeb");
        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        GameController.startGame(users);
    }
}
