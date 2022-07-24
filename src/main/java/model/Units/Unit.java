package model.Units;

import controller.gameController.GameController;
import model.*;
import model.Civilization;
import model.Map;
import model.features.FeatureType;
import model.Producible;
import model.improvements.ImprovementType;
import model.tiles.Tile;
//import view.gameMenu.Color;

//import javax.swing.text.View;
//import java.awt.*;

public abstract class Unit implements Producible, CanGetAttacked {
    protected Civilization civilization;
    protected Tile currentTile;
    protected Tile destinationTile;
    protected int movementPrice;
    protected int health = 100;
    protected UnitType unitType;
    private int remainedCost;
    protected UnitState state;

    public Unit(Tile tile, Civilization civilization, UnitType unitType) {
        this.currentTile = tile;
        this.civilization = civilization;
        this.movementPrice = unitType.getDefaultMovementPrice();
        this.remainedCost = unitType.cost;
        this.state = UnitState.AWAKE;
        this.unitType = unitType;
    }

    public boolean checkToDestroy(GameController gameController) {
        if (health <= 0) {
            civilization.getUnits().remove(this);
            this.civilization.putNotification("unit died",gameController.getCycle());
            if (this instanceof NonCivilian)
                currentTile.setNonCivilian(null);
            else currentTile.setCivilian(null);
            return true;
        }
        return false;
    }

    public double getCombatStrength(boolean isAttack) {
        double combat;
        if (isAttack)
            combat = unitType.rangedCombatStrength;
        else combat = unitType.combatStrength;
        combat = combat *
                ((double) (100 + currentTile.getCombatChange()) / 100);
        if (unitType == UnitType.CHARIOT_ARCHER &&
                currentTile.getContainedFeature() != null &&
                (currentTile.getContainedFeature()
                        .getFeatureType() == FeatureType.JUNGLE ||
                        currentTile.getContainedFeature()
                                .getFeatureType() == FeatureType.FOREST ||
                        currentTile.getContainedFeature()
                                .getFeatureType() == FeatureType.ICE)) combat *= 0.9;

        if (!isAttack && state == UnitState.FORTIFY &&
                (unitType.combatType != CombatType.MOUNTED &&
                unitType.combatType != CombatType.SIEGE
                && unitType.combatType != CombatType.ARMORED))
            combat = (combat * (((NonCivilian) this).getFortifiedCycle() + 10)) / 10;
        if (civilization.getHappiness() < 0) combat = 0.75 * combat;
        combat = combat * (50 + (double) health / 2) / 100;
        if (combat < 1) combat = 1;
        return combat;

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
        state = UnitState.AWAKE;
        destinationTile = null;
    }

    private void workerBuildImprovementProgress(GameController gameController) {
        currentTile.getImprovement()
                .setRemainedCost(currentTile.getImprovement()
                        .getRemainedCost() - 1);
        if (currentTile.getImprovement().getRemainedCost() == 0) {
            state = UnitState.AWAKE;
            gameController.getCivilizations().get(gameController.getPlayerTurn())
                    .putNotification(currentTile.getImprovement().getImprovementType() +
                            "'s production ended, cycle: ",gameController.getCycle());
            if ((currentTile.getImprovement().getImprovementType() == ImprovementType.FARM ||
                    currentTile.getImprovement().getImprovementType() == ImprovementType.MINE) &&
                    currentTile.getContainedFeature() != null &&
                    (currentTile.getContainedFeature().getFeatureType() == FeatureType.JUNGLE
                            || currentTile.getContainedFeature().getFeatureType() == FeatureType.SWAMP
                            || currentTile.getContainedFeature().getFeatureType() == FeatureType.FOREST))
                currentTile.setContainedFeature(null);
        }
    }

    private void workerBuildRoadProgress(GameController gameController) {
        currentTile.getRoad().setRemainedCost(currentTile.getRoad().getRemainedCost() - 1);
        if (currentTile.getRoad().getRemainedCost() == 0 && gameController.getSelectedCity()!=null)
        {
            state = UnitState.AWAKE;
            gameController.getCivilizations().get(gameController.getPlayerTurn())
                    .putNotification(currentTile.getRoad().getImprovementType() +
                            "'s production ended, cycle: ",gameController.getCycle());
        }
    }

    private void workerRemoveProgress() {
        if (currentTile.getContainedFeature() != null &&
                currentTile.getContainedFeature().getCyclesToFinish() != 0) {
            currentTile.getContainedFeature()
                    .setCyclesToFinish(currentTile.getContainedFeature().getCyclesToFinish() - 1);
            if (currentTile.getContainedFeature().getCyclesToFinish() == 0) {
                state = UnitState.AWAKE;
                currentTile.setContainedFeature(null);
            }
        } else
            state = UnitState.AWAKE;
    }

    private void workerRepairProgress() {
        if (currentTile.getImprovement() != null &&
                currentTile.getImprovement().getNeedsRepair() != 0) {
            currentTile.getImprovement().setNeedsRepair(currentTile.getImprovement().getNeedsRepair() - 1);
            if (currentTile.getImprovement().getNeedsRepair() == 0)
                state = UnitState.AWAKE;
        } else if (currentTile.getRoad() != null &&
                currentTile.getRoad().getNeedsRepair() != 0) {
            currentTile.getRoad().setNeedsRepair(currentTile.getRoad().getNeedsRepair() - 1);
            if (currentTile.getRoad().getNeedsRepair() == 0)
                state = UnitState.AWAKE;
        } else
            state = UnitState.AWAKE;
    }

