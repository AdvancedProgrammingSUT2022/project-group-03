package model.tiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum TileType {
    DESERT,
    FLAT,
    GRASSLAND,
    HILL,
    MOUNTAIN,
    OCEAN,
    SNOW;
    private static final List<TileType> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static TileType randomTile()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}

