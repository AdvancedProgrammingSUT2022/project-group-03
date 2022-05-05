package view;

import controller.GameController;
import controller.TechnologyController;

import model.Civilization;
import model.Units.UnitType;
import model.improvements.ImprovementType;
import model.resources.ResourcesTypes;
import model.technologies.Technology;
import model.technologies.TechnologyType;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class ChooseTechnology extends Menu {
    private final ArrayList<Technology> possibleTechnologies = new ArrayList<>();

    {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "^(\\d+)$",
                "^print tree$",
                "^print details$"
        };
    }

    private void printTree() {
        TechnologyType.prerequisites.forEach((k, v) -> {
            System.out.println(k + ":");
            for (TechnologyType technologyType : v)
                System.out.println(technologyType);
            System.out.println();
        });
    }

    public void initializeResearchInfo(boolean showFinishedResearches)
    {
        ArrayList<Technology> researches = TechnologyController.getCivilizationsResearches();
        Civilization civilization = GameController.getCivilizations().get(GameController.getPlayerTurn());
        if(civilization.getGettingResearchedTechnology()!=null)
            System.out.println("Getting researched technology: " +
                    civilization.getGettingResearchedTechnology().getTechnologyType() +
                    ", " + TechnologyController.cyclesToComplete(civilization.getGettingResearchedTechnology()) +
                    " cycles to finish");
        if(showFinishedResearches)
            System.out.println("Finished researches: ");
        for (int i = 0; i < researches.size(); i++) {
            if (researches.get(i).getRemainedCost() > 0) {
                if (GameController.getCivilizations().get(GameController.getPlayerTurn())
                        .getGettingResearchedTechnology() != researches.get(i))
                    possibleTechnologies.add(researches.get(i));
                continue;
            }
            if(showFinishedResearches)
                System.out.println(i + 1 + ". " + researches.get(i).getTechnologyType());
            ArrayList<TechnologyType> technologyTypes =
                    TechnologyType.nextTech.get(researches.get(i).getTechnologyType());
            for (TechnologyType technologyType : technologyTypes)
                if (civilization.canBeTheNextResearch(technologyType) &&
                        !doesArrayContain(possibleTechnologies,technologyType) &&
                        (civilization.getGettingResearchedTechnology() == null ||
                                civilization.doesContainTechnology(technologyType) == 3))
                    possibleTechnologies.add(new Technology(technologyType));
        }
    }

    private boolean doesArrayContain(ArrayList<Technology> possibleTechnologies, TechnologyType technologyType)
    {
        for (Technology possibleTechnology : possibleTechnologies)
            if(possibleTechnology.getTechnologyType()==technologyType)
                return true;
        return false;
    }
    public void printDetails() {
        initializeResearchInfo(true);

        System.out.println("\nPossible researches: ");
        for (int i = 0; i < possibleTechnologies.size(); i++) {
            int cyclesToComplete = TechnologyController.cyclesToComplete(possibleTechnologies.get(i));
            if (cyclesToComplete == 12345)
                System.out.println(i + 1 + ". " +
                        possibleTechnologies.get(i).getTechnologyType() +
                        ": never, your science is 0");
            else
                System.out.println(i + 1 + ". " +
                        possibleTechnologies.get(i).getTechnologyType() +
                        ": " + cyclesToComplete + " cycles to complete");
        }
    }

    private boolean addTechnologyToProduction(String command) {
        Matcher matcher = getMatcher(regexes[2], command,true);
        int entryNumber = Integer.parseInt(matcher.group(1));
        if (TechnologyController.addTechnologyToProduction(possibleTechnologies, entryNumber))
            System.out.println(possibleTechnologies.get(entryNumber - 1).getTechnologyType() +
                    "'s production started successfully");
        else
        {
            System.out.println("Invalid number");
            return false;
        }
        return true;
    }


    @Override
    protected boolean commands(String command) {
        commandNumber = getCommandNumber(command, regexes,true);
        switch (commandNumber) {
            case -1:
                System.out.println("invalid command");
                break;
            case 0: {
                return true;
            }
            case 1:
                System.out.println("Login Menu");
                break;
            case 2:
                if(addTechnologyToProduction(command))
                    return true;
                break;
            case 3:
                printTree();
                break;
            case 4:
                printDetails();
                break;

        }
        return false;
    }

}
