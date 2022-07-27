package com.example.demo.controller.gameController;

import com.example.demo.model.City;
import com.example.demo.network.GameHandler;

import java.util.Objects;

public class MapCommandsController {
    private final GameHandler game;
    private final GameController GameController;

    public MapCommandsController(GameHandler game) {
        this.game = game;
        this.GameController = game.getGameController();
    }

    public  void mapShowPosition(int x, int y) {
        GameController.startWindowX = x;
        GameController.startWindowY = y;
        if (GameController.startWindowY > GameController.getMap().getY() - (game.getMap().WINDOW_Y - 1))
            GameController.startWindowY = GameController.getMap().getY() - (game.getMap().WINDOW_Y - 1);
        if (GameController.startWindowX > GameController.getMap().getX() - (game.getMap().WINDOW_X - 1))
            GameController.startWindowX = GameController.getMap().getX() - (game.getMap().WINDOW_X - 1);
        if (GameController.startWindowY < 0)
            GameController.startWindowY = 0;
        if (GameController.startWindowX < 0)
            GameController.startWindowX = 0;
    }

    public  int mapShowCityName(String name) {
        City tempCity = GameController.nameToCity(name);
        if (tempCity == null) return 1;
        if (GameController.getCivilizations().get(GameController.getPlayerTurn())
                .getTileConditions()[tempCity.getMainTile()
                .getX()][tempCity.getMainTile().getY()] == null)
            return 2;
        mapShowPosition(tempCity.getMainTile().getX() - game.getMap().WINDOW_X / 2,
                tempCity.getMainTile().getY() - game.getMap().WINDOW_Y / 2 + 1);
        return 0;
    }

    public  void mapMove(int number, String direction) {
        if (Objects.equals(direction, "r"))
            mapShowPosition(GameController.startWindowX, GameController.startWindowY + number);
        if (Objects.equals(direction, "l"))
            mapShowPosition(GameController.startWindowX, GameController.startWindowY - number);
        if (Objects.equals(direction, "u"))
            mapShowPosition(GameController.startWindowX - number, GameController.startWindowY);
        else mapShowPosition(GameController.startWindowX + number, GameController.startWindowY);
    }
}
