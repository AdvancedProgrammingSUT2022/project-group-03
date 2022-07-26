package model;

import model.tiles.Tile;

import java.io.Serializable;

public class Tasks implements Serializable {
    Tile tile;
    TaskTypes taskTypes;

    public TaskTypes getTaskTypes() {
        return taskTypes;
    }

    public void setTaskTypes(TaskTypes taskTypes) {
        this.taskTypes = taskTypes;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public Tasks(Tile tile, TaskTypes taskTypes) {
        this.tile = tile;
        this.taskTypes = taskTypes;
    }
}
