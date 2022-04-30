package model;

import controller.GameController;
import model.Units.NonCivilian;
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
    private ArrayList<Technology> researches = new ArrayList<>();
    private ArrayList<City> cities = new ArrayList<>();
    private int science;
    private productable producingTechnology;
    private int happiness = 2;
    private HashMap<ResourcesTypes, Integer> resourcesAmount = new HashMap<>();
    private Technology gettingResearchedTechnology;
    private HashMap<ResourcesTypes, Boolean> usedLuxuryResources = new HashMap<>();
    public Civilization(User user,int color)
    {
        this.color = color;
        this.user= user;
        this.gold = 0;
        researches.add(new Technology(TechnologyType.AGRICULTURE));
        researches.get(0).changeRemainedScienceUntilCompleteTechnology(-researches.get(0).getRemainedScienceUntilCompleteTechnology());
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

    public void changeHappiness(int happiness) {
        this.happiness += happiness;
    }
    //TODO CHECK KARDAN 0 NABOODAN SCIENCE DAR MOHASEBE ROOZ

    public HashMap<ResourcesTypes, Integer> getResourcesAmount() {
        return resourcesAmount;
    }

    public HashMap<ResourcesTypes, Boolean> getUsedLuxuryResources() {
        return usedLuxuryResources;
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

    public ArrayList<Technology> getResearches() {
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
        for (City city : cities) {
            city.getHappiness();
        }
        for (City city : cities) {
            city.startTheTurn();
            science += city.getPopulation();
            gold += city.getGold();
        }
        for (City city : cities) {
            city.collectFood();
        }

        if(cities.size() > 0){
            science += 3;
        }
        for (Unit unit : units) unit.startTheTurn();
        gold -= units.size();
        if(gold < 0){
            if(units.get(0) instanceof NonCivilian)
            units.get(0).getCurrentTile().setNonCivilian(null);
            else units.get(0).getCurrentTile().setCivilian(null);
            units.remove(0);
        }
    }

    public void endTheTurn()
    {
        for(int i = 0 ; i < units.size();i++)
            units.get(i).endTheTurn();
    }

    public void deleteUnit(Unit unit)
    {

    }



    public TileCondition[][] getTileConditions() {
        return tileConditions;
    }

    public boolean canBeTheNextResearch(TechnologyType technologyType)
    {
        for(int i = 0;i<TechnologyType.prerequisites.get(technologyType).size();i++)
            if(!doesContainTechnology(i,technologyType))
                return false;
        return true;
    }
    private boolean doesContainTechnology(int j, TechnologyType technologyType)
    {
        for (Technology research : researches)
            if (research.getTechnologyType() == TechnologyType.prerequisites.get(technologyType).get(j))
                return true;
        return false;
    }

    public int getHappiness() {
        return happiness;
    }
}
