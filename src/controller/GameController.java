package controller;

import model.City;
import model.Civilization;
import model.Map;
import model.Tasks;
import model.Units.Unit;
import model.tiles.Tile;

import java.util.ArrayList;

public class GameController {
    private static ArrayList<Civilization> civilizations;
    private static Unit selectedUnit;
    private static City selectedCity;
    private static Map map;
    private static int startWindowX;
    private static int startWindowY;
    private static int PlayerTurn = 0;
    private static ArrayList<Tasks> unfinishedTasks;

    public static void startGame(String[] PlayersNames) {
        setCivilizations(PlayersNames);
        map = new Map(civilizations);
    }
    public static int setSelectedUnit(int x, int y, boolean isCitizen)
    {

    }

    public static int setSelectedCityByName(String name)
    {

    }


    public static int setSelectedCityByPosition(int x, int y)
    {

    }

    public static int UnitMoveTo(int x, int y)
    {

    }

    public static int UnitSleep()
    {

    }

    public static int UnitAlert()
    {

    }

    public static int UnitFortify(boolean shouldHeal)
    {

    }

    public static int UnitGarrison()
    {

    }

    public static int UnitSetupRanged()
    {

    }

    public static int UnitAttackPosition(int x, int y)
    {

    }
    public static int UnitFoundCity()
    {

    }
    public static int unitCancelMission()
    {

    }


    public static int UnitWake()
    {

    }


    public static int UnitDelete()
    {

    }



    public static int UnitBuildRoad()
    {

    }

    public static int UnitBuildRailRoad()
    {

    }

    public static int UnitRemoveFromTile(int isJungle)
    {

    }
    public static int UnitRepair()
    {

    }

    public static int mapShowPosition(int x, int y)
    {

    }


    public static int mapShowCityName(String name)
    {

    }

    public static int mapMove(int x, int y, String direction)
    {

    }
    private static int nextTurn()
    {

    }

    public static int buyTile(Tile tile)
    {

    }
    private static void setCivilizations(String[] PlayersNames) {

    }

    private static void setTheNumbers() {

    }
    private static boolean canUnitAttack(Unit unit, Tile tile)
    {

    }

    private static boolean canCityAttack(City city, Tile tile)
    {

    }

    private static boolean canUnitMove(City city, Tile tile)
    {

    }





    private static int isGameOver() {
        return true;
    }
}
