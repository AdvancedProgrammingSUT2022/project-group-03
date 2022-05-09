package model;

import model.tiles.Tile;

public interface CanAttack {
    void attack(Tile tile);

    int calculateDamage(double ratio);
}
