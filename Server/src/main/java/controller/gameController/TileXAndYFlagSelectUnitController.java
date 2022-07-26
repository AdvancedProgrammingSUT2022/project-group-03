package controller.gameController;

import model.City;
import network.GameHandler;

public class TileXAndYFlagSelectUnitController {
    private final GameHandler game;
    private final GameController gameController;

    public TileXAndYFlagSelectUnitController(GameHandler game) {
        this.game = game;
        this.gameController = game.getGameController();
    }
    public  boolean setSelectedNonCivilian(int x, int y) {
        if (gameController.getMap().coordinatesToTile(x, y)==null ||
                gameController.getMap().coordinatesToTile(x, y).getNonCivilian() == null)
            return false;
        gameController.setSelectedUnit(gameController.getMap()
                .coordinatesToTile(x, y).getNonCivilian());
        return true;
    }

    public  boolean setSelectedCivilian(int x, int y) {
        if (gameController.getMap().coordinatesToTile(x, y)==null ||
                gameController.getMap().coordinatesToTile(x, y).getCivilian() == null) return false;
        gameController.setSelectedUnit(gameController.getMap()
                .coordinatesToTile(x, y).getCivilian());
        return true;
    }

    public  boolean setSelectedCityByName(String name) {
        City tempCity = gameController.nameToCity(name);
        if (tempCity == null) return false;
        gameController.setSelectedCity(tempCity);
        return true;
    }

    public  boolean setSelectedCityByPosition(int x, int y) {
        if (gameController.getMap().coordinatesToTile(x, y)!=null &&
                gameController.getMap().coordinatesToTile(x, y).getCity() != null) {
            gameController.setSelectedCity(gameController
                    .getMap().coordinatesToTile(x, y).getCity());
            return true;
        }
        return false;
    }
}
