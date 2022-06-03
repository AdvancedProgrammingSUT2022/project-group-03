package com.example.demo.controller.gameController;

import com.example.demo.model.*;
import com.example.demo.model.Units.*;
import com.example.demo.model.features.FeatureType;
import com.example.demo.model.improvements.ImprovementType;
import com.example.demo.model.technologies.TechnologyType;
import com.example.demo.model.tiles.Tile;
import com.example.demo.model.tiles.TileType;

import java.util.ArrayList;
import java.util.Locale;

public class GameController {
    private static ArrayList<Civilization> civilizations = new ArrayList<>();
    private static Unit selectedUnit;
    private static City selectedCity;
    private static Map map;
    static int startWindowX = 0;
    static int startWindowY = 0;
    private static int playerTurn = 0;
    private static int cycle;
    private static ArrayList<Tasks> unfinishedTasks = new ArrayList<>();

    public static void startGame(ArrayList<User> PlayersNames) {
        cycle = 1;
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

    static City nameToCity(String name) {
        for (Civilization civilization : civilizations)
            for (int j = 0; j < civilization.getCities().size(); j++)
                if (civilization.getCities().get(j).getName()
                        .toLowerCase(Locale.ROOT).equals(name.toLowerCase(Locale.ROOT)))
                    return civilization.getCities().get(j);
        return null;
    }


    static Tasks findTask(Tasks tasks) {
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
                selectedUnit.getCivilization().doesContainTechnology(TechnologyType.MASONRY) != 1) return false;
        return improvementType != ImprovementType.FARM ||
                tile.getContainedFeature() == null ||
                tile.getContainedFeature().getFeatureType() != FeatureType.FOREST ||
                civilization.doesContainTechnology(TechnologyType.MINING) == 1;
    }

    static boolean canHaveTheImprovement(Tile tile, ImprovementType improvementType) {
        if (tile.getCivilization() != selectedUnit.getCivilization())
            return false;
        return (tile.getContainedFeature() != null &&
                FeatureType.doesContainImprovement(tile.getContainedFeature().getFeatureType(),
                        improvementType)) ||
                TileType.canContainImprovement(tile.getTileType(), improvementType);
    }


    private static void setCivilizations(ArrayList<User> users) {
        civilizations = new ArrayList<>();
        for (int i = 0; i < users.size(); i++)
            civilizations.add(new Civilization(users.get(i), i));
    }


