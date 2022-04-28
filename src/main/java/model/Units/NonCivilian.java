package model.Units;

import model.Civilization;
import model.technologies.TechnologyType;
import model.tiles.Tile;

import java.util.HashMap;


public class NonCivilian extends Unit {
    private int fortifiedCycle = 0;

    public UnitType getUnitType() {
        return unitType;
    }


    private void attack(Tile tile) {

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
