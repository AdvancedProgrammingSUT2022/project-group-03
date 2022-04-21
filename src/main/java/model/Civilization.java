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
    }
    public TileCondition[][] tileConditions;
    private final User user;

    private int gold;
    private ArrayList<Unit> units = new ArrayList<>();
    private ArrayList<TechnologyType> researches;
    private ArrayList<City> cities;
    private int science;
    private int happiness;
    private HashMap<ResourcesTypes, Integer> resourcesAmount;
    private Technology gettingResearchedTechnology;
    private HashMap<ResourcesTypes, Boolean> usedLuxuryResources;
    public Civilization(User user)
    {
        this.user= user;
        this.gold = 0;
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

    public void setGold(int gold) {
        this.gold = gold;
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
}
