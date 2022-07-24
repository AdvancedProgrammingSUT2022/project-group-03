package controller.gameController;

import model.City;
import model.Map;
import network.GameHandler;

import java.util.Objects;

public class MapCommandsController {
    private final GameHandler game;

    public MapCommandsController(GameHandler game) {
        this.game = game;
    }

    public  void mapShowPosition(int x, int y) {
        game.getGameController().startWindowX = x;
        game.getGameController().startWindowY = y;
        if (game.getGameController().startWindowY > game.getGameController().getMap().getY() - (game.getMap().WINDOW_Y - 1))
            game.getGameController().startWindowY = game.getGameController().getMap().getY() - (game.getMap().WINDOW_Y - 1);
        if (game.getGameController().startWindowX > game.getGameController().getMap().getX() - (game.getMap().WINDOW_X - 1))
            game.getGameController().startWindowX = game.getGameController().getMap().getX() - (game.getMap().WINDOW_X - 1);
        if (game.getGameController().startWindowY < 0)
            game.getGameController().startWindowY = 0;
        if (game.getGameController().startWindowX < 0)
            game.getGameController().startWindowX = 0;
    }

    public  int mapShowCityName(String name) {
        City tempCity = game.getGameController().nameToCity(name);
        if (tempCity == null) return 1;
        if (game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn())
                .getTileConditions()[tempCity.getMainTile()
                .getX()][tempCity.getMainTile().getY()] == null)
            return 2;
        mapShowPosition(tempCity.getMainTile().getX() - game.getMap().WINDOW_X / 2,
                tempCity.getMainTile().getY() - game.getMap().WINDOW_Y / 2 + 1);
        return 0;
    }

    public  void mapMove(int number, String direction) {
        if (Objects.equals(direction, "r"))
            mapShowPosition(game.getGameController().startWindowX, game.getGameController().startWindowY + number);
        if (Objects.equals(direction, "l"))
            mapShowPosition(game.getGameController().startWindowX, game.getGameController().startWindowY - number);
        if (Objects.equals(direction, "u"))
            mapShowPosition(game.getGameController().startWindowX - number, game.getGameController().startWindowY);
        else mapShowPosition(game.getGameController().startWindowX + number, game.getGameController().startWindowY);
    }
}
