package model.features;

import model.improvements.ImprovementType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeatureTypeTest {

    @Test
    void randomFeature() {
        assertNotNull(FeatureType.randomFeature());

    }

    @Test
    void canHaveTheImprovement() {
        boolean actual = FeatureType.canHaveTheImprovement(FeatureType.JUNGLE, ImprovementType.MINE);
        assertTrue(actual);
        actual = FeatureType.canHaveTheImprovement(FeatureType.ICE, ImprovementType.MINE);
        assertFalse(actual);
    }

}