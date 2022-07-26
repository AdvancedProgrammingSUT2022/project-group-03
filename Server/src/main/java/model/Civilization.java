package model;

import controller.gameController.GameController;
import model.Units.Unit;
import model.building.Building;
import model.building.BuildingType;
import model.features.FeatureType;
import model.resources.ResourcesCategory;
import model.resources.ResourcesTypes;
import model.technologies.Technology;
import model.technologies.TechnologyType;
import model.tiles.Tile;
import network.MySocketHandler;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.SocketHandler;

public class Civilization implements Serializable {
    public static class TileCondition implements Serializable {
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

    //-1 war
    // 0 neutral
    // 1 peace
    private final ArrayList<Map.Entry<Civilization, Integer>> knownCivilizations = new ArrayList<>();

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
    private final ArrayList<Tile> noFogs = new ArrayList<>();
    private City mainCapital = null;
    private final ArrayList<TradeRequest> tradeRequests = new ArrayList<>();
    private final ArrayList<Civilization> friendshipRequests = new ArrayList<>();

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

    public boolean isInTheCivilizationsBorder(Tile tile,GameController gameController) {
        for (City city : cities)
            for (Tile cityTile : city.getTiles())
                if (gameController.getMap().isInRange(2, cityTile, tile))
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

    public void turnOffTileConditionsBoolean(GameController gameController, MySocketHandler socketHandler) {
        for (int i = 0; i < gameController.getMap().getX(); i++)
            for (int j = 0; j < gameController.getMap().getY(); j++)
                if (tileConditions[i][j] != null) {
                    tileConditions[i][j].isClear = false;
                    tileConditions[i][j].getOpenedArea().setCivilian(null,socketHandler);
                    tileConditions[i][j].getOpenedArea().setNonCivilian(null,socketHandler);
                }
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

    public boolean isCivilizationAlive() {
        return cities.size() != 0 || units.size() != 0;
    }

    public int getScore(GameController gameController) {
        if (!isCivilizationAlive())
            return 0;
        int numberOfTiles = 0;
        for (City city : cities) {
            numberOfTiles += city.getTiles().size();
        }
        int numberOfPopulation = 0;
        for (City city : cities) {
            numberOfPopulation += city.getPopulation();
        }
        int numberOfFullResearches = 0;
        for (Technology research : researches) {
            if (research.getRemainedCost() == 0)
                numberOfFullResearches++;
        }
        int numberOfBuildings = 0;
        for (City city : cities) {
            for (Building building : city.getBuildings()) {
                if (building.getRemainedCost() == 0)
                    numberOfBuildings++;
            }
        }
        return (numberOfTiles * 10 +
                cities.size() * 15 +
                numberOfPopulation * 20 +
                numberOfFullResearches * 25 +
                numberOfBuildings * 30)
                * (gameController.getMap().getX() / 100 * gameController.getMap().getY() / 100) + 10;
    }

    public void startTheTurn(GameController gameController  ,MySocketHandler socketHandler) {

        happiness = 5;
        happiness -= cities.size();
        turnOffTileConditionsBoolean(gameController,socketHandler);
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
            city.collectResources(resourcesAmount,gameController);
        for (City city : cities) {
            city.startTheTurn(gameController,socketHandler);
            gold += city.getGold(gameController);
        }
        for (Tile noFog : noFogs) {
            gameController.openNewArea(noFog, this, null);
        }
        for (City city : cities)
            city.collectFood(gameController);

        int science = collectScience(gameController);
        for (Unit unit : units) unit.startTheTurn(gameController,socketHandler);
        gold -= units.size();
        if (gold < 0)
            gold = 0;
        if (gettingResearchedTechnology != null) {
            getGettingResearchedTechnology().changeRemainedCost(-science);
            if (gettingResearchedTechnology.getRemainedCost() <= 0) {
                gettingResearchedTechnology.setRemainedCost(0);
                gameController.getCivilizations().get(gameController.getPlayerTurn())
                        .putNotification(gettingResearchedTechnology.getName() +
                                "'s production ended successfully", gameController.getCycle(),socketHandler );
                gettingResearchedTechnology = null;
            }
        }
    }

    public int collectScience(GameController gameController) {
        int returner = 0;
        for (City city : cities) {
            returner += city.getPopulation();
            if (city.findBuilding(BuildingType.LIBRARY) != null)
                returner += city.getPopulation() / 2;
            if (city.findBuilding(BuildingType.UNIVERSITY) != null)
                returner = (int) ((double) returner * 1.5);
            if (city.findBuilding(BuildingType.SATRAPS_COURT) != null)
                returner = (int) ((double) returner * 1.5);
            if (city.findBuilding(BuildingType.UNIVERSITY) != null) {
                for (Tile gettingWorkedOnByCitizensTile : city.getGettingWorkedOnByCitizensTiles()) {
                    if (gettingWorkedOnByCitizensTile.getContainedFeature().getFeatureType() == FeatureType.JUNGLE)
                        returner += 3;
                }
            }
            if (city == mainCapital) returner += 3;
        }
        if (gold == 0) {
            int temp = 0;
            for (City city : cities)
                temp += city.getGold(gameController);
            if (temp < 0) returner += temp;
        }
        return returner + cheatScience;
    }

    public void endTheTurn(GameController gameController,MySocketHandler socketHandler) {
        //using
        for (Unit unit : units) unit.endTheTurn(gameController,socketHandler);
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

    public City getMainCapital() {
        return mainCapital;
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

    public void putNotification(String string, int cycle,MySocketHandler socketHandler) {
        if (!notifications.containsKey(cycle)) {
            ArrayList<String> strings = new ArrayList<>();
            notifications.put(cycle, strings);
        }
        notifications.get(cycle).add(string);
        socketHandler.sendUpdate("notif",cycle + ";;"+ string);

    }

    public void addGold(int number) {
        gold += number;
    }


    public ArrayList<Tile> getNoFogs() {
        return noFogs;
    }

    public HashMap<Integer, ArrayList<String>> getNotifications() {
        return notifications;
    }


    public void setMainCapital(City mainCapital) {
        this.mainCapital = mainCapital;
    }

    public ArrayList<Map.Entry<Civilization, Integer>> getKnownCivilizations() {
        return knownCivilizations;
    }

    public boolean knownCivilizationsContains(Civilization civilization) {
        for (Map.Entry<Civilization, Integer> knownCivilization : knownCivilizations) {
            if (knownCivilization.getKey() == civilization)
                return true;
        }
        return false;
    }

    public int knownCivilizationStatue(Civilization civilization) {
        for (Map.Entry<Civilization, Integer> knownCivilization : knownCivilizations) {
            if (knownCivilization.getKey() == civilization)
                return knownCivilization.getValue();
        }
        return -2;
    }

    public void setKnownCivilizations(Civilization civilization, int statue) {
        knownCivilizations.removeIf(knownCivilization -> knownCivilization.getKey() == civilization);
        knownCivilizations.add(new AbstractMap.SimpleImmutableEntry<>(civilization, statue));
    }

    public ArrayList<TradeRequest> getTradeRequests() {
        return tradeRequests;
    }

    public void addResources(ResourcesTypes resourcesTypes, int amount) {
        int value = amount;
        if (resourcesAmount.containsKey(resourcesTypes)) {
            value += resourcesAmount.get(resourcesTypes);
            resourcesAmount.remove(resourcesTypes);
        }
        resourcesAmount.put(resourcesTypes, value);
    }

    public void removeResource(ResourcesTypes resourcesTypes, int amount) {
        if (resourcesAmount.containsKey(resourcesTypes) && resourcesAmount.get(resourcesTypes) > amount) {
            int value = resourcesAmount.get(resourcesTypes) - amount;
            resourcesAmount.remove(resourcesTypes);
            resourcesAmount.put(resourcesTypes, value);
        }
    }

    public ArrayList<Civilization> getFriendshipRequests() {
        return friendshipRequests;
    }
}
