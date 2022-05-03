package model.features;

import model.improvements.ImprovementType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeatureTypeTest {

    @Test
    void randomFeature() {
        FeatureType actual = FeatureType.randomFeature();
        assertNotNull(actual);
    }

    @Test
    void canHaveTheImprovement() {
        boolean actual = FeatureType.canHaveTheImprovement(FeatureType.JUNGLE, ImprovementType.MINE);
        boolean expected = true;
        assertEquals(actual,expected);
        actual = FeatureType.canHaveTheImprovement(FeatureType.ICE, ImprovementType.MINE);
        expected = false;
        assertEquals(actual,expected);
    }
}