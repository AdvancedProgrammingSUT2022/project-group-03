package model.features;

abstract public class Feature {
    protected static int movingPrice;
    protected static int food;
    protected static int production;
    protected static int gold;
    protected static int changingPercentOfStrength;

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
