package com.example.demo.model.improvements;

import com.example.demo.model.technologies.TechnologyType;

import java.util.List;
import java.util.Locale;

public enum ImprovementType {
    CAMP(0, 0, 0, TechnologyType.TRAPPING, "Cp"),
    FARM(1, 0, 0, TechnologyType.AGRICULTURE, "Fm"),
    LUMBER_MILL(0, 0, 1, TechnologyType.CONSTRUCTION, "LM"),
    MINE(0, 0, 1, TechnologyType.MINING, "Mi"),
    PASTURE(0, 0, 0, TechnologyType.ANIMAL_HUSBANDRY, "Pa"),
    FIELD(0, 0, 0, TechnologyType.CALENDAR, "Fi"), // KESHT VA KAR
    QUARRY(0, 0, 0, TechnologyType.MASONRY, "Qu"),
    TRADING_POST(0, 1, 0, TechnologyType.TRAPPING, "TP"),
    FACTORY(0, 0, 2, TechnologyType.ENGINEERING, "Fc"),
    ROAD(0, -1, 0, null, "r"),
    RAILROAD(0, -1, 0, TechnologyType.STEAM_POWER, "R");
    public final int food;
    public final int gold;
    public final int production;
    public final String icon;
    public static final List<ImprovementType> VALUES = List.of(values());
    public final TechnologyType prerequisitesTechnologies;

    ImprovementType(int food, int gold, int production, TechnologyType prerequisitesTechnologies, String icon) {
        this.food = food;
        this.gold = gold;
        this.production = production;
        this.prerequisitesTechnologies = prerequisitesTechnologies;
        this.icon = icon;
    }

    public static ImprovementType stringToImprovementType(String string) {
        if (string.equals("road") || string.equals("railroad"))
            return null;
        for (ImprovementType value : VALUES)
            if (value.toString().toLowerCase(Locale.ROOT).equals(string.toLowerCase(Locale.ROOT)))
                return value;
        return null;
    }
}
