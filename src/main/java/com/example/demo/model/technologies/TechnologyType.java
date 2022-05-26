package com.example.demo.model.technologies;

import java.util.*;

public enum TechnologyType {
    AGRICULTURE(20),
    ANIMAL_HUSBANDRY(35),
    ARCHERY(35),
    BRONZE_WORKING(55),
    CALENDAR(70),
    MASONRY(55),
    MINING(35),
    POTTERY(35),
    THE_WHEEL(55),
    TRAPPING(55),
    WRITING(55),
    CONSTRUCTION(100),
    HORSEBACK_RIDING(100),
    IRON_WORKING(150),
    MATHEMATICS(100),
    PHILOSOPHY(100),
    CHIVALRY(440),
    CIVIL_SERVICE(400),
    CURRENCY(250),
    EDUCATION(440),
    ENGINEERING(250),
    MACHINARY(440),
    METAL_CASTING(240),
    PHYSICS(440),
    STEEL(440),
    THEOLOGY(250),
    ACOUSTICS(650),
    ARCHAEOLOGY(1300),
    BANKING(650),
    CHEMISTRY(900),
    ECONOMICS(899),
    FERTILIZER(1300),
    GUN_POWDER(680),
    METALLURGY(900),
    MILITARY_SCIENCE(1300),
    PRINTING_PRESS(650),
    RIFLING(1425),
    SCIENTIFIC_THEORY(1300),
    BIOLOGY(1680),
    COMBUSTION(2200),
    DYNAMITE(1900),
    ELECTRICITY(1900),
    RADIO(2200),
    RAILROAD(1900),
    REPLACEABLE_PARTS(1900),
    STEAM_POWER(1680),
    TELEGRAPH(2200);

    public final int cost;
    public static final HashMap<TechnologyType, ArrayList<TechnologyType>> nextTech = new HashMap<>();

