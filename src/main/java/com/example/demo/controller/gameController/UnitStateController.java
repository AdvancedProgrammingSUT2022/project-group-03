package com.example.demo.controller.gameController;

import com.example.demo.model.City;
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
    public static int unitMoveTo(int x, int y) {
        if (GameController.getSelectedUnit() == null ||
            GameController.getMap().coordinatesToTile(x, y) == null)
            return 1;
        if (GameController.getCivilizations().get(GameController.getPlayerTurn()) !=
            GameController.getSelectedUnit().getCivilization())
            return 2;
        if (GameController.getMap().coordinatesToTile(x, y).getTileType() == TileType.OCEAN ||
            GameController.getMap().coordinatesToTile(x, y).getTileType() == TileType.MOUNTAIN)
            return 3;
        GameController.deleteFromUnfinishedTasks(new Tasks(GameController
            .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        GameController.getSelectedUnit().setState(UnitState.AWAKE);
        if (GameController.getSelectedUnit()
            .move(GameController.getMap().coordinatesToTile(x, y), true))
            return 0;
        return 4;
    }

    public static int unitSleep() {
        if (GameController.getSelectedUnit() == null)
            return 1;
        if (GameController.getSelectedUnit().getCivilization() !=
                GameController.getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        GameController.deleteFromUnfinishedTasks(new Tasks(GameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        GameController.getSelectedUnit().setState(UnitState.SLEEP);
        return 0;
    }

    public static int unitUpgrade() {
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
        NonCivilian swordsMan = new NonCivilian(selectedUnit.getCurrentTile(), selectedUnit.getCivilization(), UnitType.SWORDSMAN);
        civilization.getUnits().remove(selectedUnit);
        selectedUnit.getCurrentTile().setNonCivilian(swordsMan);
        civilization.getUnits().add(swordsMan);
        GameController.setSelectedUnit(swordsMan);
        GameController.openNewArea(swordsMan.getCurrentTile(), swordsMan.getCivilization(), swordsMan);
        return 0;
    }

    public static int unitAlert() {
        if (GameController.getSelectedUnit() == null)
            return 1;
        if (GameController.getSelectedUnit().getCivilization() != GameController
                .getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        GameController.deleteFromUnfinishedTasks(new Tasks(GameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        GameController.getSelectedUnit().setState(UnitState.ALERT);
        if (GameController.openNewArea(GameController.getSelectedUnit().getCurrentTile(),
                GameController.getCivilizations().get(GameController.getPlayerTurn()),
                GameController.getSelectedUnit()))
            return 3;
        return 0;
    }

    public static int unitChangeState(int state) {
        if (GameController.getSelectedUnit() == null)
            return 1;
        if (GameController.getSelectedUnit().getCivilization() != GameController
                .getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
//        if (GameController.getSelectedUnit() instanceof Civilian)
//            return 3;
        GameController.deleteFromUnfinishedTasks(new Tasks(GameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        if (state == 0)
            GameController.getSelectedUnit().setState(UnitState.FORTIFY);
        if (state == 1)
            GameController.getSelectedUnit().setState(UnitState.FORTIFY_UNTIL_FULL_HEALTH);
        if (state == 2)
            GameController.getSelectedUnit().setState(UnitState.GARRISON);
        if (state == 3)
            GameController.getSelectedUnit().setState(UnitState.AWAKE);
        return 0;
    }

    public static int unitSetupRanged() {
        if (GameController.getSelectedUnit() == null)
            return 1;
        if (GameController.getSelectedUnit().getCivilization() != GameController
                .getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        if (GameController.getSelectedUnit().getUnitType().combatType != CombatType.SIEGE)
            return 3;
        GameController.getSelectedUnit().setState(UnitState.SETUP);
        GameController.deleteFromUnfinishedTasks(new Tasks(GameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        return 0;
    }

    public static int unitFoundCity(String string) {
        if (GameController.getSelectedUnit() == null) return 1;
        if (GameController.getSelectedUnit().getCivilization() !=
                GameController.getCivilizations()
                        .get(GameController.getPlayerTurn())) return 2;
        if (GameController.getSelectedUnit().getUnitType() != UnitType.SETTLER)
            return 3;
        if (GameController.getSelectedUnit().getCurrentTile().getCity() != null)
            return 4;
        for (Civilization civilization : GameController.getCivilizations())
            if (civilization.isInTheCivilizationsBorder(GameController
                    .getSelectedUnit().getCurrentTile()))
                return 4;
//        for (Civilization civilization : GameController.getCivilizations())
//            for (City city : civilization.getCities())
//                if (city.getName().equals(string))
//                    return 5;
        GameController.deleteFromUnfinishedTasks(new Tasks(GameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        ((Civilian) GameController.getSelectedUnit()).city(string);
        GameController.getCivilizations().get(GameController.getPlayerTurn()).changeHappiness(-1);
        unitDelete(GameController.getSelectedUnit());
        GameController.openNewArea(GameController.getSelectedUnit().getCurrentTile(),
                GameController.getCivilizations().get(GameController.getPlayerTurn()), null);
        GameController.setSelectedUnit(null);
        return 0;
    }

    public static int unitCancelMission() {
        if (GameController.getSelectedUnit() == null) return 1;
        if (GameController.getSelectedUnit().getCivilization() != GameController.getCivilizations()
                .get(GameController.getPlayerTurn())) return 2;
        if (GameController.getSelectedUnit().getDestinationTile() == null &&
                GameController.getSelectedUnit().getState() == UnitState.AWAKE) return 3;
        GameController.getSelectedUnit().cancelMission();
        return 0;
    }

    public static int unitDelete(Unit unit) {
        if (unit == null)
            return 1;
        if (unit.getCivilization() != GameController
                .getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        GameController.deleteFromUnfinishedTasks(new Tasks(unit.getCurrentTile(), TaskTypes.UNIT));
        GameController.getCivilizations().get(GameController.getPlayerTurn()).getUnits().remove(unit);
        if (unit instanceof NonCivilian)
            unit.getCurrentTile().setNonCivilian(null);
        else
            unit.getCurrentTile().setCivilian(null);
        GameController.getCivilizations().get(GameController.getPlayerTurn()).getUnits().remove(unit);
        GameController.getCivilizations().get(GameController.getPlayerTurn())
                .increaseGold(unit.getUnitType().getCost() / 10);
        GameController.openNewArea(unit.getCurrentTile(), unit.getCivilization(), null);
        return 0;
    }

    public static int unitBuild(ImprovementType improvementType) {
        if (GameController.getSelectedUnit() == null)
            return 1;
        if (GameController.getSelectedUnit().getCivilization() !=
                GameController.getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        if (GameController.getSelectedUnit().getUnitType() != UnitType.WORKER)
            return 3;
        if (!GameController.doesHaveTheRequiredTechnologyToBuildImprovement(improvementType,
                GameController.getSelectedUnit().getCurrentTile(),
                GameController.getSelectedUnit().getCivilization()))
            return 4;
        if (!GameController.canHaveTheImprovement(GameController
                .getSelectedUnit().getCurrentTile(), improvementType))
            return 5;
        GameController.getSelectedUnit().getCurrentTile().setImprovement
                (new Improvement(improvementType,
                        GameController.getSelectedUnit().getCurrentTile()));
        GameController.getSelectedUnit().setState(UnitState.BUILDING);
        GameController.getCivilizations().get(GameController.getPlayerTurn())
                .putNotification(improvementType + "'s production started, cycle: ", GameController.getCycle());
        return 0;
    }

    public static int unitBuildRoad() {
        if (GameController.getSelectedUnit() == null)
            return 1;
        if (GameController.getSelectedUnit().getCivilization() !=
                GameController.getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        if (GameController.getSelectedUnit().getUnitType() != UnitType.WORKER)
            return 3;
        if (GameController.getSelectedUnit().getCurrentTile().getRoad() != null)
            return 6;
        GameController.deleteFromUnfinishedTasks(new Tasks(GameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        GameController.getSelectedUnit().getCurrentTile().setRoad
                (new Improvement(ImprovementType.ROAD,
                        GameController.getSelectedUnit().getCurrentTile()));
        GameController.getSelectedUnit().setState(UnitState.BUILDING);
        GameController.getCivilizations().get(GameController.getPlayerTurn())
                .putNotification(ImprovementType.ROAD + "'s production started, cycle: "
                        , GameController.getCycle());
        return 0;
    }

    public static int unitBuildRailRoad() {
        if (GameController.getSelectedUnit() == null)
            return 1;
        if (GameController.getSelectedUnit().getCivilization() !=
                GameController.getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        if (GameController.getSelectedUnit().getUnitType() != UnitType.WORKER)
            return 3;
        if (GameController.getCivilizations().get(GameController.getPlayerTurn())
                .doesContainTechnology(TechnologyType.RAILROAD) != 1)
            return 4;
        if (GameController.getSelectedUnit().getCurrentTile().getRoad() != null)
            return 6;
        GameController.deleteFromUnfinishedTasks(new Tasks(GameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        GameController.getSelectedUnit().getCurrentTile().setRoad
                (new Improvement(ImprovementType.RAILROAD,
                        GameController.getSelectedUnit().getCurrentTile()));
        GameController.getSelectedUnit().setState(UnitState.BUILDING);
        GameController.getCivilizations().get(GameController.getPlayerTurn())
                .putNotification(ImprovementType.RAILROAD + "'s production started, cycle: "
                        , GameController.getCycle());
        return 0;
    }

    public static int unitRemoveFromTile(boolean isJungle) {
        if (GameController.getSelectedUnit() == null)
            return 1;
        if (GameController.getSelectedUnit().getCivilization() !=
                GameController.getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        if (GameController.getSelectedUnit().getUnitType() != UnitType.WORKER)
            return 3;
        if (isJungle && (GameController.getSelectedUnit()
                .getCurrentTile().getContainedFeature() == null ||
                (GameController.getSelectedUnit().getCurrentTile().getContainedFeature() != null &&
                        GameController.getSelectedUnit().getCurrentTile()
                                .getContainedFeature().getFeatureType() != FeatureType.JUNGLE &&
                        GameController.getSelectedUnit().getCurrentTile()
                                .getContainedFeature().getFeatureType() != FeatureType.FOREST &&
                        GameController.getSelectedUnit().getCurrentTile()
                                .getContainedFeature().getFeatureType() != FeatureType.SWAMP)))
            return 4;
        if (!isJungle && GameController.getSelectedUnit().getCurrentTile().getRoad() == null)
            return 5;
        GameController.deleteFromUnfinishedTasks(new Tasks(GameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        if (isJungle)
            ((Civilian) GameController.getSelectedUnit()).remove(1);
        else
            GameController.getSelectedUnit().getCurrentTile().setRoad(null);
        return 0;
    }

    public static int unitRepair() {
        if (GameController.getSelectedUnit() == null)
            return 1;
        if (GameController.getSelectedUnit().getCivilization() !=
                GameController.getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        if (GameController.getSelectedUnit().getUnitType() != UnitType.WORKER)
            return 3;
        if (GameController.getSelectedUnit().getCurrentTile().getImprovement() == null)
            return 4;
        if (GameController.getSelectedUnit().getCurrentTile().getImprovement().getNeedsRepair() == 0)
            return 5;
        GameController.deleteFromUnfinishedTasks(new Tasks(GameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        GameController.getSelectedUnit().setState(UnitState.REPAIRING);
        return 0;
    }

    public static int unitAttack(int x, int y) {
        if (GameController.getSelectedUnit() == null) return 1;
        if (GameController.getSelectedUnit().getCivilization() != GameController.getCivilizations()
                .get(GameController.getPlayerTurn())) return 2;
        if (!(GameController.getSelectedUnit() instanceof NonCivilian)) return 3;
        if (((NonCivilian) GameController.getSelectedUnit()).attacked) return 8;
        if (GameController.getMap().coordinatesToTile(x, y) == null) return 4;
        if (GameController.getSelectedUnit().getUnitType().combatType == CombatType.SIEGE &&
                (GameController.getSelectedUnit().getState() != UnitState.SETUP ||
                        ((NonCivilian) GameController.getSelectedUnit()).getFortifiedCycle() < 1))
            return 7;
        if (!GameController.canUnitAttack(GameController.getMap().coordinatesToTile(x, y))) return 5;
        Tile startTile = GameController.getSelectedUnit().getCurrentTile();
        if (!GameController.getSelectedUnit()
                .move(GameController.getMap().coordinatesToTile(x, y), true)) return 6;
        GameController.deleteFromUnfinishedTasks(new Tasks(startTile, TaskTypes.UNIT));
        if (GameController.getSelectedUnit().getCurrentTile() == startTile
                && GameController.getSelectedUnit().getUnitType().combatType == CombatType.SIEGE)
            GameController.getSelectedUnit().setState(UnitState.SETUP);
        return 0;
    }

    public static int unitPillage() {
        if (!(GameController.getSelectedUnit() instanceof NonCivilian) ||
                GameController.getSelectedUnit().getCivilization() != GameController.getCivilizations()
                        .get(GameController.getPlayerTurn())) return 4;
        if (((NonCivilian) GameController.getSelectedUnit()).pillage()) return 0;
        return 3;
    }

    public static int skipUnitTask() {
        if (GameController.getSelectedUnit() == null)
            return 1;
        if (GameController.getSelectedUnit().getCivilization() !=
                GameController.getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        if (GameController.findTask(new Tasks(GameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT)) == null)
            return 3;
        GameController.deleteFromUnfinishedTasks(new Tasks(GameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        return 0;
    }
}
