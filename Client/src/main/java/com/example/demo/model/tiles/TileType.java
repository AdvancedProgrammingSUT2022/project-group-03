package com.example.demo.model.tiles;

import com.example.demo.model.features.FeatureType;
import com.example.demo.model.improvements.ImprovementType;
import com.example.demo.model.resources.ResourcesTypes;

import java.util.List;
import java.util.Random;

public enum TileType {
    DESERT(new FeatureType[]{FeatureType.OASIS, FeatureType.DELTA},
            "DES", 1, -33, 0, 0, 0,
            new ResourcesTypes[]{ResourcesTypes.IRON, ResourcesTypes.GOLD,
                    ResourcesTypes.SILVER, ResourcesTypes.GEMSTONE,
                    ResourcesTypes.MARBLE, ResourcesTypes.COTTON,
                    ResourcesTypes.INCENSE, ResourcesTypes.SHEEP},
            new ImprovementType[]{ImprovementType.FARM, ImprovementType.MINE,
                    ImprovementType.PASTURE, ImprovementType.FIELD, ImprovementType.QUARRY,
                    ImprovementType.TRADING_POST, ImprovementType.FACTORY}),
    FLAT(new FeatureType[]{FeatureType.FOREST, FeatureType.JUNGLE},
            "FLA", 1, -33, 1, 1, 0,
            new ResourcesTypes[]{ResourcesTypes.IRON, ResourcesTypes.HORSE,
                    ResourcesTypes.COAL, ResourcesTypes.WHEAT,
                    ResourcesTypes.GOLD, ResourcesTypes.GEMSTONE,
                    ResourcesTypes.MARBLE, ResourcesTypes.IVORY,
                    ResourcesTypes.COTTON, ResourcesTypes.INCENSE,
                    ResourcesTypes.SHEEP},
            new ImprovementType[]{ImprovementType.CAMP, ImprovementType.FARM,
                    ImprovementType.MINE, ImprovementType.PASTURE,
                    ImprovementType.FIELD, ImprovementType.QUARRY,
                    ImprovementType.TRADING_POST, ImprovementType.FACTORY}),
    GRASSLAND(new FeatureType[]{FeatureType.FOREST, FeatureType.SWAMP},
            "GRA", 1, -33, 2, 0, 0,
            new ResourcesTypes[]{ResourcesTypes.IRON, ResourcesTypes.HORSE,
                    ResourcesTypes.COAL, ResourcesTypes.COW,
                    ResourcesTypes.GOLD, ResourcesTypes.GEMSTONE,
                    ResourcesTypes.MARBLE, ResourcesTypes.COTTON,
                    ResourcesTypes.SHEEP},
            new ImprovementType[]{ImprovementType.FARM, ImprovementType.MINE,
                    ImprovementType.PASTURE, ImprovementType.FIELD,
                    ImprovementType.QUARRY, ImprovementType.TRADING_POST,
                    ImprovementType.FACTORY}),
    HILL(new FeatureType[]{FeatureType.FOREST, FeatureType.JUNGLE},
            "HIL", 2, 25, 0, 2, 0,
            new ResourcesTypes[]{ResourcesTypes.IRON, ResourcesTypes.COAL,
                    ResourcesTypes.DEER, ResourcesTypes.GOLD,
                    ResourcesTypes.SILVER, ResourcesTypes.GEMSTONE,
                    ResourcesTypes.MARBLE, ResourcesTypes.SHEEP},
            new ImprovementType[]{ImprovementType.CAMP, ImprovementType.MINE,
                    ImprovementType.PASTURE, ImprovementType.QUARRY}),
    MOUNTAIN(new FeatureType[]{},
            "MOU", 12345, 25, 0, 0, 0,
            new ResourcesTypes[]{},
            new ImprovementType[]{}),
    OCEAN(new FeatureType[]{},
            "OCE", 12345, 25, 0, 0, 0,
            new ResourcesTypes[]{},
            new ImprovementType[]{}),
    SNOW(new FeatureType[]{},
            "SNO", 1, -33, 0, 0, 0,
            new ResourcesTypes[]{ResourcesTypes.IRON},
            new ImprovementType[]{ImprovementType.FACTORY, ImprovementType.MINE}),
    TUNDRA(new FeatureType[]{FeatureType.FOREST},
            "TUN", 1, -33, 1, 0, 0,
            new ResourcesTypes[]{ResourcesTypes.IRON, ResourcesTypes.HORSE,
                    ResourcesTypes.DEER, ResourcesTypes.SILVER,
                    ResourcesTypes.GEMSTONE, ResourcesTypes.MARBLE,
                    ResourcesTypes.FUR},
            new ImprovementType[]{ImprovementType.CAMP, ImprovementType.MINE,
                    ImprovementType.PASTURE, ImprovementType.QUARRY,
                    ImprovementType.TRADING_POST, ImprovementType.FACTORY});
    private static final List<TileType> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public final FeatureType[] featureTypes;
    public final ResourcesTypes[] resourcesTypes;
    public final String icon;
    public final int movementPoint;
    public final int combatChange;
    public final int food;
    public final int gold;
    public final int production;
    public final ImprovementType[] improvementTypes;

    public static TileType randomTile() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    TileType(FeatureType[] featureTypes, String icon, int movementPoint,
             int combatChange, int food, int production, int gold,
             ResourcesTypes[] resources, ImprovementType[] improvementTypes) {
        this.featureTypes = featureTypes;
        this.icon = icon;
        this.movementPoint = movementPoint;
        this.combatChange = combatChange;
        this.food = food;
        this.gold = gold;
        this.production = production;
        this.resourcesTypes = resources;
        this.improvementTypes = improvementTypes;
    }

    public static boolean canContainImprovement(TileType tileType, ImprovementType improvementType) {
        for (ImprovementType type : tileType.improvementTypes)
            if (type == improvementType)
                return true;
        return false;
    }
}
