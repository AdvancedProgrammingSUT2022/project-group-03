package controller;

import model.City;
import model.Civilization;
import model.Map;
import model.Tasks;
import model.Units.Unit;
import model.Units.NonCivilianUnitType;
import model.tiles.Tile;

import java.util.ArrayList;

public class GameController {
    private ArrayList<Civilization> civilizations;
    private Unit selectedUnit;
    private City selectedCity;
    private Map map;
    private int startWindowX;
    private int startWindowY;
    private int PlayerTurn = 0;
    private ArrayList<Tasks> unfinishedTasks;

    private void startGame(String[] PlayersNames) {
        setCivilizations(PlayersNames);
        map = new Map(civilizations);
    }

    public void setCivilizations(String[] PlayersNames) {

    }

    private void setTheNumbers() {

    }
    private boolean canUnitAttack(Unit unit, Tile tile)
    {

    }

    private boolean canCityAttack(City city, Tile tile)
    {

    }

    private boolean canUnitMove(City city, Tile tile)
    {

    }

    public int setSelectedUnit(int x, int y, boolean isCitizen)
    {

    }

    public int setSelectedCityByName(String name)
    {

    }


    public int setSelectedCityByPosition(String name)
    {

    }

    public int UnitMoveTo(int x, int y)
    {

    }

    public int UnitSleep()
    {

    }

    public int UnitAlert()
    {

    }

    public int UnitFortify(boolean shouldHeal)
    {

    }

    public int UnitGarrison()
    {

    }

    public int UnitSetupRanged()
    {

    }

    public int UnitAttackPosition(int x, int y)
    {

    }
    public int UnitFoundCity()
    {

    }
    public int unitCancelMission()
    {

    }


    public int UnitWake()
    {

    }


    public int UnitDelete()
    {

    }



    public int UnitBuildRoad()
    {

    }

    public int UnitBuildRailRoad()
    {

    }

    public int UnitRemoveFromTile(int isJungle)
    {

    }
    public int UnitRepair()
    {

    }

    public int mapShowPosition(int x, int y)
    {

    }

    public int mapShowCityName(String name)
    {

    }

    public int mapMove(int x, int y, String direction)
    {

    }
    private int nextTurn()
    {

    }

    public int buyTile(Tile tile)
    {

    }

    private int isGameOver() {
        return true;
    }
}
