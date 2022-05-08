package controller;

import model.*;
import model.Units.*;
import model.building.Building;
import model.building.BuildingType;
import model.features.FeatureType;
import model.improvements.Improvement;
import model.improvements.ImprovementType;
import model.resources.ResourcesTypes;
import model.technologies.Technology;
import model.technologies.TechnologyType;
import model.tiles.Tile;
import model.tiles.TileType;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class GameController {
    private static final ArrayList<Civilization> civilizations = new ArrayList<>();
    private static Unit selectedUnit;
    private static City selectedCity;
    private static Map map;
    private static int startWindowX = 0;
    private static int startWindowY = 0;
    private static int playerTurn = 0;
    private static ArrayList<Tasks> unfinishedTasks = new ArrayList<>();

    public static void startGame(ArrayList<User> PlayersNames) {
        setCivilizations(PlayersNames);
        map = new Map(civilizations);
        for (int i = 0; i < PlayersNames.size(); i++)
            civilizations.get(i).setTileConditions
                    (new Civilization.TileCondition[map.getX()][map.getY()]);
        map.addStartingSettlers(civilizations);
        for (int i = 0; i < GameController.getCivilizations().size(); i++)
            GameController.nextTurn();
        setUnfinishedTasks();
    }

    public static City getSelectedCity() {
        return selectedCity;
    }

    public static boolean setSelectedCombatUnit(int x, int y) {
        if (map.coordinatesToTile(x, y).getNonCivilian() == null)
            return false;
        selectedUnit = map.coordinatesToTile(x, y).getNonCivilian();
        return true;
    }

    public static boolean setSelectedNonCombatUnit(int x, int y) {
        if (map.coordinatesToTile(x, y).getCivilian() == null)
            return false;
        selectedUnit = map.coordinatesToTile(x, y).getCivilian();
        return true;
    }

    private static City nameToCity(String name) {
        for (Civilization civilization : civilizations)
            for (int j = 0; j < civilization.getCities().size(); j++)
                if (civilization.getCities().get(j).getName()
                        .toLowerCase(Locale.ROOT).equals(name.toLowerCase(Locale.ROOT)))
                    return civilization.getCities().get(j);
        return null;
    }

    public static int reAssignCitizen(int originX,
                                      int originY,
                                      int destinationX,
                                      int destinationY) {
        if (selectedCity == null) return 3;
        if (selectedCity.getCivilization() != civilizations.get(playerTurn)) return 2;
        if (originX < 0 || originY < 0 || destinationY < 0 ||
                destinationX < 0 || originX > map.getX() ||
                originY > map.getY() ||
                destinationX > map.getX() ||
                destinationY > map.getY())
            return 1;
        if (selectedCity.assignCitizenToTiles(map.coordinatesToTile(originX, originY),
                map.coordinatesToTile(destinationX, destinationY)))
            return 0;
        return 4;
    }

    public static int assignCitizen(int destinationX, int destinationY) {
        if (selectedCity == null) return 3;
        if (selectedCity.getCivilization() != civilizations.get(playerTurn)) return 2;
        if (destinationY < 0 || destinationX < 0 ||
                destinationX > map.getX() || destinationY > map.getY()) return 1;
        if (selectedCity.assignCitizenToTiles(null,
                map.coordinatesToTile(destinationX, destinationY))) return 0;
        return 4;
    }

    public static boolean setSelectedCityByName(String name) {
        City tempCity = nameToCity(name);
        if (tempCity == null)
            return false;
        selectedCity = tempCity;
        return true;
    }


    public static boolean setSelectedCityByPosition(int x, int y) {
        if (map.coordinatesToTile(x, y).getCity() != null) {
            selectedCity = map.coordinatesToTile(x, y).getCity();
            return true;
        }
        return false;
    }

    private static Tasks findTask(Tasks tasks) {
        for (Tasks unfinishedTask : unfinishedTasks)
            if (tasks.getTaskTypes() == unfinishedTask.getTaskTypes() &&
                    tasks.getTile() == unfinishedTask.getTile())
                return unfinishedTask;
        return null;
    }

    public static void deleteFromUnfinishedTasks(Tasks task) {
        Tasks gettingDeletedTask = findTask(task);
        if (findTask(task) == null)
            return;
        unfinishedTasks.remove(gettingDeletedTask);
    }

    public static boolean unitMoveTo(int x, int y) {
        if (selectedUnit == null || x < 0 || y < 0 ||
                x >= map.getX() || y > map.getY() ||
                map.coordinatesToTile(x, y).getTileType() == TileType.OCEAN ||
                map.coordinatesToTile(x, y).getTileType() == TileType.MOUNTAIN)
            return false;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(), TaskTypes.UNIT));
        selectedUnit.setState(UnitState.AWAKE);
        return selectedUnit.move(map.coordinatesToTile(x, y), true);
    }

    public static int unitSleep() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(), TaskTypes.UNIT));
        selectedUnit.setState(UnitState.SLEEP);
        return 0;
    }

    public static int unitAlert() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(), TaskTypes.UNIT));
        selectedUnit.setState(UnitState.ALERT);
        if (openNewArea(selectedUnit.getCurrentTile(),
                civilizations.get(playerTurn), selectedUnit))
            return 3;
        return 0;
    }

    public static int unitFortify() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        if(selectedUnit instanceof Civilian)
            return 3;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(), TaskTypes.UNIT));
        selectedUnit.setState(UnitState.FORTIFY);
        return 0;
    }

    public static int unitFortifyUntilFullHealth() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        if(selectedUnit instanceof Civilian)
            return 3;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(),
                TaskTypes.UNIT));
        selectedUnit.setState(UnitState.FORTIFY_UNTIL_FULL_HEALTH);
        return 0;
    }

    public static int unitGarrison() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        if(selectedUnit instanceof Civilian)
            return 3;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(), TaskTypes.UNIT));
        selectedUnit.setState(UnitState.GARRISON);
        return 0;
    }

    public static int unitSetupRanged() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        if(selectedUnit.getUnitType().combatType == CombatType.SIEGE) {
            selectedUnit.setState(UnitState.SETUP);
            deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(), TaskTypes.UNIT));
            return 0;
        }
       return 3;

    }

    public static int unitFoundCity(String string) {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        if (selectedUnit.getUnitType() != UnitType.SETTLER)
            return 3;
        if (selectedUnit.getCurrentTile().getCity() != null)
            return 4;
        for (Civilization civilization : civilizations)
            if (civilization.isInTheCivilizationsBorder(selectedUnit.getCurrentTile()))
                return 4;
        for (Civilization civilization : civilizations)
            for (City city : civilization.getCities())
                if(city.getName().equals(string))
                    return 5;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(), TaskTypes.UNIT));
        ((Civilian) selectedUnit).city(string);
        civilizations.get(playerTurn).changeHappiness(-1);
        unitDelete(selectedUnit);
        openNewArea(selectedUnit.getCurrentTile(), civilizations.get(playerTurn), null);
        selectedUnit = null;
        return 0;
    }

    public static int unitCancelMission() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        if (selectedUnit.getDestinationTile() == null &&
                selectedUnit.getState() == UnitState.AWAKE)
            return 3;
        selectedUnit.cancelMission();
        return 0;
    }


    public static int unitWake() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        if (selectedUnit.getState() == UnitState.AWAKE)
            return 3;
        unfinishedTasks.add(new Tasks(selectedUnit.getCurrentTile(),
                TaskTypes.UNIT));
        selectedUnit.setState(UnitState.AWAKE);
        return 0;
    }

    public static int unitDelete(Unit unit) {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(),
                TaskTypes.UNIT));
        civilizations.get(playerTurn).getUnits().remove(unit);
        if (unit instanceof NonCivilian)
            unit.getCurrentTile().setNonCivilian(null);
        else
            unit.getCurrentTile().setCivilian(null);
        openNewArea(unit.getCurrentTile(),unit.getCivilization(),null);
        return 0;
    }


    public static int unitBuild(ImprovementType improvementType) {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        if (selectedUnit.getUnitType() != UnitType.WORKER)
            return 3;
        if (!doesHaveTheRequiredTechnologyToBuildImprovement(improvementType,
                selectedUnit.getCurrentTile(), selectedUnit.getCivilization()))
            return 4;
        if (!canHaveTheImprovement(selectedUnit.getCurrentTile(), improvementType))
            return 5;
        selectedUnit.getCurrentTile().setImprovement
                (new Improvement(improvementType, selectedUnit.getCurrentTile()));
        selectedUnit.setState(UnitState.BUILDING);
        return 0;
    }

    public static boolean doesHaveTheRequiredTechnologyToBuildImprovement(ImprovementType improvementType,
                                                                          Tile tile, Civilization civilization) {
        if (civilization.doesContainTechnology(improvementType.prerequisitesTechnologies) != 1)
            return false;
        if ((improvementType == ImprovementType.FARM || improvementType == ImprovementType.MINE)
                && (tile.getContainedFeature() != null &&
                tile.getContainedFeature().getFeatureType() == FeatureType.JUNGLE &&
                civilization.doesContainTechnology(TechnologyType.BRONZE_WORKING) != 1))
            return false;
        if (improvementType == ImprovementType.MINE && tile.getContainedFeature() != null &&
                selectedUnit != null &&
                selectedUnit.getCurrentTile().getContainedFeature() != null &&
                selectedUnit.getCurrentTile().getContainedFeature().getFeatureType() == FeatureType.SWAMP &&
                selectedUnit.getCivilization().doesContainTechnology(TechnologyType.MASONRY) != 1)
            return false;
        if (improvementType == ImprovementType.FARM &&
                selectedUnit != null &&
                selectedUnit.getCurrentTile().getContainedFeature() != null &&
                selectedUnit.getCurrentTile().getContainedFeature().getFeatureType() == FeatureType.SWAMP &&
                selectedUnit.getCivilization().doesContainTechnology(TechnologyType.MASONRY) != 1)
            return false;
        if (improvementType == ImprovementType.FARM &&
                tile.getContainedFeature() != null &&
                tile.getContainedFeature().getFeatureType() == FeatureType.FOREST &&
                civilization.doesContainTechnology(TechnologyType.MINING) != 1)
            return false;
        return true;
    }



    private static boolean canHaveTheImprovement(Tile tile, ImprovementType improvementType) {
        if(tile.getCivilization() != selectedUnit.getCivilization())
            return false;
        if ((tile.getContainedFeature()==null ||
                !FeatureType.doesContainImprovement(tile.getContainedFeature().getFeatureType(),improvementType)) &&
                !TileType.canContainImprovement(tile.getTileType(), improvementType))
            return false;
        return true;
    }

    public static int unitBuildRoad() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        if (selectedUnit.getUnitType() != UnitType.WORKER)
            return 3;
        if(selectedUnit.getCurrentTile().getRoad()!=null)
            return 6;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(), TaskTypes.UNIT));
        selectedUnit.getCurrentTile().setRoad
                (new Improvement(ImprovementType.ROAD, selectedUnit.getCurrentTile()));
        selectedUnit.setState(UnitState.BUILDING);
        return 0;
    }

    public static int unitBuildRailRoad() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        if (selectedUnit.getUnitType() != UnitType.WORKER)
            return 3;
        if(civilizations.get(playerTurn).doesContainTechnology(TechnologyType.RAILROAD)!=1)
            return 4;
        if(selectedUnit.getCurrentTile().getRoad()!=null)
            return 6;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(),
                TaskTypes.UNIT));
        selectedUnit.getCurrentTile().setRoad
                (new Improvement(ImprovementType.RAILROAD,
                        selectedUnit.getCurrentTile()));
        selectedUnit.setState(UnitState.BUILDING);
        return 0;
    }

    public static int unitRemoveFromTile(boolean isJungle) {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        if (selectedUnit.getUnitType() != UnitType.WORKER)
            return 3;
        if (isJungle && selectedUnit.getCurrentTile().getContainedFeature()!=null &&
                selectedUnit.getCurrentTile().getContainedFeature().getFeatureType() != FeatureType.JUNGLE &&
                selectedUnit.getCurrentTile().getContainedFeature().getFeatureType() != FeatureType.FOREST)
            return 4;
        if (!isJungle && selectedUnit.getCurrentTile().getRoad()!=null &&
                selectedUnit.getCurrentTile().getRoad().getImprovementType() != ImprovementType.ROAD &&
                selectedUnit.getCurrentTile().getRoad().getImprovementType() != ImprovementType.RAILROAD)
            return 5;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(), TaskTypes.UNIT));
        if (isJungle)
            ((Civilian) selectedUnit).remove(1);
        else
            selectedUnit.getCurrentTile().setRoad(null);
        return 0;
    }

    public static int unitRepair() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        if (selectedUnit.getUnitType() != UnitType.WORKER)
            return 3;
        if (selectedUnit.getCurrentTile().getImprovement() == null)
            return 4;
        if (selectedUnit.getCurrentTile().getImprovement().getNeedsRepair() == 0)
            return 5;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(), TaskTypes.UNIT));
        selectedUnit.setState(UnitState.REPAIRING);
        return 0;
    }

    public static void mapShowPosition(int x, int y) {
        startWindowX = x;
        startWindowY = y;
        if (startWindowY > map.getY() - (Map.WINDOW_Y - 1))
            startWindowY = map.getY() - (Map.WINDOW_Y - 1);
        if (startWindowX > map.getX() - (Map.WINDOW_X - 1))
            startWindowX = map.getX() - (Map.WINDOW_X - 1);
        if (startWindowY < 0)
            startWindowY = 0;
        if (startWindowX < 0)
            startWindowX = 0;
    }


    public static int mapShowCityName(String name) {
        City tempCity = nameToCity(name);
        if (tempCity == null)
            return 1;
        if (civilizations.get(playerTurn)
                .getTileConditions()[tempCity.getMainTile().getX()][tempCity.getMainTile().getY()] == null)
            return 2;
        mapShowPosition(tempCity.getMainTile().getX() - Map.WINDOW_X / 2,
                tempCity.getMainTile().getY() - Map.WINDOW_Y / 2 + 1);
        return 0;
    }

    public static void mapMove(int number, String direction) {
        if (Objects.equals(direction, "r"))
            mapShowPosition(startWindowX, startWindowY + number);
        if (Objects.equals(direction, "l"))
            mapShowPosition(startWindowX, startWindowY - number);
        if (Objects.equals(direction, "u"))
            mapShowPosition(startWindowX - number, startWindowY);
        else
            mapShowPosition(startWindowX + number, startWindowY);
    }


    public static int buyTile(int x, int y) {
        if (x < 0 || y < 0 || x >= map.getX() || y > map.getY()) return 2;
        if (selectedCity == null || selectedCity.getCivilization() != civilizations.get(playerTurn)) return 4;
        if (!selectedCity.isTileNeighbor(map.coordinatesToTile(x, y))) return 3;
        if (civilizations.get(playerTurn).getGold() < 15 + 10 * (selectedCity.getTiles().size() - 6)) return 5;
        if (!selectedCity.addTile(map.coordinatesToTile(x, y))) return 1;
        selectedCity.getCivilization().changeGold(-(15 + 10 * (selectedCity.getTiles().size() - 6)));
        return 0;
    }

    private static void setCivilizations(ArrayList<User> users) {
        for (int i = 0; i < users.size(); i++)
            civilizations.add(new Civilization(users.get(i), i));
    }


    private static boolean canUnitAttack(Tile tile) {
        if (tile.getCity() != null &&
                tile.getCity().getCivilization() != civilizations.get(playerTurn)) {
            selectedUnit.setState(UnitState.ATTACK);
            return true;
        }

        if (tile.getNonCivilian() != null &&
                tile.getNonCivilian().getCivilization() != civilizations.get(playerTurn)) {
            selectedUnit.setState(UnitState.ATTACK);
            return true;
        }
        if (tile.getCivilian() != null &&
                tile.getCivilian().getCivilization() != civilizations.get(playerTurn) &&
                selectedUnit.getUnitType().range > 1) {
            selectedUnit.setState(UnitState.ATTACK);
            return true;
        }
        return false;
    }

    private static boolean canCityAttack(City city, Tile tile) {
        if (tile.getNonCivilian() == null ||
                tile.getNonCivilian().getCivilization() == city.getCivilization())
            return false;
        return !map.isInRange(2, city.getMainTile(), tile);

    }

    public static int buildWall() {
        if (selectedCity == null)
            return 1;
        if (selectedCity.getCivilization() != civilizations.get(playerTurn))
            return 2;
        if (selectedCity.getWall() != null)
            return 3;
        for (Building building : selectedCity.getHalfProducedBuildings())
            if (building.getRemainedCost() != 0 && building.getBuildingType() == BuildingType.WALL) {
                selectedCity.setProduct(building);
                return 0;
            }
        Building building = new Building(BuildingType.WALL);
        selectedCity.getHalfProducedBuildings().add(building);
        selectedCity.setProduct(building);
        return 0;
    }

    public static boolean nextTurnIfYouCan() {
        if (unfinishedTasks.size() != 0)
            return false;
        nextTurn();
        return true;
    }

    public static void nextTurn() {
        civilizations.get(playerTurn).endTheTurn();
        playerTurn = (playerTurn + 1) % civilizations.size();
        if (civilizations.get(playerTurn).getCities().size() == 0 &&
                civilizations.get(playerTurn).getUnits().size() == 0) {
            nextTurn();
            return;
        }
        civilizations.get(playerTurn).startTheTurn();
        setUnfinishedTasks();
        if (civilizations.get(playerTurn).getCities().size() != 0)
            mapShowPosition(civilizations.get(playerTurn).getCities()
                            .get(0).getMainTile().getX() - Map.WINDOW_X / 2,
                    civilizations.get(playerTurn).getCities().get(0)
                            .getMainTile().getY()- Map.WINDOW_Y / 2 + 1);
        else if (civilizations.get(playerTurn).getUnits().size() != 0)
            mapShowPosition(civilizations.get(playerTurn).getUnits().get(0)
                            .getCurrentTile().getX() - Map.WINDOW_X / 2,
                    civilizations.get(playerTurn).getUnits().get(0)
                            .getCurrentTile().getY()- Map.WINDOW_Y / 2 + 1);
        selectedCity = null;
        selectedUnit = null;
        if(civilizations.get(playerTurn).getCities().size()!=0)
            selectedCity = civilizations.get(playerTurn).getCities().get(0);
        if(civilizations.get(playerTurn).getUnits().size()!=0)
            selectedUnit = civilizations.get(playerTurn).getUnits().get(0);
    }

    public static void setUnfinishedTasks() {
        unfinishedTasks = new ArrayList<>();
        for (int i = 0; i < civilizations.get(playerTurn).getUnits().size(); i++)
            if (civilizations.get(playerTurn).getUnits().get(i).getRemainedCost() == 0 &&
                    civilizations.get(playerTurn).getUnits().get(i).getState() == UnitState.AWAKE &&
                    civilizations.get(playerTurn).getUnits().get(i).getDestinationTile()==null)
                unfinishedTasks.add(new Tasks(civilizations.get(playerTurn).getUnits()
                        .get(i).getCurrentTile(), TaskTypes.UNIT));
        for (int i = 0; i < civilizations.get(playerTurn).getCities().size(); i++)
            if (civilizations.get(playerTurn).getCities().get(i).getProduct() == null)
                unfinishedTasks.add(new Tasks(civilizations.get(playerTurn)
                        .getCities().get(i).getMainTile(), TaskTypes.CITY_PRODUCTION));
        if (civilizations.get(playerTurn).getCities().size() != 0 &&
                civilizations.get(playerTurn).getGettingResearchedTechnology() == null &&
                !civilizations.get(playerTurn).areTechnologiesFinished())
            unfinishedTasks.add(new Tasks(null, TaskTypes.TECHNOLOGY_PROJECT));
    }

    public static int cheatUnit(int x, int y, UnitType unitType) {
        if (map.coordinatesToTile(x, y).getMovingPrice() > 123) return 1;
        if (unitType.combatType == CombatType.CIVILIAN) {
            if(map.coordinatesToTile(x, y).getCivilian()!=null)
                return 2;
            Civilian hardcodeUnit = new Civilian(map.coordinatesToTile(x, y), civilizations.get(playerTurn), unitType);
            hardcodeUnit.setRemainedCost(0);
            civilizations.get(playerTurn).getUnits().add(hardcodeUnit);
            map.coordinatesToTile(x, y).setCivilian(hardcodeUnit);
            openNewArea(map.coordinatesToTile(x, y),
                    civilizations.get(playerTurn), hardcodeUnit);
        } else {
            if(map.coordinatesToTile(x, y).getNonCivilian()!=null)
                return 2;
            NonCivilian hardcodeUnit = new NonCivilian(map.coordinatesToTile(x, y), civilizations.get(playerTurn), unitType);
            hardcodeUnit.setRemainedCost(0);
            civilizations.get(playerTurn).getUnits().add(hardcodeUnit);
            map.coordinatesToTile(x, y).setNonCivilian( hardcodeUnit);
            openNewArea(map.coordinatesToTile(x, y), civilizations.get(playerTurn), hardcodeUnit);
        }
        return 0;
    }

    public static boolean openNewArea(Tile tile, Civilization civilization, Unit unit) {
        boolean isThereAnyEnemy = false;
        for (int i = 0; i < 6; i++) {
            if (tile.getNeighbours(i) == null)
                continue;
            civilization.getTileConditions()[tile.getNeighbours(i).getX()][tile.getNeighbours(i).getY()] =
                    new Civilization.TileCondition(tile.getNeighbours(i).
                            cloneTileForCivilization(civilization), true);
            if (unit != null && ((tile.getNeighbours(i).getCivilian() != null &&
                    tile.getNeighbours(i).getCivilian().getCivilization() != civilization) ||
                    (tile.getNeighbours(i).getNonCivilian() != null &&
                            tile.getNeighbours(i).getNonCivilian()
                                    .getCivilization() != civilization))) {
                if (unit.getState() == UnitState.ALERT)
                    unit.setState(UnitState.AWAKE);
                isThereAnyEnemy = true;
            }
            if (tile.getNeighbours(i).getTileType() == TileType.MOUNTAIN ||
                    tile.getNeighbours(i).getTileType() == TileType.HILL ||
                    (tile.getNeighbours(i).getContainedFeature() != null &&
                            (tile.getNeighbours(i).getContainedFeature()
                                    .getFeatureType() == FeatureType.FOREST ||
                                    tile.getNeighbours(i).getContainedFeature()
                                            .getFeatureType() == FeatureType.JUNGLE)))
                continue;
            for (int j = 0; j < 6; j++) {
                if (tile.getNeighbours(i).getNeighbours(j) == null)
                    continue;
                int neighbourX = tile.getNeighbours(i).getNeighbours(j).getX();
                int neighbourY = tile.getNeighbours(i).getNeighbours(j).getY();
                civilization.getTileConditions()[neighbourX][neighbourY] =
                        new Civilization.TileCondition(tile.getNeighbours(i).getNeighbours(j)
                                .cloneTileForCivilization(civilization), true);
                if (unit != null &&
                        ((tile.getNeighbours(i).getNeighbours(j).getCivilian() != null &&
                                tile.getNeighbours(i).getNeighbours(j)
                                        .getCivilian().getCivilization() != civilization) ||
                        (tile.getNeighbours(i).getNeighbours(j).getNonCivilian() != null &&
                                tile.getNeighbours(i).getNeighbours(j)
                                        .getNonCivilian().getCivilization() != civilization))) {
                    if (unit.getState() == UnitState.ALERT)
                        unit.setState(UnitState.AWAKE);
                    isThereAnyEnemy = true;
                }

            }
        }
        civilization.getTileConditions()[tile.getX()][tile.getY()] =
                new Civilization.TileCondition(tile.
                        cloneTileForCivilization(civilization), true);
        return isThereAnyEnemy;
    }

    public static Map getMap() {
        return map;
    }

    public static String printMap() {
        return civilizations.get(playerTurn).getUser().getNickname() + ":\n" +
                map.printMap(civilizations.get(playerTurn).getTileConditions(),
                startWindowX, startWindowY);
    }

    public static Unit getSelectedUnit() {
        return selectedUnit;
    }

    public static int startProducingUnit(String productIcon) {
        UnitType tempType = UnitType.stringToEnum(productIcon);
        if (tempType == null)
            return 1;
        if (selectedCity == null)
            return 2;
        if (selectedCity.getCivilization() != civilizations.get(playerTurn))
            return 3;
        if(tempType.getResourcesType()!=null &&
                (!civilizations.get(playerTurn).getResourcesAmount().containsKey(tempType.getResourcesType()) ||
                (civilizations.get(playerTurn).getResourcesAmount().containsKey(tempType.getResourcesType()) &&
                        civilizations.get(playerTurn).getResourcesAmount().get(tempType.getResourcesType())==0)))
            return 5;
        if(civilizations.get(playerTurn).doesContainTechnology(tempType.getTechnologyRequired())!=1)
            return 6;
        for (Unit unit : selectedCity.getHalfProducedUnits())
            if (unit.getRemainedCost() != 0 && unit.getUnitType() == tempType) {
                selectedCity.setProduct(unit);
                GameController.deleteFromUnfinishedTasks(new Tasks(selectedCity.getMainTile(), TaskTypes.CITY_PRODUCTION));
                return 0;
            }
        if (tempType.combatType == CombatType.CIVILIAN) {
            Civilian civilian = new Civilian(selectedCity.getMainTile(),
                    civilizations.get(playerTurn), tempType);
            selectedCity.getHalfProducedUnits().add(civilian);
            selectedCity.setProduct(civilian);
        } else {
            NonCivilian nonCivilian = new NonCivilian(selectedCity.getMainTile(),
                    civilizations.get(playerTurn), tempType);
            selectedCity.getHalfProducedUnits().add(nonCivilian);
            selectedCity.setProduct(nonCivilian);
        }
        if(tempType.getResourcesType()!=null)
        {
            int temp = civilizations.get(playerTurn).getResourcesAmount().get(tempType.getResourcesType());
            civilizations.get(playerTurn).getResourcesAmount().remove(tempType.getResourcesType());
            if(temp!=1)
                civilizations.get(playerTurn).getResourcesAmount().put(tempType.getResourcesType(),temp-1);
        }
        GameController.deleteFromUnfinishedTasks(new Tasks(selectedCity.getMainTile(),
                TaskTypes.CITY_PRODUCTION));
        return 0;
    }

    public static ArrayList<Civilization> getCivilizations() {
        return civilizations;
    }


    public static int getPlayerTurn() {
        return playerTurn;
    }

    public static ArrayList<Tasks> getUnfinishedTasks() {
        return unfinishedTasks;
    }


    public static int unitAttack(int x, int y) {
        if (!(selectedUnit instanceof NonCivilian) ||
                selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        if (x < 0 || y < 0 || x >= map.getX() || y > map.getY()) return 3;
        if (!canUnitAttack(map.coordinatesToTile(x, y))) return 1;
        if (selectedUnit.move(map.coordinatesToTile(x, y), true)) return 0;
        return 4;
    }

    public static int cityAttack(int x, int y) {
        if (selectedCity.getCivilization() != civilizations.get(playerTurn)) return 3;
        if (x < 0 || y < 0 || x >= map.getX() || y > map.getY()) return 2;
        if (selectedUnit.getUnitType().combatType == CombatType.SIEGE &&
                (selectedUnit.getState() != UnitState.SETUP ||
                        ((NonCivilian) selectedUnit).getFortifiedCycle() < 1))
            return 5;
        if (selectedUnit.getUnitType().combatType == CombatType.SIEGE &&
                !map.isInRange(selectedUnit.getUnitType().range,
                        selectedUnit.getCurrentTile(), map.coordinatesToTile(x, y)))
            return 4;
        if (!canCityAttack(selectedCity, map.coordinatesToTile(x, y))) return 1;
        selectedCity.attack(map.coordinatesToTile(x, y));
        return 0;
    }

    public static int cityDestiny(boolean burn) {
        if (selectedCity == null) return 2;
        if (selectedCity.getHP() > 0) return 1;
        if (selectedCity.isCapital && burn) return 4;
        if (burn) selectedCity.destroy();
        else selectedCity.changeCivilization(civilizations.get(playerTurn));
        deleteFromUnfinishedTasks(new Tasks(selectedCity.getMainTile(), TaskTypes.CITY_DESTINY));
        return 0;
    }

    public static void cheatScience(int number) {
        civilizations.get(playerTurn).cheatScience = number;
    }

    public static int cheatProduction(int number) {
        if (selectedCity == null)
            return 1;
        if (selectedCity.getCivilization() != civilizations.get(playerTurn))
            return 1;
        selectedCity.productionCheat += number;
        return 0;
    }

    public static void cheatResource(int number, ResourcesTypes resourcesTypes) {
        int tempNumber = 0;
        if (civilizations.get(playerTurn).getResourcesAmount().containsKey(resourcesTypes))
            tempNumber = civilizations.get(playerTurn).getResourcesAmount().get(resourcesTypes);
        civilizations.get(playerTurn).getResourcesAmount().remove(resourcesTypes);
        civilizations.get(playerTurn).getResourcesAmount().put(resourcesTypes, tempNumber + number);
    }

    public static int unitPillage() {
        if (!(selectedUnit instanceof NonCivilian) ||
                selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 4;
        if (((NonCivilian) selectedUnit).pillage()) return 0;
        return 3;
    }

    public static int skipUnitTask() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        if (findTask(new Tasks(selectedUnit.getCurrentTile(), TaskTypes.UNIT)) == null)
            return 3;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(), TaskTypes.UNIT));
        return 0;
    }
    public static int cheatTechnology(TechnologyType technologyType)
    {
        if(technologyType==null)
            return 1;
        int result = civilizations.get(playerTurn).doesContainTechnology(technologyType);
        if(result==1)
            return 2;
        if(result==2)
        {
            for (Technology research : civilizations.get(playerTurn).getResearches())
                if(research.getTechnologyType()==technologyType)
                {
                    research.setRemainedCost(0);
                    break;
                }
        }
        else
        {
            Technology technology = new Technology(technologyType);
            technology.setRemainedCost(0);
            civilizations.get(playerTurn).getResearches().add(technology);
        }
        setUnfinishedTasks();
        return 0;
    }
    public static void openMap()
    {
        for(int i = 0;i<map.getX();i++)
            for(int j=0;j<map.getY();j+=2)
                openNewArea(map.coordinatesToTile(i,j),civilizations.get(playerTurn),null);
    }
    public static int cheatMoveIt(int x, int y)
    {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        if(x<0 || y<0 || x>map.getX() || y>map.getY())
            return 3;
        Tile tempTile = map.coordinatesToTile(x,y);
        selectedUnit.setCurrentTile(map.coordinatesToTile(x,y));
        if(selectedUnit instanceof Civilian)
            tempTile.setCivilian(selectedUnit);
        if(selectedUnit instanceof NonCivilian)
            tempTile.setNonCivilian((NonCivilian) selectedUnit);
        return 0;
    }
    public static int cheatCaptureCity(String name)
    {
        City city = nameToCity(name);
        if(city==null)
            return 1;
        if(city.getCivilization()==civilizations.get(playerTurn))
            return 2;
        city.changeCivilization(civilizations.get(playerTurn));
        return 0;
    }
    public static int buyUnit(String string, int x, int y)
    {
        UnitType unitType = UnitType.stringToEnum(string);
        if(unitType==null)
            return 1;
        Tile tile = map.coordinatesToTile(x,y);
        if(tile.getCivilization()!=civilizations.get(playerTurn))
            return 2;
        if(civilizations.get(playerTurn).getGold()<unitType.getCost())
            return 3;
        if((unitType.combatType==CombatType.CIVILIAN && map.coordinatesToTile(x, y).getCivilian()!=null) ||
                (unitType.combatType!=CombatType.CIVILIAN && map.coordinatesToTile(x,y).getNonCivilian()!=null))
            return 4;
        cheatUnit(x,y,unitType);
        return 0;
    }
    public static int cheatAllScience()
    {
        for (int i = 0; i < map.getX(); i++)
            for (int j = 0; j < map.getY(); j++) {
                Improvement improvement = new Improvement(ImprovementType.ROAD,map.coordinatesToTile(i,j));
                improvement.setRemainedCost(0);
                map.coordinatesToTile(i,j).setRoad(improvement);
            }
        return 0;
    }

}
