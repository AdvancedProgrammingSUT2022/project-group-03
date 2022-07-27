package com.example.demo.controller.gameController;
import com.example.demo.model.City;
import com.example.demo.model.Civilization;
import com.example.demo.model.TaskTypes;
import com.example.demo.model.Tasks;
import com.example.demo.model.Units.CombatType;
import com.example.demo.model.Units.UnitType;
import com.example.demo.model.building.Building;
import com.example.demo.model.building.BuildingType;
import com.example.demo.model.resources.ResourcesTypes;
import com.example.demo.model.tiles.Tile;
import com.example.demo.model.tiles.TileType;
import com.example.demo.network.GameHandler;

public class CityCommandsController {
    private final GameHandler game;

    public CityCommandsController(GameHandler game) {
        this.game = game;
    }

    public  int reAssignCitizen(int originX,
                                      int originY,
                                      int destinationX,
                                      int destinationY, City city) {
        return city.assignCitizenToTiles(game.getGameController().getMap().coordinatesToTile(originX, originY),
                game.getGameController().getMap().coordinatesToTile(destinationX, destinationY));
    }

    public  int assignCitizen(int destinationX, int destinationY, City city) {
        return city.assignCitizenToTiles(null,
                game.getGameController().getMap().coordinatesToTile(destinationX, destinationY));
    }

    public  int removeCitizen(int x, int y, City city) {
        return city.removeCitizen(game.getGameController().getMap().coordinatesToTile(x, y));
    }


    public  int buyTile(int x, int y, City city) {
        if (!city.isTileNeighbor(game.getGameController().getMap().coordinatesToTile(x, y))) return 1;
        if (game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getGold() <
            15 + 10 * (city.getTiles().size() - 6))
            return 2;
        if (!city.addTile(game.getGameController().getMap().coordinatesToTile(x, y),game.getGameController())) return 3;
        city.getCivilization()
            .increaseGold(-(15 + 10 * (city.getTiles().size() - 6)));
        return 0;
    }

