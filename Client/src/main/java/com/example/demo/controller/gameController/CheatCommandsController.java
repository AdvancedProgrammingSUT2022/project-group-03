package com.example.demo.controller.gameController;

import com.example.demo.model.City;
import com.example.demo.model.Units.Civilian;
import com.example.demo.model.Units.CombatType;
import com.example.demo.model.Units.NonCivilian;
import com.example.demo.model.Units.UnitType;
import com.example.demo.model.improvements.Improvement;
import com.example.demo.model.improvements.ImprovementType;
import com.example.demo.model.resources.ResourcesTypes;
import com.example.demo.model.technologies.Technology;
import com.example.demo.model.technologies.TechnologyType;
import com.example.demo.model.tiles.Tile;

public class CheatCommandsController {
    public static void openMap() {
        for (int i = 0; i < GameController.getMap().getStaticX(); i++)
            for (int j = 0; j < GameController.getMap().getStaticY(); j += 2)
                GameController.openNewArea(GameController.getMap().coordinatesToTile(i, j),
                        GameController.getCivilizations().get(GameController.getPlayerTurn()), null);
    }

    public static int cheatProduction(int number) {
        if (GameController.getSelectedCity() == null) return 1;
        if (GameController.getSelectedCity().getCivilization() !=
                GameController.getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        GameController.getSelectedCity().productionCheat += number;
        return 0;
    }

    public static void cheatResource(int number, ResourcesTypes resourcesTypes) {
        int tempNumber = 0;
        if (GameController.getCivilizations().get(GameController.getPlayerTurn())
                .getResourcesAmount().containsKey(resourcesTypes))
            tempNumber = GameController.getCivilizations().get(GameController.getPlayerTurn())
                    .getResourcesAmount().get(resourcesTypes);
        GameController.getCivilizations().get(GameController.getPlayerTurn())
                .getResourcesAmount().remove(resourcesTypes);
        GameController.getCivilizations().get(GameController.getPlayerTurn())
                .getResourcesAmount().put(resourcesTypes, tempNumber + number);
    }

    public static int cheatTechnology(TechnologyType technologyType) {
        if (technologyType == null) return 1;
        int result = GameController.getCivilizations()
                .get(GameController.getPlayerTurn()).doesContainTechnology(technologyType);
        if (result == 1) return 2;
        if (result == 2) {
            for (Technology research : GameController.getCivilizations()
                    .get(GameController.getPlayerTurn()).getResearches())
                if (research.getTechnologyType() == technologyType) {
                    research.setRemainedCost(0);
                    break;
                }
        } else {
            Technology technology = new Technology(technologyType);
            technology.setRemainedCost(0);
            GameController.getCivilizations().get(GameController
                    .getPlayerTurn()).getResearches().add(technology);
        }
        GameController.setUnfinishedTasks();
        return 0;
    }

    public static int cheatMoveIt(int x, int y) {
        if (GameController.getSelectedUnit() == null) return 1;
        if (GameController.getSelectedUnit().getCivilization() !=
                GameController.getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        if (x < 0 || y < 0 || x > GameController.getMap().getStaticX() || y > GameController.getMap().getStaticY())
            return 3;
        Tile tempTile = GameController.getMap().coordinatesToTile(x, y);
        if(tempTile==null)
            return 4;
        GameController.getSelectedUnit().setCurrentTile(GameController.getMap().coordinatesToTile(x, y));
        if (GameController.getSelectedUnit() instanceof Civilian)
            tempTile.setCivilian(GameController.getSelectedUnit());
        if (GameController.getSelectedUnit() instanceof NonCivilian)
            tempTile.setNonCivilian((NonCivilian) GameController.getSelectedUnit());
        return 0;
    }

    public static int cheatCaptureCity(String name) {
        City city = GameController.nameToCity(name);
        if (city == null) return 1;
        if (city.getCivilization() == GameController
                .getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        city.changeCivilization(GameController.getCivilizations().get(GameController.getPlayerTurn()));
        return 0;
    }

    public static int cheatRoadEverywhere() {
        for (int i = 0; i < GameController.getMap().getStaticX(); i++)
            for (int j = 0; j < GameController.getMap().getStaticY(); j++) {
                Improvement improvement = new Improvement(ImprovementType.ROAD,
                        GameController.getMap().coordinatesToTile(i, j));
                improvement.setRemainedCost(0);
                GameController.getMap().coordinatesToTile(i, j).setRoad(improvement);
            }
        return 0;
    }

    public static void cheatScience(int number) {
        GameController.getCivilizations().get(GameController.getPlayerTurn()).cheatScience = number;
    }

    public static int cheatUnit(int x, int y, UnitType unitType) {
        if(GameController.getMap().coordinatesToTile(x, y)==null)
            return 3;
        if (GameController.getMap().coordinatesToTile(x, y).getMovingPrice() > 123) return 1;
        if (unitType.combatType == CombatType.CIVILIAN) {
            if (GameController.getMap().coordinatesToTile(x, y).getCivilian() != null) return 2;
            Civilian hardcodeUnit = new Civilian(GameController.getMap().coordinatesToTile(x, y),
                    GameController.getCivilizations().get(GameController.getPlayerTurn()), unitType);
            hardcodeUnit.setRemainedCost(0);
            GameController.getCivilizations().get(GameController.getPlayerTurn()).getUnits().add(hardcodeUnit);
            GameController.getMap().coordinatesToTile(x, y).setCivilian(hardcodeUnit);
            GameController.openNewArea(GameController.getMap().coordinatesToTile(x, y),
                    GameController.getCivilizations().get(GameController.getPlayerTurn()), hardcodeUnit);
        } else {
            if (GameController.getMap().coordinatesToTile(x, y).getNonCivilian() != null) return 2;
            NonCivilian hardcodeUnit = new NonCivilian(GameController.getMap().coordinatesToTile(x, y),
                    GameController.getCivilizations().get(GameController.getPlayerTurn()), unitType);
            hardcodeUnit.setRemainedCost(0);
            GameController.getCivilizations().get(GameController.getPlayerTurn()).getUnits().add(hardcodeUnit);
            GameController.getMap().coordinatesToTile(x, y).setNonCivilian(hardcodeUnit);
            GameController.openNewArea(GameController.getMap().coordinatesToTile(x, y),
                    GameController.getCivilizations().get(GameController.getPlayerTurn()), hardcodeUnit);
        }
        return 0;
    }
}
