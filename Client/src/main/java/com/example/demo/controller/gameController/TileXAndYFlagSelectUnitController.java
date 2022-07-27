package com.example.demo.controller.gameController;

import com.example.demo.model.City;

public class TileXAndYFlagSelectUnitController {



    public static boolean setSelectedCityByPosition(int x, int y) {
        if (GameController.getMap().coordinatesToTile(x, y)!=null &&
                GameController.getMap().coordinatesToTile(x, y).getCity() != null) {
            GameController.setSelectedCity(GameController
                    .getMap().coordinatesToTile(x, y).getCity());
            return true;
        }
        return false;
    }
}
