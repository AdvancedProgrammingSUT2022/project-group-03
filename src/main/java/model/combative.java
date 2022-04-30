package model;

import model.tiles.Tile;

public interface combative {
    public boolean attack(Tile tile);

    public void defense();

    public int getCombatStrength(boolean isAttack);
}
