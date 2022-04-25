package model.Units;

import model.Civilization;
import model.resources.Resource;
import model.technologies.TechnologyType;
import model.tiles.Tile;

import java.util.HashMap;


public class NonCivilian extends Unit {
    private static HashMap<UnitType,TechnologyType> prerequisitesTechnologies;
    private static HashMap<UnitType,Integer> range;
    private static HashMap<UnitType,Integer> rangedCombatStrength;
    private static HashMap<UnitType,Integer> combatStrength;
    private static HashMap<UnitType,CombatType> combatType;
    private static HashMap<UnitType,Resource> resources;
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
        super(tile, civilization);
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
