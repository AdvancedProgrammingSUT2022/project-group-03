package com.example.demo.model;

import com.example.demo.model.tiles.Tile;

public interface CanAttack {
    void attack(Tile tile);

    int calculateDamage(double ratio);
}
