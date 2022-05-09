package controller.gameController;

import model.City;
import model.Map;

import java.util.Objects;

public class MapCommandsController {
    public static void mapShowPosition(int x, int y) {
        GameController.startWindowX = x;
        GameController.startWindowY = y;
        if (GameController.startWindowY > GameController.getMap().getY() - (Map.WINDOW_Y - 1))
            GameController.startWindowY = GameController.getMap().getY() - (Map.WINDOW_Y - 1);
        if (GameController.startWindowX > GameController.getMap().getX() - (Map.WINDOW_X - 1))
            GameController.startWindowX = GameController.getMap().getX() - (Map.WINDOW_X - 1);
        if (GameController.startWindowY < 0)
            GameController.startWindowY = 0;
        if (GameController.startWindowX < 0)
            GameController.startWindowX = 0;
    }

    public static int mapShowCityName(String name) {
        City tempCity = GameController.nameToCity(name);
        if (tempCity == null)
            return 1;
        if (GameController.getCivilizations().get(GameController.getPlayerTurn())
                .getTileConditions()[tempCity.getMainTile().getX()][tempCity.getMainTile().getY()] == null)
            return 2;
        mapShowPosition(tempCity.getMainTile().getX() - Map.WINDOW_X / 2,
                tempCity.getMainTile().getY() - Map.WINDOW_Y / 2 + 1);
        return 0;
    }

    public static void mapMove(int number, String direction) {
        if (Objects.equals(direction, "r"))
            mapShowPosition(GameController.startWindowX, GameController.startWindowY + number);
        if (Objects.equals(direction, "l"))
            mapShowPosition(GameController.startWindowX, GameController.startWindowY - number);
        if (Objects.equals(direction, "u"))
            mapShowPosition(GameController.startWindowX - number, GameController.startWindowY);
        else
            mapShowPosition(GameController.startWindowX + number, GameController.startWindowY);
    }
}
