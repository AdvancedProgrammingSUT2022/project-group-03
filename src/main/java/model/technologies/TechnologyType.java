package model.technologies;

import java.util.HashMap;

public enum TechnologyType {
    AGRICULTURE(20),
    ANIMAL_HUSBANDARY(35),
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
    public static final HashMap<TechnologyType, TechnologyType[]> nextTech = new HashMap<>();
    static {
        nextTech.put(AGRICULTURE,new TechnologyType[]{POTTERY,ANIMAL_HUSBANDARY,ARCHERY,MINING});
        nextTech.put(ANIMAL_HUSBANDARY, new TechnologyType[]{TRAPPING,THE_WHEEL});
        nextTech.put(ARCHERY,new TechnologyType[]{MATHEMATICS});
        nextTech.put(BRONZE_WORKING,new TechnologyType[]{IRON_WORKING});
        nextTech.put(CALENDAR,new TechnologyType[]{THEOLOGY});
        nextTech.put(MASONRY,new TechnologyType[]{CONSTRUCTION});
        nextTech.put(MINING,new TechnologyType[]{MASONRY,BRONZE_WORKING});
        nextTech.put(POTTERY,new TechnologyType[]{CALENDAR,WRITING});
        nextTech.put(THE_WHEEL,new TechnologyType[]{HORSEBACK_RIDING,MATHEMATICS});
        nextTech.put(TRAPPING,new TechnologyType[]{CIVIL_SERVICE});
        nextTech.put(WRITING,new TechnologyType[]{PHILOSOPHY});
        nextTech.put(CONSTRUCTION,new TechnologyType[]{ENGINEERING});
        nextTech.put(HORSEBACK_RIDING,new TechnologyType[]{CHIVALRY});
        nextTech.put(IRON_WORKING,new TechnologyType[]{METAL_CASTING});
        nextTech.put(MATHEMATICS,new TechnologyType[]{CURRENCY,ENGINEERING});
        nextTech.put(PHILOSOPHY,new TechnologyType[]{THEOLOGY,CIVIL_SERVICE});
        nextTech.put(CHIVALRY,new TechnologyType[]{BANKING});
        nextTech.put(CIVIL_SERVICE,new TechnologyType[]{CHIVALRY});
        nextTech.put(CURRENCY,new TechnologyType[]{CHIVALRY});
        nextTech.put(EDUCATION,new TechnologyType[]{ACOUSTICS,BANKING});
        nextTech.put(ENGINEERING,new TechnologyType[]{MACHINARY,PHYSICS});
        nextTech.put(MACHINARY,new TechnologyType[]{PRINTING_PRESS});
        nextTech.put(METAL_CASTING,new TechnologyType[]{PHYSICS,STEEL});
        nextTech.put(PHYSICS,new TechnologyType[]{PRINTING_PRESS,GUN_POWDER});
        nextTech.put(STEEL,new TechnologyType[]{GUN_POWDER});
        nextTech.put(THEOLOGY,new TechnologyType[]{EDUCATION});
        nextTech.put(ACOUSTICS,new TechnologyType[]{SCIENTIFIC_THEORY});
        nextTech.put(ARCHAEOLOGY,new TechnologyType[]{BIOLOGY});
        nextTech.put(BANKING,new TechnologyType[]{ECONOMICS});
        nextTech.put(CHEMISTRY,new TechnologyType[]{MILITARY_SCIENCE,FERTILIZER});
        nextTech.put(ECONOMICS,new TechnologyType[]{MILITARY_SCIENCE});
        nextTech.put(FERTILIZER,new TechnologyType[]{DYNAMITE});
        nextTech.put(GUN_POWDER,new TechnologyType[]{CHEMISTRY,METALLURGY});
        nextTech.put(METALLURGY,new TechnologyType[]{RIFLING});
        nextTech.put(MILITARY_SCIENCE,new TechnologyType[]{STEAM_POWER});
        nextTech.put(PRINTING_PRESS,new TechnologyType[]{ECONOMICS});
        nextTech.put(RIFLING,new TechnologyType[]{DYNAMITE});
        nextTech.put(SCIENTIFIC_THEORY,new TechnologyType[]{BIOLOGY,STEAM_POWER});
        nextTech.put(BIOLOGY,new TechnologyType[]{ELECTRICITY});
        nextTech.put(COMBUSTION,new TechnologyType[]{});
        nextTech.put(DYNAMITE,new TechnologyType[]{COMBUSTION});
        nextTech.put(ELECTRICITY,new TechnologyType[]{TELEGRAPH,RADIO});
        nextTech.put(RADIO,new TechnologyType[]{});
        nextTech.put(RAILROAD,new TechnologyType[]{COMBUSTION});
        nextTech.put(REPLACEABLE_PARTS,new TechnologyType[]{COMBUSTION});
        nextTech.put(STEAM_POWER,new TechnologyType[]{ELECTRICITY,REPLACEABLE_PARTS,RAILROAD});
        nextTech.put(TELEGRAPH,new TechnologyType[]{});


    }
    TechnologyType(int cost)
    {
        this.cost = cost;
    }
}
