package model;

import controller.GameController;
import model.Units.*;
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
    private int foodForCitizen = 1;

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
        for (Tile value : tiles) GameController.openNewArea(value, civilization,null);
        GameController.setUnfinishedTasks();
    }
    public int collectFood(){
        int food = 0;
        for (Tile gettingWorkedOnByCitizensTile : gettingWorkedOnByCitizensTiles) {
            if (civilization.getHappiness() < 0)
            food += (gettingWorkedOnByCitizensTile.getTileType().food
                + gettingWorkedOnByCitizensTile.getContainedFeature().getFeatureType().food) / 3;
        }
        return food;
    }
    public void getHappiness(){
        for (Tile gettingWorkedOnByCitizensTile : gettingWorkedOnByCitizensTiles) {
            if (!civilization.getResourcesAmount().containsKey(gettingWorkedOnByCitizensTile.getResources())) {
                civilization.getResourcesAmount().put(gettingWorkedOnByCitizensTile.getResources(), 1);
            } else {
                int temp = civilization.getResourcesAmount().get(gettingWorkedOnByCitizensTile.getResources());
                civilization.getResourcesAmount().remove(gettingWorkedOnByCitizensTile.getResources());
                civilization.getResourcesAmount().put(gettingWorkedOnByCitizensTile.getResources(), temp);
            }
            if (!civilization.getUsedLuxuryResources().containsKey(gettingWorkedOnByCitizensTile.getResources())) {
                civilization.getUsedLuxuryResources().put(gettingWorkedOnByCitizensTile.getResources(), true);
                civilization.changeHappiness(4);

            }
        }
    }
    public int collectProduction(){
        int production = 0;
        for (Tile gettingWorkedOnByCitizensTile : gettingWorkedOnByCitizensTiles) {
            //todo resources;
            production += gettingWorkedOnByCitizensTile.getTileType().production
                    + gettingWorkedOnByCitizensTile.getContainedFeature().getFeatureType().production + citizen;
        }
        return production;
    }

    public void startTheTurn() {
        food += collectFood();
        production += collectProduction();
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
        food = food - 2*population;
        if(food < 0){
            population--;
            food = 0;
            if(citizen > 0) citizen--;
            else gettingWorkedOnByCitizensTiles.remove(0);
        }
        if(food > foodForCitizen){
            food -= foodForCitizen;
            foodForCitizen*=2;
            population++;
            citizen++;
        }

        for (Tile tile : tiles) GameController.openNewArea(tile, civilization,null);
    }
    public void endTheTurn(){

    }
    public int getGold(){
        int gold = 0;
        for (Tile gettingWorkedOnByCitizensTile : gettingWorkedOnByCitizensTiles) {
            int temp = 0;
            for (int i = 0; i < 6; i++) {
                if (gettingWorkedOnByCitizensTile.isRiverWithNeighbour(i)) temp += 1;
            }
            gold += gettingWorkedOnByCitizensTile.getTileType().gold
                    + gettingWorkedOnByCitizensTile.getContainedFeature().getFeatureType().gold + temp;
        }
        return gold;
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
            if (unitType == UnitType.SETTLER) {
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
