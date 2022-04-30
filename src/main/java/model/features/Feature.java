package model.features;

public class Feature {
    private FeatureType featureType;
    private int cyclesToFinish;
    public Feature(FeatureType featureType)
    {
        this.featureType = featureType;
        this.cyclesToFinish = -1;
    }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public int getCyclesToFinish() {
        return cyclesToFinish;
    }

    public void setCyclesToFinish(int cyclesToFinish) {
        this.cyclesToFinish = cyclesToFinish;
    }
}
