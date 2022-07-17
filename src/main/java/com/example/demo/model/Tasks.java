package com.example.demo.model;

import com.example.demo.model.tiles.Tile;

public class Tasks {
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