    private void startTheTurnForWorker(GameController gameController) {
        if (state == UnitState.BUILDING &&
                currentTile.getImprovement() != null &&
                currentTile.getImprovement().getRemainedCost() != 0)
            workerBuildImprovementProgress(gameController);
        if (state == UnitState.BUILDING &&
                currentTile.getRoad() != null &&
                currentTile.getRoad().getRemainedCost() != 0)
            workerBuildRoadProgress(gameController);
        if (state == UnitState.REPAIRING)
            workerRepairProgress();
        if (state == UnitState.REMOVING)
            workerRemoveProgress();
    }

    public void startTheTurn(GameController gameController) {
        gameController.openNewArea(currentTile, civilization, this);
        health += 5;
        if (state == UnitState.FORTIFY) health += 15;
        if (unitType.combatType != CombatType.CIVILIAN) ((NonCivilian) this).attacked = false;
        if (currentTile.getCivilization() == civilization) health += 5;
        if (health > 100) {
            if (state == UnitState.FORTIFY_UNTIL_FULL_HEALTH) state = UnitState.AWAKE;
            health = 100;
        }
        movementPrice = unitType.getDefaultMovementPrice();
        if (unitType.combatType != CombatType.CIVILIAN &&
                (state == UnitState.FORTIFY || state == UnitState.SETUP) &&
                ((NonCivilian) this).getFortifiedCycle() != 2)
            ((NonCivilian) this).setFortifiedCycle(((NonCivilian) this).getFortifiedCycle() + 1);

        if (unitType == UnitType.WORKER)
            startTheTurnForWorker(gameController);
    }

    public void endTheTurn(GameController gameController) {
        if (destinationTile != null)
            move(destinationTile, true,gameController);
    }

    public Tile getDestinationTile() {
        return destinationTile;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    private int initializeMove(boolean ogCall, Map.TileAndMP[] tileAndMPS, Tile destinationTile,GameController gameController) {
        if (ogCall) {
            gameController.openNewArea(this.currentTile, civilization, null);
            this.destinationTile = destinationTile;
            if (state != UnitState.ATTACK) this.state = UnitState.MOVING;
        }
        if (tileAndMPS == null) {
            this.destinationTile = null;
            state = UnitState.AWAKE;
            return -1;
        }

        Tile tempTile = null;
        int i = tileAndMPS.length - 1;
        for (; i >= 0; i--)
            if (tileAndMPS[i] != null) {
                tempTile = tileAndMPS[i].tile;
                break;
            }
        if (tempTile == null ||
                ((tileAndMPS[i].movePoint == 0 || tileAndMPS[i].movePoint == unitType.movePoint) &&
                        (tempTile.getNonCivilian() != null && this instanceof NonCivilian ||
                                tempTile.getCivilian() != null && !(this instanceof NonCivilian))))
            return -1;

        if (this.unitType.movePoint != tileAndMPS[i].movePoint)
            this.movementPrice = tileAndMPS[i].movePoint;
        else
            this.movementPrice = 0;
        this.currentTile = tempTile;
        gameController.openNewArea(this.currentTile, civilization, null);
        return i;
    }

    public boolean move(Tile destinationTile, boolean ogCall,GameController gameController) {
        gameController.openNewArea(this.currentTile, civilization, this);
        if (state == UnitState.ATTACK && gameController.getMap()
                .isInRange(unitType.range, destinationTile, currentTile)) {
            attack(destinationTile);
            return ogCall;
        }
        if (movementPrice == 0) return false;
        Tile startTile = this.currentTile;
        Map.TileAndMP[] tileAndMPS = gameController.getMap().findNextTile(civilization,
                currentTile, movementPrice, unitType.movePoint, destinationTile,
                this instanceof Civilian, this);
        int i = initializeMove(ogCall, tileAndMPS, destinationTile,gameController);
        if (i == -1) return false;
        boolean notEnd = true;
        for (int j = i; j > 0 && notEnd && movementPrice > 0; j--) {
            notEnd = move(tileAndMPS[j - 1].tile, false,gameController);
        }
        if (ogCall) {
            if (this instanceof NonCivilian) {
                startTile.setNonCivilian(null);
                currentTile.setNonCivilian((NonCivilian) this);
            } else {
                startTile.setCivilian(null);
                currentTile.setCivilian(this);
            }
            gameController.openNewArea(currentTile, civilization, null);
            if (destinationTile == currentTile) {
                this.destinationTile = null;
                state = UnitState.AWAKE;
            }
            return movementPrice == 0 ||
                    destinationTile == currentTile ||
                    state == UnitState.ATTACK;
        }
        return true;
    }


    public int getMovementPrice() {
        return movementPrice;
    }

    public void attack(Tile tile) {
    }

    public void takeDamage(int amount,Civilization civilization,GameController gameController) {
        health -= amount;
        civilization.putNotification(unitType+ " @ "+ currentTile.getX() + " , "+ currentTile.getY()  + " : " +
                "oopsy woopsy you just got smashed by "+ Color.getColorByNumber(civilization.getColor())
                + civilization.getUser().getNickname() + Color.RESET ,gameController.getCycle());
    }

    public void setState(UnitState state) {
        this.state = state;
        if (this instanceof NonCivilian) ((NonCivilian) this).setFortifiedCycle(0);
    }

    public UnitState getState() {
        return state;
    }

    public int getRemainedCost() {
        return remainedCost;
    }

    public void setRemainedCost(int remainedCost) {
        this.remainedCost = remainedCost;
    }

    public UnitType getUnitType() {
        return unitType;
    }


    public void setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
    }

    @Override
    public String getName() {
        return unitType.toString();
    }
}
