package model.tiles;

import model.features.FeatureType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum TileType {
    DESERT(new FeatureType[]{FeatureType.OASIS, FeatureType.FOREST}),
    FLAT(new FeatureType[]{FeatureType.FOREST, FeatureType.DENSEFOREST}),
    GRASSLAND(new FeatureType[]{FeatureType.FOREST, FeatureType.SWAMP}),
    HILL(new FeatureType[]{FeatureType.FOREST, FeatureType.DENSEFOREST}),
    MOUNTAIN(new FeatureType[]{}),
    OCEAN(new FeatureType[]{}),
    SNOW(new FeatureType[]{});
    private static final List<TileType> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public final FeatureType[] featureTypes;
    public static TileType randomTile()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
    TileType(FeatureType[] featureTypes)
    {
        this.featureTypes = featureTypes;
    }
}
