package com.example.demo.controller.gameController;

import com.example.demo.model.City;
import com.example.demo.model.Map;

import java.util.Objects;

public class MapCommandsController {
    public static void mapShowPosition(int x, int y) {
        GameController.startWindowX = x;
        GameController.startWindowY = y;
        if (GameController.startWindowY > GameController.getMap().getStaticY() - (Map.WINDOW_Y_STATIC - 1))
            GameController.startWindowY = GameController.getMap().getStaticY() - (Map.WINDOW_Y_STATIC - 1);
        if (GameController.startWindowX > GameController.getMap().getStaticX() - (Map.WINDOW_X_STATIC - 1))
            GameController.startWindowX = GameController.getMap().getStaticX() - (Map.WINDOW_X_STATIC - 1);
        if (GameController.startWindowY < 0)
            GameController.startWindowY = 0;
        if (GameController.startWindowX < 0)
            GameController.startWindowX = 0;
    }

}
