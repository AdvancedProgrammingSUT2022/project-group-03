package com.example.demo.controller.gameController;

import com.example.demo.network.GameHandler;

public class TileXAndYFlagSelectUnitController {
    private final GameHandler game;
    private final GameController gameController;

    public TileXAndYFlagSelectUnitController(GameHandler game) {
        this.game = game;
        this.gameController = game.getGameController();
    }



}
