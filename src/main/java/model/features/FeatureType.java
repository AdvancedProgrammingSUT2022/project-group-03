package model.features;

import model.tiles.TileType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
public enum FeatureType {
    DENSEFOREST("DF"),
    FOREST("FO"),
    ICE("IC"),
    OASIS("OA"),
    PLAIN("PL"),
    DELTA("DE"),
    SWAMP("SW");
    private static final List<FeatureType> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public final String icon;
    FeatureType(String icon)
    {
        this.icon = icon;
    }
    public static FeatureType randomFeature()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
