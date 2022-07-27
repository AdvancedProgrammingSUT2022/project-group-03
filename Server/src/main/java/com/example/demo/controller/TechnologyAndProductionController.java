package com.example.demo.controller;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.Civilization;
import com.example.demo.model.TaskTypes;
import com.example.demo.model.Tasks;
import com.example.demo.model.technologies.Technology;
import com.example.demo.model.technologies.TechnologyType;
import com.example.demo.network.GameHandler;

import java.util.ArrayList;

public class TechnologyAndProductionController {
    private final GameHandler game;
    private final GameController gameController;

    public TechnologyAndProductionController(GameHandler game) {
        this.game = game;
        this.gameController = game.getGameController();
    }
    public  ArrayList<Technology> getCivilizationsResearches() {
        return gameController.getCivilizations()
                .get(gameController.getPlayerTurn()).getResearches();
    }

    public  boolean addTechnologyToProduction(Technology technology) {
        Civilization civilization = gameController.getCivilizations()
                .get(gameController.getPlayerTurn());
        if (civilization.doesContainTechnology(technology.getTechnologyType()) == 3)
            civilization.getResearches().add(technology);
        civilization.setGettingResearchedTechnology(technology);
        gameController.deleteFromUnfinishedTasks(new Tasks(null,
                TaskTypes.TECHNOLOGY_PROJECT));
        return true;
    }


    public  int cyclesToComplete(Technology technology,GameController gameController) {
        if (gameController.getCivilizations().get(gameController.getPlayerTurn()).collectScience(gameController) == 0)
            return 12345;
        return (int) Math.ceil((double) technology.getRemainedCost() /
                (double) gameController.getCivilizations()
                        .get(gameController.getPlayerTurn()).collectScience(gameController) - 0.03);
    }

    public  ArrayList<Technology> initializeResearchInfo() {
        ArrayList<Technology> possibleTechnologies = new ArrayList<>();
        ArrayList<Technology> researches = this.getCivilizationsResearches();
        Civilization civilization = gameController.getCivilizations().get(gameController.getPlayerTurn());
        for (Technology research : researches) {
            if (research.getRemainedCost() > 0) {
                if (gameController.getCivilizations().get(gameController.getPlayerTurn())
                        .getGettingResearchedTechnology() != research)
                    possibleTechnologies.add(research);
                continue;
            }
            ArrayList<TechnologyType> technologyTypes =
                    TechnologyType.nextTech.get(research.getTechnologyType());
            for (TechnologyType technologyType : technologyTypes)
                if (civilization.canBeTheNextResearch(technologyType) &&
                        !doesArrayContain(possibleTechnologies, technologyType) &&
                        (civilization.getGettingResearchedTechnology() == null ||
                                civilization.doesContainTechnology(technologyType) == 3))
                    possibleTechnologies.add(new Technology(technologyType));
        }
        return possibleTechnologies;
    }

    private  boolean doesArrayContain(ArrayList<Technology> possibleTechnologies,
                                            TechnologyType technologyType) {
        for (Technology possibleTechnology : possibleTechnologies)
            if (possibleTechnology.getTechnologyType() == technologyType) return true;
        return false;
    }

}
