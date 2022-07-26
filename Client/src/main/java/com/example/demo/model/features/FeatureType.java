package com.example.demo.model.features;

import com.example.demo.model.improvements.ImprovementType;
import com.example.demo.model.resources.ResourcesTypes;

import java.util.List;
import java.util.Random;

public enum FeatureType {
    JUNGLE("JU", 1, -1, 0, 25, 2,
            new ImprovementType[]{ImprovementType.MINE,
                    ImprovementType.FIELD},
            new ResourcesTypes[]{ResourcesTypes.BANANA,
                    ResourcesTypes.GEMSTONE,
                    ResourcesTypes.COLOR}),
    FOREST("FO", 1, 1, 0, 25, 2,
            new ImprovementType[]{ImprovementType.CAMP,
                    ImprovementType.LUMBER_MILL,
                    ImprovementType.MINE,
                    ImprovementType.FIELD},
            new ResourcesTypes[]{ResourcesTypes.DEER,
                    ResourcesTypes.FUR,
                    ResourcesTypes.COLOR,
                    ResourcesTypes.SILK}),
    ICE("IC", 0, 0, 0, 0, 12345,
            new ImprovementType[]{},
            new ResourcesTypes[]{}),
    OASIS("OA", 3, 0, 1, -33, 1,
            new ImprovementType[]{},
            new ResourcesTypes[]{}),
    DELTA("DE", 2, 0, 0, -33, 1,
            new ImprovementType[]{ImprovementType.FIELD},
            new ResourcesTypes[]{ResourcesTypes.WHEAT, ResourcesTypes.SUGAR}),
    SWAMP("SW", -1, 0, 0, -33, 2,
            new ImprovementType[]{ImprovementType.MINE, ImprovementType.FIELD},
            new ResourcesTypes[]{ResourcesTypes.SUGAR});
    private static final List<FeatureType> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public final String icon;
    public final int food;
    public final int production;
    public final int gold;
    public final int combatChange;
    public final int movePoint;
    public final ResourcesTypes[] resourcesTypes;
    private final ImprovementType[] improvementTypes;

    FeatureType(String icon, int food, int production, int gold, int combatChange,
                int movePoint, ImprovementType[] improvementTypes, ResourcesTypes[] resourcesTypes) {
        this.icon = icon;
        this.food = food;
        this.production = production;
        this.gold = gold;
        this.combatChange = combatChange;
        this.movePoint = movePoint;
        this.improvementTypes = improvementTypes;
        this.resourcesTypes = resourcesTypes;
    }

    public static FeatureType randomFeature() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public static boolean doesContainImprovement(FeatureType featureType, ImprovementType improvementType) {
        if (featureType == null)
            return false;
        for (ImprovementType type : featureType.improvementTypes)
            if (type == improvementType)
                return true;
        return false;
    }
}
