package controller;

import controller.gameController.GameController;
import model.City;
import model.Civilization;
import model.TaskTypes;
import model.Tasks;
import model.Units.*;
import model.technologies.Technology;
import model.technologies.TechnologyType;
import network.GameHandler;

import java.util.ArrayList;

public class TechnologyAndProductionController {
    private final GameHandler game;

    public TechnologyAndProductionController(GameHandler gameHandler) {
        this.game = gameHandler;
    }

    public  ArrayList<Technology> getCivilizationsResearches() {
        return game.getGameController().getCivilizations()
                .get(game.getGameController().getPlayerTurn()).getResearches();
    }

    public  boolean addTechnologyToProduction(ArrayList<Technology> possibleTechnologies,
                                                    int entry) {
        if (entry > possibleTechnologies.size() || entry < 1)
            return false;
        Technology tempTechnology = possibleTechnologies.get(entry - 1);
        Civilization civilization = game.getGameController().getCivilizations()
                .get(game.getGameController().getPlayerTurn());
        if (civilization.doesContainTechnology(tempTechnology.getTechnologyType()) == 3)
            civilization.getResearches().add(tempTechnology);
        civilization.setGettingResearchedTechnology(tempTechnology);
        game.getGameController().deleteFromUnfinishedTasks(new Tasks(null,
                TaskTypes.TECHNOLOGY_PROJECT));
        return true;
    }

    public  int cyclesToComplete(Technology technology) {
        if (game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).collectScience(game.getGameController()) == 0)
            return 12345;
        return (int) Math.ceil((double) technology.getRemainedCost() /
                (double) game.getGameController().getCivilizations()
                        .get(game.getGameController().getPlayerTurn()).collectScience(game.getGameController()) - 0.03);
    }

    public  String initializeResearchInfo(boolean showFinishedResearches,
                                                ArrayList<Technology> possibleTechnologies) {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Technology> researches = getCivilizationsResearches();
        Civilization civilization = game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn());
        if (civilization.getGettingResearchedTechnology() != null)
            stringBuilder.append("Getting researched technology: ")
                    .append(civilization.getGettingResearchedTechnology().getTechnologyType())
                    .append(", ").append(cyclesToComplete(civilization.getGettingResearchedTechnology()))
                    .append(" cycles to finish\n");
        if (showFinishedResearches)
            stringBuilder.append("Finished researches: \n");
        int sizeOfFinished = 1;
        for (int i = 0; i < researches.size(); i++) {
            if (researches.get(i).getRemainedCost() > 0) {
                if (game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn())
                        .getGettingResearchedTechnology() != researches.get(i))
                    possibleTechnologies.add(researches.get(i));
                continue;
            }
            if (showFinishedResearches)
            {
                stringBuilder.append(sizeOfFinished).append(". ")
                        .append(researches.get(i).getTechnologyType()).append("\n");
                sizeOfFinished++;
            }

            ArrayList<TechnologyType> technologyTypes =
                    TechnologyType.nextTech.get(researches.get(i).getTechnologyType());
            for (TechnologyType technologyType : technologyTypes)
                if (civilization.canBeTheNextResearch(technologyType) &&
                        !doesArrayContain(possibleTechnologies, technologyType) &&
                        (civilization.getGettingResearchedTechnology() == null ||
                                civilization.doesContainTechnology(technologyType) == 3))
                    possibleTechnologies.add(new Technology(technologyType));
        }
        return stringBuilder.toString();
    }

    private static boolean doesArrayContain(ArrayList<Technology> possibleTechnologies,
                                            TechnologyType technologyType) {
        for (Technology possibleTechnology : possibleTechnologies)
            if (possibleTechnology.getTechnologyType() == technologyType) return true;
        return false;
    }


    private String printPossibleUnits(boolean doesHaveAny, ArrayList<Unit> possibleUnits) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < possibleUnits.size(); i++) {
            int cyclesToComplete = game.getGameController().getSelectedCity()
                    .cyclesToComplete(possibleUnits.get(i).getRemainedCost(),game.getGameController());
            stringBuilder.append(i + 1).append(". ").append(possibleUnits.get(i).getUnitType()).append(": ");
            if (cyclesToComplete == 12345)
                stringBuilder.append("never, your production is 0\n");
            else
                stringBuilder.append(game.getGameController().getSelectedCity()
                                .cyclesToComplete(possibleUnits.get(i).getRemainedCost(),game.getGameController()))
                        .append(" cycles to finish\n");
        }
        if (!doesHaveAny)
            stringBuilder.append("you can't produce anything right now\n");
        return stringBuilder.toString();
    }

    public  String printDetails(ArrayList<Unit> possibleUnits) {
        StringBuilder stringBuilder = new StringBuilder();
        City city = game.getGameController().getSelectedCity();
        stringBuilder.append("name: ").append(city.getName()).append(" | ");
        if (city.getProduct() == null)
            stringBuilder.append("production: -\n");
        else if (city.getProduct() instanceof Unit)
            stringBuilder.append("production: ")
                    .append(((Unit) city.getProduct()).getUnitType())
                    .append(", ").append(city.cyclesToComplete(city.getProduct().getRemainedCost(),game.getGameController()))
                    .append(" cycles to finish\n");
        boolean doesHaveAny = false;
        mainFor:
        for (int i = 0; i < UnitType.VALUES.size(); i++) {
            if (game.getGameController().getSelectedCity().getCivilization()
                    .doesContainTechnology(UnitType.VALUES.get(i).technologyRequired) != 1)
                continue;
            doesHaveAny = true;
            if ((UnitType.VALUES.get(i).resourcesType != null &&
                    !game.getGameController().getSelectedCity().getCivilization()
                            .getResourcesAmount().containsKey(UnitType.VALUES.get(i).resourcesType)) ||
                    (game.getGameController().getSelectedCity().getCivilization()
                            .getResourcesAmount().containsKey(UnitType.VALUES.get(i).resourcesType) &&
                            game.getGameController().getSelectedCity().getCivilization()
                                    .getResourcesAmount().get(UnitType.VALUES.get(i).resourcesType) == 0))
                stringBuilder.append("(not enough ")
                        .append(UnitType.VALUES.get(i).resourcesType)
                        .append(") ").append(UnitType.VALUES.get(i)).append("\n");
            else {
                for (Unit unit : game.getGameController().getSelectedCity().getHalfProducedUnits())
                    if (unit.getRemainedCost() != 0 && unit.getUnitType() == UnitType.VALUES.get(i) &&
                            (city.getProduct() == null || (city.getProduct() != null &&
                                    ((Unit) city.getProduct()).getUnitType() != unit.getUnitType()))) {
                        possibleUnits.add(unit);
                        continue mainFor;
                    }
                if (city.getProduct() == null ||
                        (city.getProduct() != null &&
                                ((Unit) city.getProduct()).getUnitType() != UnitType.VALUES.get(i))) {
                    if (UnitType.VALUES.get(i).combatType == CombatType.CIVILIAN)
                        possibleUnits.add(new Civilian(game.getGameController().getSelectedCity().getMainTile(),
                                game.getGameController().getSelectedCity().getCivilization(), UnitType.VALUES.get(i)));
                    else
                        possibleUnits.add(new NonCivilian(game.getGameController().getSelectedCity().getMainTile(),
                                game.getGameController().getSelectedCity().getCivilization(), UnitType.VALUES.get(i)));
                }
            }
        }
        stringBuilder.append(printPossibleUnits(doesHaveAny, possibleUnits));
        return stringBuilder.toString();
    }
}
