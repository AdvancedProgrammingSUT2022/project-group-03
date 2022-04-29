package controller;

import model.*;
import model.Units.Settler;
import model.Units.Unit;
import model.Units.UnitType;
import model.technologies.Technology;
import model.tiles.Tile;
import model.tiles.TileType;

import java.util.ArrayList;
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
            civilizations.get(i).tileConditions = new Civilization.TileCondition[map.getX()][map.getY()];
        //HARDCODE
        Settler hardcodeUnit = new Settler(map.coordinatesToTile(2,5),civilizations.get(0),UnitType.Settler);
        civilizations.get(0).getUnits().add(hardcodeUnit);
        map.coordinatesToTile(2, 5).setCivilian(hardcodeUnit);


        //
    }

    public static City getSelectedCity() {
        return selectedCity;
    }

    public static boolean setSelectedCombatUnit(int x, int y) {
        if (map.coordinatesToTile(x, y).getNonCivilian() == null ||
                map.coordinatesToTile(x, y).getNonCivilian().getCivilization() != civilizations.get(playerTurn))
            return false;
        selectedUnit = map.coordinatesToTile(x, y).getNonCivilian();
        return true;
    }

    public static boolean setSelectedNonCombatUnit(int x, int y) {
        if (map.coordinatesToTile(x, y).getCivilian() == null ||
                map.coordinatesToTile(x, y).getCivilian().getCivilization() != civilizations.get(playerTurn))
            return false;
        selectedUnit = map.coordinatesToTile(x, y).getCivilian();
        return true;
    }

    private static City nameToCity(String name)
    {
        for (Civilization civilization : civilizations)
            for (int j = 0; j < civilization.getCities().size(); j++)
                if (civilization.getCities().get(j).getName().equals(name))
                    return civilization.getCities().get(j);
        return null;
    }

    public static boolean setSelectedCityByName(String name) {
        City tempCity = nameToCity(name);
        if(tempCity==null)
            return false;
        selectedCity = tempCity;
        return true;
    }


    public static boolean setSelectedCityByPosition(int x, int y) {
        if(map.coordinatesToTile(x,y).getCity()!=null)
        {
            selectedCity = map.coordinatesToTile(x,y).getCity();
            return true;
        }
        return false;
    }

    public static boolean unitMoveTo(int x, int y) {
        if (selectedUnit == null ||
                map.coordinatesToTile(x, y).getTileType() == TileType.OCEAN ||
                map.coordinatesToTile(x, y).getTileType() == TileType.MOUNTAIN)
            return false;
        return selectedUnit.move(map.coordinatesToTile(x, y));
    }

    public static int unitSleep() {
        if(selectedUnit==null)
            return 1;
        if(selectedUnit.getCivilization()==civilizations.get(playerTurn))
        {
            selectedUnit.setIsSleep(true);
            return 2;
        }
        return 0;
    }

    public static int unitAlert() {
        return 0;
    }

    public static int unitFortify(boolean shouldHeal) {
        return 0;
    }

    public static int unitGarrison() {
        return 0;
    }

    public static int unitSetupRanged() {
        return 0;
    }

    public static int unitAttackPosition(int x, int y) {
        return 0;
    }

    public static int unitFoundCity() {
        if (!(selectedUnit instanceof Settler))
            return 1;
        if (selectedUnit.getCurrentTile().getCity() != null)
            return 2;
        for (int i = 0; i < civilizations.size(); i++)
            if (civilizations.get(i).isInTheCivilizationsBorder(selectedUnit.getCurrentTile()))
                return 2;
        ((Settler) selectedUnit).city();
        return 0;
    }

    public static int unitCancelMission() {
        return 0;
    }


    public static int unitWake() {
        return 0;
    }


    public static int unitDelete() {
        return 0;
    }


    public static int unitBuildRoad() {
        return 0;
    }

    public static int unitBuildRailRoad() {
        return 0;
    }

    public static int unitRemoveFromTile(int isJungle) {
        return 0;
    }

    public static int unitRepair() {
        return 0;
    }

    public static boolean mapShowPosition(int x, int y) {
        startWindowX = x;
        startWindowY = y;
        boolean returner = true;
        if (startWindowY > map.getY() - 13)
        {
            startWindowY = map.getY() - 13;
            returner = false;
        }
        if (startWindowX > map.getX() - 4)
        {
            startWindowX = map.getX() - 4;
            returner = false;
        }
        if (startWindowY < 0)
        {
            startWindowY = 0;
            returner = false;
        }
        if (startWindowX < 0)
        {
            startWindowX = 0;
            returner = false;
        }
        return returner;
    }


    public static int mapShowCityName(String name) {
        City tempCity = nameToCity(name);
        if(tempCity==null)
            return 1;
        if(civilizations.get(playerTurn).tileConditions[tempCity.getMainTile().getX()][tempCity.getMainTile().getY()]==null)
            return 2;
        mapShowPosition(tempCity.getMainTile().getX(),tempCity.getMainTile().getY());
        return 0;
    }

    public static boolean mapMove(int number, String direction) {
        if (Objects.equals(direction, "R"))
            return mapShowPosition(startWindowX,startWindowY+number);
        if (Objects.equals(direction, "L"))
            return mapShowPosition(startWindowX,startWindowY-number);
        if (Objects.equals(direction, "U"))
            return mapShowPosition(startWindowX-number,startWindowY);
        else
            return mapShowPosition(startWindowX+number,startWindowY);
    }


    public static int buyTile(Tile tile) {
        return 0;
    }

    private static void setCivilizations(ArrayList<User> users) {
        for (int i = 0; i < users.size(); i++)
            civilizations.add(new Civilization(users.get(i), i));
    }


    private static boolean canUnitAttack(Unit unit, Tile tile) {
        return true;
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

    public static boolean nextTurnIfYouCan()
    {
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

    }

    public static void cheatSettler(int x, int y) {
        Settler hardcodeUnit = new Settler(map.coordinatesToTile(x, y), civilizations.get(playerTurn), UnitType.Settler);
        civilizations.get(playerTurn).getUnits().add(hardcodeUnit);
        map.coordinatesToTile(x, y).setCivilian(hardcodeUnit);
    }

    public static Map getMap() {
        return map;
    }

    public static String printMap() {
        return map.printMap(civilizations.get(playerTurn).tileConditions, startWindowX, startWindowY);
    }

    public static Unit getSelectedUnit() {
        return selectedUnit;
    }

    public static int startProducing(String productIcon) {
        UnitType tempType = UnitType.valueOf(productIcon);
        if (productIcon == null)
            return 1;
        if (!selectedCity.createUnit(tempType))
            return 2;
        return 0;
    }

    public static ArrayList<Civilization> getCivilizations() {
        return civilizations;
    }
    public static ArrayList<Technology> getCivilizationsResearches()
    {
        return civilizations.get(playerTurn).getResearches();
    }

    public static int getPlayerTurn() {
        return playerTurn;
    }
}
