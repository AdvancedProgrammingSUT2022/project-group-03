package model.resources;

import model.improvements.ImprovementType;
import model.technologies.Technology;
import model.technologies.TechnologyType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum ResourcesTypes {
    BANANA("BA",ResourcesCategory.BONUS,new TechnologyType[]{},new ImprovementType[]{ImprovementType.FIELD}),
    COW("CO",ResourcesCategory.BONUS,new TechnologyType[]{},new ImprovementType[]{ImprovementType.PASTURE}),
    DEER("DE",ResourcesCategory.BONUS,new TechnologyType[]{},new ImprovementType[]{ImprovementType.CAMP}),
    SHEEP("SH",ResourcesCategory.BONUS,new TechnologyType[]{},new ImprovementType[]{ImprovementType.PASTURE}),
    WHEAT("WH",ResourcesCategory.BONUS,new TechnologyType[]{},new ImprovementType[]{ImprovementType.FACTORY}),
    COTTON("CT",ResourcesCategory.LUXURY,new TechnologyType[]{},new ImprovementType[]{ImprovementType.FIELD}),
    COLOR("CL",ResourcesCategory.LUXURY,new TechnologyType[]{},new ImprovementType[]{ImprovementType.FIELD}),
    FUR("FU",ResourcesCategory.LUXURY,new TechnologyType[]{},new ImprovementType[]{ImprovementType.CAMP}),
    GEMSTONE("GS",ResourcesCategory.LUXURY,new TechnologyType[]{},new ImprovementType[]{ImprovementType.MINE}),
    GOLD("Au",ResourcesCategory.LUXURY,new TechnologyType[]{},new ImprovementType[]{ImprovementType.MINE}),
    INCENSE("IN",ResourcesCategory.LUXURY,new TechnologyType[]{},new ImprovementType[]{ImprovementType.FIELD}),
    IVORY("IV",ResourcesCategory.LUXURY,new TechnologyType[]{},new ImprovementType[]{ImprovementType.CAMP}),
    MARBLE("MA",ResourcesCategory.LUXURY,new TechnologyType[]{},new ImprovementType[]{ImprovementType.MINE}),
    SILK("SI",ResourcesCategory.LUXURY,new TechnologyType[]{},new ImprovementType[]{ImprovementType.FIELD}),
    SILVER("Ag",ResourcesCategory.LUXURY,new TechnologyType[]{},new ImprovementType[]{ImprovementType.MINE}),
    SUGAR("SU",ResourcesCategory.LUXURY,new TechnologyType[]{},new ImprovementType[]{ImprovementType.FIELD}),
    COAL("CL",ResourcesCategory.STRATEGIC,new TechnologyType[]{},new ImprovementType[]{ImprovementType.MINE}),
    HORSE("HO",ResourcesCategory.STRATEGIC,new TechnologyType[]{},new ImprovementType[]{ImprovementType.PASTURE}),
    IRON("Fe",ResourcesCategory.STRATEGIC,new TechnologyType[]{},new ImprovementType[]{ImprovementType.MINE});
    private static final List<ResourcesTypes> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public final String icon;
    public final ResourcesCategory resourcesCategory;
    private final TechnologyType[] technologyTypes;
    private final ImprovementType[] improvementTypes;

    ResourcesTypes(String icon,ResourcesCategory resourcesCategory,TechnologyType[] technologyTypes,ImprovementType[] improvementTypes) {
        this.icon = icon;
        this.resourcesCategory = resourcesCategory;
        this.improvementTypes =  improvementTypes;
        this.technologyTypes = technologyTypes;
    }

    public static ResourcesTypes randomResource(){
        return VALUES.get(RANDOM.nextInt(SIZE));
    }


    public boolean isTechnologyUnlocked(ArrayList<Technology> technologies){
        //TODO TECH CHECK
        return true;
    }

}
