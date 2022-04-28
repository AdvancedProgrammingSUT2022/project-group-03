package model.resources;

import model.technologies.Technology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum ResourcesTypes {
    BANANA("BA"),
    COW("CO"),
    DEER("DE"),
    SHEEP("SH"),
    WHEAT("WH"),
    COTTON("CO"),
    COLOR("CL"),
    FUR("FU"),
    GEMSTONE("GS"),
    GOLD("Au"),
    INCENSE("IN"),
    IVORY("IV"),
    MARBLE("MA"),
    SILK("SI"),
    SILVER("Ag"),
    SUGAR("SU"),
    COAL("CL"),
    HORSE("HO"),
    IRON("Fe");
    private static final List<ResourcesTypes> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public final String icon;

    ResourcesTypes(String icon) {
        this.icon = icon;
    }

    public static ResourcesTypes randomResource(){
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public boolean IsTechnologyUnlocked(ArrayList<Technology> technologies){
        //TODO TECH CHECK
        return true;
    }

}
