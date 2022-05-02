package model.improvements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImprovementTypeTest {

    @Test
    void stringToImprovementType() {
        ImprovementType actual = ImprovementType.stringToImprovementType("road");
        ImprovementType expected = null;
        assertNull(actual);

        actual = ImprovementType.stringToImprovementType("railroad");
        assertNull(actual);

        actual = ImprovementType.stringToImprovementType("randomBSGo");
        assertNull(actual);

        actual = ImprovementType.stringToImprovementType("factory");
        expected = ImprovementType.FACTORY;
        assertEquals(expected,actual);
    }
}