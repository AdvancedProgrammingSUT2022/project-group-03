package com.example.demo.model;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.Units.Unit;
import com.example.demo.model.resources.ResourcesCategory;
import com.example.demo.model.resources.ResourcesTypes;
import com.example.demo.model.technologies.Technology;
import com.example.demo.model.technologies.TechnologyType;
import com.example.demo.model.tiles.Tile;

import java.util.ArrayList;
import java.util.HashMap;

public class Civilization {
    public static class TileCondition {
        private final Tile openedArea;
        private boolean isClear;

        public TileCondition(Tile tile, boolean isClear) {
            openedArea = tile;
            this.isClear = isClear;
        }

        public Tile getOpenedArea() {
            return openedArea;
        }

        public boolean getIsClear() {
            return isClear;
        }
    }

    private TileCondition[][] tileConditions;
    private final User user;

    public void setTileConditions(TileCondition[][] tileConditions) {
        this.tileConditions = tileConditions;
    }

    private final int color;
    private int gold;
    private final ArrayList<Unit> units = new ArrayList<>();
    private final ArrayList<Technology> researches = new ArrayList<>();
    private final ArrayList<City> cities = new ArrayList<>();
    private int happiness;
    private final HashMap<ResourcesTypes, Integer> resourcesAmount = new HashMap<>();
    private Technology gettingResearchedTechnology;
    private HashMap<ResourcesTypes, Boolean> usedLuxuryResources = new HashMap<>();
    public int cheatScience;
    private final HashMap<Integer, ArrayList<String>> notifications = new HashMap<>();

    public Civilization(User user, int color) {
        this.color = color;
        this.user = user;
        this.gold = 0;
        researches.add(new Technology(TechnologyType.AGRICULTURE));
        researches.get(0).changeRemainedCost(-researches.get(0).getRemainedCost());
    }

    public int getColor() {
        return color;
    }

    public boolean isInTheCivilizationsBorder(Tile tile) {
        for (City city : cities)
            for (Tile cityTile : city.getTiles())
                if (GameController.getMap().isInRange(2, cityTile, tile))
                    return true;
        return false;
    }

    public void changeHappiness(int happiness) {
        this.happiness += happiness;
    }


    public HashMap<ResourcesTypes, Boolean> getUsedLuxuryResources() {
        return usedLuxuryResources;
    }

    public Technology getGettingResearchedTechnology() {
        return gettingResearchedTechnology;
    }

    public void setGettingResearchedTechnology(Technology gettingResearchedTechnology) {
        this.gettingResearchedTechnology = gettingResearchedTechnology;
    }

    public void turnOffTileConditionsBoolean() {
        for (int i = 0; i < GameController.getMap().getX(); i++)
            for (int j = 0; j < GameController.getMap().getY(); j++)
                if (tileConditions[i][j] != null)
                    tileConditions[i][j].isClear = false;
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

    public HashMap<ResourcesTypes, Integer> getResourcesAmount() {
        return resourcesAmount;
    }

    public int getGold() {
        return gold;
    }

    public void startTheTurn() {
        happiness = 5;
        happiness -= cities.size();
        turnOffTileConditionsBoolean();
        usedLuxuryResources = new HashMap<>();
        for (City city : cities)
            if (city.getAnxiety() > 0) happiness -= 2;
        for (int i = 0; i < ResourcesTypes.VALUES.size(); i++)
            if (ResourcesTypes.VALUES.get(i).getResourcesCategory() == ResourcesCategory.LUXURY &&
                    resourcesAmount.containsKey(ResourcesTypes.VALUES.get(i))) {
                resourcesAmount.remove(ResourcesTypes.VALUES.get(i));
                resourcesAmount.put(ResourcesTypes.VALUES.get(i), 0);
            }
        for (City city : cities)
            city.collectResources(resourcesAmount);
        for (City city : cities) {
            city.startTheTurn();
            gold += city.getGold();
        }
        for (City city : cities)
            city.collectFood();
        int science = collectScience();
        for (Unit unit : units) unit.startTheTurn();
        gold -= units.size();
        if (gold < 0)
            gold = 0;
        if (gettingResearchedTechnology != null) {
            getGettingResearchedTechnology().changeRemainedCost(-science);
            if (gettingResearchedTechnology.getRemainedCost() <= 0) {
                gettingResearchedTechnology.setRemainedCost(0);
                GameController.getCivilizations().get(GameController.getPlayerTurn())
                        .putNotification(gettingResearchedTechnology.getName() +
                                "'s production ended successfully", GameController.getCycle());
                gettingResearchedTechnology = null;
            }
        }
    }

    public int collectScience() {
        int returner = 0;
        for (City city : cities)
            returner += city.getPopulation();
        for (City city : cities)
            if (city.isCapital) returner += 3;
        if (gold == 0) {
            int temp = 0;
            for (City city : cities)
                temp += city.getGold();
            if (temp < 0) returner += temp;
        }
        return returner + cheatScience;
    }

    public void endTheTurn() {
        //using
        for (Unit unit : units) unit.endTheTurn();
    }

    public TileCondition[][] getTileConditions() {
        return tileConditions;
    }

    public boolean canBeTheNextResearch(TechnologyType technologyType) {
        if (doesContainTechnology(technologyType) != 3) return false;
        for (int i = 0; i < TechnologyType.prerequisites.get(technologyType).size(); i++)
            if (!canTechnologyBeAchievedNext(i, technologyType)) return false;
        return true;
    }

    private boolean canTechnologyBeAchievedNext(int j, TechnologyType technologyType) {
        for (Technology research : researches)
            if (research.getTechnologyType() == TechnologyType.prerequisites.get(technologyType).get(j))
                return true;
        return false;
    }

    public int getHappiness() {
        return happiness;
    }

    public int doesContainTechnology(TechnologyType technologyType) {
        if (technologyType == null) return 1;
        for (Technology research : researches)
            if (research.getTechnologyType() == technologyType) {
                if (research.getRemainedCost() == 0)
                    return 1;
                return 2;
            }
        return 3;
    }

    public City getCapital() {
        for (City city : cities)
            if (city.isCapital)
                return city;
        return null;
    }

    public int getSize() {
        int size = 0;
        for (City city : cities)
            size += city.getTiles().size();
        return size;
    }

    public boolean areTechnologiesFinished() {
        if (researches.size() != TechnologyType.values().length)
            return false;
        for (Technology research : researches)
            if (research.getRemainedCost() != 0)
                return false;
        return true;
    }

    public void putNotification(String string, int cycle) {
        if (!notifications.containsKey(cycle)) {
            ArrayList<String> strings = new ArrayList<>();
            notifications.put(cycle, strings);
        }
        notifications.get(cycle).add(string);
    }

    public HashMap<Integer, ArrayList<String>> getNotifications() {
        return notifications;
    }
}
