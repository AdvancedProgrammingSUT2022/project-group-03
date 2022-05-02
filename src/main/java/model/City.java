package model;

import controller.GameController;
import model.Units.*;
import model.tiles.Tile;

import java.util.ArrayList;


public class City implements CanAttack,CanGetAttacked {
    private final String name;
    private int strength;
    private final Tile mainTile;
    private Civilization civilization;
    private boolean doesHaveWall = false;
    private int HP = 200;
    private int food;
    private int population;
    private Producible product;
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
    private ArrayList<Producible> halfProducedUnits = new ArrayList<>();

    public City(Tile tile, String name, Civilization civilization) {

        this.mainTile = tile;
        mainTile.setCivilization(civilization);
        this.civilization = civilization;
        this.founder = civilization;
        this.name = name;
        this.tiles.add(mainTile);
        for (int i = 0; i < 6; i++){
            this.tiles.add(mainTile.getNeighbours(i));
            mainTile.getNeighbours(i).setCivilization(civilization);
        }
        for (Tile value : tiles) GameController.openNewArea(value, civilization,null);
        GameController.setUnfinishedTasks();
    }
    public int collectFood(){
        int food = 0;
        for (Tile gettingWorkedOnByCitizensTile : gettingWorkedOnByCitizensTiles) {
            if (civilization.getHappiness() < 0)
            food += (gettingWorkedOnByCitizensTile.getTileType().food
                + gettingWorkedOnByCitizensTile.getFeatureType().food) / 3;
        }
        return food;
    }
    public void collectResources(){
        for (Tile gettingWorkedOnByCitizensTile : gettingWorkedOnByCitizensTiles) {
            //if(gettingWorkedOnByCitizensTile.getResources().isTechnologyUnlocked(civilization.getResearches())
                  //  && gettingWorkedOnByCitizensTile.getResources())
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
                    + gettingWorkedOnByCitizensTile.getFeatureType().production + citizen;
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
                    + gettingWorkedOnByCitizensTile.getFeatureType().gold + temp;
        }
        return gold;
    }




    public int getProduction() {
        return production;
    }

    public int getFood() {
        return food;
    }

    public Producible getProduct() {
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
    public int getCombatStrength(boolean isAttack){
        int strength = 1;
        if(mainTile.getNonCivilian()!= null && mainTile.getNonCivilian().getState() == UnitState.GARRISON){
            if(isAttack && mainTile.getNonCivilian().getUnitType().range > 1) strength += mainTile.getNonCivilian().getUnitType().rangedCombatStrength;
            else strength += mainTile.getNonCivilian().getUnitType().combatStrength;
        }
        if(tiles.size() > 10 && !isAttack) strength += 4*(tiles.size()- 10);
        if(!isAttack) strength += tiles.size();
        if()
        /*double combat;
        if(isAttack){
            combat = ((double)unitType.rangedCombatStrength * (100 + currentTile.getCombatChange())/ 100);
        }
        else combat = ((double)unitType.combatStrength * (100 + currentTile.getCombatChange())/ 100);
        if (civilization.collectResources() < 0) combat = 0.75 * combat;
        combat = combat*(50 + (double)health/2)/100;
        if (combat < 1) combat = 1;
        return (int) combat;*/
        return 50;
    }

    public boolean checkToDestroy(){ //todo add city to civilization
        if(this.HP <= 0) {
            civilization.getCities().remove(this);
            for (Tile tile : tiles) {
                tile.setImprovement(null);
            }
            return true;
        }
        return false;
    }
    public int calculateDamage(double ratio){return 0;}
    public void takeDamage(int amount){
        HP -= amount;
    }
}