    static {
        nextTech.put(AGRICULTURE, new ArrayList<>(Arrays.asList(POTTERY, ANIMAL_HUSBANDRY, ARCHERY, MINING)));
        nextTech.put(ANIMAL_HUSBANDRY, new ArrayList<>(Arrays.asList(TRAPPING, THE_WHEEL)));
        nextTech.put(ARCHERY, new ArrayList<>(List.of(MATHEMATICS)));
        nextTech.put(BRONZE_WORKING, new ArrayList<>(List.of(IRON_WORKING)));
        nextTech.put(CALENDAR, new ArrayList<>(List.of(THEOLOGY)));
        nextTech.put(MASONRY, new ArrayList<>(List.of(CONSTRUCTION)));
        nextTech.put(MINING, new ArrayList<>(Arrays.asList(MASONRY, BRONZE_WORKING)));
        nextTech.put(POTTERY, new ArrayList<>(Arrays.asList(CALENDAR, WRITING)));
        nextTech.put(THE_WHEEL, new ArrayList<>(Arrays.asList(HORSEBACK_RIDING, MATHEMATICS)));
        nextTech.put(TRAPPING, new ArrayList<>(List.of(CIVIL_SERVICE)));
        nextTech.put(WRITING, new ArrayList<>(List.of(PHILOSOPHY)));
        nextTech.put(CONSTRUCTION, new ArrayList<>(List.of(ENGINEERING)));
        nextTech.put(HORSEBACK_RIDING, new ArrayList<>(List.of(CHIVALRY)));
        nextTech.put(IRON_WORKING, new ArrayList<>(List.of(METAL_CASTING)));
        nextTech.put(MATHEMATICS, new ArrayList<>(Arrays.asList(CURRENCY, ENGINEERING)));
        nextTech.put(PHILOSOPHY, new ArrayList<>(Arrays.asList(THEOLOGY, CIVIL_SERVICE)));
        nextTech.put(CHIVALRY, new ArrayList<>(List.of(BANKING)));
        nextTech.put(CIVIL_SERVICE, new ArrayList<>(List.of(CHIVALRY)));
        nextTech.put(CURRENCY, new ArrayList<>(List.of(CHIVALRY)));
        nextTech.put(EDUCATION, new ArrayList<>(Arrays.asList(ACOUSTICS, BANKING)));
        nextTech.put(ENGINEERING, new ArrayList<>(Arrays.asList(MACHINARY, PHYSICS)));
        nextTech.put(MACHINARY, new ArrayList<>(List.of(PRINTING_PRESS)));
        nextTech.put(METAL_CASTING, new ArrayList<>(Arrays.asList(PHYSICS, STEEL)));
        nextTech.put(PHYSICS, new ArrayList<>(Arrays.asList(PRINTING_PRESS, GUN_POWDER)));
        nextTech.put(STEEL, new ArrayList<>(List.of(GUN_POWDER)));
        nextTech.put(THEOLOGY, new ArrayList<>(List.of(EDUCATION)));
        nextTech.put(ACOUSTICS, new ArrayList<>(List.of(SCIENTIFIC_THEORY)));
        nextTech.put(ARCHAEOLOGY, new ArrayList<>(List.of(BIOLOGY)));
        nextTech.put(BANKING, new ArrayList<>(List.of(ECONOMICS)));
        nextTech.put(CHEMISTRY, new ArrayList<>(Arrays.asList(MILITARY_SCIENCE, FERTILIZER)));
        nextTech.put(ECONOMICS, new ArrayList<>(List.of(MILITARY_SCIENCE)));
        nextTech.put(FERTILIZER, new ArrayList<>(List.of(DYNAMITE)));
        nextTech.put(GUN_POWDER, new ArrayList<>(Arrays.asList(CHEMISTRY, METALLURGY)));
        nextTech.put(METALLURGY, new ArrayList<>(List.of(RIFLING)));
        nextTech.put(MILITARY_SCIENCE, new ArrayList<>(List.of(STEAM_POWER)));
        nextTech.put(PRINTING_PRESS, new ArrayList<>(List.of(ECONOMICS)));
        nextTech.put(RIFLING, new ArrayList<>(List.of(DYNAMITE)));
        nextTech.put(SCIENTIFIC_THEORY, new ArrayList<>(Arrays.asList(BIOLOGY, STEAM_POWER)));
        nextTech.put(BIOLOGY, new ArrayList<>(List.of(ELECTRICITY)));
        nextTech.put(COMBUSTION, new ArrayList<>());
        nextTech.put(DYNAMITE, new ArrayList<>(List.of(COMBUSTION)));
        nextTech.put(ELECTRICITY, new ArrayList<>(Arrays.asList(TELEGRAPH, RADIO)));
        nextTech.put(RADIO, new ArrayList<>());
        nextTech.put(RAILROAD, new ArrayList<>(List.of(COMBUSTION)));
        nextTech.put(REPLACEABLE_PARTS, new ArrayList<>(List.of(COMBUSTION)));
        nextTech.put(STEAM_POWER, new ArrayList<>(Arrays.asList(ELECTRICITY, REPLACEABLE_PARTS, RAILROAD)));
        nextTech.put(TELEGRAPH, new ArrayList<>());
    }

    public static final HashMap<TechnologyType, ArrayList<TechnologyType>> prerequisites = new HashMap<>();

