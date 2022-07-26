package model.Units;

import controller.gameController.GameController;
import model.CanGetAttacked;
import model.City;
import model.Civilization;
import model.CanAttack;
import model.tiles.Tile;


public class NonCivilian extends Unit implements CanAttack {

    private int fortifiedCycle = 0;
    public boolean attacked = false;
    public UnitType getUnitType() {
        return unitType;
    }

    private double calculateRatio(CanGetAttacked target) {
        double ratio = getCombatStrength(true) /
                target.getCombatStrength(false);
        if ((unitType == UnitType.PIKEMAN ||
                unitType == UnitType.SPEARMAN) &&
                target instanceof Unit &&
                ((Unit) target).unitType.combatType == CombatType.MOUNTED)
            ratio *= 2;
        if (unitType.combatType == CombatType.SIEGE &&
                target instanceof City) ratio *= 1.1;
        if (unitType == UnitType.TANK &&
                target instanceof City) ratio *= 0.9;
        if (unitType == UnitType.ANTI_TANK_GUN &&
                target instanceof Unit &&
                ((Unit) target).unitType == UnitType.TANK) ratio *= 1.1;
        return ratio;
    }

    public void attack(Tile tile,GameController gameController) {
        CanGetAttacked target;
        if (tile.getCity() != null) target = tile.getCity();
        else if (tile.getNonCivilian() != null) target = tile.getNonCivilian();
        else if (tile.getCivilian() != null) target = tile.getCivilian();
        else return;
        double ratio = calculateRatio(target);
        attacked = true;
        target.takeDamage(calculateDamage(ratio),civilization,gameController);
        gameController.openNewArea(tile, civilization, null);
        state = UnitState.AWAKE;
        destinationTile = null;
        if (!this.checkToDestroy(gameController) && target.checkToDestroy(gameController) && (!(target instanceof City) || this.getUnitType().range == 1)) this.move(tile, true,gameController);
        if (unitType.combatType != CombatType.MOUNTED || unitType.range > 1) {
            movementPrice = 0;
        }
        if (target instanceof Unit) ((Unit) target).setState(UnitState.AWAKE);
    }

    public int calculateDamage(double ratio) {
        if (ratio >= 1) {
            if (unitType.range <= 1) health -= 16.774 * Math.exp(0.5618 * ratio) /
                    (0.3294 * Math.exp(1.1166 * ratio));
            return (int) (16.774 * Math.exp(0.5618 * ratio));
        } else {
            if (unitType.range <= 1) health -= 16.774 * Math.exp(0.5618 / ratio);
            return (int) (16.774 * Math.exp(0.5618 / ratio) /
                    (0.3294 * Math.exp(1.1166 / ratio)));
        }
    }


    public NonCivilian(Tile tile, Civilization civilization, UnitType unitType) {
        super(tile, civilization, unitType);
    }

    public boolean pillage() {
        boolean result = false;
        if (currentTile.getCivilization() != civilization &&
                currentTile.getImprovement() != null
                && currentTile.getImprovement().getNeedsRepair() < 3) {
            currentTile.getImprovement().setNeedsRepair(3);
            result = true;
        }
        if (currentTile.getCivilization() != civilization &&
                currentTile.getRoad() != null &&
                currentTile.getRoad().getNeedsRepair() < 3) {
            currentTile.getImprovement().setNeedsRepair(3);
            result = true;
        }
        return result;
    }

    public int getFortifiedCycle() {
        return fortifiedCycle;
    }

    public void setFortifiedCycle(int fortifiedCycle) {
        this.fortifiedCycle = fortifiedCycle;
    }

    public Tile getTile() {
        return currentTile;
    }
}
