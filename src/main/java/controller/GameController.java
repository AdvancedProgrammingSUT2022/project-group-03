package controller;

import model.*;
import model.Units.Civilian;
import model.Units.Settler;
import model.Units.Unit;
import model.tiles.Tile;
import model.tiles.TileType;

import java.util.ArrayList;
import java.util.Objects;

public class GameController {
    private static ArrayList<Civilization> civilizations = new ArrayList<>();
    private static Unit selectedUnit;
    private static City selectedCity;
    private static Map map;
    private static int startWindowX = 2;
    private static int startWindowY = 2;
    private static int playerTurn = 0;
    private static ArrayList<Tasks> unfinishedTasks = new ArrayList<>();

    public static void startGame(ArrayList<User> PlayersNames) {
        setCivilizations(PlayersNames);
        map = new Map(civilizations);
        for(int i = 0 ; i <PlayersNames.size(); i++)
            civilizations.get(i).tileConditions = new Civilization.TileCondition[map.getX()][map.getY()];
        //HARDCODE
        Civilian hardcodeUnit = new Settler(map.coordinatesToTile(5,5),civilizations.get(0));
        civilizations.get(0).getUnits().add(hardcodeUnit);
        map.coordinatesToTile(5,5).setCivilian(hardcodeUnit);







        //
    }

    public static boolean setSelectedCombatUnit(int x, int y) {
        if(map.coordinatesToTile(x,y).getNonCivilian()==null ||
                map.coordinatesToTile(x,y).getNonCivilian().getCivilization() != civilizations.get(playerTurn))
            return false;
        selectedUnit = map.coordinatesToTile(x,y).getNonCivilian();
        return true;
    }
    public static boolean setSelectedNonCombatUnit(int x, int y) {
        if(map.coordinatesToTile(x,y).getCivilian()==null ||
                map.coordinatesToTile(x,y).getCivilian().getCivilization() != civilizations.get(playerTurn))
            return false;
        selectedUnit = map.coordinatesToTile(x,y).getCivilian();
        return true;
    }


    public static int setSelectedCityByName(String name) {
        return 0;
    }


    public static int setSelectedCityByPosition(int x, int y) {
        return 0;
    }

    public static boolean UnitMoveTo(int x, int y) {
        if(selectedUnit==null ||
                map.coordinatesToTile(x,y).getTileType()== TileType.OCEAN ||
                map.coordinatesToTile(x,y).getTileType()== TileType.MOUNTAIN)
            return false;
        return selectedUnit.move(map.coordinatesToTile(x,y));
    }

    public static int UnitSleep() {
        return 0;
    }

    public static int UnitAlert() {
        return 0;
    }

    public static int UnitFortify(boolean shouldHeal) {
        return 0;
    }

    public static int UnitGarrison() {
        return 0;
    }

    public static int UnitSetupRanged() {
        return 0;
    }

    public static int UnitAttackPosition(int x, int y) {
        return 0;
    }

    public static int UnitFoundCity() {
        if(!(selectedUnit instanceof Settler))
           return 1;
        if(selectedUnit.getCurrentTile().getCity()!=null)
            return 2;
        for(int i = 0 ; i< civilizations.size();i++)
            if(civilizations.get(i).isInTheCivilizationsBorder(selectedUnit.getCurrentTile()))
                return 2;
        ((Settler) selectedUnit).city();
        return 0;
    }

    public static int unitCancelMission() {
        return 0;
    }


    public static int UnitWake() {
        return 0;
    }


    public static int UnitDelete() {
        return 0;
    }


    public static int UnitBuildRoad() {
        return 0;
    }

    public static int UnitBuildRailRoad() {
        return 0;
    }

    public static int UnitRemoveFromTile(int isJungle) {
        return 0;
    }

    public static int UnitRepair() {
        return 0;
    }

    public static int mapShowPosition(int x, int y) {
        return 0;
    }


    public static int mapShowCityName(String name) {
        return 0;
    }

    public static void mapMove(int number, String direction) {
        if(Objects.equals(direction, "R"))
            startWindowY+=number;
        if(Objects.equals(direction, "L"))
            startWindowY-=number;
        if(Objects.equals(direction, "U"))
            startWindowX-=number;
        if(Objects.equals(direction, "D"))
            startWindowX+=number;
        if(startWindowY>map.getY() - 14)
            startWindowY = map.getY() - 14;
        if(startWindowX>map.getX() - 5)
            startWindowX = map.getX() - 5;
        if(startWindowY<0)
            startWindowY =0;
        if(startWindowX<0)
            startWindowX=0;
    }


    public static int buyTile(Tile tile) {
        return 0;
    }

    private static void setCivilizations(ArrayList<User> users) {
        for (int i = 0; i < users.size(); i++)
            civilizations.add(new Civilization(users.get(i),i));
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

    public static boolean nextTurn()
    {
        if(unfinishedTasks.size()!=0)
            return false;

        civilizations.get(playerTurn).endTheTurn();
        playerTurn = (playerTurn +1)%civilizations.size();
        setUnfinishedTasks();
        civilizations.get(playerTurn).startTheTurn();




        return true;
    }

    public static void setUnfinishedTasks() {

    }

    public static void cheatSettler(int x, int y)
    {
        Civilian hardcodeUnit = new Settler(map.coordinatesToTile(x,y),civilizations.get(playerTurn));
        civilizations.get(playerTurn).getUnits().add(hardcodeUnit);
        map.coordinatesToTile(x,y).setCivilian(hardcodeUnit);
    }
    public static Map getMap() {
        return map;
    }

    public static String printMap()
    {
        return map.printMap(civilizations.get(playerTurn).tileConditions,startWindowX, startWindowY);
    }
}
