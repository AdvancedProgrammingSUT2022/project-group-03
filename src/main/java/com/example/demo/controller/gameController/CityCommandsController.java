package com.example.demo.controller.gameController;

import com.example.demo.model.TaskTypes;
import com.example.demo.model.Tasks;
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
                                      int destinationY) {
        if (GameController.getSelectedCity() == null)
            return 3;
        if (GameController.getSelectedCity().getCivilization() != GameController
                .getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        if (GameController.getMap().coordinatesToTile(originX, originY) == null ||
                GameController.getMap().coordinatesToTile(destinationX, destinationY) == null)
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
        if (GameController.getMap().coordinatesToTile(destinationX, destinationY) == null)
            return 1;
        if (GameController.getSelectedCity().assignCitizenToTiles(null,
                GameController.getMap().coordinatesToTile(destinationX, destinationY))) return 0;
        return 4;
    }

    public static int removeCitizen(int x, int y) {
        if (GameController.getSelectedCity() == null)
            return 3;
        if (GameController.getSelectedCity().getCivilization() !=
                GameController.getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        if (GameController.getMap().coordinatesToTile(x, y) == null)
            return 1;
        if (GameController.getSelectedCity().removeCitizen(GameController.getMap().coordinatesToTile(x, y))) return 0;
        return 4;
    }


    public static int buyTile(int x, int y) {
        if (GameController.getMap().coordinatesToTile(x, y) == null) return 2;
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
                .increaseGold(-(15 + 10 * (GameController.getSelectedCity().getTiles().size() - 6)));
        return 0;
    }

    public static int buildBuilding(BuildingType buildingType,Tile tile) {
        if(tile.getTileType()== TileType.OCEAN || tile.getTileType()== TileType.MOUNTAIN)
            return 9;
        if (GameController.getSelectedCity() == null)
            return 1;
        if (GameController.getSelectedCity().getCivilization() != GameController
                .getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        if (GameController.getSelectedCity().findBuilding(buildingType) != null)
            return 3;
        if (buildingType == BuildingType.STOCK_EXCHANGE) {
            if(GameController.getSelectedCity().findBuilding(BuildingType.BANK)==null &&
                    GameController.getSelectedCity().findBuilding(BuildingType.SATRAPS_COURT)==null)
                return 4;
        } else {
            for (BuildingType type : BuildingType.prerequisites.get(buildingType)) {
                if (GameController.getSelectedCity().findBuilding(type)==null)
                    return 4;
            }
        }
        if (buildingType == BuildingType.WATER_MILL && !GameController.getSelectedCity().doesHaveRiver())
            return 6;
        if(buildingType==BuildingType.FORGE_GARDEN
                && !GameController.getSelectedCity().doesHaveRiver()
                && !GameController.getSelectedCity().doesHaveLakeAround())
            return 7;
        if(buildingType ==BuildingType.WINDMILL && tile.getTileType()== TileType.HILL)
            return 8;
        if(buildingType ==BuildingType.CIRCUS ||
                buildingType==BuildingType.STABLE ||
                buildingType==BuildingType.FORGE_GARDEN)
        {
            boolean isValid=false;
            for (Tile tile1 : GameController.getSelectedCity().getTiles()) {
                if((buildingType ==BuildingType.STABLE && tile1.getResource()== ResourcesTypes.HORSE) ||
                        (buildingType ==BuildingType.CIRCUS && (tile1.getResource()==ResourcesTypes.IVORY || tile1.getResource()== ResourcesTypes.HORSE)) ||
                        (buildingType==BuildingType.FORGE_GARDEN && tile1.getResource()==ResourcesTypes.IRON))
                {
                    isValid=true;
                    break;
                }
            }
            if(!isValid)
                return 10;
        }

        for (Building building : GameController.getSelectedCity().getHalfProducedBuildings())
            if (building.getRemainedCost() > 0 && building.getBuildingType() == buildingType) {
                GameController.getSelectedCity().setProduct(building);
                return 0;
            }
        Building building = new Building(buildingType);
        GameController.getSelectedCity().getHalfProducedBuildings().add(building);
        GameController.getSelectedCity().setProduct(building);
        return 0;
    }

    public static int cityAttack(int x, int y) {
        if (GameController.getSelectedCity() == null) return 1;
        if (GameController.getSelectedCity().getCivilization() != GameController.getCivilizations()
                .get(GameController.getPlayerTurn())) return 2;
        if (GameController.getMap().coordinatesToTile(x, y) == null) return 3;
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
        UnitType unitType = UnitType.stringToEnum(string);
        if (unitType == null) return 1;
        Tile tile = GameController.getMap().coordinatesToTile(x, y);
        if (tile == null)
            return 5;
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
