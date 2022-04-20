package model.features;

import model.tiles.TileType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
public enum FeatureType {
    DENSEFOREST,
    FOREST,
    ICE,
    OASIS,
    PLAIN,
    DELTA,
    SWAMP;
    private static final List<FeatureType> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static FeatureType randomFeature()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
