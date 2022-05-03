package model.features;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeatureTest {
    Feature feature = new Feature(FeatureType.JUNGLE);

    @Test
    void getFeatureType() {
        assertEquals(FeatureType.JUNGLE,feature.getFeatureType());
    }

    @Test
    void getCyclesToFinish() {
        assertEquals(-1,feature.getCyclesToFinish());
    }

    @Test
    void setCyclesToFinish() {
        feature.setCyclesToFinish(15);
        assertEquals(15,feature.getCyclesToFinish());
    }
}