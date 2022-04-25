package controller;

import model.technologies.Technology;
import model.technologies.TechnologyType;

import java.util.ArrayList;

public class TechnologyController {
    public static ArrayList<Technology> getCivilizationsResearches()
    {
        return GameController.getCivilizations().get(GameController.getPlayerTurn()).getResearches();
    }
    public static boolean canBeTheNextResearch(TechnologyType technologyType)
    {
        return GameController.getCivilizations().get(GameController.getPlayerTurn()).canBeTheNextResearch(technologyType);
    }
    public static boolean addTechnologyToProduction(ArrayList<TechnologyType> possibleTechnologies, int entry)
    {
        if(entry>possibleTechnologies.size() || entry<1)
            return false;
        GameController.getCivilizations()
                .get(GameController.getPlayerTurn()).getResearches()
                .add(new Technology(possibleTechnologies.get(entry-1)));
        return true;
    }
}
