package controller;

import model.TaskTypes;
import model.Tasks;
import model.technologies.Technology;

import java.util.ArrayList;

public class TechnologyController {
    public static ArrayList<Technology> getCivilizationsResearches() {
        return GameController.getCivilizations()
                .get(GameController.getPlayerTurn()).getResearches();
    }
    public static boolean addTechnologyToProduction(ArrayList<Technology> possibleTechnologies, int entry) {
        if (entry > possibleTechnologies.size() || entry < 1)
            return false;
        Technology tempTechnology = possibleTechnologies.get(entry - 1);
        if (GameController.getCivilizations().get(GameController.getPlayerTurn()).doesContainTechnology(tempTechnology.getTechnologyType()) == 3)
            GameController.getCivilizations()
                    .get(GameController.getPlayerTurn()).getResearches()
                    .add(tempTechnology);
        GameController.getCivilizations()
                .get(GameController.getPlayerTurn()).setGettingResearchedTechnology(tempTechnology);
        GameController.deleteFromUnfinishedTasks(new Tasks(null, TaskTypes.TECHNOLOGY_PROJECT));
        return true;
    }

    public static int cyclesToComplete(Technology technology) {
        if (GameController.getCivilizations().get(GameController.getPlayerTurn()).collectScience() == 0)
            return 12345;
        return (int) Math.ceil((double)technology.getRemainedCost() / (double)GameController.getCivilizations().get(GameController.getPlayerTurn()).collectScience());
    }
}
