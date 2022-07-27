package com.example.demo.controller.gameController;

import com.example.demo.model.Civilization;
import com.example.demo.model.TaskTypes;
import com.example.demo.model.Tasks;
import com.example.demo.model.Units.*;
import com.example.demo.model.improvements.Improvement;
import com.example.demo.model.improvements.ImprovementType;
import com.example.demo.model.technologies.TechnologyType;
import com.example.demo.model.tiles.Tile;
import com.example.demo.model.tiles.TileType;
import com.example.demo.network.GameHandler;

public class UnitStateController {
    private final GameHandler game;
    private final GameController gameController;

    public UnitStateController(GameHandler game) {
        this.game = game;
        this.gameController = game.getGameController();
    }
    public  boolean isMapMoveValid(Tile tile, Civilization civilization) {
        return tile != null &&
                gameController.getCivilizations().get(gameController.getPlayerTurn()) == civilization &&
                tile.getTileType() != TileType.OCEAN &&
                tile.getTileType() != TileType.MOUNTAIN;
    }

    public  int unitMoveTo(int x, int y, Unit unit) {
        Tile tile = gameController.getMap().coordinatesToTile(x, y);
        if (tile.getTileType() == TileType.OCEAN ||
                tile.getTileType() == TileType.MOUNTAIN)
            return 3;
        gameController.deleteFromUnfinishedTasks(new Tasks(unit.getCurrentTile(), TaskTypes.UNIT));
        unit.setState(UnitState.AWAKE);
        if(unit.getUnitType().combatType== CombatType.CIVILIAN && tile.getNonCivilian()!=null &&
                tile.getCivilian().getCivilization()!= unit.getCivilization())
            return 5;
        if (unit.move(tile, true,gameController,game.getSocketHandlers().get(game.getGameController().getPlayerTurn()))) {
            if(unit.getUnitType().combatType!=CombatType.CIVILIAN && tile.getCivilian()!=null &&
                    tile.getCivilian().getCivilization()!= unit.getCivilization())
            {
                tile.getCivilian().setCivilization(unit.getCivilization());
            }
            return 0;
        }
        return 4;
    }

    public  void unitSleep(Unit unit) {
        gameController.deleteFromUnfinishedTasks(new Tasks(unit.getCurrentTile(), TaskTypes.UNIT));
        unit.setState(UnitState.SLEEP);
    }

    public  int unitUpgrade(Unit unit) {
        Unit selectedUnit =unit;
        Civilization civilization = gameController.getCivilizations().get(gameController.getPlayerTurn());
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
        selectedUnit.getCurrentTile().setNonCivilian(swordsMan,game.getSocketHandlers().get(game.getGameController().getPlayerTurn()));
        civilization.getUnits().add(swordsMan);
        gameController.openNewArea(swordsMan.getCurrentTile(), swordsMan.getCivilization(), swordsMan);
        return 0;
    }

    public  int unitAlert(Unit unit) {
        gameController.deleteFromUnfinishedTasks(new Tasks(unit.getCurrentTile(), TaskTypes.UNIT));
        unit.setState(UnitState.ALERT);
        if (gameController.openNewArea(unit.getCurrentTile(),
                gameController.getCivilizations().get(gameController.getPlayerTurn()),
                unit))
            return 3;
        return 0;
    }

    public  int unitChangeState(int state,Unit unit) {
//        if (GameController.getSelectedUnit() instanceof Civilian)
//            return 3;
        gameController.deleteFromUnfinishedTasks(new Tasks(unit.getCurrentTile(), TaskTypes.UNIT));
        if (state == 0)
            unit.setState(UnitState.FORTIFY);
        if (state == 1)
            unit.setState(UnitState.FORTIFY_UNTIL_FULL_HEALTH);
        if (state == 2)
            unit.setState(UnitState.GARRISON);
        if (state == 3)
           unit.setState(UnitState.AWAKE);
        return 0;
    }

    public  void unitSetupRanged(Unit unit) {
        unit.setState(UnitState.SETUP);
        gameController.deleteFromUnfinishedTasks(new Tasks(unit.getCurrentTile(), TaskTypes.UNIT));
    }

    public  int unitFoundCity(String string,Unit unit) {
        if (unit.getCurrentTile().getCity() != null)
            return 4;
        for (Civilization civilization : gameController.getCivilizations())
            if (civilization.isInTheCivilizationsBorder(unit.getCurrentTile(),gameController))
                return 4;
        gameController.deleteFromUnfinishedTasks(new Tasks(unit.getCurrentTile(), TaskTypes.UNIT));
        ((Civilian) unit).city(string,gameController,game.getTileXAndYFlagSelectUnitController());
        gameController.getCivilizations().get(gameController.getPlayerTurn()).changeHappiness(-1);
        unitDelete(unit);
        gameController.openNewArea(unit.getCurrentTile(),
                gameController.getCivilizations().get(gameController.getPlayerTurn()), null);
        System.out.println("at the end of unitfoundcity function");
        return 0;
    }

