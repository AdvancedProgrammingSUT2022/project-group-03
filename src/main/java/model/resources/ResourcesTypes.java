package model.resources;

import model.technologies.Technology;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum ResourcesTypes {
    BANANA("BA",ResourcesCategory.BONUS),
    COW("CO",ResourcesCategory.BONUS),
    DEER("DE",ResourcesCategory.BONUS),
    SHEEP("SH",ResourcesCategory.BONUS),
    WHEAT("WH",ResourcesCategory.BONUS),
    COTTON("CT",ResourcesCategory.LUXURY),
    COLOR("CL",ResourcesCategory.LUXURY),
    FUR("FU",ResourcesCategory.LUXURY),
    GEMSTONE("GS",ResourcesCategory.LUXURY),
    GOLD("Au",ResourcesCategory.LUXURY),
    INCENSE("IN",ResourcesCategory.LUXURY),
    IVORY("IV",ResourcesCategory.LUXURY),
    MARBLE("MA",ResourcesCategory.LUXURY),
    SILK("SI",ResourcesCategory.LUXURY),
    SILVER("Ag",ResourcesCategory.LUXURY),
    SUGAR("SU",ResourcesCategory.LUXURY),
    COAL("CL",ResourcesCategory.STRATEGIC),
    HORSE("HO",ResourcesCategory.STRATEGIC),
    IRON("Fe",ResourcesCategory.STRATEGIC);
    private static final List<ResourcesTypes> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public final String icon;
    public final ResourcesCategory resourcesCategory;

    ResourcesTypes(String icon,ResourcesCategory resourcesCategory) {
        this.icon = icon;
        this.resourcesCategory = resourcesCategory;
    }

    public static ResourcesTypes randomResource(){
        return VALUES.get(RANDOM.nextInt(SIZE));
    }


    public boolean isTechnologyUnlocked(ArrayList<Technology> technologies){
        //TODO TECH CHECK
        return true;
    }

}
