package com.example.demo.controller.gameController;

import com.example.demo.model.City;
import com.example.demo.model.TaskTypes;
import com.example.demo.model.Tasks;
import com.example.demo.model.Units.Civilian;
import com.example.demo.model.Units.CombatType;
import com.example.demo.model.Units.UnitType;
import com.example.demo.model.building.Building;
import com.example.demo.model.building.BuildingType;
import com.example.demo.model.resources.ResourcesTypes;
import com.example.demo.model.tiles.Tile;
import com.example.demo.model.tiles.TileType;

public class CityCommandsController {
    public static int reAssignCitizen(int originX,
                                      int originY,
                                      int destinationX,
                                      int destinationY, City city) {
//        if (GameController.getSelectedCity() == null)
//            return 3;
//        if (GameController.getSelectedCity().getCivilization() != GameController
//                .getCivilizations().get(GameController.getPlayerTurn()))
//            return 2;
//        if (GameController.getMap().coordinatesToTile(originX, originY) == null ||
//                GameController.getMap().coordinatesToTile(destinationX, destinationY) == null)
//            return 3;
        return city.assignCitizenToTiles(GameController.getMap().coordinatesToTile(originX, originY),
                        GameController.getMap().coordinatesToTile(destinationX, destinationY));
    }

    public static int assignCitizen(int destinationX, int destinationY,City city) {
        return city.assignCitizenToTiles(null,
                GameController.getMap().coordinatesToTile(destinationX, destinationY));
    }

    public static int removeCitizen(int x, int y,City city) {
//        if (GameController.getSelectedCity() == null)
//            return 3;
//        if (GameController.getSelectedCity().getCivilization() !=
//                GameController.getCivilizations().get(GameController.getPlayerTurn()))
//            return 2;
//        if (GameController.getMap().coordinatesToTile(x, y) == null)
//            return 1;
        return city.removeCitizen(GameController.getMap().coordinatesToTile(x, y));
    }


    public static int buyTile(int x, int y, City city) {
//        if (GameController.getMap().coordinatesToTile(x, y) == null) return 4;
//        if (GameController.getSelectedCity() == null ||
//                GameController.getSelectedCity().getCivilization() != GameController
//                        .getCivilizations().get(GameController.getPlayerTurn()))
//            return 5;
        if (!city.isTileNeighbor(GameController.getMap().coordinatesToTile(x, y))) return 1;
        if (GameController.getCivilizations().get(GameController.getPlayerTurn()).getGold() <
                15 + 10 * (city.getTiles().size() - 6))
            return 2;
        if (!city.addTile(GameController.getMap().coordinatesToTile(x, y))) return 3;
        city.getCivilization()
                .increaseGold(-(15 + 10 * (city.getTiles().size() - 6)));
        return 0;
    }

