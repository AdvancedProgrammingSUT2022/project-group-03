package view;

import controller.GameController;
import model.technologies.TechnologyType;

import java.util.ArrayList;

public class ChooseTechnology extends Menu {

    {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "",
                "",
                ""
        };
    }

    private void printDetails()
    {
        ArrayList<TechnologyType> researches = GameController.getCivilizationsResearches();
        System.out.println("Finished researches: ");
//        for(int i=0;i<researches.size();i++)
//            System.out.println(researches.);
    }
    @Override
    protected boolean commands(String command)
    {
        printDetails();
        commandNumber = getCommandNumber(command, regexes);
        switch (commandNumber) {
            case -1:
                System.out.println("invalid command");
                break;
            case 0:
            {
                nextMenu = -1;
                return true;
            }
            case 1:
                System.out.println("Login Menu");
                break;
        }
        return false;
    }

}
