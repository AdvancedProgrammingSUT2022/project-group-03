package com.example.demo.view;


import com.example.demo.model.tiles.Tile;

public interface HealthyBeing {
    double greenBarPercent();

    double blueBarPercent();

    String getHealthDigit();

    Tile getTile();
}
