package model.Units;

import controller.GameController;
import model.Civilization;
import model.Map;
import model.combative;
import model.tiles.Tile;

import java.lang.Math;


public class NonCivilian extends Unit implements combative {

    private int fortifiedCycle = 0;

    public UnitType getUnitType() {
        return unitType;
    }


    public boolean setAttack(Tile tile) {
        Map.TileAndMP[] tileAndMPS = GameController.getMap().findNextTile(civilization,currentTile, movementPrice,unitType.movePoint, destinationTile, false);
        this.destinationTile = tile;
        state = UnitState.ATTACK;
        if (tileAndMPS == null) {
            this.destinationTile = null;
            return false;
        }
        if (tileAndMPS.length == 1){
            this.destinationTile = null;
            state = UnitState.AWAKE;
        }
        Tile tempTile = null;
        for (int i = tileAndMPS.length - 1; i >= 0; i--)
            if (tileAndMPS[i] != null) {
                tempTile = tileAndMPS[i].tile;
                break;
            }
        if (tempTile == null)
            return false;
        if ((tempTile.getNonCivilian() == null && unitType.range== 1) ||
                (tempTile.getCivilian() == null && tempTile.getNonCivilian() == null && unitType.range > 1) ||
                        tempTile.getNonCivilian().getCivilization() == civilization) {
            this.destinationTile = null;
            state = UnitState.AWAKE;
            return false;
        }
        if(tempTile == tile) attack(tile);
        if (unitType.movePoint != tempTile.getMovingPrice())
            this.movementPrice = tempTile.getMovingPrice();
        else
            this.movementPrice = 0;
        this.currentTile = tempTile;
        openNewArea();
        return true;

    }
    public boolean attack(Tile tile) {
        if(unitType.range > 1){
            if(tile.getNonCivilian() == null){

            }


        }
        return false;
    }
        public void defense(){

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
}
