package model.Units;

import controller.GameController;
import model.*;
import model.Civilization;
import model.Map;
import model.features.FeatureType;
import model.Producible;
import model.tiles.Tile;
import model.tiles.TileType;

public abstract class Unit implements Producible, CanGetAttacked {
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
        if (health <= 0) {
        civilization.getUnits().remove(this);
        if(this instanceof NonCivilian) currentTile.setCivilian(null);
        else currentTile.setNonCivilian(null);
        return true;
        }
        return false;

    }
    public int getCombatStrength(boolean isAttack){
        double combat;
        if(isAttack){
            combat = ((double)unitType.rangedCombatStrength * (100 + currentTile.getCombatChange())/ 100);
        }
        else combat = ((double)unitType.combatStrength * (100 + currentTile.getCombatChange())/ 100);
        if (civilization.getHappiness() < 0) combat = 0.75 * combat;
        combat = combat*(50 + (double)health/2)/100;
        if (combat < 1) combat = 1;
        return (int) combat;

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
        if (health > 100)
            health = 100;
        movementPrice = unitType.getDefaultMovementPrice();

        if(state== UnitState.FORTIFY_UNTIL_FULL_HEALTH && health==10)
            state = UnitState.AWAKE;

        if(unitType==UnitType.WORKER && currentTile.getImprovement()!=null && currentTile.getImprovement().getRemainedCost()!=0)
            currentTile.getImprovement().setRemainedCost(currentTile.getImprovement().getRemainedCost()-1);

        if(unitType==UnitType.WORKER && state==UnitState.REPAIRING)
        {
            if(currentTile.getImprovement()!= null && currentTile.getImprovement().getNeedsRepair()!=0)
            {
                currentTile.getImprovement().setNeedsRepair(currentTile.getImprovement().getNeedsRepair()-1);
                if(currentTile.getImprovement().getNeedsRepair()==0)
                    state=UnitState.AWAKE;
            }
            else
                state=UnitState.AWAKE;
        }
        if(unitType==UnitType.WORKER && state==UnitState.REMOVING)
        {
            if(currentTile.getContainedFeature()!=null && currentTile.getContainedFeature().getCyclesToFinish()!=0)
            {
                currentTile.getContainedFeature().setCyclesToFinish(currentTile.getContainedFeature().getCyclesToFinish()-1);
                if(currentTile.getContainedFeature().getCyclesToFinish()==0)
                {
                    state=UnitState.AWAKE;
                    currentTile.setFeature(null);
                }
            }
            else
                state=UnitState.AWAKE;
        }
    }

    public void endTheTurn() {
        if (destinationTile != null)
            move(destinationTile,true);
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
                    (currentTile.getNeighbours(i).getFeatureType() != null && (currentTile.getNeighbours(i).getFeatureType() == FeatureType.FOREST ||
                            currentTile.getNeighbours(i).getFeatureType() == FeatureType.DENSEFOREST)))
                continue;
            for (int j = 0; j < 6; j++) {
                neighbourX = currentTile.getNeighbours(i).getNeighbours(j).getX();
                neighbourY = currentTile.getNeighbours(i).getNeighbours(j).getY();
                civilization.getTileConditions()[neighbourX][neighbourY] = new Civilization.TileCondition(currentTile.getNeighbours(i).getNeighbours(j).CloneTileForCivilization(civilization.getResearches()), true);
            }
        }
        civilization.getTileConditions()[currentTile.getX()][currentTile.getY()] = new Civilization.TileCondition(currentTile.CloneTileForCivilization(civilization.getResearches()), true);
    }


    public boolean move(Tile destinationTile,boolean ogCall) {
        if (movementPrice == 0) return false;
        if(state == UnitState.ATTACK && GameController.getMap().isInRange(unitType.range,destinationTile,currentTile)) {
            attack(destinationTile);
            return ogCall;
        }
        Map.TileAndMP[] tileAndMPS = GameController.getMap().findNextTile(civilization,currentTile, movementPrice,unitType.movePoint, destinationTile, unitType.combatType==CombatType.CIVILIAN);
        if(ogCall){this.destinationTile = destinationTile;
            if(state != UnitState.ATTACK) this.state = UnitState.MOVING;
        }
        Tile startTile = this.currentTile;
        if (tileAndMPS == null) {
            this.destinationTile = null;
            state = UnitState.AWAKE;
            return false;
        }

        Tile tempTile = null;
        int i =  tileAndMPS.length - 1;
        for (; i >= 0; i--)
            if (tileAndMPS[i] != null) {
                tempTile = tileAndMPS[i].tile;
                break;
            }
        if (tempTile == null ||
                (tileAndMPS[i].movePoint != 0 && tileAndMPS[i].movePoint != unitType.movePoint &&
                        (tempTile.getNonCivilian() != null && this instanceof NonCivilian|| tempTile.getCivilian() != null && !(this instanceof NonCivilian))))
            return false;

        if (this.unitType.movePoint != tileAndMPS[i].movePoint)
            this.movementPrice =tileAndMPS[i].movePoint;
        else
            this.movementPrice = 0;
        this.currentTile = tempTile;
        GameController.openNewArea(this.currentTile,civilization,null);
        boolean notEnd = true;
        for (int j = i; j > 0 && notEnd && movementPrice> 0; j--) {
            notEnd = move(tileAndMPS[j-1].tile,false);
        }
        if(ogCall){
            if (this instanceof NonCivilian) {
                startTile.setNonCivilian(null);
                currentTile.setNonCivilian((NonCivilian) this);
            } else {
                startTile.setCivilian(null);
                currentTile.setCivilian(this);
            }
            GameController.openNewArea(currentTile,civilization,null);
            if(destinationTile == currentTile){
                this.destinationTile = null;
                state = UnitState.AWAKE;
            }
            return movementPrice==0 || destinationTile == currentTile || state == UnitState.ATTACK;
        }
        return true;
    }


    public int getMovementPrice() {
        return movementPrice;
    }

    public void attack(Tile tile) {}

    public void takeDamage(int amount){
        health -= amount;
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
