package model.Units;

import controller.GameController;
import model.CanGetAttacked;
import model.Civilization;
import model.CanAttack;
import model.tiles.Tile;


public class NonCivilian extends Unit implements CanAttack{

    private int fortifiedCycle = 0;

    public UnitType getUnitType() {
        return unitType;
    }


    public void attack(Tile tile) {
        CanGetAttacked target = null;
        if(tile.getCity() != null) target = (CanGetAttacked) tile.getCity();
        else if (tile.getNonCivilian() != null) target = tile.getNonCivilian();
        else if (tile.getCivilian() != null) target = (CanGetAttacked) tile.getCivilian();
        double ratio = (double) getCombatStrength(true) /target.getCombatStrength(false);
        target.takeDamage(calculateDamage(ratio));
        GameController.openNewArea(tile,civilization,null);
        state = UnitState.AWAKE;
        destinationTile = null;
        if(!this.checkToDestroy() && target.checkToDestroy()) this.move(tile,true);

    }
    public int calculateDamage(double ratio){
        if(ratio >= 1) {
            health -= 16.774 * Math.exp(0.5618 * ratio) /  (0.3294 * Math.exp(1.1166 * ratio));
            return (int) (16.774 * Math.exp(0.5618 * ratio));
        }
        else health -= 16.774 * Math.exp(0.5618 / ratio);
        return (int) (16.774 * Math.exp(0.5618 / ratio) / (0.3294 * Math.exp(1.1166 / ratio)));
    }



    private boolean defense(Tile tile)
    {
        return true;
    }
    public NonCivilian(Tile tile, Civilization civilization, UnitType unitType) {
        super(tile, civilization,unitType);
        this.unitType = unitType;

//        switch (unitType)
//        {
//            case
//        }
    }

    private void Fortify() {
        if (fortifiedCycle == 0)
            return;
    }

    public int getFortifiedCycle() {
        return fortifiedCycle;
    }

    public void setFortifiedCycle(int fortifiedCycle) {
        this.fortifiedCycle = fortifiedCycle;
    }
}
