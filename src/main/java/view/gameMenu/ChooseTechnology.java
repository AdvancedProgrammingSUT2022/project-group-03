package view.gameMenu;


import controller.TechnologyAndProductionController;
import controller.gameController.GameController;
import model.technologies.Technology;
import model.technologies.TechnologyType;
import view.Menu;

import java.util.ArrayList;

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

    public void printDetails() {
        System.out.println(TechnologyAndProductionController
                .initializeResearchInfo(true, possibleTechnologies));
        System.out.println("\nPossible researches: ");
        for (int i = 0; i < possibleTechnologies.size(); i++) {
            int cyclesToComplete = TechnologyAndProductionController
                    .cyclesToComplete(possibleTechnologies.get(i));
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
        int entryNumber = Integer.parseInt(getMatcher(regexes[2], command, true).group(1));
        if (TechnologyAndProductionController
                .addTechnologyToProduction(possibleTechnologies, entryNumber)) {
            String string = possibleTechnologies.get(entryNumber - 1).getTechnologyType() +
                    "'s production started successfully";
            System.out.println(string);
            GameController.getCivilizations().get(GameController.getPlayerTurn())
                    .putNotification(string,GameController.getCycle());
            return true;
        }
        System.out.println("Invalid number");
        return false;
    }


    @Override
    protected boolean commands(String command) {
        commandNumber = getCommandNumber(command, regexes, true);
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
                if (addTechnologyToProduction(command))
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
