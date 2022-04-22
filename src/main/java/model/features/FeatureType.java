package model.features;

import model.tiles.TileType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
public enum FeatureType {
    DENSEFOREST("DF",1,-1,0,25,2),
    FOREST("FO",1,1,0,25,2),
    ICE("IC",0,0,0,0,12345),
    OASIS("OA",3,0,1,-33,1),
    DELTA("DE",2,0,0,-33,1),
    SWAMP("SW",-1,0,0,-33,2);
    private static final List<FeatureType> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public final String icon;
    public final int food;
    public final int production;
    public final int gold;
    public final int combatChange;
    public final int movePoint;
    FeatureType(String icon,int food, int production, int gold, int combatChange, int movePoint)
    {
        this.icon = icon;
        this.food = food;
        this.production = production;
        this.gold = gold;
        this.combatChange = combatChange;
        this.movePoint = movePoint;
    }
    public static FeatureType randomFeature()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
