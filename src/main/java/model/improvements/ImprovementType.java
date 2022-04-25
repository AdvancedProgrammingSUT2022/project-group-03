package model.improvements;

import model.technologies.TechnologyType;

public enum ImprovementType {
    CAMP(0,0,0, TechnologyType.TRAPPING),
    FARM(1,0,0,TechnologyType.AGRICULTURE),
    LUMBER_MILL(0,0,1,TechnologyType.CONSTRUCTION),
    MINE(0,0,1,TechnologyType.MINING),
    PASTURE(0,0,0,TechnologyType.ANIMAL_HUSBANDARY),
    FIELD(0,0,0,TechnologyType.CALENDAR), // KESHT VA KAR
    QUARRY(0,0,0,TechnologyType.MASONRY),
    TRADING_POST(0,1,0,TechnologyType.TRAPPING),
    FACTORY(0,0,2,TechnologyType.ENGINEERING);
    public final int food;
    public final int gold;
    public final int production;
    public final TechnologyType prerequisitesTechnologies;
    ImprovementType(int food, int gold, int production, TechnologyType prerequisitesTechnologies)
    {
        this.food= food;
        this.gold=gold;
        this.production=production;
        this.prerequisitesTechnologies = prerequisitesTechnologies;
    }
}