    public static int buildBuilding(BuildingType buildingType, Tile tile,City city) {
        if (tile.getTileType() == TileType.OCEAN || tile.getTileType() == TileType.MOUNTAIN)
            return 9;
//        if (GameController.getSelectedCity() == null)
//            return 1;
//        if (GameController.getSelectedCity().getCivilization() != GameController
//                .getCivilizations().get(GameController.getPlayerTurn()))
//            return 2;
        if (city.findBuilding(buildingType) != null)
            return 3;
        if (buildingType == BuildingType.STOCK_EXCHANGE) {
            if (city.findBuilding(BuildingType.BANK) == null &&
                    city.findBuilding(BuildingType.SATRAPS_COURT) == null)
                return 4;
        } else {
            for (BuildingType type : BuildingType.prerequisites.get(buildingType)) {
                if (city.findBuilding(type) == null)
                    return 4;
            }
        }
        if (buildingType == BuildingType.WATER_MILL && !city.doesHaveRiver())
            return 6;
        if (buildingType == BuildingType.FORGE_GARDEN
                && !city.doesHaveRiver()
                && !city.doesHaveLakeAround())
            return 7;
        if (buildingType == BuildingType.WINDMILL && tile.getTileType() == TileType.HILL)
            return 8;
        if (buildingType == BuildingType.CIRCUS ||
                buildingType == BuildingType.STABLE ||
                buildingType == BuildingType.FORGE_GARDEN) {
            boolean isValid = false;
            for (Tile tile1 : city.getTiles()) {
                if ((buildingType == BuildingType.STABLE && tile1.getResource() == ResourcesTypes.HORSE) ||
                        (buildingType == BuildingType.CIRCUS && (tile1.getResource() == ResourcesTypes.IVORY || tile1.getResource() == ResourcesTypes.HORSE)) ||
                        (buildingType == BuildingType.FORGE_GARDEN && tile1.getResource() == ResourcesTypes.IRON)) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid)
                return 10;
        }
        for (Building building : city.getHalfProducedBuildings())
            if(building.getTile()==tile)
                return 12;
        for (Building building : city.getBuildings())
            if(building.getTile()==tile)
                return 12;

        for (Building building : city.getHalfProducedBuildings())
            if (building.getRemainedCost() > 0 && building.getBuildingType() == buildingType) {
                city.setProduct(building);
                return 0;
            }
        if(buildingType.getCost()> GameController.getCivilizations().get(GameController.getPlayerTurn()).getGold())
            return 11;
        Building building = new Building(buildingType,tile);
        city.getHalfProducedBuildings().add(building);
        city.setProduct(building);
        return 0;
    }

    public static int cityAttack(int x, int y, City city) {
//        if (GameController.getSelectedCity() == null) return 1;
//        if (GameController.getSelectedCity().getCivilization() != GameController.getCivilizations()
//                .get(GameController.getPlayerTurn())) return 2;
//        if (GameController.getMap().coordinatesToTile(x, y) == null) return 3;
        if(GameController.getMap().coordinatesToTile(x, y)==city.getMainTile())
            return 1;
        if (GameController.getMap().coordinatesToTile(x, y).getNonCivilian() == null)
            return 2;
        if (GameController.getMap().coordinatesToTile(x, y).getNonCivilian().getCivilization() ==
                GameController.getCivilizations().get(GameController.getPlayerTurn()))
            return 3;
        if (!GameController.getMap().isInRange(2,
                city.getMainTile(),
                GameController.getMap().coordinatesToTile(x, y))) return 4;
        city.attack(GameController.getMap().coordinatesToTile(x, y));
        return 0;
    }

    public static int cityDestiny(boolean burn) {
        if (GameController.getSelectedCity() == null) return 2;
        if (GameController.getSelectedCity().getHP() > 0) return 1;
        if (GameController.getSelectedCity().isCapital && burn) return 4;
        if (burn) GameController.getSelectedCity().destroy(GameController
                .getCivilizations().get(GameController.getPlayerTurn()));
        else
            GameController.getSelectedCity().changeCivilization(GameController
                    .getCivilizations().get(GameController.getPlayerTurn()));
        GameController.deleteFromUnfinishedTasks(new Tasks(GameController
                .getSelectedCity().getMainTile(), TaskTypes.CITY_DESTINY));
        return 0;
    }

    public static int buyUnit(String string, int x, int y) {
        UnitType unitType = UnitType.stringToEnum(string.substring(0,string.indexOf(": cost")));
//        if (unitType == null) return 1;
        Tile tile = GameController.getMap().coordinatesToTile(x, y);
//        if (tile == null)
//            return 5;
        if (tile.getCivilization() != GameController.getCivilizations().get(GameController.getPlayerTurn()))
            return 1;
        if (GameController.getCivilizations().get(GameController.getPlayerTurn()).getGold() < unitType.getCost())
            return 2;
        if ((unitType.combatType == CombatType.CIVILIAN &&
                GameController.getMap().coordinatesToTile(x, y).getCivilian() != null) ||
                (unitType.combatType != CombatType.CIVILIAN &&
                        GameController.getMap().coordinatesToTile(x, y).getNonCivilian() != null))
            return 3;
        CheatCommandsController.cheatUnit(x, y, unitType);
        return 0;
    }
}
