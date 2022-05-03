package model.technologies;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TechnologyTest {

    @Test
    void getTechnologyType() {
        Technology technology = new Technology(TechnologyType.WRITING);
        assertEquals(technology.getTechnologyType(),TechnologyType.WRITING);
        assertEquals(technology.getName(),"WRITING");
    }

    @Test
    void getRemainedCost() {
        Technology technology = new Technology(TechnologyType.WRITING);
        assertEquals(technology.getRemainedCost(),55);
    }

    @Test
    void changeRemainedCost() {
        Technology technology = new Technology(TechnologyType.WRITING);
        technology.changeRemainedCost(-2);
        assertEquals(technology.getRemainedCost(),53);
    }

    @Test
    void getCost() {
        Technology technology = new Technology(TechnologyType.WRITING);
        assertEquals(technology.getCost(),55);
    }

    @Test
    void setRemainedCost() {
        Technology technology = new Technology(TechnologyType.WRITING);
        technology.setRemainedCost(22);
        assertEquals(technology.getRemainedCost(),22);
    }
}