package model;

import controller.gameController.GameController;
import model.Units.*;
import model.building.Building;
import model.building.BuildingType;
import model.resources.ResourcesCategory;
import model.resources.ResourcesTypes;
import model.tiles.Tile;
import model.tiles.TileType;
import view.gameMenu.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class City implements CanAttack, CanGetAttacked {
    private final String name;
    private final Tile mainTile;
    private Civilization civilization;
    private int HP = 200;
    private int food;
    private int population;
    private Producible product;
    private int production;
    private final ArrayList<Tile> tiles = new ArrayList<>();
    private final Civilization founder;
    private int citizen;
    private int foodForCitizen = 1;
    public boolean isCapital;
    public int productionCheat;
    private Building wall;
    private int anxiety = 0;

    public String getName() {
        return name;
    }

    public Civilization getFounder() {
        return founder;
    }

    private final ArrayList<Tile> gettingWorkedOnByCitizensTiles = new ArrayList<>();
    private final ArrayList<Unit> halfProducedUnits = new ArrayList<>();
    private final ArrayList<Building> halfProducedBuildings = new ArrayList<>();

    public City(Tile tile, String name, Civilization civilization) {
        this.mainTile = tile;
        mainTile.setCivilization(civilization);
        this.civilization = civilization;
        this.founder = civilization;
        this.name = name;
        this.tiles.add(mainTile);
        for (int i = 0; i < 6; i++)
            if (mainTile.getNeighbours(i) != null) {
                this.tiles.add(mainTile.getNeighbours(i));
                mainTile.getNeighbours(i).setCivilization(civilization);
            }
        population = 1;
        citizen = 1;
        for (Tile value : tiles) GameController.openNewArea(value, civilization, null);
        GameController.setUnfinishedTasks();
        isCapital = civilization.getCities().size() == 0;
    }

    private boolean doesContainSettler() {
        for (Tile tile : tiles) {
            if (tile.getCivilian() != null
                    && tile.getCivilian().getCivilization() != civilization
                    && tile.getCivilian().getUnitType() == UnitType.SETTLER)
                return true;
        }
        return false;
    }

    public int collectFood() {
        int food = 0;
        for (Tile gettingWorkedOnByCitizensTile : gettingWorkedOnByCitizensTiles) {
            food += gettingWorkedOnByCitizensTile.getTileType().food;
            if (gettingWorkedOnByCitizensTile.getImprovement() != null)
                food += gettingWorkedOnByCitizensTile.getImprovement().getImprovementType().food;
            if (gettingWorkedOnByCitizensTile.getContainedFeature() != null &&
                    gettingWorkedOnByCitizensTile.getContainedFeature().getFeatureType() != null)
                food += gettingWorkedOnByCitizensTile.getContainedFeature().getFeatureType().food;
            if (gettingWorkedOnByCitizensTile.getResource() != null &&
                    gettingWorkedOnByCitizensTile.getResource()
                            .isTechnologyUnlocked(civilization, gettingWorkedOnByCitizensTile)
                    && gettingWorkedOnByCitizensTile.getImprovement() != null &&
                    (0 == gettingWorkedOnByCitizensTile.getImprovement().getNeedsRepair())
                    && gettingWorkedOnByCitizensTile.getResource().getImprovementType() ==
                    gettingWorkedOnByCitizensTile.getImprovement().getImprovementType())
                food += gettingWorkedOnByCitizensTile.getResource().getFood();
        }
        if (civilization.getHappiness() < 0) food /= 3;
        return food;
    }


    public void collectResources(HashMap<ResourcesTypes, Integer> resourcesAmount) {
        for (Tile gettingWorkedOnByCitizensTile : gettingWorkedOnByCitizensTiles) {
            if (gettingWorkedOnByCitizensTile.getResource() != null &&
                    gettingWorkedOnByCitizensTile.getResource()
                            .isTechnologyUnlocked(civilization, gettingWorkedOnByCitizensTile)
                    && gettingWorkedOnByCitizensTile.getImprovement() != null &&
                    (0 == gettingWorkedOnByCitizensTile.getImprovement().getNeedsRepair())
                    && gettingWorkedOnByCitizensTile.getResource() != null &&
                    gettingWorkedOnByCitizensTile.getResource().getImprovementType() ==
                            gettingWorkedOnByCitizensTile.getImprovement().getImprovementType()) {
                if (!resourcesAmount.containsKey(gettingWorkedOnByCitizensTile.getResource()))
                    resourcesAmount.put(gettingWorkedOnByCitizensTile.getResource(), 1);
                else {
                    int temp = resourcesAmount.get(gettingWorkedOnByCitizensTile.getResource());
                    resourcesAmount.remove(gettingWorkedOnByCitizensTile.getResource());
                    resourcesAmount.put(gettingWorkedOnByCitizensTile.getResource(), temp + 1);
                }
                if (!civilization.getUsedLuxuryResources()
                        .containsKey(gettingWorkedOnByCitizensTile.getResource()) &&
                        gettingWorkedOnByCitizensTile.getResource()
                                .getResourcesCategory() == ResourcesCategory.LUXURY) {
                    civilization.getUsedLuxuryResources()
                            .put(gettingWorkedOnByCitizensTile.getResource(), true);
                    civilization.changeHappiness(4);
                }
            }
        }
    }

    public int collectProduction() {
        int production = 0;
        for (Tile gettingWorkedOnByCitizensTile : gettingWorkedOnByCitizensTiles) {
            production += gettingWorkedOnByCitizensTile.getTileType().production + citizen;
            if (gettingWorkedOnByCitizensTile.getImprovement() != null)
                production += gettingWorkedOnByCitizensTile
                        .getImprovement().getImprovementType().production;
            if (gettingWorkedOnByCitizensTile.getContainedFeature() != null)
                production += gettingWorkedOnByCitizensTile
                        .getContainedFeature().getFeatureType().production;
            if (gettingWorkedOnByCitizensTile.getResource() != null &&
                    gettingWorkedOnByCitizensTile.getResource()
                            .isTechnologyUnlocked(civilization, gettingWorkedOnByCitizensTile)
                    && gettingWorkedOnByCitizensTile.getImprovement() != null
                    && 0 == gettingWorkedOnByCitizensTile.getImprovement().getNeedsRepair()
                    && gettingWorkedOnByCitizensTile.getResource().improvementType ==
                    gettingWorkedOnByCitizensTile.getImprovement().getImprovementType())
                production += gettingWorkedOnByCitizensTile.getResource().production;
        }
        return production + productionCheat + citizen;
    }

    private void minus5Percent() {
        for (int i = 0; i < halfProducedUnits.size(); i++)
            if (halfProducedUnits.get(i) != product) {
                halfProducedUnits.get(i).setRemainedCost(halfProducedUnits.get(i).getRemainedCost() +
                        (int) (0.05 * halfProducedUnits.get(i).getUnitType().cost));
                if (halfProducedUnits.get(i).getRemainedCost() >= halfProducedUnits.get(i).getUnitType().cost) {
                    halfProducedUnits.remove(halfProducedUnits.get(i));
                    i--;
                }
            }
    }

    private Tile properTileForProduct(boolean isCivilian) {
        if ((isCivilian && mainTile.getCivilian() == null) ||
                (!isCivilian && mainTile.getNonCivilian() == null))
            return mainTile;
        for (int i = 0; i < 6; i++)
            if (mainTile.getNeighbours(i) != null &&
                    mainTile.getNeighbours(i).getMovingPrice() <12345 &&
                    ((!isCivilian && mainTile.getNeighbours(i).getNonCivilian() == null) ||
                            (isCivilian && mainTile.getNeighbours(i).getCivilian() == null)))
                return mainTile.getNeighbours(i);
        for (int i = 0; i < 6; i++) {
            if (mainTile.getNeighbours(i) == null)
                continue;
            for (int j = 0; j < 6; j++)
                if (mainTile.getNeighbours(i).getNeighbours(j) != null &&
                        mainTile.getNeighbours(i).getNeighbours(j).getMovingPrice() <12345 &&
                        ((!isCivilian && mainTile.getNeighbours(i).getNeighbours(j).getNonCivilian() == null) ||
                                (isCivilian && mainTile.getNeighbours(i).getNeighbours(j).getCivilian() == null)))
                    return mainTile.getNeighbours(i).getNeighbours(j);
        }
        return null;
    }

    public int getAnxiety() {
        return anxiety;
    }

    private void productStartTheTurnProgress() {
        if (product != null) {
            int tempRemaining = product.getRemainedCost();
            product.setRemainedCost(product.getRemainedCost() - production);
            production -= tempRemaining;
            if (production <= 0)
                production = 0;
            if (product.getRemainedCost() <= 0) {
                if (product instanceof Unit) {
                    if (product instanceof NonCivilian) {
                        Tile tile = properTileForProduct(false);
                        if (tile != null) {
                            ((Unit) product).setCurrentTile(tile);
                            tile.setNonCivilian(((NonCivilian) product));
                        }
                    } else {
                        Tile tile = properTileForProduct(true);
                        if (tile != null) {
                            ((Unit) product).setCurrentTile(tile);
                            tile.setCivilian((Civilian) product);
                        }
                    }
                    civilization.getUnits().add((Unit) product);
                    halfProducedUnits.remove(product);
                }
                if (product instanceof Building) {
                    if (((Building) product).getBuildingType() == BuildingType.WALL)
                        wall = ((Building) product);
                }
                GameController.getCivilizations().get(GameController.getPlayerTurn())
                        .putNotification(this.name + ": " +
                        product.getName() + "'s production ended, cycle: ",GameController.getCycle());
                product = null;
            }
        }
    }

    public void startTheTurn() {
        anxiety--;
        if (anxiety < 0) anxiety = 0;
        HP += 10;
        if (HP > 200) HP = 200;
        food += collectFood();
        production += collectProduction();
        productStartTheTurnProgress();
        food = food - population;
        if (food < 0) {
            food = 0;
            civilization.changeHappiness(-1);
        }
        if (food > foodForCitizen) {
            food -= foodForCitizen;
            foodForCitizen *= 2;
            population++;
            citizen++;
            expandBorders();
        }
        if (doesContainSettler()) food = 0;
        minus5Percent();
        for (Tile tile : tiles) GameController.openNewArea(tile, civilization, null);
    }

    public void expandBorders() {
        Random rand = new Random();
        int x = 0;
        while (150 > x) {
            Tile tile = tiles.get(Math.abs(rand.nextInt(tiles.size())));
            for (int i = 0; i < 6; i++) {
                if (tile.getNeighbours(i) != null && addTile(tile.getNeighbours(i))) return;
                x++;
            }
        }
    }

    public int getGold() {
        int gold = 0;
        for (Tile gettingWorkedOnByCitizensTile : gettingWorkedOnByCitizensTiles) {
            int temp = 0;
            for (int i = 0; i < 6; i++)
                if (gettingWorkedOnByCitizensTile.isRiverWithNeighbour(i)) temp += 1;
            gold += gettingWorkedOnByCitizensTile.getTileType().gold + temp;
            if (gettingWorkedOnByCitizensTile.getImprovement() != null)
                gold += gettingWorkedOnByCitizensTile.getImprovement().getImprovementType().gold;
            if (gettingWorkedOnByCitizensTile.getContainedFeature() != null &&
                    gettingWorkedOnByCitizensTile.getContainedFeature().getFeatureType() != null)
                gold += gettingWorkedOnByCitizensTile.getContainedFeature().getFeatureType().gold;
            if (gettingWorkedOnByCitizensTile.getResource() != null &&
                    gettingWorkedOnByCitizensTile.getResource()
                            .isTechnologyUnlocked(civilization, gettingWorkedOnByCitizensTile)
                    && gettingWorkedOnByCitizensTile.getImprovement() != null &&
                    (0 == gettingWorkedOnByCitizensTile.getImprovement().getNeedsRepair()) &&
                    gettingWorkedOnByCitizensTile.getResource().getImprovementType() ==
                            gettingWorkedOnByCitizensTile.getImprovement().getImprovementType())
                gold += gettingWorkedOnByCitizensTile.getResource().gold;
        }
        return gold;
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

    public void attack(Tile tile) {
        if (tile.getNonCivilian() != null) {
            tile.getNonCivilian().takeDamage(calculateDamage(getCombatStrength(true) /
                    tile.getNonCivilian().getCombatStrength(false)),civilization);
            tile.getNonCivilian().checkToDestroy();
        } else {
            tile.getCivilian().takeDamage(calculateDamage(getCombatStrength(true) /
                    tile.getCivilian().getCombatStrength(false)),civilization);
            tile.getCivilian().checkToDestroy();
        }

        GameController.openNewArea(tile, civilization, null);
    }

    public boolean isTileNeighbor(Tile tile) {
        for (Tile tile1 : tiles)
            for (int i = 0; i < 6; i++)
                if (tile1.getNeighbours(i) == tile) return true;
        return false;
    }

    public Tile getMainTile() {
        return mainTile;
    }

    public int getHP() {
        return HP;
    }

    public Civilization getCivilization() {
        return civilization;
    }

    public boolean assignCitizenToTiles(Tile originTile, Tile destinationTile) {
        if (originTile == null && tiles.contains(destinationTile) && citizen > 0) {
            citizen--;
            gettingWorkedOnByCitizensTiles.add(destinationTile);
            return true;
        } else if (tiles.contains(originTile) &&
                gettingWorkedOnByCitizensTiles.contains(originTile) &&
                tiles.contains(destinationTile)) {
            gettingWorkedOnByCitizensTiles.remove(originTile);
            gettingWorkedOnByCitizensTiles.add(destinationTile);
            return true;
        }
        return false;
    }
    public boolean removeCitizen(Tile tile){
        if(tiles.contains(tile) &&
                gettingWorkedOnByCitizensTiles.contains(tile)) {
            gettingWorkedOnByCitizensTiles.remove(tile);
            return true;
        }
        return false;
    }

    public boolean doWeHaveEnoughMoney(UnitType unitType) {
        return unitType.cost <= civilization.getGold();
    }

    public double getCombatStrength(boolean isAttack) {
        double strength = 4;
        if (mainTile.getNonCivilian() != null &&
                mainTile.getNonCivilian().getCivilization() == civilization &&
                mainTile.getNonCivilian().getState() == UnitState.GARRISON) {
            if (isAttack && mainTile.getNonCivilian().getUnitType().range > 1)
                strength += mainTile.getNonCivilian().getUnitType().rangedCombatStrength;
            else strength += mainTile.getNonCivilian().getUnitType().combatStrength;
        }
        if (tiles.size() > 10) strength += 2 * (tiles.size() - 10);
        if(tiles.size() > 10 && isAttack) strength -=  (tiles.size() - 10);
        if (!isAttack) strength += tiles.size();
        if(mainTile.getTileType() == TileType.HILL) strength *= 1.2;
        if (wall != null && !isAttack) strength = (strength * 1.2);
        return strength;
    }

    public boolean checkToDestroy() {
        if (HP <= 0) {
            GameController.getUnfinishedTasks().add(new Tasks(mainTile, TaskTypes.CITY_DESTINY));
            if (mainTile.getNonCivilian() != null &&
                    mainTile.getNonCivilian().getState() == UnitState.GARRISON) {
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

    public void destroy(Civilization civilization) {
        this.civilization.putNotification("The "+Color.getColorByNumber(civilization.getColor())
                + civilization.getUser().getNickname() + Color.RESET
                + " dudes burned your city " + name,GameController.getCycle());
        civilization.putNotification("You burned "+name+" successfully",GameController.getCycle());
        this.civilization.getCities().remove(this);
        for (Tile tile : tiles) {
            tile.setImprovement(null);
            tile.setCivilization(null);
        }
        for (Tile tile : tiles) {
            GameController.openNewArea(tile,this.civilization,null);
            GameController.openNewArea(tile,civilization,null);
        }

    }

    public void changeCivilization(Civilization civilization) {
        HP = 25;
        anxiety = 5;
        this.civilization.putNotification("The "+Color.getColorByNumber(civilization.getColor())
                + civilization.getUser().getNickname() + Color.RESET
                + " dudes captured your city " + name,GameController.getCycle());
        civilization.putNotification("You captured "+name+" successfully",GameController.getCycle());
        for (Tile tile : tiles) tile.setCivilization(civilization);
        for (Tile tile : tiles) {
            GameController.openNewArea(tile,this.civilization,null);
            GameController.openNewArea(tile,civilization,null);
        }
        isCapital = false;
        product = null;
        while (halfProducedUnits.size() != 0)
            halfProducedUnits.remove(0);
        this.civilization.getCities().remove(this);
        civilization.getCities().add(this);
        this.civilization = civilization;

    }

    public int calculateDamage(double ratio) {
        if (ratio >= 1)
            return (int) (16.774 * Math.exp(0.5618 * ratio));
        else
            return (int) (16.774 * Math.exp(0.5618 / ratio) / (0.3294 * Math.exp(1.1166 / ratio)));

    }

    public void takeDamage(int amount,Civilization civilization) {
        HP -= amount;
        civilization.putNotification(name+ " @ "+ mainTile.getX() + " , "+ mainTile.getY()  + " : " +
                "oopsy woopsy you just got smashed by"+ Color.getColorByNumber(civilization.getColor())
                + civilization.getUser().getNickname() + Color.RESET ,GameController.getCycle());
    }

    public void setProduct(Producible product) {
        this.product = product;
    }

    public int cyclesToComplete(int remainedCost) {
        if (collectProduction() == 0)
            return 12345;
        return (int) Math.ceil((double) remainedCost / (double) collectProduction() - 0.03);
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
