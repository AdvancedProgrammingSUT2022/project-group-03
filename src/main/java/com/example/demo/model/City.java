package com.example.demo.model;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.Units.*;
import com.example.demo.model.building.Building;
import com.example.demo.model.building.BuildingType;
import com.example.demo.model.resources.ResourcesCategory;
import com.example.demo.model.resources.ResourcesTypes;
import com.example.demo.model.tiles.Tile;
import com.example.demo.model.tiles.TileType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class City implements Serializable, CanAttack, CanGetAttacked {
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
    public boolean isMainCapital = false;
    public int productionCheat;
    private int anxiety = 0;
    private final ArrayList<Building> buildings = new ArrayList<>();
    private boolean hasAttackedThisCycle = false;

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
        System.out.println("main: " + mainTile.getX() + " " + mainTile.getY());
        for (int i = 0; i < 6; i++)
            if (mainTile.getNeighbours(i) != null) {
                this.tiles.add(mainTile.getNeighbours(i));
                mainTile.getNeighbours(i).setCivilization(civilization);
            }
        population = 1;
        citizen = 1;
        for (Tile value : tiles) GameController.openNewArea(value, civilization, null);
        GameController.setUnfinishedTasks();
//        if(civilization.getMainCapital()!=null && civilization.getMainCapital().getCivilization()!=civilization)
//            isCapital = civilization.getCities().size() == 0;
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
        if (findBuilding(BuildingType.GRANARY) != null)
            food += 2;
        if (findBuilding(BuildingType.WATER_MILL) != null)
            food += 2;
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
        production += citizen;
        if (findBuilding(BuildingType.WINDMILL) != null)
            production = (int) ((double) production * 1.15);
        if (findBuilding(BuildingType.FACTORY) != null)
            production = (int) ((double) production * 1.5);

        return production + productionCheat;
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
                    mainTile.getNeighbours(i).getMovingPrice() < 12345 &&
                    ((!isCivilian && mainTile.getNeighbours(i).getNonCivilian() == null) ||
                            (isCivilian && mainTile.getNeighbours(i).getCivilian() == null)))
                return mainTile.getNeighbours(i);
        for (int i = 0; i < 6; i++) {
            if (mainTile.getNeighbours(i) == null)
                continue;
            for (int j = 0; j < 6; j++)
                if (mainTile.getNeighbours(i).getNeighbours(j) != null &&
                        mainTile.getNeighbours(i).getNeighbours(j).getMovingPrice() < 12345 &&
                        ((!isCivilian && mainTile.getNeighbours(i).getNeighbours(j).getNonCivilian() == null) ||
                                (isCivilian && mainTile.getNeighbours(i).getNeighbours(j).getCivilian() == null)))
                    return mainTile.getNeighbours(i).getNeighbours(j);
        }
        return null;
    }

    public int getAnxiety() {
        return anxiety;
    }

    public boolean doesHaveLakeAround() {
        for (Tile tile : tiles) {
            if (tile.doesHaveLakeAround())
                return true;
        }
        return false;
    }

    private void productStartTheTurnProgress() {
        if (product != null) {
            int tempRemaining = product.getRemainedCost();
            double ratio = 1;
            if (findBuilding(BuildingType.FORGE) != null && product instanceof Unit)
                ratio = 1.25;
            if (findBuilding(BuildingType.WORKSHOP) != null && product instanceof Building)
                ratio = 1.2;
            if (findBuilding(BuildingType.ARSENAL) != null && product instanceof Unit)
                ratio = 1.2;
            if (findBuilding(BuildingType.STABLE) != null && product instanceof Unit && ((Unit) product).getUnitType().combatType == CombatType.MOUNTED)
                ratio = 1.25;
            product.setRemainedCost(product.getRemainedCost() - (int) (production * ratio));
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
                    buildings.add((Building) product);
                    if (((Building) product).getBuildingType() == BuildingType.BURIAL_TOMB)
                        civilization.changeHappiness(2);
                    if (((Building) product).getBuildingType() == BuildingType.SATRAPS_COURT)
                        civilization.changeHappiness(2);
                    if (((Building) product).getBuildingType() == BuildingType.THEATER)
                        civilization.changeHappiness(4);
                    if (((Building) product).getBuildingType() == BuildingType.COLOSSEUM)
                        civilization.changeHappiness(4);
                    if (((Building) product).getBuildingType() == BuildingType.CIRCUS)
                        civilization.changeHappiness(3);
                    if (((Building) product).getBuildingType() == BuildingType.HOSPITAL)
                        foodForCitizen /= 2;

                }
                GameController.getCivilizations().get(GameController.getPlayerTurn())
                        .putNotification(this.name + ": " +
                                product.getName() + "'s production ended, cycle: ", GameController.getCycle());
                product = null;
            }
        }
    }

    public void startTheTurn() {
        hasAttackedThisCycle=false;
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
            if (gettingWorkedOnByCitizensTile.getResource() == ResourcesTypes.SILVER ||
                    gettingWorkedOnByCitizensTile.getResource() == ResourcesTypes.GOLD)
                gold += 3;
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
        if (findBuilding(BuildingType.MARKET) != null)
            gold = (int) ((double) gold * 1.25);
        if (findBuilding(BuildingType.BANK) != null)
            gold = (int) ((double) gold * 1.25);
        if (findBuilding(BuildingType.SATRAPS_COURT) != null)
            gold = (int) ((double) gold * 1.25);
        if (findBuilding(BuildingType.STOCK_EXCHANGE) != null)
            gold = (int) ((double) gold * 1.33);

        return gold;
    }

    public boolean doesHaveRiver() {
        for (Tile tile : tiles) {
            if (tile.doesHaveRiver())
                return true;
        }
        return false;
    }

    public Building findBuilding(BuildingType buildingType) {
        for (Building building : buildings) {
            if (building.getBuildingType() == buildingType)
                return building;
        }
        return null;
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
                    tile.getNonCivilian().getCombatStrength(false)), civilization);
            tile.getNonCivilian().checkToDestroy();
        } else {
            tile.getCivilian().takeDamage(calculateDamage(getCombatStrength(true) /
                    tile.getCivilian().getCombatStrength(false)), civilization);
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

    public int assignCitizenToTiles(Tile originTile, Tile destinationTile) {

        if (!tiles.contains(destinationTile))
            return 1;
        if (originTile == null && citizen <= 0)
            return 2;

        if (originTile == null) {
            citizen--;
            gettingWorkedOnByCitizensTiles.add(destinationTile);
            return 0;
        }
        if (!tiles.contains(originTile))
            return 3;
        if (!gettingWorkedOnByCitizensTiles.contains(originTile))
            return 4;
        gettingWorkedOnByCitizensTiles.remove(originTile);
        gettingWorkedOnByCitizensTiles.add(destinationTile);
        return 0;
    }

    public int removeCitizen(Tile tile) {
        if (!tiles.contains(tile))
            return 1;
        if (!gettingWorkedOnByCitizensTiles.contains(tile))
            return 2;
        gettingWorkedOnByCitizensTiles.remove(tile);
        return 0;
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
        if (tiles.size() > 10 && isAttack) strength -= (tiles.size() - 10);
        if (!isAttack) strength += tiles.size();
        if (mainTile.getTileType() == TileType.HILL) strength *= 1.2;
        if (findBuilding(BuildingType.WALLS) != null && !isAttack) strength = (strength * 1.2);
        if (!isAttack && findBuilding(BuildingType.WALLS) != null) strength += 5;
        if (!isAttack && findBuilding(BuildingType.MILITARY_BASE) != null) strength += 12;
        if (!isAttack && findBuilding(BuildingType.CASTLE) != null) strength += 7.5;
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
        this.civilization.putNotification("The " + Color.getColorByNumber(civilization.getColor())
                + civilization.getUser().getNickname() + Color.RESET
                + " dudes burned your city " + name, GameController.getCycle());
        civilization.putNotification("You burned " + name + " successfully", GameController.getCycle());
        this.civilization.getCities().remove(this);

        for (Tile tile : tiles) {
            tile.setImprovement(null);
            tile.setCivilization(null);
        }
        for (Tile tile : tiles) {
            GameController.openNewArea(tile, this.civilization, null);
            GameController.openNewArea(tile, civilization, null);
        }

    }

    public void changeCivilization(Civilization civilization) {
        if(GameController.getCivilizations().get(GameController.getPlayerTurn()).getMainCapital()==this)
            for (City city : GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities()) {
                city.setCapital(false);
            }
        HP = 25;
        if (findBuilding(BuildingType.COURTHOUSE) != null)
            anxiety = 5;
        this.civilization.putNotification("The " + Color.getColorByNumber(civilization.getColor())
                + civilization.getUser().getNickname() + Color.RESET
                + " dudes captured your city " + name, GameController.getCycle());
        civilization.putNotification("You captured " + name + " successfully", GameController.getCycle());
        for (Tile tile : tiles) tile.setCivilization(civilization);
        for (Tile tile : tiles) {
            GameController.openNewArea(tile, this.civilization, null);
            GameController.openNewArea(tile, civilization, null);
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

    public void takeDamage(int amount, Civilization civilization) {
        HP -= amount;
        civilization.putNotification(name + " @ " + mainTile.getX() + " , " + mainTile.getY() + " : " +
                "oopsy woopsy you just got smashed by" + Color.getColorByNumber(civilization.getColor())
                + civilization.getUser().getNickname() + Color.RESET, GameController.getCycle());
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


    public Unit findHalfProducedUnit(UnitType unitType)
    {
        for (Unit halfProducedUnit : halfProducedUnits) {
            if(halfProducedUnit.getUnitType()==unitType)
                return halfProducedUnit;
        }
        return null;
    }
    public Building findHalfBuiltBuildings(BuildingType buildingType)
    {
        for (Building halfBuiltBuildings : halfProducedBuildings) {
            if(halfBuiltBuildings.getBuildingType()==buildingType)
                return halfBuiltBuildings;
        }
        return null;
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public void addPopulation()
    {
        population++;
    }

    public void setCapital(boolean capital) {
        isCapital = capital;
    }

    public boolean isMainCapital() {
        return isMainCapital;
    }

    public void setMainCapital(boolean mainCapital) {
        isMainCapital = mainCapital;
    }

    public boolean getHasAttackedThisCycle() {
        return hasAttackedThisCycle;
    }

    public void setHasAttackedThisCycle(boolean hasAttackedThisCycle) {
        this.hasAttackedThisCycle = hasAttackedThisCycle;
    }
}
