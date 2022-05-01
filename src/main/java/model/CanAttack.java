package model;

import model.tiles.Tile;

public interface CanAttack {
    public void attack(Tile tile);

    public int calculateDamage(double ratio);
}
