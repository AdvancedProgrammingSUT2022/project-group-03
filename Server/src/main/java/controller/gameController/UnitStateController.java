package controller.gameController;

import model.City;
import model.Civilization;
import model.TaskTypes;
import model.Tasks;
import model.Units.*;
import model.features.FeatureType;
import model.improvements.Improvement;
import model.improvements.ImprovementType;
import model.technologies.TechnologyType;
import model.tiles.Tile;
import model.tiles.TileType;
import network.GameHandler;

public class UnitStateController {
    private final GameHandler game;

    public UnitStateController(GameHandler game) {
        this.game = game;
    }

    public boolean unitMoveTo(int x, int y) {
        if (game.getGameController().getSelectedUnit() == null ||
                game.getGameController().getMap().coordinatesToTile(x, y) == null ||
                game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()) !=
                        game.getGameController().getSelectedUnit().getCivilization() ||
                game.getGameController().getMap().coordinatesToTile(x, y).getTileType() == TileType.OCEAN ||
                game.getGameController().getMap().coordinatesToTile(x, y).getTileType() == TileType.MOUNTAIN)
            return false;
        game.getGameController().deleteFromUnfinishedTasks(new Tasks(game.getGameController()
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        game.getGameController().getSelectedUnit().setState(UnitState.AWAKE);
        return game.getGameController().getSelectedUnit()
                .move(game.getGameController().getMap().coordinatesToTile(x, y), true,game.getGameController());
    }

    public int unitSleep() {
        if (game.getGameController().getSelectedUnit() == null)
            return 1;
        if (game.getGameController().getSelectedUnit().getCivilization() !=
                game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 2;
        game.getGameController().deleteFromUnfinishedTasks(new Tasks(game.getGameController()
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        game.getGameController().getSelectedUnit().setState(UnitState.SLEEP);
        return 0;
    }

    public  int unitUpgrade() {
        Unit selectedUnit = game.getGameController().getSelectedUnit();
        Civilization civilization = game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn());
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
        if(selectedUnit.getCurrentTile().getCivilization()!=civilization)
            return 6;
        NonCivilian swordsMan = new NonCivilian(selectedUnit.getCurrentTile(), selectedUnit.getCivilization(), UnitType.SWORDSMAN);
        civilization.getUnits().remove(selectedUnit);
        selectedUnit.getCurrentTile().setNonCivilian(swordsMan);
        civilization.getUnits().add(swordsMan);
        game.getGameController().setSelectedUnit(swordsMan);
        game.getGameController().openNewArea(swordsMan.getCurrentTile(),swordsMan.getCivilization(),swordsMan);
        return 0;
    }

    public int unitAlert() {
        if (game.getGameController().getSelectedUnit() == null)
            return 1;
        if (game.getGameController().getSelectedUnit().getCivilization() != game.getGameController()
                .getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 2;
        game.getGameController().deleteFromUnfinishedTasks(new Tasks(game.getGameController()
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        game.getGameController().getSelectedUnit().setState(UnitState.ALERT);
        if (game.getGameController().openNewArea(game.getGameController().getSelectedUnit().getCurrentTile(),
                game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()),
                game.getGameController().getSelectedUnit()))
            return 3;
        return 0;
    }

    public int unitChangeState(int state) {
        if (game.getGameController().getSelectedUnit() == null)
            return 1;
        if (game.getGameController().getSelectedUnit().getCivilization() != game.getGameController()
                .getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 2;
        if (game.getGameController().getSelectedUnit() instanceof Civilian)
            return 3;
        game.getGameController().deleteFromUnfinishedTasks(new Tasks(game.getGameController()
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        if (state == 0)
            game.getGameController().getSelectedUnit().setState(UnitState.FORTIFY);
        if (state == 1)
            game.getGameController().getSelectedUnit().setState(UnitState.FORTIFY_UNTIL_FULL_HEALTH);
        if (state == 2)
            game.getGameController().getSelectedUnit().setState(UnitState.GARRISON);
        if (state == 3)
            game.getGameController().getSelectedUnit().setState(UnitState.AWAKE);
        return 0;
    }

    public  int unitSetupRanged() {
        if (game.getGameController().getSelectedUnit() == null)
            return 1;
        if (game.getGameController().getSelectedUnit().getCivilization() != game.getGameController()
                .getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 2;
        if (game.getGameController().getSelectedUnit().getUnitType().combatType != CombatType.SIEGE)
            return 3;
        game.getGameController().getSelectedUnit().setState(UnitState.SETUP);
        game.getGameController().deleteFromUnfinishedTasks(new Tasks(game.getGameController()
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        return 0;
    }

    public int unitFoundCity(String string) {
        if (game.getGameController().getSelectedUnit() == null) return 1;
        if (game.getGameController().getSelectedUnit().getCivilization() !=
                game.getGameController().getCivilizations()
                        .get(game.getGameController().getPlayerTurn())) return 2;
        if (game.getGameController().getSelectedUnit().getUnitType() != UnitType.SETTLER)
            return 3;
        if (game.getGameController().getSelectedUnit().getCurrentTile().getCity() != null)
            return 4;
        for (Civilization civilization : game.getGameController().getCivilizations())
            if (civilization.isInTheCivilizationsBorder(game.getGameController()
                    .getSelectedUnit().getCurrentTile(),game.getGameController()))
                return 4;
        for (Civilization civilization : game.getGameController().getCivilizations())
            for (City city : civilization.getCities())
                if (city.getName().equals(string))
                    return 5;
        game.getGameController().deleteFromUnfinishedTasks(new Tasks(game.getGameController()
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        ((Civilian) game.getGameController().getSelectedUnit()).city(string,game.getGameController(),game.getTileXAndYFlagSelectUnitController());
        game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).changeHappiness(-1);
        unitDelete(game.getGameController().getSelectedUnit());
        game.getGameController().openNewArea(game.getGameController().getSelectedUnit().getCurrentTile(),
                game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()), null);
        game.getGameController().setSelectedUnit(null);
        return 0;
    }

    public  int unitCancelMission() {
        if (game.getGameController().getSelectedUnit() == null) return 1;
        if (game.getGameController().getSelectedUnit().getCivilization() != game.getGameController().getCivilizations()
                .get(game.getGameController().getPlayerTurn())) return 2;
        if (game.getGameController().getSelectedUnit().getDestinationTile() == null &&
                game.getGameController().getSelectedUnit().getState() == UnitState.AWAKE) return 3;
        game.getGameController().getSelectedUnit().cancelMission();
        return 0;
    }

    public  int unitDelete(Unit unit) {
        if (unit == null)
            return 1;
        if (unit.getCivilization() != game.getGameController()
                .getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 2;
        game.getGameController().deleteFromUnfinishedTasks(new Tasks(unit.getCurrentTile(), TaskTypes.UNIT));
        game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getUnits().remove(unit);
        if (unit instanceof NonCivilian)
            unit.getCurrentTile().setNonCivilian(null);
        else
            unit.getCurrentTile().setCivilian(null);
        game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getUnits().remove(unit);
        game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn())
                .increaseGold(unit.getUnitType().getCost() / 10);
        game.getGameController().openNewArea(unit.getCurrentTile(), unit.getCivilization(), null);
        return 0;
    }

    public int unitBuild(ImprovementType improvementType) {
        if (game.getGameController().getSelectedUnit() == null)
            return 1;
        if (game.getGameController().getSelectedUnit().getCivilization() !=
                game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 2;
        if (game.getGameController().getSelectedUnit().getUnitType() != UnitType.WORKER)
            return 3;
        if (!game.getGameController().doesHaveTheRequiredTechnologyToBuildImprovement(improvementType,
                game.getGameController().getSelectedUnit().getCurrentTile(),
                game.getGameController().getSelectedUnit().getCivilization()))
            return 4;
        if (!game.getGameController().canHaveTheImprovement(game.getGameController()
                .getSelectedUnit().getCurrentTile(), improvementType))
            return 5;
        game.getGameController().getSelectedUnit().getCurrentTile().setImprovement
                (new Improvement(improvementType,
                        game.getGameController().getSelectedUnit().getCurrentTile()));
        game.getGameController().getSelectedUnit().setState(UnitState.BUILDING);
        game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn())
                .putNotification(improvementType + "'s production started, cycle: ", game.getGameController().getCycle());
        return 0;
    }

    public  int unitBuildRoad() {
        if (game.getGameController().getSelectedUnit() == null)
            return 1;
        if (game.getGameController().getSelectedUnit().getCivilization() !=
                game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 2;
        if (game.getGameController().getSelectedUnit().getUnitType() != UnitType.WORKER)
            return 3;
        if (game.getGameController().getSelectedUnit().getCurrentTile().getRoad() != null)
            return 6;
        game.getGameController().deleteFromUnfinishedTasks(new Tasks(game.getGameController()
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        game.getGameController().getSelectedUnit().getCurrentTile().setRoad
                (new Improvement(ImprovementType.ROAD,
                        game.getGameController().getSelectedUnit().getCurrentTile()));
        game.getGameController().getSelectedUnit().setState(UnitState.BUILDING);
        game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn())
                .putNotification(ImprovementType.ROAD + "'s production started, cycle: "
                        , game.getGameController().getCycle());
        return 0;
    }

    public  int unitBuildRailRoad() {
        if (game.getGameController().getSelectedUnit() == null)
            return 1;
        if (game.getGameController().getSelectedUnit().getCivilization() !=
                game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 2;
        if (game.getGameController().getSelectedUnit().getUnitType() != UnitType.WORKER)
            return 3;
        if (game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn())
                .doesContainTechnology(TechnologyType.RAILROAD) != 1)
            return 4;
        if (game.getGameController().getSelectedUnit().getCurrentTile().getRoad() != null)
            return 6;
        game.getGameController().deleteFromUnfinishedTasks(new Tasks(game.getGameController()
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        game.getGameController().getSelectedUnit().getCurrentTile().setRoad
                (new Improvement(ImprovementType.RAILROAD,
                        game.getGameController().getSelectedUnit().getCurrentTile()));
        game.getGameController().getSelectedUnit().setState(UnitState.BUILDING);
        game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn())
                .putNotification(ImprovementType.RAILROAD + "'s production started, cycle: "
                        , game.getGameController().getCycle());
        return 0;
    }

    public int unitRemoveFromTile(boolean isJungle) {
        if (game.getGameController().getSelectedUnit() == null)
            return 1;
        if (game.getGameController().getSelectedUnit().getCivilization() !=
                game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 2;
        if (game.getGameController().getSelectedUnit().getUnitType() != UnitType.WORKER)
            return 3;
        if (isJungle && (game.getGameController().getSelectedUnit()
                .getCurrentTile().getContainedFeature() == null ||
                (game.getGameController().getSelectedUnit().getCurrentTile().getContainedFeature() != null &&
                        game.getGameController().getSelectedUnit().getCurrentTile()
                                .getContainedFeature().getFeatureType() != FeatureType.JUNGLE &&
                        game.getGameController().getSelectedUnit().getCurrentTile()
                                .getContainedFeature().getFeatureType() != FeatureType.FOREST &&
                        game.getGameController().getSelectedUnit().getCurrentTile()
                                .getContainedFeature().getFeatureType() != FeatureType.SWAMP)))
            return 4;
        if (!isJungle && game.getGameController().getSelectedUnit().getCurrentTile().getRoad() == null)
            return 5;
        game.getGameController().deleteFromUnfinishedTasks(new Tasks(game.getGameController()
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        if (isJungle)
            ((Civilian) game.getGameController().getSelectedUnit()).remove(1,game.getGameController());
        else
            game.getGameController().getSelectedUnit().getCurrentTile().setRoad(null);
        return 0;
    }

    public int unitRepair() {
        if (game.getGameController().getSelectedUnit() == null)
            return 1;
        if (game.getGameController().getSelectedUnit().getCivilization() !=
                game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 2;
        if (game.getGameController().getSelectedUnit().getUnitType() != UnitType.WORKER)
            return 3;
        if (game.getGameController().getSelectedUnit().getCurrentTile().getImprovement() == null)
            return 4;
        if (game.getGameController().getSelectedUnit().getCurrentTile().getImprovement().getNeedsRepair() == 0)
            return 5;
        game.getGameController().deleteFromUnfinishedTasks(new Tasks(game.getGameController()
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        game.getGameController().getSelectedUnit().setState(UnitState.REPAIRING);
        return 0;
    }

    public int unitAttack(int x, int y) {
        if (game.getGameController().getSelectedUnit() == null) return 1;
        if (game.getGameController().getSelectedUnit().getCivilization() != game.getGameController().getCivilizations()
                .get(game.getGameController().getPlayerTurn())) return 2;
        if (!(game.getGameController().getSelectedUnit() instanceof NonCivilian)) return 3;
        if (((NonCivilian) game.getGameController().getSelectedUnit()).attacked) return 8;
        if (game.getGameController().getMap().coordinatesToTile(x, y) == null) return 4;
        if (game.getGameController().getSelectedUnit().getUnitType().combatType == CombatType.SIEGE &&
                (game.getGameController().getSelectedUnit().getState() != UnitState.SETUP ||
                        ((NonCivilian) game.getGameController().getSelectedUnit()).getFortifiedCycle() < 1))
            return 7;
        if (!game.getGameController().canUnitAttack(game.getGameController().getMap().coordinatesToTile(x, y))) return 5;
        Tile startTile = game.getGameController().getSelectedUnit().getCurrentTile();
        if (!game.getGameController().getSelectedUnit()
                .move(game.getGameController().getMap().coordinatesToTile(x, y), true,game.getGameController())) return 6;
        game.getGameController().deleteFromUnfinishedTasks(new Tasks(startTile, TaskTypes.UNIT));
        if (game.getGameController().getSelectedUnit().getCurrentTile() == startTile
                && game.getGameController().getSelectedUnit().getUnitType().combatType == CombatType.SIEGE)
            game.getGameController().getSelectedUnit().setState(UnitState.SETUP);
        return 0;
    }

    public int unitPillage() {
        if (!(game.getGameController().getSelectedUnit() instanceof NonCivilian) ||
                game.getGameController().getSelectedUnit().getCivilization() != game.getGameController().getCivilizations()
                        .get(game.getGameController().getPlayerTurn())) return 4;
        if (((NonCivilian) game.getGameController().getSelectedUnit()).pillage()) return 0;
        return 3;
    }

    public  int skipUnitTask() {
        if (game.getGameController().getSelectedUnit() == null)
            return 1;
        if (game.getGameController().getSelectedUnit().getCivilization() !=
                game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 2;
        if (game.getGameController().findTask(new Tasks(game.getGameController()
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT)) == null)
            return 3;
        game.getGameController().deleteFromUnfinishedTasks(new Tasks(game.getGameController()
                .getSelectedUnit().getCurrentTile(), TaskTypes.UNIT));
        return 0;
    }
}
