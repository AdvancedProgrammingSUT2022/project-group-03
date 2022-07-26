package model;

import controller.gameController.GameController;
import model.tiles.Tile;
import network.MySocketHandler;

public interface CanAttack {
    void attack(Tile tile, GameController gameController, MySocketHandler socketHandler);

    int calculateDamage(double ratio);
}
