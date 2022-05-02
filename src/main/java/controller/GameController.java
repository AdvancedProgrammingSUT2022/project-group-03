package controller;

import model.*;
import model.Units.*;
import model.features.FeatureType;
import model.improvements.ImprovementType;
import model.technologies.Technology;
import model.technologies.TechnologyType;
import model.tiles.Tile;
import model.tiles.TileType;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class GameController {
    private static ArrayList<Civilization> civilizations = new ArrayList<>();
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
            civilizations.get(i).setTileConditions(new Civilization.TileCondition[map.getX()][map.getY()]);
        //HARDCODE
        NonCivilian unit = new NonCivilian(map.coordinatesToTile(3,3),civilizations.get(0),UnitType.PANZER);
        civilizations.get(0).getUnits().add(unit);
        map.coordinatesToTile(3,3).setNonCivilian(unit);
         unit = new NonCivilian(map.coordinatesToTile(3,4),civilizations.get(1),UnitType.WARRIOR);
        civilizations.get(1).getUnits().add(unit);
        map.coordinatesToTile(3,4).setNonCivilian(unit);
        //
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
                if (civilization.getCities().get(j).getName().toLowerCase(Locale.ROOT).equals(name.toLowerCase(Locale.ROOT)))
                    return civilization.getCities().get(j);
        return null;
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
    public static void deleteFromUnfinishedTasks(Tasks tasks)
    {
        for(int i =0;i<unfinishedTasks.size();i++)
        {
            if(tasks.getTaskTypes() == unfinishedTasks.get(i).getTaskTypes() &&
                tasks.getTile() == unfinishedTasks.get(i).getTile())
            {
                unfinishedTasks.remove(i);
                return;
            }
        }
    }
    public static boolean unitMoveTo(int x, int y) {
        if (selectedUnit == null || x < 0 || y < 0 || x >= map.getX() || y > map.getY() ||
                map.coordinatesToTile(x, y).getTileType() == TileType.OCEAN ||
                map.coordinatesToTile(x, y).getTileType() == TileType.MOUNTAIN)
            return false;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(),TaskTypes.UNIT));
        selectedUnit.setState(UnitState.AWAKE);
        return selectedUnit.move(map.coordinatesToTile(x, y),true);
    }

    public static int unitSleep() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(),TaskTypes.UNIT));
        selectedUnit.setState(UnitState.SLEEP);
        return 0;
    }

    public static int unitAlert() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(),TaskTypes.UNIT));
        selectedUnit.setState(UnitState.ALERT);
        if(openNewArea(selectedUnit.getCurrentTile(),civilizations.get(playerTurn),selectedUnit))
            return 3;
        return 0;
    }

    public static int unitFortify() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(),TaskTypes.UNIT));
        selectedUnit.setState(UnitState.FORTIFY);
        return 0;
    }
    public static int unitFortifyUntilFullHealth() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(),TaskTypes.UNIT));
        selectedUnit.setState(UnitState.FORTIFY_UNTIL_FULL_HEALTH);
        return 0;
    }

    public static int unitGarrison() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(),TaskTypes.UNIT));
        selectedUnit.setState(UnitState.GARRISON);
        return 0;
    }

    public static int unitSetupRanged() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(),TaskTypes.UNIT));
        return 0;
    }

    public static int unitAttackPosition(int x, int y) {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(),TaskTypes.UNIT));
        return 0;
    }

    public static int unitFoundCity() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        if (!(selectedUnit instanceof Settler))
            return 3;
        if (selectedUnit.getCurrentTile().getCity() != null)
            return 4;
        for (Civilization civilization : civilizations)
            if (civilization.isInTheCivilizationsBorder(selectedUnit.getCurrentTile()))
                return 4;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(),TaskTypes.UNIT));
        ((Settler) selectedUnit).city();
        civilizations.get(playerTurn).changeHappiness(-1);
        unitDelete(selectedUnit);
        openNewArea(selectedUnit.getCurrentTile(),civilizations.get(playerTurn),null);
        selectedUnit=null;
        return 0;
    }

    public static int unitCancelMission() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        if(selectedUnit.getDestinationTile()==null && selectedUnit.getState()==UnitState.AWAKE)
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
        unfinishedTasks.add(new Tasks(selectedUnit.getCurrentTile(),TaskTypes.UNIT));
        selectedUnit.setState(UnitState.AWAKE);
        return 0;
    }


    public static int unitDelete(Unit unit) {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(),TaskTypes.UNIT));
        civilizations.get(playerTurn).getUnits().remove(unit);
        if(unit instanceof NonCivilian)
            unit.getCurrentTile().setNonCivilian(null);
        else
            unit.getCurrentTile().setCivilian(null);
        return 0;
    }


    public static int unitBuild(ImprovementType improvementType)
    {
        if(selectedUnit==null)
            return 1;
        if(selectedUnit.getCivilization()!=civilizations.get(playerTurn))
            return 2;
        if(selectedUnit.getUnitType()!=UnitType.WORKER)
            return 3;
        if(!civilizations.get(playerTurn).doesContainTechnology(improvementType.prerequisitesTechnologies))
            return 4;
        if(!canHaveTheImprovement(selectedUnit.getCurrentTile(),improvementType))
            return 5;
        if(!doesHaveTheRequiredTechnologyToBuildImprovement(improvementType,selectedUnit))
            return 6;
        ((Worker) selectedUnit).buildImprovement(improvementType);
        return 0;
    }

    private static boolean doesHaveTheRequiredTechnologyToBuildImprovement(ImprovementType improvementType,Unit unit)
    {
        if(improvementType==ImprovementType.FARM && (unit.getCurrentTile().getContainedFeature().getFeatureType()==FeatureType.DENSEFOREST &&
                !unit.getCivilization().doesContainTechnology(TechnologyType.BRONZE_WORKING)))
            return false;
        if(improvementType==ImprovementType.QUARRY && selectedUnit.getCurrentTile().getContainedFeature().getFeatureType()==FeatureType.SWAMP &&
                !selectedUnit.getCivilization().doesContainTechnology(TechnologyType.MASONRY))
            return false;
        if(selectedUnit.getCurrentTile().getContainedFeature().getFeatureType()==FeatureType.SWAMP &&
                !selectedUnit.getCivilization().doesContainTechnology(TechnologyType.MASONRY))
            return false;
        if(improvementType==ImprovementType.FARM && unit.getCurrentTile().getContainedFeature().getFeatureType()==FeatureType.FOREST &&
                !unit.getCivilization().doesContainTechnology(TechnologyType.MINING))
                return false;
        return true;
    }

    public static int unitBuildRoad() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(),TaskTypes.UNIT));
        return 0;
    }
    private static boolean canHaveTheImprovement(Tile tile, ImprovementType improvementType)
    {
        if(!TileType.canHaveTheImprovement(tile.getTileType(), improvementType))
            return false;
        if(tile.getContainedFeature()!=null && !FeatureType.canHaveTheImprovement(tile.getContainedFeature().getFeatureType(), improvementType))
            return false;
        return true;
    }
    public static int unitBuildRailRoad() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(),TaskTypes.UNIT));
        return 0;
    }

    public static int unitRemoveFromTile(boolean isJungle) {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        if(isJungle && selectedUnit.getCurrentTile().getContainedFeature().getFeatureType()!= FeatureType.DENSEFOREST && selectedUnit.getCurrentTile().getContainedFeature().getFeatureType()!=FeatureType.FOREST)
            return 3;
        //TODO if(!isJungle && notroad && notrailroad) {....}
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(),TaskTypes.UNIT));
        if(isJungle)
            ((Worker) selectedUnit).remove(1);
        return 0;
    }

    public static int unitRepair() {
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilizations.get(playerTurn))
            return 2;
        deleteFromUnfinishedTasks(new Tasks(selectedUnit.getCurrentTile(),TaskTypes.UNIT));
        if(selectedUnit.getCurrentTile().getImprovement()==null)
            return 3;
        if(selectedUnit.getCurrentTile().getImprovement().getNeedsRepair()==0)
            return 4;
        ((Worker) selectedUnit).repair();
        return 0;
    }

    public static void mapShowPosition(int x, int y) {
        startWindowX = x;
        startWindowY = y;
        if (startWindowY > map.getY() - (Map.WINDOW_Y-1))
            startWindowY = map.getY() - (Map.WINDOW_Y-1);
        if (startWindowX > map.getX() - (Map.WINDOW_X-1))
            startWindowX = map.getX() - (Map.WINDOW_X-1);
        if (startWindowY < 0)
            startWindowY = 0;
        if (startWindowX < 0)
            startWindowX = 0;
    }


    public static int mapShowCityName(String name) {
        City tempCity = nameToCity(name);
        if (tempCity == null)
            return 1;
        if (civilizations.get(playerTurn).getTileConditions()[tempCity.getMainTile().getX()][tempCity.getMainTile().getY()] == null)
            return 2;
        mapShowPosition(tempCity.getMainTile().getX() - Map.WINDOW_X/2, tempCity.getMainTile().getY() - Map.WINDOW_Y/2 + 1);
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


    public static int buyTile(Tile tile) {
        return 0;
    }

    private static void setCivilizations(ArrayList<User> users) {
        for (int i = 0; i < users.size(); i++)
            civilizations.add(new Civilization(users.get(i), i));
    }


    private static boolean canUnitAttack(Tile tile) {
        if(tile.getCity() != null && tile.getCity().getCivilization() != civilizations.get(playerTurn)){
            selectedUnit.setState(UnitState.ATTACK);
            return true;
        }

        if(tile.getNonCivilian() != null && tile.getNonCivilian().getCivilization() != civilizations.get(playerTurn)){
            selectedUnit.setState(UnitState.ATTACK);
            return true;
        }
        if( tile.getCivilian() != null && tile.getCivilian().getCivilization() != civilizations.get(playerTurn) && selectedUnit.getUnitType().range > 1){
            selectedUnit.setState(UnitState.ATTACK);
            return true;
        }
        return false;
    }

    private static boolean canCityAttack(City city, Tile tile) {
        return true;
    }

    private static boolean canUnitMove(City city, Tile tile) {
        return true;
    }

    private static int isGameOver() {
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
        setUnfinishedTasks();
        civilizations.get(playerTurn).startTheTurn();
        selectedCity = null;
        selectedUnit = null;
    }

    public static void setUnfinishedTasks() {
        unfinishedTasks = new ArrayList<>();
        for(int i = 0 ; i <civilizations.get(playerTurn).getUnits().size();i++)
            if(civilizations.get(playerTurn).getUnits().get(i).getState()==UnitState.AWAKE)
                unfinishedTasks.add(new Tasks(civilizations.get(playerTurn).getUnits().get(i).getCurrentTile(),TaskTypes.UNIT));
        for(int i = 0;i<civilizations.get(playerTurn).getCities().size();i++)
            if(civilizations.get(playerTurn).getCities().get(i).getProduct()==null)
                unfinishedTasks.add(new Tasks(civilizations.get(playerTurn).getCities().get(i).getMainTile(),TaskTypes.CITY_PRODUCTION));
        if(civilizations.get(playerTurn).getCities().size()!=0 &&
                civilizations.get(playerTurn).getGettingResearchedTechnology()==null)
            unfinishedTasks.add(new Tasks(null,TaskTypes.TECHNOLOGY_PROJECT));
    }

    public static void cheatSettler(int x, int y) {
        Settler hardcodeUnit = new Settler(map.coordinatesToTile(x, y), civilizations.get(playerTurn), UnitType.SETTLER);
        civilizations.get(playerTurn).getUnits().add(hardcodeUnit);
        map.coordinatesToTile(x, y).setCivilian(hardcodeUnit);
        openNewArea(map.coordinatesToTile(x, y),civilizations.get(playerTurn),hardcodeUnit);
    }
    public static boolean openNewArea(Tile tile, Civilization civilization, Unit unit) {
        boolean isThereAnyEnemy = false;
        for (int i = 0; i < 6; i++) {
            if(tile.getNeighbours(i)==null)
                continue;
            civilization.getTileConditions()[tile.getNeighbours(i).getX()][tile.getNeighbours(i).getY()] =
                    new Civilization.TileCondition(tile.getNeighbours(i).
                            CloneTileForCivilization(civilization.getResearches()), true);
            if(unit!=null && ((tile.getNeighbours(i).getCivilian()!=null && tile.getNeighbours(i).getCivilian().getCivilization()!=civilization) ||
                    (tile.getNeighbours(i).getNonCivilian()!=null && tile.getNeighbours(i).getNonCivilian().getCivilization()!=civilization)))
            {
                if(unit.getState()==UnitState.ALERT)
                    unit.setState(UnitState.AWAKE);
                isThereAnyEnemy=true;
            }
            if (tile.getNeighbours(i).getTileType() == TileType.MOUNTAIN ||
                    tile.getNeighbours(i).getTileType() == TileType.HILL ||
                    (tile.getNeighbours(i).getContainedFeature() != null &&
                            (tile.getNeighbours(i).getContainedFeature().getFeatureType() == FeatureType.FOREST ||
                            tile.getNeighbours(i).getContainedFeature().getFeatureType() == FeatureType.DENSEFOREST)))
                continue;
            for (int j = 0; j < 6; j++) {
                if(tile.getNeighbours(i).getNeighbours(j)==null)
                    continue;
                int neighbourX = tile.getNeighbours(i).getNeighbours(j).getX();
                int neighbourY = tile.getNeighbours(i).getNeighbours(j).getY();
                civilization.getTileConditions()[neighbourX][neighbourY] =
                        new Civilization.TileCondition(tile.getNeighbours(i).getNeighbours(j)
                                .CloneTileForCivilization(civilization.getResearches()), true);
                if(unit!=null && ((tile.getNeighbours(i).getNeighbours(j).getCivilian()!=null && tile.getNeighbours(i).getNeighbours(j).getCivilian().getCivilization()!=civilization) ||
                        (tile.getNeighbours(i).getNeighbours(j).getNonCivilian()!=null && tile.getNeighbours(i).getNeighbours(j).getNonCivilian().getCivilization()!=civilization)))
                {
                    if(unit.getState()==UnitState.ALERT)
                        unit.setState(UnitState.AWAKE);
                    isThereAnyEnemy=true;
                }

            }
        }
        civilization.getTileConditions()[tile.getX()][tile.getY()] =
                new Civilization.TileCondition(tile.
                CloneTileForCivilization(civilization.getResearches()), true);
        return isThereAnyEnemy;
    }
    public static Map getMap() {
        return map;
    }

    public static String printMap() {
        return map.printMap(civilizations.get(playerTurn).getTileConditions(), startWindowX, startWindowY);
    }

    public static Unit getSelectedUnit() {
        return selectedUnit;
    }

    public static int startProducing(String productIcon) {
        UnitType tempType = UnitType.stringToEnum(productIcon);
        if (tempType == null)
            return 1;
        if(selectedCity==null)
            return 2;
        if(selectedCity.getCivilization()!=civilizations.get(playerTurn))
            return 3;
        if (!selectedCity.createUnit(tempType))
            return 4;
        return 0;
    }

    public static ArrayList<Civilization> getCivilizations() {
        return civilizations;
    }

    public static ArrayList<Technology> getCivilizationsResearches() {
        return civilizations.get(playerTurn).getResearches();
    }

    public static int getPlayerTurn() {
        return playerTurn;
    }

    public static ArrayList<Tasks> getUnfinishedTasks() {
        return unfinishedTasks;
    }

    public static int unitAttack(int x,int y){
        if(!(selectedUnit instanceof NonCivilian) || selectedUnit.getCivilization() != civilizations.get(playerTurn)) return 2;
        if(x < 0 || y < 0 || x >= map.getX() || y > map.getY()) return 3;
        if(!canUnitAttack(map.coordinatesToTile(x,y))) return 1;
        if(selectedUnit.move(map.coordinatesToTile(x,y),true)) return 0;
        return 4;
    }
}
