package model;

import controller.GameController;
import model.Units.*;
import model.building.Building;
import model.building.BuildingType;
import model.tiles.Tile;
import model.tiles.TileType;

import java.util.ArrayList;
import java.util.Random;


public class City implements CanAttack, CanGetAttacked {
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
    public boolean isCapital;
    public int productionCheat;
    private Building wall;
    public String getName() {
        return name;
    }

    public Civilization getFounder() {
        return founder;
    }

    private ArrayList<Tile> gettingWorkedOnByCitizensTiles = new ArrayList<>();
    private ArrayList<Unit> halfProducedUnits = new ArrayList<>();
    private ArrayList<Building> halfProducedBuildings = new ArrayList<>();

    public City(Tile tile, String name, Civilization civilization) {

        this.mainTile = tile;
        mainTile.setCivilization(civilization);
        this.civilization = civilization;
        this.founder = civilization;
        this.name = name;
        this.tiles.add(mainTile);
        for (int i = 0; i < 6; i++) {
            this.tiles.add(mainTile.getNeighbours(i));
            mainTile.getNeighbours(i).setCivilization(civilization);
        }
        population = 1;
        Random random = new Random();
        gettingWorkedOnByCitizensTiles.add(tiles.get(1 + random.nextInt(tiles.size() - 1)));
        for (Tile value : tiles) GameController.openNewArea(value, civilization, null);
        GameController.setUnfinishedTasks();
        isCapital = civilization.getCities().size() == 0;
    }

    public int collectFood() {
        int food = 0;
        for (Tile gettingWorkedOnByCitizensTile : gettingWorkedOnByCitizensTiles) {
            food += gettingWorkedOnByCitizensTile.getTileType().food;
            if(gettingWorkedOnByCitizensTile.getImprovement() != null) food +=gettingWorkedOnByCitizensTile.getImprovement().getImprovementType().food;
            if(gettingWorkedOnByCitizensTile.getContainedFeature().getFeatureType()!= null) food += gettingWorkedOnByCitizensTile.getContainedFeature().getFeatureType().food;
            if (gettingWorkedOnByCitizensTile.getResources().isTechnologyUnlocked(civilization, gettingWorkedOnByCitizensTile)
                    && gettingWorkedOnByCitizensTile.getImprovement() != null && (0==gettingWorkedOnByCitizensTile.getImprovement().getNeedsRepair())
                    && gettingWorkedOnByCitizensTile.getResources().improvementType == gettingWorkedOnByCitizensTile.getImprovement().getImprovementType())
                food += gettingWorkedOnByCitizensTile.getResources().food;

        }
        if (civilization.getHappiness() < 0) food /= 3;
        return food;
    }