    static {
        prerequisites.put(AGRICULTURE, new ArrayList<>());
        prerequisites.put(ANIMAL_HUSBANDRY, new ArrayList<>(List.of(AGRICULTURE)));
        prerequisites.put(ARCHERY, new ArrayList<>(List.of(AGRICULTURE)));
        prerequisites.put(BRONZE_WORKING, new ArrayList<>(List.of(MINING)));
        prerequisites.put(CALENDAR, new ArrayList<>(List.of(POTTERY)));
        prerequisites.put(MASONRY, new ArrayList<>(List.of(MINING)));
        prerequisites.put(MINING, new ArrayList<>(List.of(AGRICULTURE)));
        prerequisites.put(POTTERY, new ArrayList<>(List.of(AGRICULTURE)));
        prerequisites.put(THE_WHEEL, new ArrayList<>(List.of(ANIMAL_HUSBANDRY)));
        prerequisites.put(TRAPPING, new ArrayList<>(List.of(ANIMAL_HUSBANDRY)));
        prerequisites.put(WRITING, new ArrayList<>(List.of(POTTERY)));
        prerequisites.put(CONSTRUCTION, new ArrayList<>(List.of(MASONRY)));
        prerequisites.put(HORSEBACK_RIDING, new ArrayList<>(List.of(THE_WHEEL)));
        prerequisites.put(IRON_WORKING, new ArrayList<>(List.of(BRONZE_WORKING)));
        prerequisites.put(MATHEMATICS, new ArrayList<>(Arrays.asList(THE_WHEEL, ARCHERY)));
        prerequisites.put(PHILOSOPHY, new ArrayList<>(List.of(WRITING)));
        prerequisites.put(CHIVALRY, new ArrayList<>(Arrays.asList(CIVIL_SERVICE, HORSEBACK_RIDING, CURRENCY)));
        prerequisites.put(CIVIL_SERVICE, new ArrayList<>(Arrays.asList(PHILOSOPHY, TRAPPING)));
        prerequisites.put(CURRENCY, new ArrayList<>(List.of(MATHEMATICS)));
        prerequisites.put(EDUCATION, new ArrayList<>(List.of(THEOLOGY)));
        prerequisites.put(ENGINEERING, new ArrayList<>(Arrays.asList(MATHEMATICS, CONSTRUCTION)));
        prerequisites.put(MACHINARY, new ArrayList<>(List.of(ENGINEERING)));
        prerequisites.put(METAL_CASTING, new ArrayList<>(List.of(IRON_WORKING)));
        prerequisites.put(PHYSICS, new ArrayList<>(Arrays.asList(ENGINEERING, METAL_CASTING)));
        prerequisites.put(STEEL, new ArrayList<>(List.of(METAL_CASTING)));
        prerequisites.put(THEOLOGY, new ArrayList<>(Arrays.asList(CALENDAR, PHILOSOPHY)));
        prerequisites.put(ACOUSTICS, new ArrayList<>(List.of(EDUCATION)));
        prerequisites.put(ARCHAEOLOGY, new ArrayList<>(List.of(ACOUSTICS)));
        prerequisites.put(BANKING, new ArrayList<>(Arrays.asList(EDUCATION, CHIVALRY)));
        prerequisites.put(CHEMISTRY, new ArrayList<>(List.of(GUN_POWDER)));
        prerequisites.put(ECONOMICS, new ArrayList<>(Arrays.asList(BANKING, PRINTING_PRESS)));
        prerequisites.put(FERTILIZER, new ArrayList<>(List.of(CHEMISTRY)));
        prerequisites.put(GUN_POWDER, new ArrayList<>(Arrays.asList(PHYSICS, STEEL)));
        prerequisites.put(METALLURGY, new ArrayList<>(List.of(GUN_POWDER)));
        prerequisites.put(MILITARY_SCIENCE, new ArrayList<>(Arrays.asList(ECONOMICS, CHEMISTRY)));
        prerequisites.put(PRINTING_PRESS, new ArrayList<>(Arrays.asList(MACHINARY, PHYSICS)));
        prerequisites.put(RIFLING, new ArrayList<>(List.of(METALLURGY)));
        prerequisites.put(SCIENTIFIC_THEORY, new ArrayList<>(List.of(ACOUSTICS)));
        prerequisites.put(BIOLOGY, new ArrayList<>(Arrays.asList(ARCHAEOLOGY, SCIENTIFIC_THEORY)));
        prerequisites.put(COMBUSTION, new ArrayList<>(Arrays.asList(REPLACEABLE_PARTS, RAILROAD, DYNAMITE)));
        prerequisites.put(DYNAMITE, new ArrayList<>(Arrays.asList(FERTILIZER, RIFLING)));
        prerequisites.put(ELECTRICITY, new ArrayList<>(Arrays.asList(BIOLOGY, STEAM_POWER)));
        prerequisites.put(RADIO, new ArrayList<>(List.of(ELECTRICITY)));
        prerequisites.put(RAILROAD, new ArrayList<>(List.of(STEAM_POWER)));
        prerequisites.put(REPLACEABLE_PARTS, new ArrayList<>(List.of(STEAM_POWER)));
        prerequisites.put(STEAM_POWER, new ArrayList<>(Arrays.asList(SCIENTIFIC_THEORY, MILITARY_SCIENCE)));
        prerequisites.put(TELEGRAPH, new ArrayList<>(List.of(ELECTRICITY)));
    }

    TechnologyType(int cost) {
        this.cost = cost;
    }

    public static final List<TechnologyType> VALUES = List.of(values());

    public static TechnologyType stringToEnum(String string) {
        for (TechnologyType value : VALUES)
            if (string.toLowerCase(Locale.ROOT).equals(value.toString().toLowerCase(Locale.ROOT)))
                return value;
        return null;
    }
}
