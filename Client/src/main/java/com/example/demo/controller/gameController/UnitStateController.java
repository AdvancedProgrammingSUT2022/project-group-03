package com.example.demo.controller.gameController;

import com.example.demo.model.Civilization;
import com.example.demo.model.TaskTypes;
import com.example.demo.model.Tasks;
import com.example.demo.model.Units.*;
import com.example.demo.model.features.FeatureType;
import com.example.demo.model.improvements.Improvement;
import com.example.demo.model.improvements.ImprovementType;
import com.example.demo.model.technologies.TechnologyType;
import com.example.demo.model.tiles.Tile;
import com.example.demo.model.tiles.TileType;

public class UnitStateController {
    public static boolean isMapMoveValid(Tile tile, Civilization civilization) {
        return tile != null &&
                GameController.getCivilizations().get(GameController.getPlayerTurn()) == civilization &&
                tile.getTileType() != TileType.OCEAN &&
                tile.getTileType() != TileType.MOUNTAIN;
    }



    public static int unitUpgradeCheck() {
        Unit selectedUnit = GameController.getSelectedUnit();
        Civilization civilization = GameController.getCivilizations().get(GameController.getPlayerTurn());
        if (selectedUnit == null)
            return 1;
        if (selectedUnit.getCivilization() != civilization)
            return 2;
        if (selectedUnit.getUnitType().combatType == CombatType.CIVILIAN ||
            selectedUnit.getUnitType().getCost() > UnitType.SWORDSMAN.getCost())
            return 3;
        if (civilization.doesContainTechnology(TechnologyType.IRON_WORKING) != 1)
            return 4;
        if (civilization.getGold() < UnitType.SWORDSMAN.getCost() - selectedUnit.getCost())
            return 5;
        if (selectedUnit.getCurrentTile().getCivilization() != civilization)
            return 6;
        return 0;
    }


}
