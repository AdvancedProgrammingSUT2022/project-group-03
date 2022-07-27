package controller.gameController;

import model.City;
import network.GameHandler;

public class TileXAndYFlagSelectUnitController {
    private final GameHandler game;
    private final GameController gameController;

    public TileXAndYFlagSelectUnitController(GameHandler game) {
        this.game = game;
        this.gameController = game.getGameController();
    }



}