    public void collectResources() {
        for (Tile gettingWorkedOnByCitizensTile : gettingWorkedOnByCitizensTiles) {
            if (gettingWorkedOnByCitizensTile.getResources().isTechnologyUnlocked(civilization, gettingWorkedOnByCitizensTile)
                    && gettingWorkedOnByCitizensTile.getImprovement() != null && (0==gettingWorkedOnByCitizensTile.getImprovement().getNeedsRepair())
                    && gettingWorkedOnByCitizensTile.getResources().improvementType == gettingWorkedOnByCitizensTile.getImprovement().getImprovementType()) {
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
    }

    public int collectProduction() {
        int production = 0;
        for (Tile gettingWorkedOnByCitizensTile : gettingWorkedOnByCitizensTiles) {
            production += gettingWorkedOnByCitizensTile.getTileType().production
                     + citizen;
            if(gettingWorkedOnByCitizensTile.getImprovement() != null) production +=gettingWorkedOnByCitizensTile.getImprovement().getImprovementType().production;
            if(gettingWorkedOnByCitizensTile.getContainedFeature().getFeatureType()!= null) production += gettingWorkedOnByCitizensTile.getContainedFeature().getFeatureType().production;
            if (gettingWorkedOnByCitizensTile.getResources().isTechnologyUnlocked(civilization, gettingWorkedOnByCitizensTile)
                    && gettingWorkedOnByCitizensTile.getImprovement() != null && (0==gettingWorkedOnByCitizensTile.getImprovement().getNeedsRepair())
                    && gettingWorkedOnByCitizensTile.getResources().improvementType == gettingWorkedOnByCitizensTile.getImprovement().getImprovementType())
                production += gettingWorkedOnByCitizensTile.getResources().production;
        }
        return production + productionCheat;
    }

    private void mines5Percent() {
        for (int i = 0; i < halfProducedUnits.size(); i++)
            if (halfProducedUnits.get(i) != product) {
                halfProducedUnits.get(i).setRemainedCost(halfProducedUnits.get(i).getRemainedCost() + (int) (0.05 * halfProducedUnits.get(i).getUnitType().cost));
                if (halfProducedUnits.get(i).getRemainedCost() >= halfProducedUnits.get(i).getUnitType().cost) {
                    halfProducedUnits.remove(halfProducedUnits.get(i));
                    i--;
                }
            }
    }

    private Tile properTileForProduct(boolean isCivilian) {
        for (int i = 0; i < 6; i++)
            if (mainTile.getNeighbours(i) != null &&
                    mainTile.getNeighbours(i).getMovingPrice() != 123 &&
                    ((!isCivilian && mainTile.getNeighbours(i).getNonCivilian() == null) ||
                            (isCivilian && mainTile.getNeighbours(i).getCivilian() == null)))
                return mainTile.getNeighbours(i);
        for (int i = 0; i < 6; i++) {
            if (mainTile.getNeighbours(i) == null)
                continue;
            for (int j = 0; j < 6; j++)
                if (mainTile.getNeighbours(i).getNeighbours(j) != null &&
                        mainTile.getNeighbours(i).getNeighbours(j).getMovingPrice() != 123 &&
                        ((!isCivilian && mainTile.getNeighbours(i).getNeighbours(j).getNonCivilian() == null) ||
                                (isCivilian && mainTile.getNeighbours(i).getNeighbours(j).getCivilian() == null)))
                    return mainTile.getNeighbours(i).getNeighbours(j);
        }
        return null;
    }

    public void startTheTurn() {
        HP += 10;
        if (HP > 200) HP = 200;
        food += collectFood();
        production += collectProduction();
        if (product != null) {
            int tempRemaining = product.getRemainedCost();
            product.setRemainedCost(product.getRemainedCost() - production);
            production -= tempRemaining;
            if (production <= 0)
                production = 0;
            if (product.getRemainedCost() <= 0) {
                if (product instanceof Unit) {
                    if (product instanceof NonCivilian) {
                        ((Unit) product).getCurrentTile().setNonCivilian((NonCivilian) product);
                        if (mainTile.getNonCivilian() != null)
                            ((Unit) product).setCurrentTile(properTileForProduct(false));
                    } else {
                        ((Unit) product).getCurrentTile().setCivilian((Unit) product);
                        if (mainTile.getCivilian() != null)
                            ((Unit) product).setCurrentTile(properTileForProduct(true));
                    }
                    civilization.getUnits().add((Unit) product);
                    halfProducedUnits.remove(product);
                }
                if(product instanceof Building)
                {
                    if(((Building) product).getBuildingType() == BuildingType.WALL)
                        wall = ((Building) product);
                }
                product = null;
            }
        }
        food = food - 2 * population;
        if (food < 0) {
            population--;
            food = 0;
            if (citizen > 0) citizen--;
            else gettingWorkedOnByCitizensTiles.remove(0);
        }
        if (food > foodForCitizen) {
            food -= foodForCitizen;
            foodForCitizen *= 2;
            population++;
            citizen++;
            expandBorders();
        }
        mines5Percent();
        for (Tile tile : tiles) GameController.openNewArea(tile, civilization, null);
    }

    public void expandBorders() {
        Random rand = new Random();
        int x = 0;
        while (150 > x) {
            Tile tile = tiles.get(rand.nextInt() % tiles.size());
            for (int i = 0; i < 6; i++) {
                if (tile.getNeighbours(i) != null && addTile(tile.getNeighbours(i))) return;
                x++;
            }
        }

    }

    public void endTheTurn() {

    }

    public int getGold() {
        int gold = 0;
        for (Tile gettingWorkedOnByCitizensTile : gettingWorkedOnByCitizensTiles) {
            int temp = 0;
            for (int i = 0; i < 6; i++) {
                if (gettingWorkedOnByCitizensTile.isRiverWithNeighbour(i)) temp += 1;
            }
            gold += gettingWorkedOnByCitizensTile.getTileType().gold + temp;
            if(gettingWorkedOnByCitizensTile.getImprovement() != null) gold +=gettingWorkedOnByCitizensTile.getImprovement().getImprovementType().gold;
            if(gettingWorkedOnByCitizensTile.getContainedFeature().getFeatureType()!= null) gold += gettingWorkedOnByCitizensTile.getContainedFeature().getFeatureType().gold;
            if (gettingWorkedOnByCitizensTile.getResources().isTechnologyUnlocked(civilization, gettingWorkedOnByCitizensTile)
                    && gettingWorkedOnByCitizensTile.getImprovement() != null && (0==gettingWorkedOnByCitizensTile.getImprovement().getNeedsRepair())
                    && gettingWorkedOnByCitizensTile.getResources().improvementType == gettingWorkedOnByCitizensTile.getImprovement().getImprovementType())
                gold += gettingWorkedOnByCitizensTile.getResources().gold;
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
        calculateDamage( getCombatStrength(true) / tile.getNonCivilian().getCombatStrength(false));
        tile.getNonCivilian().checkToDestroy();
        GameController.openNewArea(tile, civilization, null);
    }

    public boolean isTileNeighbor(Tile tile) {
        for (Tile tile1 : tiles) {
            for (int i = 0; i < 6; i++) {
                if (tile1.getNeighbours(i) == tile) return true;
            }
        }
        return false;
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

    public boolean assignCitizenToTiles(Tile originTile, Tile destinationTile) {
        if (originTile == null) citizen--;
        else gettingWorkedOnByCitizensTiles.remove(originTile);
        gettingWorkedOnByCitizensTiles.add(destinationTile);
        return false;
    }

    public boolean doWeHaveEnoughMoney(UnitType unitType) {
        if (unitType.cost > civilization.getGold())
            return false;
        return true;
    }

    public boolean getDoesHaveWall() {
        return doesHaveWall;
    }

    public double getCombatStrength(boolean isAttack) {
        double strength = 1;
        if (mainTile.getNonCivilian() != null && mainTile.getNonCivilian().getState() == UnitState.GARRISON) {
            if (isAttack && mainTile.getNonCivilian().getUnitType().range > 1)
                strength += mainTile.getNonCivilian().getUnitType().rangedCombatStrength;
            else strength += mainTile.getNonCivilian().getUnitType().combatStrength;
        }
        if (tiles.size() > 10 && !isAttack) strength += 2 * (tiles.size() - 10);
        if (!isAttack) strength += tiles.size();
        for (Tile tile : tiles) {
            if (!isAttack && (tile.getTileType() == TileType.MOUNTAIN || tile.getTileType() == TileType.HILL))
                strength += 1;
        }
        if(wall!=null && !isAttack) strength =  (strength * 1.2);
        return strength;
    }

    public boolean checkToDestroy() {
        if (this.HP <= 0) {
            GameController.getUnfinishedTasks().add(new Tasks(mainTile, TaskTypes.CITY_DESTINY));
            if (mainTile.getNonCivilian() != null && mainTile.getNonCivilian().getState() == UnitState.GARRISON) {
                civilization.getUnits().remove(mainTile.getNonCivilian());
                mainTile.setCivilian(null);

            }
            return true;
        }
        return false;
    }

    public boolean addTile(Tile tile) {
        if (tile.getCivilization() != null) return false;
        tiles.add(tile);
        tile.setCivilization(civilization);
        GameController.openNewArea(tile, civilization, null);
        return true;
    }

    public void destroy() {
        civilization.getCities().remove(this);
        for (Tile tile : tiles) {
            tile.setImprovement(null);
        }
    }

    public void changeCivilization(Civilization civilization) {
        HP = 25;
        for (Tile tile : tiles) {
            tile.setCivilization(civilization);
        }
        product = null;
        for (Unit halfProducedUnit : halfProducedUnits) {
            halfProducedUnits.remove(halfProducedUnit);
        }
        this.civilization = civilization;

    }

    public int calculateDamage(double ratio) {
        if (ratio >= 1) {
            return (int) (16.774 * Math.exp(0.5618 * ratio));
        } else {
            return (int) (16.774 * Math.exp(0.5618 / ratio) / (0.3294 * Math.exp(1.1166 / ratio)));
        }

    }

    public void takeDamage(int amount) {
        HP -= amount;
    }

    public void setProduct(Producible product) {
        this.product = product;
    }

    public int cyclesToComplete(int remainedCost) {
        if (collectProduction() == 0)
            return 12345;
        return (int) Math.ceil((double) remainedCost / (double) collectProduction());
    }

    public void setProduction(int production) {
        this.production = production;
    }

    public ArrayList<Unit> getHalfProducedUnits() {
        return halfProducedUnits;
    }

    public ArrayList<Building> getHalfProducedBuildings() {
        return halfProducedBuildings;
    }

    public Building getWall() {
        return wall;
    }
}
