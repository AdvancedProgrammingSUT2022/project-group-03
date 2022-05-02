package view;

import controller.GameController;
import controller.TechnologyController;
import model.Units.Unit;
import model.technologies.Technology;
import model.technologies.TechnologyType;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class ChooseTechnology extends Menu {
    private ArrayList<Technology> possibleTechnologies = new ArrayList<>();
    {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "^back$",
                "^(\\d+)$",
                "^print tree$",
                "^print details$"
        };
    }

    private void printTree()
    {
        TechnologyType.prerequisites.forEach((k,v)->{
            System.out.println(k + ":");
            for (TechnologyType technologyType : v)
                System.out.println(technologyType);
            System.out.println();
        });
    }
    public void printDetails() {
        possibleTechnologies = new ArrayList<>();

        ArrayList<Technology> researches = TechnologyController.getCivilizationsResearches();
        System.out.println("Finished researches: ");
        for (int i = 0; i < researches.size(); i++) {
            if(researches.get(i).getRemainedCost()>0)
            {
                if(GameController.getCivilizations().get(GameController.getPlayerTurn()).getGettingResearchedTechnology()!=researches.get(i))
                possibleTechnologies.add(researches.get(i));
                continue;
            }
            System.out.println(i + 1 + ". " + researches.get(i).getTechnologyType());
            for (int j = 0; j < TechnologyType.nextTech.get(researches.get(i).getTechnologyType()).size(); j++)
                if (TechnologyController.
                        canBeTheNextResearch(TechnologyType.nextTech.get(researches.get(i).getTechnologyType()).get(j)) &&
                        (GameController.getCivilizations().get(GameController.getPlayerTurn()).getGettingResearchedTechnology()==null ||
                                GameController.getCivilizations().get(GameController.getPlayerTurn()).doesContainTechnology(TechnologyType.nextTech.get(researches.get(i).getTechnologyType()).get(j))==3)
                )
                    possibleTechnologies.add(new Technology(TechnologyType.nextTech.get(researches.get(i).getTechnologyType()).get(j)));
        }
        String tempString = null;
        if(GameController.getCivilizations().get(GameController.getPlayerTurn()).getGettingResearchedTechnology()!=null)
            tempString = GameController.getCivilizations().get(GameController.getPlayerTurn()).getGettingResearchedTechnology().getTechnologyType().toString() +
                    ": " + TechnologyController.cyclesToComplete(GameController.getCivilizations().get(GameController.getPlayerTurn()).getGettingResearchedTechnology()) +
                    " cycles to complete";
        System.out.println("\nBeing developed technology: " + tempString);
        System.out.println("\nPossible next researches: ");
        for (int i = 0; i < possibleTechnologies.size(); i++)
        {
            int cyclesToComplete = TechnologyController.cyclesToComplete(possibleTechnologies.get(i));
            if(cyclesToComplete ==12345)
                System.out.println(i + 1 + ". " + possibleTechnologies.get(i).getTechnologyType() + ": never, your science is 0");
            else
                System.out.println(i + 1 + ". " + possibleTechnologies.get(i).getTechnologyType() + ": " + cyclesToComplete + " cycles to complete");
        }
    }
    private void addTechnologyToProduction(String command)
    {
        Matcher matcher = getMatcher(regexes[3],command);
        int entryNumber = Integer.parseInt(matcher.group(1));
        if(TechnologyController.addTechnologyToProduction(possibleTechnologies,entryNumber))
            System.out.println(possibleTechnologies.get(entryNumber-1).getTechnologyType() + "'s production started successfully");
        else
            System.out.println("Invalid number");
    }

    @Override
    protected boolean commands(String command) {
        commandNumber = getCommandNumber(command, regexes);
        switch (commandNumber) {
            case -1:
                System.out.println("invalid command");
                break;
            case 0: {
                nextMenu = -1;
                return true;
            }
            case 1:
                System.out.println("Login Menu");
                break;
            case 2:
            {
                System.out.println("technology menu closed successfully");
                return true;
            }
            case 3:
                addTechnologyToProduction(command);
                break;
            case 4:
                printTree();
                break;
            case 5:
                printDetails();
                break;

        }
        return false;
    }

}
