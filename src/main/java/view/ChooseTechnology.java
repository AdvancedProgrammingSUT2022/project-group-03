package view;

import controller.GameController;
import model.technologies.Technology;
import model.technologies.TechnologyType;

import java.util.ArrayList;

public class ChooseTechnology extends Menu {

    {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "^back$",
                "",
                ""
        };
    }

    private void printDetails() {
        ArrayList<Technology> researches = GameController.getCivilizationsResearches();
        System.out.println("Finished researches: ");
        ArrayList<TechnologyType> possibleTechnologies = new ArrayList<>();
        for (int i = 0; i < researches.size(); i++) {
            System.out.println(i + 1 + ". " + researches.get(i));
            for (int j = 0; j < TechnologyType.nextTech.get(researches.get(i)).size(); j++)
                if (GameController.canBeTheNextResearch(TechnologyType.nextTech.get(researches.get(i)).get(j)))
                    possibleTechnologies.add(TechnologyType.nextTech.get(researches.get(i)).get(j));
        }
        System.out.println("Possible next researches: ");
        for (int i = 0; i < possibleTechnologies.size(); i++)
            System.out.println(i + 1 + ". " + possibleTechnologies.get(i));
    }

    @Override
    protected boolean commands(String command) {
        printDetails();
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
                return true;
        }
        return false;
    }

}
