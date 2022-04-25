package model;

import controller.GameController;
import model.Units.Unit;
import model.resources.ResourcesTypes;
import model.technologies.Technology;
import model.technologies.TechnologyType;
import model.tiles.Tile;

import java.util.ArrayList;
import java.util.HashMap;

public class Civilization {
    public static class TileCondition{
        private Tile openedArea;
        private boolean isClear;
        public TileCondition(Tile tile, boolean isClear)
        {
            openedArea = tile;
            this.isClear = isClear;
        }

        public Tile getOpenedArea() {
            return openedArea;
        }
    }
    public TileCondition[][] tileConditions;
    private final User user;


    private final int color;
    private int[][] openedArea = null;

    private int gold;
    private ArrayList<Unit> units = new ArrayList<>();
    private ArrayList<TechnologyType> researches = new ArrayList<>();
    private ArrayList<City> cities = new ArrayList<>();
    private int science;
    private productable producingTechnology;
    private int happiness;
    private HashMap<ResourcesTypes, Integer> resourcesAmount = new HashMap<>();
    private Technology gettingResearchedTechnology;
    private HashMap<ResourcesTypes, Boolean> usedLuxuryResources = new HashMap<>();
    public Civilization(User user,int color)
    {
        this.color = color;
        this.user= user;
        this.gold = 0;
        researches.add(TechnologyType.AGRICULTURE);
    }

    public int getColor() {
        return color;
    }
    public boolean isInTheCivilizationsBorder(Tile tile)
    {
        for (City city : cities) {
            for (Tile cityTile : city.getTiles()) {
                if(cityTile==tile)
                    return true;
            }
        }
        return false;
    }

    public void setGettingResearchedTechnology(Technology gettingResearchedTechnology) {
        this.gettingResearchedTechnology = gettingResearchedTechnology;
    }

    public void addTechnologyToResearches(Technology technology)
    {

    }
    public void turnOffTileConditionsBoolean()
    {
        for(int i=0; i < GameController.getMap().getX();i++)
            for(int j=0; j<GameController.getMap().getY();j++)
                if(tileConditions[i][j]!=null)
                    tileConditions[i][j].isClear=false;
    }
    public City findCityByName(String name)
    {
        return null;
    }

    public City findCityByPosition(int x, int y)
    {
        return null;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public ArrayList<TechnologyType> getResearches() {
        return researches;
    }

    public User getUser() {
        return user;
    }

    public void increaseGold(int gold) {
        this.gold += gold;
    }


    public int getGold() {
        return gold;
    }

    public void startTheTurn()
    {

        turnOffTileConditionsBoolean();
        for(int i = 0 ; i < cities.size();i++)
            cities.get(i).startTheTurn();

        for(int i = 0 ; i < units.size();i++)
            units.get(i).startTheTurn();
    }

    public void endTheTurn()
    {
        for(int i = 0 ; i < units.size();i++)
            units.get(i).endTheTurn();
    }

    public void deleteUnit(Unit unit)
    {

    }

    private void countTheTotalOfCityResources()
    {

    }
    public TileCondition[][] getTileConditions() {
        return tileConditions;
    }
}
