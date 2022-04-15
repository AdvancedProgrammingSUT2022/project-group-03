package model.features;

abstract public class Feature {
    private static int movingPrice;
    private static int food;
    private static int production;
    private static int gold;
    private static int changingPercentOfStrength;
    private FeatureType featureType;
    public Feature(FeatureType featureType)
    {

    }
    public static int getGold() {
        return gold;
    }

    public static int getProduction() {
        return production;
    }

    public static int getFood() {
        return food;
    }

    public static int getChangingPercentOfStrength() {
        return changingPercentOfStrength;
    }

    public static int getMovingPrice() {
        return movingPrice;
    }
}
