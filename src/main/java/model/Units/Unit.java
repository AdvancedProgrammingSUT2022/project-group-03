package model.Units;

import controller.GameController;
import model.Civilization;
import model.Map;
import model.FeatureType;
import model.productable;
import model.tiles.Tile;
import model.tiles.TileType;

public abstract class Unit implements productable {
    protected Civilization civilization;
    private static int state;
    protected Tile currentTile;
    private Tile destinationTile;
    private int movementPrice;
    private int health = 10;
    protected UnitType unitType;
    private int XP;
    private boolean hasDoneAnything;


    public Unit(Tile tile, Civilization civilization, UnitType unitType) {
        this.currentTile = tile;
        this.civilization = civilization;
        this.movementPrice = unitType.getDefaultMovementPrice();
    }

    public int getHealth() {
        return health;
    }

    public Civilization getCivilization() {
        return civilization;
    }

    @Override
    public int getCost() {
        return unitType.cost;
    }

    public void cancelMission() {

    }

    public void wake() {

    }

    public void startTheTurn() {
        openNewArea();
//        health += ...
        if (health > 10)
            health = 10;
        movementPrice = unitType.getDefaultMovementPrice();


    }

    public void endTheTurn() {
        if (destinationTile != null)
            move(destinationTile);
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    private void openNewArea() {
        for (int i = 0; i < 6; i++) {
            int neighbourX = currentTile.getNeighbours(i).getX();
            int neighbourY = currentTile.getNeighbours(i).getY();
            civilization.tileConditions[neighbourX][neighbourY] = new Civilization.TileCondition(currentTile.getNeighbours(i).CloneTileForCivilization(civilization.getResearches()), true);
            if (currentTile.getNeighbours(i).getTileType() == TileType.MOUNTAIN ||
                    currentTile.getNeighbours(i).getTileType() == TileType.HILL ||
                    (currentTile.getNeighbours(i).getFeature() != null && (currentTile.getNeighbours(i).getFeature() == FeatureType.FOREST ||
                            currentTile.getNeighbours(i).getFeature() == FeatureType.DENSEFOREST)))
                continue;
            for (int j = 0; j < 6; j++) {
                neighbourX = currentTile.getNeighbours(i).getNeighbours(j).getX();
                neighbourY = currentTile.getNeighbours(i).getNeighbours(j).getY();
                civilization.tileConditions[neighbourX][neighbourY] = new Civilization.TileCondition(currentTile.getNeighbours(i).getNeighbours(j).CloneTileForCivilization(civilization.getResearches()), true);
            }
        }
        civilization.tileConditions[currentTile.getX()][currentTile.getY()] = new Civilization.TileCondition(currentTile.CloneTileForCivilization(civilization.getResearches()), true);
    }


    public boolean move(Tile destinationTile) {
        Map.TileAndMP[] tileAndMPS = GameController.getMap().findNextTile(currentTile, movementPrice, destinationTile, unitType.combatType==CombatType.CIVILIAN);
        this.destinationTile = destinationTile;
        if (tileAndMPS == null) {
            this.destinationTile = null;
            return false;
        }
        if (tileAndMPS.length == 1)
            this.destinationTile = null;
        Tile tempTile = null;
        for (int i = tileAndMPS.length - 1; i >= 0; i--)
            if (tileAndMPS[i] != null) {
                tempTile = tileAndMPS[i].tile;
                break;
            }
        if (tempTile == null)
            return false;
        if (this instanceof NonCivilian) {
            this.currentTile.setNonCivilian(null);
            tempTile.setNonCivilian((NonCivilian) this);
        } else {
            this.currentTile.setCivilian(null);
            tempTile.setCivilian(this);
        }
        if (this.movementPrice != tempTile.getMovingPrice())
            this.movementPrice = tempTile.getMovingPrice();
        else
            this.movementPrice = 0;
        this.currentTile = tempTile;
        openNewArea();
        return true;
    }


    public int getMovementPrice() {
        return movementPrice;
    }




    public UnitType getUnitType() {
        return unitType;
    }
}
