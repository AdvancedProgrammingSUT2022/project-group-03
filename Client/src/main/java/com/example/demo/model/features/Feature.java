package com.example.demo.model.features;

import java.io.Serializable;

public class Feature implements Serializable {
    private final FeatureType featureType;
    private int cyclesToFinish;

    public Feature(FeatureType featureType) {
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

    @Override
    public String toString() {
        return this.getFeatureType().toString();
    }
}
