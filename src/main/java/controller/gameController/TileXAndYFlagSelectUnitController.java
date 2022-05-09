package controller.gameController;

import model.City;

public class TileXAndYFlagSelectUnitController {
    public static boolean setSelectedNonCivilian(int x, int y) {
        if (GameController.getMap().coordinatesToTile(x, y).getNonCivilian() == null)
            return false;
        GameController.setSelectedUnit(GameController.getMap().coordinatesToTile(x, y).getNonCivilian());
        return true;
    }

    public static boolean setSelectedCivilian(int x, int y) {
        if (GameController.getMap().coordinatesToTile(x, y).getCivilian() == null)
            return false;
        GameController.setSelectedUnit(GameController.getMap().coordinatesToTile(x, y).getCivilian());
        return true;
    }

    public static boolean setSelectedCityByName(String name) {
        City tempCity = GameController.nameToCity(name);
        if (tempCity == null)
            return false;
        GameController.setSelectedCity(tempCity);
        return true;
    }

    public static boolean setSelectedCityByPosition(int x, int y) {
        if (GameController.getMap().coordinatesToTile(x, y).getCity() != null) {
            GameController.setSelectedCity(GameController.getMap().coordinatesToTile(x, y).getCity());
            return true;
        }
        return false;
    }
}
