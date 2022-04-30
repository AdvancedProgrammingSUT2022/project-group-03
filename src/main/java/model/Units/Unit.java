package model.Units;

import controller.GameController;
import model.*;
import model.tiles.Tile;
import model.tiles.TileType;

public abstract class Unit implements producible, mortal {
    protected Civilization civilization;
    protected Tile currentTile;
    protected Tile destinationTile;
    protected int movementPrice;
    protected int health = 100;
    protected UnitType unitType;
    protected boolean isAttacking = false;
    public int remainedCost;
    protected UnitState state;
    public Unit(Tile tile, Civilization civilization, UnitType unitType) {
        this.currentTile = tile;
        this.civilization = civilization;
        this.movementPrice = unitType.getDefaultMovementPrice();
        this.remainedCost = unitType.cost;
        this.state = UnitState.AWAKE;
    }
    public boolean checkToDestroy(){
        if (health < 0) {
        civilization.getUnits().remove(this);
        if(this instanceof NonCivilian) currentTile.setCivilian(null);
        else currentTile.setNonCivilian(null);
        return true;
        }
        return false;

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
        state= UnitState.AWAKE;
        destinationTile = null;
    }

    public void startTheTurn() {
        GameController.openNewArea(currentTile,civilization,this);
//        health += ...
        if (health > 10)
            health = 10;
        movementPrice = unitType.getDefaultMovementPrice();

        if(state== UnitState.FORTIFY_UNTIL_FULL_HEALTH && health==10)
            state = UnitState.AWAKE;

        if(unitType==UnitType.WORKER && currentTile.getImprovement()!=null && currentTile.getImprovement().getRemainedCost()!=0)
            currentTile.getImprovement().setRemainedCost(currentTile.getImprovement().getRemainedCost()-1);

        if(unitType==UnitType.WORKER && state==UnitState.REPAIRING)
        {
            if(currentTile.getImprovement()!= null && currentTile.getImprovement().getNeedsRepair()!=0)
                currentTile.getImprovement().setNeedsRepair(currentTile.getImprovement().getNeedsRepair()-1);
            else
                state=UnitState.AWAKE;
        }
    }

    public void endTheTurn() {
        if (destinationTile != null)
            move(destinationTile);
    }

    public Tile getDestinationTile() {
        return destinationTile;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    void openNewArea() {
        for (int i = 0; i < 6; i++) {
            int neighbourX = currentTile.getNeighbours(i).getX();
            int neighbourY = currentTile.getNeighbours(i).getY();
            civilization.getTileConditions()[neighbourX][neighbourY] = new Civilization.TileCondition(currentTile.getNeighbours(i).CloneTileForCivilization(civilization.getResearches()), true);
            if (currentTile.getNeighbours(i).getTileType() == TileType.MOUNTAIN ||
                    currentTile.getNeighbours(i).getTileType() == TileType.HILL ||
                    (currentTile.getNeighbours(i).getFeature() != null && (currentTile.getNeighbours(i).getFeature() == FeatureType.FOREST ||
                            currentTile.getNeighbours(i).getFeature() == FeatureType.DENSEFOREST)))
                continue;
            for (int j = 0; j < 6; j++) {
                neighbourX = currentTile.getNeighbours(i).getNeighbours(j).getX();
                neighbourY = currentTile.getNeighbours(i).getNeighbours(j).getY();
                civilization.getTileConditions()[neighbourX][neighbourY] = new Civilization.TileCondition(currentTile.getNeighbours(i).getNeighbours(j).CloneTileForCivilization(civilization.getResearches()), true);
            }
        }
        civilization.getTileConditions()[currentTile.getX()][currentTile.getY()] = new Civilization.TileCondition(currentTile.CloneTileForCivilization(civilization.getResearches()), true);
    }


    public boolean move(Tile destinationTile) {
        Map.TileAndMP[] tileAndMPS = GameController.getMap().findNextTile(civilization,currentTile, movementPrice,unitType.movePoint, destinationTile, unitType.combatType==CombatType.CIVILIAN);
        this.destinationTile = destinationTile;
        if (tileAndMPS == null) {
            this.destinationTile = null;
            return false;
        }
        for (int i = tileAndMPS.length-1; i >= 0 ; i--) {
            GameController.openNewArea(tileAndMPS[i].tile,civilization,null);
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
        if (this.unitType.movePoint != tempTile.getMovingPrice())
            this.movementPrice = tempTile.getMovingPrice();
        else
            this.movementPrice = 0;
        this.currentTile = tempTile;
        GameController.openNewArea(this.currentTile,civilization,null);
        return true;
    }


    public int getMovementPrice() {
        return movementPrice;
    }


    public void setState(UnitState state) {
        this.state = state;
    }

    public UnitState getState() {
        return state;
    }

    public int getRemainedCost()
    {
        return remainedCost;
    }
    public void setRemainedCost(int remainedCost) { this.remainedCost = remainedCost;}
    public UnitType getUnitType() {
        return unitType;
    }
}
