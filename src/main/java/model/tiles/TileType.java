package model.tiles;

import model.features.FeatureType;
import model.resources.ResourcesTypes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum TileType {
    DESERT(new FeatureType[]{FeatureType.OASIS, FeatureType.FOREST},"DES",1,-33,0,0,0,
            new ResourcesTypes[]{ResourcesTypes.IRON, ResourcesTypes.GOLD, ResourcesTypes.SILVER, ResourcesTypes.GEMSTONE, ResourcesTypes.MARBLE, ResourcesTypes.COTTON, ResourcesTypes.INCENSE,ResourcesTypes.SHEEP}),
    FLAT(new FeatureType[]{FeatureType.FOREST, FeatureType.DENSEFOREST},"FLA",1,-33,1,1,0,
            new ResourcesTypes[]{ResourcesTypes.IRON,ResourcesTypes.HORSE, ResourcesTypes.COAL,ResourcesTypes.WHEAT,ResourcesTypes.GOLD,ResourcesTypes.GEMSTONE,ResourcesTypes.MARBLE,ResourcesTypes.IVORY,ResourcesTypes.COTTON,ResourcesTypes.INCENSE,ResourcesTypes.SHEEP}),
    GRASSLAND(new FeatureType[]{FeatureType.FOREST, FeatureType.SWAMP},"GRA",1,-33,2,0,0,
            new ResourcesTypes[]{ResourcesTypes.IRON,ResourcesTypes.HORSE,ResourcesTypes.COAL,ResourcesTypes.COW,ResourcesTypes.GOLD,ResourcesTypes.GEMSTONE,ResourcesTypes.MARBLE,ResourcesTypes.COTTON,ResourcesTypes.SHEEP}),
    HILL(new FeatureType[]{FeatureType.FOREST, FeatureType.DENSEFOREST},"HIL",2,25,0,2,0,
            new ResourcesTypes[]{ResourcesTypes.IRON, ResourcesTypes.COAL, ResourcesTypes.DEER, ResourcesTypes.GOLD, ResourcesTypes.SILVER, ResourcesTypes.GEMSTONE, ResourcesTypes.MARBLE, ResourcesTypes.SHEEP}),
    MOUNTAIN(new FeatureType[]{},"MOU",12345,25,0,0,0,
            new ResourcesTypes[]{}),
    OCEAN(new FeatureType[]{},"OCE",12345,25,0,0,0,
            new ResourcesTypes[]{}),
    SNOW(new FeatureType[]{},"SNO",1,-33,0,0,0,
            new ResourcesTypes[]{ResourcesTypes.IRON}),
    TUNDRA(new FeatureType[]{FeatureType.FOREST},"TUN",1,-33,1,0,0,
            new ResourcesTypes[]{ResourcesTypes.IRON,ResourcesTypes.HORSE,ResourcesTypes.DEER,ResourcesTypes.SILVER,ResourcesTypes.GEMSTONE,ResourcesTypes.MARBLE,ResourcesTypes.FUR});
    private static final List<TileType> VALUES = List.of(values());
    private final ResourcesTypes[] RESOURCES;
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public final FeatureType[] featureTypes;
    public final String icon;
    public final int movementPoint;
    public final int combatChange;
    public final int food;
    public final int gold;
    public final int production;
    public static TileType randomTile()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
    TileType(FeatureType[] featureTypes, String icon,int movementPoint,int combatChange, int food, int production, int gold, ResourcesTypes[] resources)
    {
        this.featureTypes = featureTypes;
        this.icon = icon;
        this.movementPoint = movementPoint;
        this.combatChange = combatChange;
        this.food = food;
        this.gold = gold;
        this.production = production;
        this.RESOURCES = resources;
    }
}
