package model;

import model.Units.Unit;
import model.resources.ResourcesTypes;
import model.technologies.Technology;
import model.technologies.TechnologyType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Civilization {
    private final User user;
    private int[][] openedArea = null;
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

    public void setOpenedArea(int x, int y) {
        this.openedArea = new int[x][y];
    }

    public int[][] getOpenedArea() {
        return openedArea;
    }

    public int getGold() {
        return gold;
    }

    public void SetTheNumbers()
    {

    }

    public void deleteUnit(Unit unit)
    {

    }

    private void countTheTotalOfCityResources()
    {

    }
}