    public  int unitCancelMission(Unit unit) {
        if (unit.getDestinationTile() == null &&
                unit.getState() == UnitState.AWAKE) return 3;
        unit.cancelMission();
        return 0;
    }

    public  void unitDelete(Unit unit) {
        gameController.deleteFromUnfinishedTasks(new Tasks(unit.getCurrentTile(), TaskTypes.UNIT));
        gameController.getCivilizations().get(gameController.getPlayerTurn()).getUnits().remove(unit);
        if (unit instanceof NonCivilian)
            unit.getCurrentTile().setNonCivilian(null,game.getSocketHandlers().get(game.getGameController().getPlayerTurn()));
        else
            unit.getCurrentTile().setCivilian(null,game.getSocketHandlers().get(game.getGameController().getPlayerTurn()));
        gameController.getCivilizations().get(gameController.getPlayerTurn()).getUnits().remove(unit);
        gameController.getCivilizations().get(gameController.getPlayerTurn())
                .increaseGold(unit.getUnitType().getCost() / 10);
        gameController.openNewArea(unit.getCurrentTile(), unit.getCivilization(), null);
    }

    public  void unitBuild(ImprovementType improvementType,Unit unit) {
        unit.getCurrentTile().setImprovement
                (new Improvement(improvementType,
                        unit.getCurrentTile()));
        unit.setState(UnitState.BUILDING);
        gameController.getCivilizations().get(gameController.getPlayerTurn())
                .putNotification(improvementType + "'s production started, cycle: ", gameController.getCycle(),game.getSocketHandlers().get(game.getGameController().getPlayerTurn()));
    }

    public  void unitBuildRoad(Unit unit) {
        gameController.deleteFromUnfinishedTasks(new Tasks(unit.getCurrentTile(), TaskTypes.UNIT));
        unit.getCurrentTile().setRoad
                (new Improvement(ImprovementType.ROAD,
                        unit.getCurrentTile()));
        unit.setState(UnitState.BUILDING);
        gameController.getCivilizations().get(gameController.getPlayerTurn())
                .putNotification(ImprovementType.ROAD + "'s production started, cycle: "
                        , gameController.getCycle(),game.getSocketHandlers().get(game.getGameController().getPlayerTurn()));
    }

    public  void unitBuildRailRoad(Unit unit) {
        gameController.deleteFromUnfinishedTasks(new Tasks(unit.getCurrentTile(), TaskTypes.UNIT));
        unit.getCurrentTile().setRoad
                (new Improvement(ImprovementType.RAILROAD,
                        unit.getCurrentTile()));
        unit.setState(UnitState.BUILDING);
        gameController.getCivilizations().get(gameController.getPlayerTurn())
                .putNotification(ImprovementType.RAILROAD + "'s production started, cycle: "
                        , gameController.getCycle(),game.getSocketHandlers().get(game.getGameController().getPlayerTurn()));
    }

    public  void unitRemoveFromTile(boolean isJungle,Unit unit) {
        gameController.deleteFromUnfinishedTasks(new Tasks(unit.getCurrentTile(), TaskTypes.UNIT));
        if (isJungle)
            ((Civilian) unit).remove(1,gameController);
        else
            unit.getCurrentTile().setRoad(null);
    }

    public  void unitRepair(Unit unit) {
        gameController.deleteFromUnfinishedTasks(new Tasks(unit.getCurrentTile(), TaskTypes.UNIT));
        unit.setState(UnitState.REPAIRING);
    }

    public  int unitAttack(int x, int y,Unit unit) {
        if (!(unit instanceof NonCivilian)) return 3;
        if (((NonCivilian) unit).attacked) return 8;
        if (unit.getUnitType().combatType == CombatType.SIEGE &&
                (unit.getState() != UnitState.SETUP ||
                        ((NonCivilian) unit).getFortifiedCycle() < 1))
            return 7;
        if (!gameController.canUnitAttack(gameController.getMap().coordinatesToTile(x, y),unit)) return 5;
        Tile startTile = unit.getCurrentTile();
        if (!unit
                .move(gameController.getMap().coordinatesToTile(x, y), true,gameController,game.getSocketHandlers().get(game.getGameController().getPlayerTurn()))) return 6;
        gameController.deleteFromUnfinishedTasks(new Tasks(startTile, TaskTypes.UNIT));
        if (unit.getCurrentTile() == startTile
                && unit.getUnitType().combatType == CombatType.SIEGE)
            unit.setState(UnitState.SETUP);
        return 0;
    }

    public  int unitPillage(Unit unit) {
        if (!(unit instanceof NonCivilian) ||
                unit.getCivilization() != gameController.getCivilizations()
                        .get(gameController.getPlayerTurn())) return 4;
        if (((NonCivilian) unit).pillage()) return 0;
        return 3;
    }

    public  int skipUnitTask(Unit unit) {
        if (gameController.findTask(new Tasks(unit.getCurrentTile(), TaskTypes.UNIT)) == null)
            return 3;
        gameController.deleteFromUnfinishedTasks(new Tasks(unit.getCurrentTile(), TaskTypes.UNIT));
        return 0;
    }
}
