package model.Units;

import model.resources.ResourcesTypes;
import model.technologies.TechnologyType;

import java.util.List;
import java.util.Locale;

public enum UnitType {
    ARCHER("Arc", 2, 70, CombatType.ARCHERY, 4, 6, 2, null, TechnologyType.ARCHERY),
    CHARIOT_ARCHER("CAr", 4, 60, CombatType.MOUNTED, 3, 6, 2, ResourcesTypes.HORSE, TechnologyType.THE_WHEEL),
    SCOUT("Sco", 2, 25, CombatType.RECON, 4, 4, 1, null, null),
    SETTLER("SE", 2, 89, CombatType.CIVILIAN, 0, 0, 0, null, null),
    SPEARMAN("Spm", 2, 50, CombatType.MELEE, 7, 7, 1, null, TechnologyType.BRONZE_WORKING),
    WARRIOR("War", 2, 40, CombatType.MELEE, 6, 6, 1, null, null),
    WORKER("WO", 2, 70, CombatType.CIVILIAN, 0, 0, 0, null, null),
    CATAPULT("Cat", 2, 100, CombatType.SIEGE, 4, 14, 2, ResourcesTypes.IRON, TechnologyType.MATHEMATICS),
    HORSEMAN("Hom", 4, 80, CombatType.MOUNTED, 12, 12, 1, ResourcesTypes.HORSE, TechnologyType.HORSEBACK_RIDING),
    SWORDSMAN("Swm", 2, 80, CombatType.MELEE, 11, 11, 1, ResourcesTypes.IRON, TechnologyType.IRON_WORKING),
    CROSSBOWMAN("Crm", 2, 120, CombatType.ARCHERY, 6, 12, 2, null, TechnologyType.MACHINARY),
    KNIGHT("Kni", 3, 150, CombatType.MOUNTED, 18, 18, 1, ResourcesTypes.HORSE, TechnologyType.CHIVALRY),
    LONG_SWORDS_MAN("LSM", 3, 150, CombatType.MELEE, 18, 18, 1, ResourcesTypes.IRON, TechnologyType.STEEL),
    PIKEMAN("Pim", 2, 100, CombatType.MELEE, 10, 10, 1, null, TechnologyType.CIVIL_SERVICE),
    TREBUCHET("Trb", 2, 170, CombatType.SIEGE, 6, 20, 2, ResourcesTypes.IRON, TechnologyType.PHYSICS),
    CANON("Can", 2, 250, CombatType.SIEGE, 10, 26, 2, null, TechnologyType.CHEMISTRY),
    CAVALRY("Cav", 3, 260, CombatType.MOUNTED, 25, 25, 1, ResourcesTypes.HORSE, TechnologyType.MILITARY_SCIENCE),
    LANCER("Lan", 4, 220, CombatType.MOUNTED, 22, 22, 1, ResourcesTypes.HORSE, TechnologyType.METALLURGY),
    MUSKETMAN("Msm", 2, 120, CombatType.GUNPOWDER, 16, 16, 1, null, TechnologyType.GUN_POWDER),
    RIFLEMAN("Rim", 2, 200, CombatType.GUNPOWDER, 25, 25, 1, null, TechnologyType.RIFLING),
    ANTI_TANK_GUN("ATG", 2, 300, CombatType.GUNPOWDER, 32, 32, 1, null, TechnologyType.REPLACEABLE_PARTS),
    ARTILLERY("Art", 2, 420, CombatType.SIEGE, 16, 32, 3, null, TechnologyType.DYNAMITE),
    INFANTRY("Inf", 2, 300, CombatType.GUNPOWDER, 36, 36, 1, null, TechnologyType.REPLACEABLE_PARTS),
    PANZER("Pan", 5, 450, CombatType.ARMORED, 60, 60, 1, null, TechnologyType.COMBUSTION),
    TANK("Tan", 4, 450, CombatType.ARMORED, 50, 50, 1, null, TechnologyType.COMBUSTION);
    public final String icon;
    public final int movePoint;
    public final int cost;
    public final CombatType combatType;
    public static final List<UnitType> VALUES = List.of(values());
    public final int combatStrength;
    public final int rangedCombatStrength;
    public final int range;
    public final TechnologyType technologyRequired;
    public final ResourcesTypes resourcesType;

    UnitType(String icon,
             int movePoint,
             int cost,
             CombatType combatType,
             int combatStrength,
             int rangedCombatStrength,
             int range,
             ResourcesTypes resourcesType,
             TechnologyType technologyRequired) {
        this.icon = icon;
        this.movePoint = movePoint;
        this.cost = cost;
        this.combatType = combatType;
        this.combatStrength = combatStrength;
        this.rangedCombatStrength = rangedCombatStrength;
        this.range = range;
        this.resourcesType = resourcesType;
        this.technologyRequired = technologyRequired;
    }

    public int getDefaultMovementPrice() {
        return movePoint;
    }


    public static UnitType stringToEnum(String string) {
        for (UnitType value : VALUES)
            if (string.toLowerCase(Locale.ROOT).equals(value.toString().toLowerCase(Locale.ROOT)))
                return value;
        return null;
    }


    public ResourcesTypes getResourcesType() {
        return resourcesType;
    }

    public TechnologyType getTechnologyRequired() {
        return technologyRequired;
    }

    public int getCost() {
        return cost;
    }
}