package controller.gameController;

import model.TaskTypes;
import model.Tasks;
import model.Units.CombatType;
import model.Units.UnitType;
import model.building.Building;
import model.building.BuildingType;
import model.tiles.Tile;

public class CityCommandsController {
    public static int reAssignCitizen(int originX,
                                      int originY,
                                      int destinationX,
                                      int destinationY) {
        if (GameController.getSelectedCity() == null)
            return 3;
        if (GameController.getSelectedCity().getCivilization() != GameController
                .getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        if (originX < 0 || originY < 0 ||
                destinationY < 0 || destinationX < 0 ||
                originX > GameController.getMap().getX() || originY > GameController.getMap().getY() ||
                destinationX > GameController.getMap().getX() || destinationY > GameController.getMap().getY())
            return 1;
        if (GameController.getSelectedCity()
                .assignCitizenToTiles(GameController.getMap().coordinatesToTile(originX, originY),
                GameController.getMap().coordinatesToTile(destinationX, destinationY))) return 0;
        return 4;
    }

    public static int assignCitizen(int destinationX, int destinationY) {
        if (GameController.getSelectedCity() == null)
            return 3;
        if (GameController.getSelectedCity().getCivilization() !=
                GameController.getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        if (destinationY < 0 || destinationX < 0 ||
                destinationX > GameController.getMap().getX() ||
                destinationY > GameController.getMap().getY())
            return 1;
        if (GameController.getSelectedCity().assignCitizenToTiles(null,
                GameController.getMap().coordinatesToTile(destinationX, destinationY))) return 0;
        return 4;
    }

    public static int buyTile(int x, int y) {
        if (x < 0 || y < 0 || x >= GameController.getMap().getX() || y > GameController.getMap().getY()) return 2;
        if (GameController.getSelectedCity() == null ||
                GameController.getSelectedCity().getCivilization() != GameController
                        .getCivilizations().get(GameController.getPlayerTurn()))
            return 4;
        if (!GameController.getSelectedCity()
                .isTileNeighbor(GameController.getMap().coordinatesToTile(x, y))) return 3;
        if (GameController.getCivilizations().get(GameController.getPlayerTurn()).getGold() <
                15 + 10 * (GameController.getSelectedCity().getTiles().size() - 6))
            return 5;
        if (!GameController.getSelectedCity()
                .addTile(GameController.getMap().coordinatesToTile(x, y))) return 1;
        GameController.getSelectedCity().getCivilization()
                .changeGold(-(15 + 10 * (GameController.getSelectedCity().getTiles().size() - 6)));
        return 0;
    }

    public static int buildWall() {
        if (GameController.getSelectedCity() == null)
            return 1;
        if (GameController.getSelectedCity().getCivilization() != GameController
                .getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        if (GameController.getSelectedCity().getWall() != null)
            return 3;
        for (Building building : GameController.getSelectedCity().getHalfProducedBuildings())
            if (building.getRemainedCost() != 0 && building.getBuildingType() == BuildingType.WALL) {
                GameController.getSelectedCity().setProduct(building);
                return 0;
            }
        Building building = new Building(BuildingType.WALL);
        GameController.getSelectedCity().getHalfProducedBuildings().add(building);
        GameController.getSelectedCity().setProduct(building);
        return 0;
    }

    public static int cityAttack(int x, int y) {
        if (GameController.getSelectedCity() == null) return 1;
        if (GameController.getSelectedCity().getCivilization() != GameController.getCivilizations()
                .get(GameController.getPlayerTurn())) return 2;
        if (x < 0 || y < 0 || x >= GameController.getMap().getX() ||
                y > GameController.getMap().getY() ||
                GameController.getMap().coordinatesToTile(x, y) == null) return 3;
        if (GameController.getMap().coordinatesToTile(x, y).getNonCivilian() == null)
            return 4;
        if (GameController.getMap().coordinatesToTile(x, y).getNonCivilian().getCivilization() ==
                GameController.getCivilizations().get(GameController.getPlayerTurn()))
            return 5;
        if (!GameController.getMap().isInRange(2,
                GameController.getSelectedCity().getMainTile(),
                GameController.getMap().coordinatesToTile(x, y))) return 6;
        GameController.getSelectedCity().attack(GameController.getMap().coordinatesToTile(x, y));
        return 0;
    }

    public static int cityDestiny(boolean burn) {
        if (GameController.getSelectedCity() == null) return 2;
        if (GameController.getSelectedCity().getHP() > 0) return 1;
        if (GameController.getSelectedCity().isCapital && burn) return 4;
        if (burn) GameController.getSelectedCity().destroy();
        else
            GameController.getSelectedCity().changeCivilization(GameController
                    .getCivilizations().get(GameController.getPlayerTurn()));
        GameController.deleteFromUnfinishedTasks(new Tasks(GameController
                .getSelectedCity().getMainTile(), TaskTypes.CITY_DESTINY));
        return 0;
    }

    public static int buyUnit(String string, int x, int y) {
        UnitType unitType = UnitType.stringToEnum(string);
        if (unitType == null) return 1;
        Tile tile = GameController.getMap().coordinatesToTile(x, y);
        if (tile.getCivilization() != GameController.getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        if (GameController.getCivilizations().get(GameController.getPlayerTurn()).getGold() < unitType.getCost())
            return 3;
        if ((unitType.combatType == CombatType.CIVILIAN &&
                GameController.getMap().coordinatesToTile(x, y).getCivilian() != null) ||
                (unitType.combatType != CombatType.CIVILIAN &&
                        GameController.getMap().coordinatesToTile(x, y).getNonCivilian() != null))
            return 4;
        CheatCommandsController.cheatUnit(x, y, unitType);
        return 0;
    }
}
