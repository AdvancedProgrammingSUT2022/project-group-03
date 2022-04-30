package model;

import controller.GameController;
import model.Units.*;
import model.resources.ResourcesTypes;
import model.tiles.Tile;

import java.util.ArrayList;


public class City {
    private final String name;
    private int strength;
    private final Tile mainTile;
    private Civilization civilization;
    private boolean doesHaveWall = false;
    private int HP = 20;
    private int food;
    private int population;
    private producible product;
    private int production;
    private ArrayList<Tile> tiles = new ArrayList<>();
    private final Civilization founder;
    private int citizen;

    public String getName() {
        return name;
    }

    public Civilization getFounder() {
        return founder;
    }

    private ArrayList<Tile> gettingWorkedOnByCitizensTiles = new ArrayList<>();
    private ArrayList<producible> halfProducedUnits = new ArrayList<>();

    public City(Tile tile, String name, Civilization civilization) {

        this.mainTile = tile;
        this.civilization = civilization;
        this.founder = civilization;
        this.name = name;
        this.tiles.add(mainTile);
        for (int i = 0; i < 6; i++)
            this.tiles.add(mainTile.getNeighbours(i));
    }

    public void startTheTurn() {
        for (Tile gettingWorkedOnByCitizensTile : gettingWorkedOnByCitizensTiles) {
            if (!civilization.getResourcesAmount().containsKey(gettingWorkedOnByCitizensTile.getResources()))
                civilization.getResourcesAmount().put(gettingWorkedOnByCitizensTile.getResources(), 1);
            else {
                int temp = civilization.getResourcesAmount().get(gettingWorkedOnByCitizensTile.getResources());
                civilization.getResourcesAmount().remove(gettingWorkedOnByCitizensTile.getResources());
                civilization.getResourcesAmount().put(gettingWorkedOnByCitizensTile.getResources(), temp);
            }
            food += gettingWorkedOnByCitizensTile.getTileType().food
                    + gettingWorkedOnByCitizensTile.getFeature().food;
            //todo resources;
            production += gettingWorkedOnByCitizensTile.getTileType().production
                    + gettingWorkedOnByCitizensTile.getFeature().production;
            int temp = 0;
            for (int i = 0; i < 6; i++) {
                if (gettingWorkedOnByCitizensTile.isRiverWithNeighbour(i)) temp += 1;
            }
            civilization.increaseGold(gettingWorkedOnByCitizensTile.getTileType().gold
                    + gettingWorkedOnByCitizensTile.getFeature().gold + temp);
        }
        if (product != null) {
            int tempRemaining = product.getRemainedCost();
            product.setRemainedCost(product.getRemainedCost() - production);
            production -= tempRemaining;
            if (production <= 0)
                production = 0;
            if (product.getRemainedCost() <= 0) {
                if (product instanceof Unit)
                    civilization.getUnits().add((Unit) product);
                product = null;
            }
        }

    }

    public int getGold() {
        return 0;
    }

    public int getProduction() {
        return production;
    }

    public int getFood() {
        return food;
    }

    public producible getProduct() {
        return product;
    }

    public int getPopulation() {
        return population;
    }

    public int getCitizen() {
        return citizen;
    }


    public ArrayList<Tile> getGettingWorkedOnByCitizensTiles() {
        return gettingWorkedOnByCitizensTiles;
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    private void countTheTotalOfCityResources() {

    }

    public boolean buildWall() {
        return false;
    }

    public boolean defense(Unit attackers) {
        return false;
    }

    public void attack(Tile tile) {

    }

    public void buyTile(Tile tile) {

    }

    public Tile getMainTile() {
        return mainTile;
    }

    public int getHP() {
        return HP;
    }

    public int getStrength() {
        return strength;
    }

    public Civilization getCivilization() {
        return civilization;
    }

    public void assignCitizenToTiles(Tile originTile, Tile destinationTile) {
        if (originTile == null) citizen--;
        else gettingWorkedOnByCitizensTiles.remove(originTile);
        gettingWorkedOnByCitizensTiles.add(destinationTile);
    }

    public boolean createUnit(UnitType unitType) {
        if (unitType.cost > civilization.getGold())
            return false;
        if(product!=null)
        {
            halfProducedUnits.add(product);
            product=null;
        }
        GameController.deleteFromUnfinishedTasks(new Tasks(mainTile,TaskTypes.CITY_PRODUCTION));
        if (unitType.combatType == CombatType.CIVILIAN) {
            if (unitType == UnitType.Settler) {
                product = new Settler(mainTile, civilization, unitType);
                return true;
            }
            product = new Worker(mainTile, civilization, unitType);
            return true;
        }
        product = new NonCivilian(mainTile, civilization, unitType);
        return true;
    }

    public boolean getDoesHaveWall() {
        return doesHaveWall;
    }
}
