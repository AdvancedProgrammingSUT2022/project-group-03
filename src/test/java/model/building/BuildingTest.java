package model.building;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildingTest {
    Building building = new Building(BuildingType.WALL);

    @Test
    void getName() {
        assertEquals(building.getName(),"WALL");
    }

    @Test
    void getRemainedCost() {
        building.setRemainedCost(15);
        assertEquals(building.getRemainedCost(), 15);
    }

    @Test
    void getCost() {
        assertEquals(building.getCost(), 80);
    }

    @Test
    void getBuildingType() {
        assertEquals(building.getBuildingType(), BuildingType.WALL);
    }
}