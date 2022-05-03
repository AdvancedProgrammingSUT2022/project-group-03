package model.Units;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitTypeTest {

    @Test
    void getDefaultMovementPrice() {
        assertEquals(UnitType.WORKER.getDefaultMovementPrice(),2);
    }

    @Test
    void stringToEnum() {
        assertEquals(UnitType.stringToEnum("worker"),UnitType.WORKER);
        assertNull(UnitType.stringToEnum("randomBS"));
    }
}