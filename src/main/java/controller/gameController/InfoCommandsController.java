package controller.gameController;

import model.City;
import model.Units.Civilian;
import model.Units.NonCivilian;
import model.improvements.Improvement;
import model.improvements.ImprovementType;
import model.resources.ResourcesTypes;
import model.technologies.Technology;
import model.technologies.TechnologyType;
import model.tiles.Tile;

public class InfoCommandsController {
    public static void openMap() {
        for (int i = 0; i < GameController.getMap().getX(); i++)
            for (int j = 0; j < GameController.getMap().getY(); j += 2)
                GameController.openNewArea(GameController.getMap().coordinatesToTile(i, j),
                        GameController.getCivilizations().get(GameController.getPlayerTurn()), null);
    }

    public static int cheatProduction(int number) {
        if (GameController.getSelectedCity() == null)
            return 1;
        if (GameController.getSelectedCity().getCivilization() !=
                GameController.getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        GameController.getSelectedCity().productionCheat += number;
        return 0;
    }

    public static void cheatResource(int number, ResourcesTypes resourcesTypes) {
        int tempNumber = 0;
        if (GameController.getCivilizations().get(GameController.getPlayerTurn()).getResourcesAmount().containsKey(resourcesTypes))
            tempNumber = GameController.getCivilizations().get(GameController.getPlayerTurn()).getResourcesAmount().get(resourcesTypes);
        GameController.getCivilizations().get(GameController.getPlayerTurn()).getResourcesAmount().remove(resourcesTypes);
        GameController.getCivilizations().get(GameController.getPlayerTurn()).getResourcesAmount().put(resourcesTypes, tempNumber + number);
    }

    public static int cheatTechnology(TechnologyType technologyType) {
        if (technologyType == null)
            return 1;
        int result = GameController.getCivilizations().get(GameController.getPlayerTurn()).doesContainTechnology(technologyType);
        if (result == 1)
            return 2;
        if (result == 2) {
            for (Technology research : GameController.getCivilizations().get(GameController.getPlayerTurn()).getResearches())
                if (research.getTechnologyType() == technologyType) {
                    research.setRemainedCost(0);
                    break;
                }
        } else {
            Technology technology = new Technology(technologyType);
            technology.setRemainedCost(0);
            GameController.getCivilizations().get(GameController.getPlayerTurn()).getResearches().add(technology);
        }
        GameController.setUnfinishedTasks();
        return 0;
    }

    public static int cheatMoveIt(int x, int y) {
        if (GameController.getSelectedUnit() == null)
            return 1;
        if (GameController.getSelectedUnit().getCivilization() != GameController.getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        if (x < 0 || y < 0 || x > GameController.getMap().getX() || y > GameController.getMap().getY())
            return 3;
        Tile tempTile = GameController.getMap().coordinatesToTile(x, y);
        GameController.getSelectedUnit().setCurrentTile(GameController.getMap().coordinatesToTile(x, y));
        if (GameController.getSelectedUnit() instanceof Civilian)
            tempTile.setCivilian(GameController.getSelectedUnit());
        if (GameController.getSelectedUnit() instanceof NonCivilian)
            tempTile.setNonCivilian((NonCivilian) GameController.getSelectedUnit());
        return 0;
    }

    public static int cheatCaptureCity(String name) {
        City city = GameController.nameToCity(name);
        if (city == null)
            return 1;
        if (city.getCivilization() == GameController.getCivilizations().get(GameController.getPlayerTurn()))
            return 2;
        city.changeCivilization(GameController.getCivilizations().get(GameController.getPlayerTurn()));
        return 0;
    }

    public static int cheatRoadEverywhere() {
        for (int i = 0; i < GameController.getMap().getX(); i++)
            for (int j = 0; j < GameController.getMap().getY(); j++) {
                Improvement improvement = new Improvement(ImprovementType.ROAD, GameController.getMap().coordinatesToTile(i, j));
                improvement.setRemainedCost(0);
                GameController.getMap().coordinatesToTile(i, j).setRoad(improvement);
            }
        return 0;
    }
}
