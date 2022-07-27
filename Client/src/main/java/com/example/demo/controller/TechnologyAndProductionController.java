package com.example.demo.controller;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.City;
import com.example.demo.model.Civilization;
import com.example.demo.model.TaskTypes;
import com.example.demo.model.Tasks;
import com.example.demo.model.Units.*;
import com.example.demo.model.technologies.Technology;
import com.example.demo.model.technologies.TechnologyType;

import java.util.ArrayList;

public class TechnologyAndProductionController {
    public static ArrayList<Technology> getCivilizationsResearches() {
        return GameController.getCivilizations()
                .get(GameController.getPlayerTurn()).getResearches();
    }



    public static int cyclesToComplete(Technology technology) {
        if (GameController.getCivilizations().get(GameController.getPlayerTurn()).collectScience() == 0)
            return 12345;
        return (int) Math.ceil((double) technology.getRemainedCost() /
                (double) GameController.getCivilizations()
                        .get(GameController.getPlayerTurn()).collectScience() - 0.03);
    }

    public static ArrayList<Technology> initializeResearchInfo() {
        ArrayList<Technology> possibleTechnologies = new ArrayList<>();
        ArrayList<Technology> researches = TechnologyAndProductionController.getCivilizationsResearches();
        Civilization civilization = GameController.getCivilizations().get(GameController.getPlayerTurn());
        for (Technology research : researches) {
            if (research.getRemainedCost() > 0) {
                if (GameController.getCivilizations().get(GameController.getPlayerTurn())
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

    private static boolean doesArrayContain(ArrayList<Technology> possibleTechnologies,
                                            TechnologyType technologyType) {
        for (Technology possibleTechnology : possibleTechnologies)
            if (possibleTechnology.getTechnologyType() == technologyType) return true;
        return false;
    }

}
