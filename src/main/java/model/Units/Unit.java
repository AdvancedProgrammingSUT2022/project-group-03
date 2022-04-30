package model.Units;

import controller.GameController;
import model.Civilization;
import model.Map;
import model.FeatureType;
import model.producible;
import model.tiles.Tile;
import model.tiles.TileType;

public abstract class Unit implements producible {
    protected Civilization civilization;
    protected Tile currentTile;
    private Tile destinationTile;
    private int movementPrice;
    private int health = 10;
    protected UnitType unitType;
    public int remainedCost;
    private UnitState state;
    public Unit(Tile tile, Civilization civilization, UnitType unitType) {
        this.currentTile = tile;
        this.civilization = civilization;
        this.movementPrice = unitType.getDefaultMovementPrice();
        this.remainedCost = unitType.cost;
        this.state = UnitState.AWAKE;
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
