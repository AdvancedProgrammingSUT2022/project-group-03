package model.Units;

public enum NonCivilianUnitType {
    Archer("Arc",2),
    ChariotArcher("CAr",4),
    Scout("Sco",2),
    Spearman("Spm",2),
    Warrior("War",2),
    CATAPULT("Cat",2),
    CROSSBOWMAN("Crm",2),
    HORSEMAN("Hom",4),
    KNIGHT("Kni",3),
    LONG_SWORDS_MAN("LSM",3),
    PIKEMAN("Pim",2),
    SWORDSMAN("Swm",2),
    TREBUCHET("Trb",2),
    ANTI_TANK_GUN("ATG",2),
    ARTILLERY("Art",2),
    INFANTRY("Inf",2),
    PANZER("Pan",5),
    TANK("Tan",4),
    CANON("Can",2),
    CAVALRY("Cav",3),
    LANCER("Lan",4),
    MUSKETMAN("Msm",2),
    RIFLEMAN("Rim",2);
    public final String icon;
    public int movePoint;
    NonCivilianUnitType(String icon, int movePoint){
        this.icon = icon;
        this.movePoint = movePoint;
    }
}