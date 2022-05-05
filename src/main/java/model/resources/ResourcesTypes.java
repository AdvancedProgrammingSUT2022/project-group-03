package model.resources;

import controller.GameController;
import model.Civilization;
import model.improvements.ImprovementType;
import model.Units.UnitType;
import model.technologies.Technology;
import model.technologies.TechnologyType;
import model.tiles.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public enum ResourcesTypes {
    BANANA("BA",ResourcesCategory.BONUS,null,ImprovementType.FIELD,1,0,0),
    COW("CO",ResourcesCategory.BONUS,null,ImprovementType.PASTURE,1,0,0),
    DEER("DE",ResourcesCategory.BONUS,null,ImprovementType.CAMP,1,0,0),
    SHEEP("SH",ResourcesCategory.BONUS,null,ImprovementType.PASTURE,2,0,0),
    WHEAT("WH",ResourcesCategory.BONUS,null,ImprovementType.FARM,1,0,0),
    COTTON("CT",ResourcesCategory.LUXURY,null,ImprovementType.FIELD,0,0,2),
    COLOR("CR",ResourcesCategory.LUXURY,null,ImprovementType.FIELD,0,0,2),
    FUR("FU",ResourcesCategory.LUXURY,null,ImprovementType.CAMP,0,0,2),
    GEMSTONE("GS",ResourcesCategory.LUXURY,null,ImprovementType.MINE,0,0,3),
    GOLD("Au",ResourcesCategory.LUXURY,null,ImprovementType.MINE,0,0,2),
    INCENSE("IN",ResourcesCategory.LUXURY,null,ImprovementType.FIELD,0,0,2),
    IVORY("IV",ResourcesCategory.LUXURY,null,ImprovementType.CAMP,0,0,2),
    MARBLE("MA",ResourcesCategory.LUXURY,null,ImprovementType.MINE,0,0,2),
    SILK("SI",ResourcesCategory.LUXURY,null,ImprovementType.FIELD,0,0,2),
    SILVER("Ag",ResourcesCategory.LUXURY,null,ImprovementType.MINE,0,0,2),
    SUGAR("SU",ResourcesCategory.LUXURY,null,ImprovementType.FIELD,0,0,2),
    COAL("CL",ResourcesCategory.STRATEGIC,TechnologyType.SCIENTIFIC_THEORY,ImprovementType.MINE,0,1,0),
    HORSE("HO",ResourcesCategory.STRATEGIC,TechnologyType.ANIMAL_HUSBANDRY,ImprovementType.PASTURE,0,1,0),
    IRON("Fe",ResourcesCategory.STRATEGIC,TechnologyType.IRON_WORKING,ImprovementType.MINE,0,1,0);
    public static final List<ResourcesTypes> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public final String icon;
    public final ResourcesCategory resourcesCategory;
    public final TechnologyType technologyTypes;
    public final ImprovementType improvementType;
    public final int gold;
    public final int food;
    public final int production;

    ResourcesTypes(String icon,ResourcesCategory resourcesCategory,TechnologyType technologyTypes,ImprovementType improvementTypes,int food,int production,int gold) {
        this.icon = icon;
        this.resourcesCategory = resourcesCategory;
        this.improvementType =  improvementTypes;
        this.technologyTypes = technologyTypes;
        this.gold = gold;
        this.food = food;
        this.production = production;
    }

    public static ResourcesTypes randomResource(){
        return VALUES.get(RANDOM.nextInt(SIZE));
    }


    public boolean isTechnologyUnlocked(Civilization civilization, Tile tile){
        if(technologyTypes != null) {
            boolean found = false;
            for (Technology research : civilization.getResearches()) {
                if (research.getTechnologyType() == technologyTypes) {
                    found = true;
                    break;
                }
            }
            if(!found) return false;
        }
        return GameController.doesHaveTheRequiredTechnologyToBuildImprovement(improvementType, tile, civilization);
    }
    public static ResourcesTypes stringToEnum(String string)
    {
        for (ResourcesTypes value : VALUES)
            if (string.toLowerCase(Locale.ROOT).equals(value.toString().toLowerCase(Locale.ROOT)))
                return value;
        return null;
    }

    public ImprovementType getImprovementType() {
        return improvementType;
    }

    public int getFood() {
        return food;
    }

    public ResourcesCategory getResourcesCategory() {
        return resourcesCategory;
    }
}
