package model;

import controller.gameController.GameController;
import model.tiles.Tile;

public interface CanAttack {
    void attack(Tile tile, GameController gameController);

    int calculateDamage(double ratio);
}
