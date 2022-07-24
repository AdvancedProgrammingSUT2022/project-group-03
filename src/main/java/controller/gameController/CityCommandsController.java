package controller.gameController;

import model.TaskTypes;
import model.Tasks;
import model.Units.CombatType;
import model.Units.UnitType;
import model.building.Building;
import model.building.BuildingType;
import model.tiles.Tile;
import network.GameHandler;

public class CityCommandsController {
    private final GameHandler game;

    public CityCommandsController(GameHandler game) {
        this.game = game;
    }

    public int reAssignCitizen(int originX,
                                      int originY,
                                      int destinationX,
                                      int destinationY) {
        if (game.getGameController().getSelectedCity() == null)
            return 3;
        if (game.getGameController().getSelectedCity().getCivilization() != game.getGameController()
                .getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 2;
        if (game.getGameController().getMap().coordinatesToTile(originX,originY)==null ||
                game.getGameController().getMap().coordinatesToTile(destinationX,destinationY)==null)
            return 1;
        if (game.getGameController().getSelectedCity()
                .assignCitizenToTiles(game.getGameController().getMap().coordinatesToTile(originX, originY),
                        game.getGameController().getMap().coordinatesToTile(destinationX, destinationY))) return 0;
        return 4;
    }

    public  int assignCitizen(int destinationX, int destinationY) {
        if (game.getGameController().getSelectedCity() == null)
            return 3;
        if (game.getGameController().getSelectedCity().getCivilization() !=
                game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 2;
        if (game.getGameController().getMap().coordinatesToTile(destinationX,destinationY)==null)
            return 1;
        if (game.getGameController().getSelectedCity().assignCitizenToTiles(null,
                game.getGameController().getMap().coordinatesToTile(destinationX, destinationY))) return 0;
        return 4;
    }
    public  int removeCitizen(int x, int y) {
        if (game.getGameController().getSelectedCity() == null)
            return 3;
        if (game.getGameController().getSelectedCity().getCivilization() !=
                game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 2;
        if (game.getGameController().getMap().coordinatesToTile(x,y)==null)
            return 1;
        if (game.getGameController().getSelectedCity().removeCitizen(game.getGameController().getMap().coordinatesToTile(x, y))) return 0;
        return 4;
    }


    public  int buyTile(int x, int y) {
        if (game.getGameController().getMap().coordinatesToTile(x,y)==null) return 2;
        if (game.getGameController().getSelectedCity() == null ||
                game.getGameController().getSelectedCity().getCivilization() != game.getGameController()
                        .getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 4;
        if (!game.getGameController().getSelectedCity()
                .isTileNeighbor(game.getGameController().getMap().coordinatesToTile(x, y))) return 3;
        if (game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getGold() <
                15 + 10 * (game.getGameController().getSelectedCity().getTiles().size() - 6))
            return 5;
        if (!game.getGameController().getSelectedCity()
                .addTile(game.getGameController().getMap().coordinatesToTile(x, y),game.getGameController())) return 1;
        game.getGameController().getSelectedCity().getCivilization()
                .increaseGold(-(15 + 10 * (game.getGameController().getSelectedCity().getTiles().size() - 6)));
        return 0;
    }

    public  int buildWall() {
        if (game.getGameController().getSelectedCity() == null)
            return 1;
        if (game.getGameController().getSelectedCity().getCivilization() != game.getGameController()
                .getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 2;
        if (game.getGameController().getSelectedCity().getWall() != null)
            return 3;
        for (Building building : game.getGameController().getSelectedCity().getHalfProducedBuildings())
            if (building.getRemainedCost() != 0 && building.getBuildingType() == BuildingType.WALL) {
                game.getGameController().getSelectedCity().setProduct(building);
                return 0;
            }
        Building building = new Building(BuildingType.WALL);
        game.getGameController().getSelectedCity().getHalfProducedBuildings().add(building);
        game.getGameController().getSelectedCity().setProduct(building);
        return 0;
    }

    public  int cityAttack(int x, int y) {
        if (game.getGameController().getSelectedCity() == null) return 1;
        if (game.getGameController().getSelectedCity().getCivilization() != game.getGameController().getCivilizations()
                .get(game.getGameController().getPlayerTurn())) return 2;
        if (game.getGameController().getMap().coordinatesToTile(x, y) == null) return 3;
        if (game.getGameController().getMap().coordinatesToTile(x, y).getNonCivilian() == null)
            return 4;
        if (game.getGameController().getMap().coordinatesToTile(x, y).getNonCivilian().getCivilization() ==
                game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 5;
        if (!game.getGameController().getMap().isInRange(2,
                game.getGameController().getSelectedCity().getMainTile(),
                game.getGameController().getMap().coordinatesToTile(x, y))) return 6;
        game.getGameController().getSelectedCity().attack(game.getGameController().getMap().coordinatesToTile(x, y),game.getGameController());
        return 0;
    }

    public  int cityDestiny(boolean burn) {
        if (game.getGameController().getSelectedCity() == null) return 2;
        if (game.getGameController().getSelectedCity().getHP() > 0) return 1;
        if (game.getGameController().getSelectedCity().isCapital && burn) return 4;
        if (burn) game.getGameController().getSelectedCity().destroy(game.getGameController()
                .getCivilizations().get(game.getGameController().getPlayerTurn()),game.getGameController());
        else
            game.getGameController().getSelectedCity().changeCivilization(game.getGameController()
                    .getCivilizations().get(game.getGameController().getPlayerTurn()),game.getGameController());
        game.getGameController().deleteFromUnfinishedTasks(new Tasks(game.getGameController()
                .getSelectedCity().getMainTile(), TaskTypes.CITY_DESTINY));
        return 0;
    }

    public  int buyUnit(String string, int x, int y) {
        UnitType unitType = UnitType.stringToEnum(string);
        if (unitType == null) return 1;
        Tile tile = game.getGameController().getMap().coordinatesToTile(x, y);
        if(tile==null)
            return 5;
        if (tile.getCivilization() != game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 2;
        if (game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getGold() < unitType.getCost())
            return 3;
        if ((unitType.combatType == CombatType.CIVILIAN &&
                game.getGameController().getMap().coordinatesToTile(x, y).getCivilian() != null) ||
                (unitType.combatType != CombatType.CIVILIAN &&
                        game.getGameController().getMap().coordinatesToTile(x, y).getNonCivilian() != null))
            return 4;
        game.getCheatCommandsController().cheatUnit(x, y, unitType);
        return 0;
    }
}
