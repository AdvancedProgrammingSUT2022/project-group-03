package model.Units;

import java.util.List;

public enum UnitType {
    Archer("Arc",2,70,CombatType.ARCHERY),
    ChariotArcher("CAr",4,60,CombatType.MOUNTED),
    Scout("Sco",2,25,CombatType.RECON),
    Settler("SE",2,89,CombatType.CIVILIAN),
    Spearman("Spm",2,50,CombatType.MELEE),
    Warrior("War",2, 40,CombatType.MELEE),
    Worker("WO",2,70,CombatType.CIVILIAN),
    CATAPULT("Cat",2, 100,CombatType.SIEGE),
    HORSEMAN("Hom",4,80,CombatType.MOUNTED),
    SWORDSMAN("Swm",2,80,CombatType.MELEE),
    CROSSBOWMAN("Crm",2,120,CombatType.ARCHERY),
    KNIGHT("Kni",3,150,CombatType.MOUNTED),
    LONG_SWORDS_MAN("LSM",3,150,CombatType.MELEE),
    PIKEMAN("Pim",2,100,CombatType.MELEE),
    TREBUCHET("Trb",2,170,CombatType.SIEGE),
    CANON("Can",2,250,CombatType.SIEGE),
    CAVALRY("Cav",3,260,CombatType.MOUNTED),
    LANCER("Lan",4,220,CombatType.MOUNTED),
    MUSKETMAN("Msm",2,120,CombatType.GUNPOWDER),
    RIFLEMAN("Rim",2,200,CombatType.GUNPOWDER),
    ANTI_TANK_GUN("ATG",2,300,CombatType.GUNPOWDER),
    ARTILLERY("Art",2,420,CombatType.SIEGE),
    INFANTRY("Inf",2,300,CombatType.GUNPOWDER),
    PANZER("Pan",5,450,CombatType.ARMORED),
    TANK("Tan",4,450,CombatType.ARMORED);
    public final String icon;
    public final int movePoint;
    public final int cost;
    public final CombatType combatType;
    private static final List<UnitType> VALUES = List.of(values());
    UnitType(String icon, int movePoint, int cost, CombatType combatType){
        this.icon = icon;
        this.movePoint = movePoint;
        this.cost=cost;
        this.combatType = combatType;
    }

    public String getIcon(){
        return  icon;
    }

    public int getDefaultMovementPrice()
    {
        return movePoint;
    }
}