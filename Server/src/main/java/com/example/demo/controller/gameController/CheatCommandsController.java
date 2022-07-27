package com.example.demo.controller.gameController;

import com.example.demo.model.City;
import com.example.demo.model.Units.*;
import com.example.demo.model.improvements.Improvement;
import com.example.demo.model.improvements.ImprovementType;
import com.example.demo.model.resources.ResourcesTypes;
import com.example.demo.model.technologies.Technology;
import com.example.demo.model.technologies.TechnologyType;
import com.example.demo.model.tiles.Tile;
import com.example.demo.network.GameHandler;

public class CheatCommandsController {
    private final GameHandler game;

    public CheatCommandsController(GameHandler game) {
        this.game = game;
    }
    public  void openMap() {
        for (int i = 0; i < game.getGameController().getMap().getX(); i++)
            for (int j = 0; j < game.getGameController().getMap().getY(); j += 2)
                game.getGameController().openNewArea(game.getGameController().getMap().coordinatesToTile(i, j),
                        game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()), null);
    }

    public  int cheatProduction(int number,City city) {
        if (city == null) return 1;
        if (city.getCivilization() !=
                game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 2;
        city.productionCheat += number;
        return 0;
    }

    public  void cheatResource(int number, ResourcesTypes resourcesTypes) {
        int tempNumber = 0;
        if (game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn())
                .getResourcesAmount().containsKey(resourcesTypes))
            tempNumber = game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn())
                    .getResourcesAmount().get(resourcesTypes);
        game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn())
                .getResourcesAmount().remove(resourcesTypes);
        game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn())
                .getResourcesAmount().put(resourcesTypes, tempNumber + number);
    }

    public int cheatTechnology(TechnologyType technologyType) {
        if (technologyType == null) return 1;
        int result = game.getGameController().getCivilizations()
                .get(game.getGameController().getPlayerTurn()).doesContainTechnology(technologyType);
        if (result == 1) return 2;
        if (result == 2) {
            for (Technology research : game.getGameController().getCivilizations()
                    .get(game.getGameController().getPlayerTurn()).getResearches())
                if (research.getTechnologyType() == technologyType) {
                    research.setRemainedCost(0);
                    break;
                }
        } else {
            Technology technology = new Technology(technologyType);
            technology.setRemainedCost(0);
            game.getGameController().getCivilizations().get(game.getGameController()
                    .getPlayerTurn()).getResearches().add(technology);
        }
        game.getGameController().setUnfinishedTasks();
        return 0;
    }

    public  int cheatMoveIt(int x, int y, Unit unit) {
        if (unit == null) return 1;
        if (unit.getCivilization() !=
                game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 2;
        if (x < 0 || y < 0 || x > game.getGameController().getMap().getX() || y > game.getGameController().getMap().getY())
            return 3;
        Tile tempTile = game.getGameController().getMap().coordinatesToTile(x, y);
        if(tempTile==null)
            return 4;
        unit.setCurrentTile(game.getGameController().getMap().coordinatesToTile(x, y));
        if (unit instanceof Civilian)
            tempTile.setCivilian(unit,game.getSocketHandlers().get(game.getGameController().getPlayerTurn()));
        if (unit instanceof NonCivilian)
            tempTile.setNonCivilian((NonCivilian) unit,game.getSocketHandlers().get(game.getGameController().getPlayerTurn()));
        return 0;
    }

    public int cheatCaptureCity(String name) {
        City city = game.getGameController().nameToCity(name);
        if (city == null) return 1;
        if (city.getCivilization() == game.getGameController()
                .getCivilizations().get(game.getGameController().getPlayerTurn()))
            return 2;
        city.changeCivilization(game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn())
                ,game.getGameController(),game.getSocketHandlers().get(game.getGameController().getPlayerTurn()));
        return 0;
    }

    public  int cheatRoadEverywhere() {
        for (int i = 0; i < game.getGameController().getMap().getX(); i++)
            for (int j = 0; j < game.getGameController().getMap().getY(); j++) {
                Improvement improvement = new Improvement(ImprovementType.ROAD,
                        game.getGameController().getMap().coordinatesToTile(i, j));
                improvement.setRemainedCost(0);
                game.getGameController().getMap().coordinatesToTile(i, j).setRoad(improvement);
            }
        return 0;
    }

    public  void cheatScience(int number) {
        game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).cheatScience = number;
    }

    public int cheatUnit(int x, int y, UnitType unitType) {
        if(game.getGameController().getMap().coordinatesToTile(x, y)==null)
            return 3;
        if (game.getGameController().getMap().coordinatesToTile(x, y).getMovingPrice() > 123) return 1;
        if (unitType.combatType == CombatType.CIVILIAN) {
            if (game.getGameController().getMap().coordinatesToTile(x, y).getCivilian() != null) return 2;
            Civilian hardcodeUnit = new Civilian(game.getGameController().getMap().coordinatesToTile(x, y),
                    game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()), unitType);
            hardcodeUnit.setRemainedCost(0);
            game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getUnits().add(hardcodeUnit);
            game.getGameController().getMap().coordinatesToTile(x, y).setCivilian(hardcodeUnit,game.getSocketHandlers().get(game.getGameController().getPlayerTurn()));
            game.getGameController().openNewArea(game.getGameController().getMap().coordinatesToTile(x, y),
                    game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()), hardcodeUnit);
        } else {
            if (game.getGameController().getMap().coordinatesToTile(x, y).getNonCivilian() != null) return 2;
            NonCivilian hardcodeUnit = new NonCivilian(game.getGameController().getMap().coordinatesToTile(x, y),
                    game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()), unitType);
            hardcodeUnit.setRemainedCost(0);
            game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getUnits().add(hardcodeUnit);
            game.getGameController().getMap().coordinatesToTile(x, y).setNonCivilian(hardcodeUnit,game.getSocketHandlers().get(game.getGameController().getPlayerTurn()));
            game.getGameController().openNewArea(game.getGameController().getMap().coordinatesToTile(x, y),
                    game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()), hardcodeUnit);
        }
        return 0;
    }
}
