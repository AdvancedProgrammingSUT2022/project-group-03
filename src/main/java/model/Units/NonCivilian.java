package model.Units;

import model.Civilization;
import model.Color;
import model.resources.Resource;
import model.technologies.TechnologyType;
import model.tiles.Tile;

import java.util.HashMap;


public class NonCivilian extends Unit {
    private static HashMap<NonCivilianUnitType, Integer> cost;
    private static HashMap<NonCivilianUnitType,TechnologyType> prerequisitesTechnologies;
    private static HashMap<NonCivilianUnitType,Integer> range;
    private static HashMap<NonCivilianUnitType,Integer> rangedCombatStrength;
    private static HashMap<NonCivilianUnitType,Integer> combatStrength;
    private static HashMap<NonCivilianUnitType,CombatType> combatType;
    private static HashMap<NonCivilianUnitType,Resource> resources;
    private int fortifiedCycle = 0;
    private NonCivilianUnitType unitType;
    private boolean isReady;
    private boolean isGarrisoned;
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

    public NonCivilianUnitType getUnitType() {
        return unitType;
    }

    public static NonCivilian canCreateUnit(Tile tile, Civilization civilization, NonCivilianUnitType unitType)
    {
        if(cost.get(unitType)>civilization.getGold())
            return null;
        return new NonCivilian(tile,civilization,unitType);
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

//        switch (unitType)
//        {
//            case
//        }
    }

    private void Fortify() {
        if (fortifiedCycle == 0)
            return;
    }
    public String getIcon(){
        return " "+Color.getColorByNumber(civilization.getColor()) + unitType.icon;
    }
}
