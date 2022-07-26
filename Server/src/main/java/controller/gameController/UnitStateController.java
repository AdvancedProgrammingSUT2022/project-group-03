package  controller.gameController;

import model.Civilization;
import model.TaskTypes;
import model.Tasks;
import model.Units.*;
import model.improvements.Improvement;
import model.improvements.ImprovementType;
import model.technologies.TechnologyType;
import model.tiles.Tile;
import model.tiles.TileType;
import network.GameHandler;

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

    public  int unitMoveTo(int x, int y) {
        Tile tile = gameController.getMap().coordinatesToTile(x, y);
        if (tile.getTileType() == TileType.OCEAN ||
                tile.getTileType() == TileType.MOUNTAIN)
            return 3;
        gameController.deleteFromUnfinishedTasks(new Tasks(gameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        gameController.getSelectedUnit().setState(UnitState.AWAKE);
        if(gameController.getSelectedUnit().getUnitType().combatType==CombatType.CIVILIAN && tile.getNonCivilian()!=null &&
                tile.getCivilian().getCivilization()!= gameController.getSelectedUnit().getCivilization())
            return 5;
        if (gameController.getSelectedUnit().move(tile, true)) {
            if(gameController.getSelectedUnit().getUnitType().combatType!=CombatType.CIVILIAN && tile.getCivilian()!=null &&
                    tile.getCivilian().getCivilization()!= gameController.getSelectedUnit().getCivilization())
            {
                tile.getCivilian().setCivilization(gameController.getSelectedUnit().getCivilization());
            }
            return 0;
        }
        return 4;
    }

    public  void unitSleep() {
        gameController.deleteFromUnfinishedTasks(new Tasks(gameController
            .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        gameController.getSelectedUnit().setState(UnitState.SLEEP);
    }

    public  int unitUpgradeCheck() {
        Unit selectedUnit = gameController.getSelectedUnit();
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
        return 0;
    }

    public  int unitUpgrade() {
        Unit selectedUnit = gameController.getSelectedUnit();
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
        selectedUnit.getCurrentTile().setNonCivilian(swordsMan);
        civilization.getUnits().add(swordsMan);
        gameController.setSelectedUnit(swordsMan);
        gameController.openNewArea(swordsMan.getCurrentTile(), swordsMan.getCivilization(), swordsMan);
        return 0;
    }

    public  int unitAlert() {
        gameController.deleteFromUnfinishedTasks(new Tasks(gameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        gameController.getSelectedUnit().setState(UnitState.ALERT);
        if (gameController.openNewArea(gameController.getSelectedUnit().getCurrentTile(),
                gameController.getCivilizations().get(gameController.getPlayerTurn()),
                gameController.getSelectedUnit()))
            return 3;
        return 0;
    }

    public  int unitChangeState(int state) {
//        if (GameController.getSelectedUnit() instanceof Civilian)
//            return 3;
        gameController.deleteFromUnfinishedTasks(new Tasks(gameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        if (state == 0)
            gameController.getSelectedUnit().setState(UnitState.FORTIFY);
        if (state == 1)
            gameController.getSelectedUnit().setState(UnitState.FORTIFY_UNTIL_FULL_HEALTH);
        if (state == 2)
            gameController.getSelectedUnit().setState(UnitState.GARRISON);
        if (state == 3)
            gameController.getSelectedUnit().setState(UnitState.AWAKE);
        return 0;
    }

    public  void unitSetupRanged() {
        gameController.getSelectedUnit().setState(UnitState.SETUP);
        gameController.deleteFromUnfinishedTasks(new Tasks(gameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
    }

    public  int unitFoundCity(String string) {
        if (gameController.getSelectedUnit().getCurrentTile().getCity() != null)
            return 4;
        for (Civilization civilization : gameController.getCivilizations())
            if (civilization.isInTheCivilizationsBorder(gameController
                    .getSelectedUnit().getCurrentTile(),gameController))
                return 4;
        gameController.deleteFromUnfinishedTasks(new Tasks(gameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        ((Civilian) gameController.getSelectedUnit()).city(string);
        gameController.getCivilizations().get(gameController.getPlayerTurn()).changeHappiness(-1);
        unitDelete(gameController.getSelectedUnit());
        gameController.openNewArea(gameController.getSelectedUnit().getCurrentTile(),
                gameController.getCivilizations().get(gameController.getPlayerTurn()), null);
        gameController.setSelectedUnit(null);
        return 0;
    }

    public  int unitCancelMission() {
        if (gameController.getSelectedUnit().getDestinationTile() == null &&
                gameController.getSelectedUnit().getState() == UnitState.AWAKE) return 3;
        gameController.getSelectedUnit().cancelMission();
        return 0;
    }

    public  void unitDelete(Unit unit) {
        gameController.deleteFromUnfinishedTasks(new Tasks(unit.getCurrentTile(), TaskTypes.UNIT));
        gameController.getCivilizations().get(gameController.getPlayerTurn()).getUnits().remove(unit);
        if (unit instanceof NonCivilian)
            unit.getCurrentTile().setNonCivilian(null);
        else
            unit.getCurrentTile().setCivilian(null);
        gameController.getCivilizations().get(gameController.getPlayerTurn()).getUnits().remove(unit);
        gameController.getCivilizations().get(gameController.getPlayerTurn())
                .increaseGold(unit.getUnitType().getCost() / 10);
        gameController.openNewArea(unit.getCurrentTile(), unit.getCivilization(), null);
    }

    public  void unitBuild(ImprovementType improvementType) {
        gameController.getSelectedUnit().getCurrentTile().setImprovement
                (new Improvement(improvementType,
                        gameController.getSelectedUnit().getCurrentTile()));
        gameController.getSelectedUnit().setState(UnitState.BUILDING);
        gameController.getCivilizations().get(gameController.getPlayerTurn())
                .putNotification(improvementType + "'s production started, cycle: ", gameController.getCycle());
    }

    public  void unitBuildRoad() {
        gameController.deleteFromUnfinishedTasks(new Tasks(gameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        gameController.getSelectedUnit().getCurrentTile().setRoad
                (new Improvement(ImprovementType.ROAD,
                        gameController.getSelectedUnit().getCurrentTile()));
        gameController.getSelectedUnit().setState(UnitState.BUILDING);
        gameController.getCivilizations().get(gameController.getPlayerTurn())
                .putNotification(ImprovementType.ROAD + "'s production started, cycle: "
                        , gameController.getCycle());
    }

    public  void unitBuildRailRoad() {
        gameController.deleteFromUnfinishedTasks(new Tasks(gameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        gameController.getSelectedUnit().getCurrentTile().setRoad
                (new Improvement(ImprovementType.RAILROAD,
                        gameController.getSelectedUnit().getCurrentTile()));
        gameController.getSelectedUnit().setState(UnitState.BUILDING);
        gameController.getCivilizations().get(gameController.getPlayerTurn())
                .putNotification(ImprovementType.RAILROAD + "'s production started, cycle: "
                        , gameController.getCycle());
    }

    public  void unitRemoveFromTile(boolean isJungle) {
        gameController.deleteFromUnfinishedTasks(new Tasks(gameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        if (isJungle)
            ((Civilian) gameController.getSelectedUnit()).remove(1);
        else
            gameController.getSelectedUnit().getCurrentTile().setRoad(null);
    }

    public  void unitRepair() {
        gameController.deleteFromUnfinishedTasks(new Tasks(gameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        gameController.getSelectedUnit().setState(UnitState.REPAIRING);
    }

    public  int unitAttack(int x, int y) {
        if (!(gameController.getSelectedUnit() instanceof NonCivilian)) return 3;
        if (((NonCivilian) gameController.getSelectedUnit()).attacked) return 8;
        if (gameController.getSelectedUnit().getUnitType().combatType == CombatType.SIEGE &&
                (gameController.getSelectedUnit().getState() != UnitState.SETUP ||
                        ((NonCivilian) gameController.getSelectedUnit()).getFortifiedCycle() < 1))
            return 7;
        if (!gameController.canUnitAttack(gameController.getMap().coordinatesToTile(x, y))) return 5;
        Tile startTile = gameController.getSelectedUnit().getCurrentTile();
        if (!gameController.getSelectedUnit()
                .move(gameController.getMap().coordinatesToTile(x, y), true)) return 6;
        gameController.deleteFromUnfinishedTasks(new Tasks(startTile, TaskTypes.UNIT));
        if (gameController.getSelectedUnit().getCurrentTile() == startTile
                && gameController.getSelectedUnit().getUnitType().combatType == CombatType.SIEGE)
            gameController.getSelectedUnit().setState(UnitState.SETUP);
        return 0;
    }

    public  int unitPillage() {
        if (!(gameController.getSelectedUnit() instanceof NonCivilian) ||
                gameController.getSelectedUnit().getCivilization() != gameController.getCivilizations()
                        .get(gameController.getPlayerTurn())) return 4;
        if (((NonCivilian) gameController.getSelectedUnit()).pillage()) return 0;
        return 3;
    }

    public  int skipUnitTask() {
        if (gameController.findTask(new Tasks(gameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT)) == null)
            return 3;
        gameController.deleteFromUnfinishedTasks(new Tasks(gameController
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        return 0;
    }
}
