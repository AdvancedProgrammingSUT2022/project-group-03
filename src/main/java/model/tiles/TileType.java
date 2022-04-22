package model.tiles;

import model.features.FeatureType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum TileType {
    DESERT(new FeatureType[]{FeatureType.OASIS, FeatureType.FOREST},"DES"),
    FLAT(new FeatureType[]{FeatureType.FOREST, FeatureType.DENSEFOREST},"FLA"),
    GRASSLAND(new FeatureType[]{FeatureType.FOREST, FeatureType.SWAMP},"GRA"),
    HILL(new FeatureType[]{FeatureType.FOREST, FeatureType.DENSEFOREST},"HIL"),
    MOUNTAIN(new FeatureType[]{},"MOU"),
    OCEAN(new FeatureType[]{},"OCE"),
    SNOW(new FeatureType[]{},"SNO");
    private static final List<TileType> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public final FeatureType[] featureTypes;
    public final String icon;
    public static TileType randomTile()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
    TileType(FeatureType[] featureTypes, String icon)
    {
        this.featureTypes = featureTypes;
        this.icon = icon;
    }
}
