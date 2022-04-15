package model.Units;

import model.Civilization;
import model.technologies.TechnologyType;
import model.tiles.Tile;

import java.util.HashMap;


public class NonCivilian extends Unit {
    private static HashMap<NonCivilianUnitType, Integer> cost;
    private static HashMap<NonCivilianUnitType,TechnologyType> prerequisitesTechnologies;
    private int fortifiedCycle = 0;
    private NonCivilianUnitType unitType;
    private CombatType combatType;
    private int range;
    private int rangedCombatStrength;
    private int combatStrength;
    private boolean isReady;
    private boolean isFortifiedUntilCompleteHealth;
    private boolean isOnAlert = false;
    static
    {
        cost.put(NonCivilianUnitType.Archer, 3);
        //
        //
        //
        //
        //
    }
    public static NonCivilian canCreateUnit(Tile tile, Civilization civilization, NonCivilianUnitType unitType)
    {
            if(cost.get(unitType)>civilization.getGold())
                return null;
            return NonCivilian(tile,);
    }

    private void attack(Tile tile) {

    }
    private boolean defense(Tile tile)
    {
        return true;
    }
    public NonCivilian(Tile tile, Civilization civilization, NonCivilianUnitType unitType) {
        super(tile, civilization);
        this.unitType = unitType;

        switch (unitType)
        {
            case
        }
    }

    private void Fortify() {
        if (fortifiedCycle == 0)
            return;
    }
}
