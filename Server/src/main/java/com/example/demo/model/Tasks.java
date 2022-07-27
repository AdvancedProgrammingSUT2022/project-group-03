package com.example.demo.model;

import com.example.demo.model.tiles.Tile;

import java.io.Serial;
import java.io.Serializable;

public class Tasks implements Serializable {
    @Serial
    private static final long serialVersionUID = 7777138846210415395L;
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
