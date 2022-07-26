package controller.gameController;

import model.City;
import network.GameHandler;

public class TileXAndYFlagSelectUnitController {
    private final GameHandler game;

    public TileXAndYFlagSelectUnitController(GameHandler game) {
        this.game = game;
    }

    public boolean setSelectedNonCivilian(int x, int y) {
        if (game.getGameController().getMap().coordinatesToTile(x, y)==null ||
                game.getGameController().getMap().coordinatesToTile(x, y).getNonCivilian() == null)
            return false;
        game.getGameController().setSelectedUnit(game.getGameController().getMap()
                .coordinatesToTile(x, y).getNonCivilian());
        return true;
    }

    public  boolean setSelectedCivilian(int x, int y) {
        if (game.getGameController().getMap().coordinatesToTile(x, y)==null ||
                game.getGameController().getMap().coordinatesToTile(x, y).getCivilian() == null) return false;
        game.getGameController().setSelectedUnit(game.getGameController().getMap()
                .coordinatesToTile(x, y).getCivilian());
        return true;
    }

    public  boolean setSelectedCityByName(String name) {
        City tempCity = game.getGameController().nameToCity(name);
        if (tempCity == null) return false;
        game.getGameController().setSelectedCity(tempCity);
        return true;
    }

    public  boolean setSelectedCityByPosition(int x, int y) {
        if (game.getGameController().getMap().coordinatesToTile(x, y)!=null &&
                game.getGameController().getMap().coordinatesToTile(x, y).getCity() != null) {
            game.getGameController().setSelectedCity(game.getGameController()
                    .getMap().coordinatesToTile(x, y).getCity());
            return true;
        }
        return false;
    }
}