    static boolean canUnitAttack(Tile tile) {
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

    public static boolean nextTurnIfYouCan() {
        if (unfinishedTasks.size() != 0) return false;
        nextTurn();
        return true;
    }

    public static void nextTurn() {
        civilizations.get(playerTurn).endTheTurn();
        playerTurn = (playerTurn + 1) % civilizations.size();
        if (civilizations.get(playerTurn).getCities().size() == 0 &&
                civilizations.get(playerTurn).getUnits().size() == 0) {
            boolean isItTheEnd = true;
            for (int i = 0; i < civilizations.size(); i++) {
                if (civilizations.get(playerTurn).getCities().size() != 0 ||
                        civilizations.get(playerTurn).getUnits().size() != 0) {
                    isItTheEnd = false;
                    break;
                }
            }
            if (!isItTheEnd) nextTurn();
            return;
        }
        if(playerTurn%civilizations.size()==0)
            cycle++;
        civilizations.get(playerTurn).startTheTurn();
        setUnfinishedTasks();
        if (civilizations.get(playerTurn).getCities().size() != 0)
            MapCommandsController.mapShowPosition(civilizations.get(playerTurn).getCities()
                            .get(0).getMainTile().getX() - Map.WINDOW_X / 2,
                    civilizations.get(playerTurn).getCities().get(0)
                            .getMainTile().getY() - Map.WINDOW_Y / 2 + 1);
        else if (civilizations.get(playerTurn).getUnits().size() != 0)
            MapCommandsController.mapShowPosition(civilizations.get(playerTurn).getUnits().get(0)
                            .getCurrentTile().getX() - Map.WINDOW_X / 2,
                    civilizations.get(playerTurn).getUnits().get(0)
                            .getCurrentTile().getY() - Map.WINDOW_Y / 2 + 1);
        selectedCity = null;
        selectedUnit = null;
        if (civilizations.get(playerTurn).getCities().size() != 0)
            selectedCity = civilizations.get(playerTurn).getCities().get(0);
        for (Unit unit : civilizations.get(playerTurn).getUnits()) {
            if(unit.getState()==UnitState.AWAKE)
            {
                selectedUnit = unit;
                break;
            }
        }
        if (selectedUnit==null &&
                civilizations.get(playerTurn).getUnits().size() != 0)
            selectedUnit = civilizations.get(playerTurn).getUnits().get(0);
        if(GameController.getCivilizations()
                .get(GameController.getPlayerTurn()).getNotifications().containsKey(cycle))
            for (String string : GameController.getCivilizations()
                    .get(GameController.getPlayerTurn()).getNotifications().get(cycle))
                System.out.println(cycle + ". " + string);
    }

    public static void setUnfinishedTasks() {
        unfinishedTasks = new ArrayList<>();
        for (int i = 0; i < civilizations.get(playerTurn).getUnits().size(); i++)
            if (civilizations.get(playerTurn).getUnits().get(i).getRemainedCost() == 0 &&
                    civilizations.get(playerTurn).getUnits().get(i).getState() == UnitState.AWAKE &&
                    civilizations.get(playerTurn).getUnits().get(i).getDestinationTile() == null)
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

    private static boolean secondForOpenArea(int i, Tile tile, Civilization civilization,
                                             Unit unit, boolean isThereAnyEnemy) {
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
                isThereAnyEnemy = true;
            }
        }
        return isThereAnyEnemy;
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
                isThereAnyEnemy = true;
            }
            if (tile.getNeighbours(i).getTileType() == TileType.MOUNTAIN ||
                    tile.getNeighbours(i).getTileType() == TileType.HILL ||
                    (tile.getNeighbours(i).getContainedFeature() != null &&
                            tile.getNeighbours(i).getContainedFeature().getFeatureType() == FeatureType.JUNGLE))
                continue;
            isThereAnyEnemy = secondForOpenArea(i, tile, civilization, unit, isThereAnyEnemy);
        }
        civilization.getTileConditions()[tile.getX()][tile.getY()] =
                new Civilization.TileCondition(tile.
                        cloneTileForCivilization(civilization), true);
        if (isThereAnyEnemy && unit != null && unit.getState() == UnitState.ALERT)
            unit.setState(UnitState.AWAKE);
        return isThereAnyEnemy;
    }

    public static Map getMap() {
        return map;
    }

    public static String printMap() {
        return playerTurn + 1 + ". " + civilizations.get(playerTurn).getUser().getNickname() + ":\n" +
                map.printMap(civilizations.get(playerTurn).getTileConditions(),
                        startWindowX, startWindowY);
    }

    public static Unit getSelectedUnit() {
        return selectedUnit;
    }


    private static void startProducingsOperation(UnitType tempType) {
        for (Unit unit : selectedCity.getHalfProducedUnits())
            if (unit.getRemainedCost() != 0 && unit.getUnitType() == tempType) {
                selectedCity.setProduct(unit);
                GameController.deleteFromUnfinishedTasks(new Tasks(selectedCity.getMainTile(),
                        TaskTypes.CITY_PRODUCTION));
                return;
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
        if (tempType.getResourcesType() != null) {
            int temp = civilizations.get(playerTurn).getResourcesAmount().get(tempType.getResourcesType());
            civilizations.get(playerTurn).getResourcesAmount().remove(tempType.getResourcesType());
            if (temp != 1)
                civilizations.get(playerTurn).getResourcesAmount().put(tempType.getResourcesType(), temp - 1);
        }
        GameController.deleteFromUnfinishedTasks(new Tasks(selectedCity.getMainTile(),
                TaskTypes.CITY_PRODUCTION));
        civilizations.get(playerTurn).putNotification(selectedCity.getName() + ": " +
                tempType + "'s production started",cycle);
    }

    public static int startProducingUnit(String productIcon) {
        UnitType tempType = UnitType.stringToEnum(productIcon);
        if (tempType == null) return 1;
        if (selectedCity == null) return 2;
        if (selectedCity.getCivilization() != civilizations.get(playerTurn)) return 3;
        if (tempType.getResourcesType() != null &&
                (!civilizations.get(playerTurn).getResourcesAmount()
                        .containsKey(tempType.getResourcesType()) ||
                        (civilizations.get(playerTurn).getResourcesAmount()
                                .containsKey(tempType.getResourcesType()) &&
                                civilizations.get(playerTurn)
                                        .getResourcesAmount().get(tempType.getResourcesType()) == 0)))
            return 5;
        if (civilizations.get(playerTurn).doesContainTechnology(tempType.getTechnologyRequired()) != 1)
            return 6;
        startProducingsOperation(tempType);
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


    public static void setMap(Map map) {
        GameController.map = map;
    }

    public static void setSelectedUnit(Unit selectedUnit) {
        GameController.selectedUnit = selectedUnit;
    }

    public static void setSelectedCity(City selectedCity) {
        GameController.selectedCity = selectedCity;
    }

    public static int getCycle() {
        return cycle;
    }
}
