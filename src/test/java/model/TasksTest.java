package model;

import model.tiles.Tile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TasksTest {
    @Mock
    Tile tile;
    @Mock Tile anotherTile;
    Tasks tasks = new Tasks(tile, TaskTypes.UNIT);

    @Test
    void setTaskTypes() {
        tasks.setTaskTypes(TaskTypes.TECHNOLOGY_PROJECT);
        assertEquals(TaskTypes.TECHNOLOGY_PROJECT,tasks.getTaskTypes());
    }

    @Test
    void setTile() {
        tasks.setTile(anotherTile);
        assertEquals(tasks.getTile(),anotherTile);
    }
}