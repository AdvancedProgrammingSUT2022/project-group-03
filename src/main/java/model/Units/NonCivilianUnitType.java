package model.Units;

import model.tiles.TileType;

import java.util.List;

public enum NonCivilianUnitType {
    Archer("Arc",2,70),
    ChariotArcher("CAr",4,60),
    Scout("Sco",2,25),
    Spearman("Spm",2,50),
    Warrior("War",2, 40),
    CATAPULT("Cat",2, 100),
    HORSEMAN("Hom",4,80),
    SWORDSMAN("Swm",2,80),
    CROSSBOWMAN("Crm",2,120),
    KNIGHT("Kni",3,150),
    LONG_SWORDS_MAN("LSM",3,150),
    PIKEMAN("Pim",2,100),
    TREBUCHET("Trb",2,170),
    CANON("Can",2,250),
    CAVALRY("Cav",3,260),
    LANCER("Lan",4,220),
    MUSKETMAN("Msm",2,120),
    RIFLEMAN("Rim",2,200),
    ANTI_TANK_GUN("ATG",2,300),
    ARTILLERY("Art",2,420),
    INFANTRY("Inf",2,300),
    PANZER("Pan",5,450),
    TANK("Tan",4,450);
    public final String icon;
    public final int movePoint;
    public final int cost;
    private static final List<NonCivilianUnitType> VALUES = List.of(values());
    NonCivilianUnitType(String icon, int movePoint, int cost){
        this.icon = icon;
        this.movePoint = movePoint;
        this.cost=cost;
    }

    public NonCivilianUnitType iconToType(String icon)
    {
        for (NonCivilianUnitType value : VALUES)
            if(icon.equals(value.icon))
                return value;
        return null;
    }
}