    public  int buildBuilding(BuildingType buildingType, Tile tile, City city, boolean buy) {
        if (tile.getTileType() == TileType.OCEAN || tile.getTileType() == TileType.MOUNTAIN)
            return 9;
        if (!game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).equals(tile.getCivilization()))
            return 15;
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
        if (buildingType == BuildingType.FORGE
            && !city.doesHaveRiver()
            && !city.doesHaveLakeAround())
            return 7;
        if (buildingType == BuildingType.WINDMILL && tile.getTileType() == TileType.HILL)
            return 8;
        if (buildingType == BuildingType.CIRCUS ||
            buildingType == BuildingType.STABLE ||
            buildingType == BuildingType.FORGE) {
            boolean isValid = false;
            for (Tile tile1 : city.getTiles()) {
                if ((buildingType == BuildingType.STABLE && tile1.getResource() == ResourcesTypes.HORSE) ||
                    (buildingType == BuildingType.CIRCUS && (tile1.getResource() == ResourcesTypes.IVORY || tile1.getResource() == ResourcesTypes.HORSE)) ||
                    (buildingType == BuildingType.FORGE && tile1.getResource() == ResourcesTypes.IRON)) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid)
                return 10;
        }
        for (Building building : city.getHalfProducedBuildings())
            if (building.getTile() == tile)
                return 12;
        for (Building building : city.getBuildings())
            if (building.getTile() == tile)
                return 12;
        if (!buy) {
            for (Building building : city.getHalfProducedBuildings())
                if (building.getRemainedCost() > 0 && building.getBuildingType() == buildingType) {
                    city.setProduct(building);
                    return 0;
                }
        }
        if (buildingType.getCost() > game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getGold())
            return 11;
        Building building = new Building(buildingType, tile);
        if (buy) {
            building.setRemainedCost(0);
            city.getBuildings().add(building);
            tile.setBuilding(building);
            city.getCivilization().increaseGold(-building.getCost());
            if (building.getBuildingType() == BuildingType.BURIAL_TOMB)
                city.getCivilization().changeHappiness(2);
            if (building.getBuildingType() == BuildingType.SATRAPS_COURT)
                city.getCivilization().changeHappiness(2);
            if (building.getBuildingType() == BuildingType.THEATER)
                city.getCivilization().changeHappiness(4);
            if (building.getBuildingType() == BuildingType.COLOSSEUM)
                city.getCivilization().changeHappiness(4);
            if (building.getBuildingType() == BuildingType.CIRCUS)
                city.getCivilization().changeHappiness(3);
//            if (building.getBuildingType() == BuildingType.HOSPITAL)
//                city.foodForCitizen /= 2;
            return 20;
        }
        city.getHalfProducedBuildings().add(building);
        city.setProduct(building);
        return 0;
    }

    public  int cityAttack(int x, int y, City city) {
        if (game.getGameController().getMap().coordinatesToTile(x, y) == city.getMainTile())
            return 1;
        if (game.getGameController().getMap().coordinatesToTile(x, y).getNonCivilian() == null)
            return 2;
        if (game.getGameController().getMap().coordinatesToTile(x, y).getNonCivilian().getCivilization() ==
                game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 3;
        if(city.getHasAttackedThisCycle())
            return 5;
        if (!game.getGameController().getMap().isInRange(2,
            city.getMainTile(),
                game.getGameController().getMap().coordinatesToTile(x, y))) return 4;
        city.attack(game.getGameController().getMap().coordinatesToTile(x, y),game.getGameController(),game.getSocketHandlers().get(game.getGameController().getPlayerTurn()));
        city.setHasAttackedThisCycle(true);
        return 0;
    }

    public  int cityDestiny(boolean burn, City city) {
        if (city == null) return 2;
        if (city.getHP() > 0) return 1;
        if (city.isMainCapital() && burn) return 4;
        if (city.getCivilization().getMainCapital() == city) {
            for (City city1 : city.getCivilization().getCities())
                if (city1 != city) {
                    city1.setCapital(true);
                    break;
                }
        }
        if (burn) city.destroy(game.getGameController()
            .getCivilizations().get(game.getGameController().getPlayerTurn()),game.getGameController()
                ,game.getSocketHandlers().get(game.getGameController().getPlayerTurn()));
        else
            city.changeCivilization(game.getGameController()
                .getCivilizations().get(game.getGameController().getPlayerTurn()),game.getGameController()
                    ,game.getSocketHandlers().get(game.getGameController().getPlayerTurn()));
        game.getGameController().deleteFromUnfinishedTasks(new Tasks(city.getMainTile(), TaskTypes.CITY_DESTINY));
        return 0;
    }

    public  int buyUnit(String string, int x, int y) {
        UnitType unitType = UnitType.stringToEnum(string);
        Tile tile = game.getGameController().getMap().coordinatesToTile(x, y);
        if (tile.getCivilization() != game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 1;
        if (game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getGold() < unitType.getCost())
            return 2;
        if ((unitType.combatType == CombatType.CIVILIAN &&
                game.getGameController().getMap().coordinatesToTile(x, y).getCivilian() != null) ||
            (unitType.combatType != CombatType.CIVILIAN &&
                    game.getGameController().getMap().coordinatesToTile(x, y).getNonCivilian() != null))
            return 3;
        Civilization civilization = game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn());
        civilization.increaseGold(-unitType.cost);
        game.getCheatCommandsController().cheatUnit(x, y, unitType);
        return 0;
    }

    public  int buyBuilding(BuildingType type, int x, int y,City city) {
        Tile tile = game.getGameController().getMap().coordinatesToTile(x, y);
        if (tile.getCivilization() != game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 1;
        if (game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getGold() < type.getCost())
            return 2;
        if(!city.getTiles().contains(tile))
            return 3;
        if(city.findBuilding(type)!=null)
            return 4;
        Building building = new Building(type,tile);
        building.setRemainedCost(0);
        city.getBuildings().add(building);
        return 0;
    }
}
