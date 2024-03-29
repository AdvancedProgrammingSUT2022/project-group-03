package com.example.demo.model.Units;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.CanGetAttacked;
import com.example.demo.model.City;
import com.example.demo.model.Civilization;
import com.example.demo.model.CanAttack;
import com.example.demo.model.tiles.Tile;

import java.io.Serial;


public class NonCivilian extends Unit implements CanAttack {
    @Serial
    private static final long serialVersionUID = 1111111119195060495L;
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

    public void attack(Tile tile) {
        CanGetAttacked target;
        if (tile.getCity() != null) target = tile.getCity();
        else if (tile.getNonCivilian() != null) target = tile.getNonCivilian();
        else if (tile.getCivilian() != null) target = tile.getCivilian();
        else return;
        double ratio = calculateRatio(target);
        attacked = true;
        target.takeDamage(calculateDamage(ratio),civilization);
        GameController.openNewArea(tile, civilization, null);
        state = UnitState.AWAKE;
        destinationTile = null;
        if (!this.checkToDestroy() && target.checkToDestroy() && (!(target instanceof City) || this.getUnitType().range == 1)) this.move(tile, true);
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

    @Override
    public double greenBarPercent() {
        return (double) health/100;
    }

    @Override
    public double blueBarPercent() {
        return (double) movementPrice/unitType.movePoint;
    }

    @Override
    public String getHealthDigit() {
        return health + "/100";
    }

    @Override
    public Tile getTile() {
        return currentTile;
    }
}
