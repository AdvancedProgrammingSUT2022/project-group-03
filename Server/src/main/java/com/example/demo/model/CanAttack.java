package com.example.demo.model;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.tiles.Tile;
import com.example.demo.network.MySocketHandler;

public interface CanAttack {
    void attack(Tile tile, GameController gameController, MySocketHandler socketHandler);

    int calculateDamage(double ratio);
}
