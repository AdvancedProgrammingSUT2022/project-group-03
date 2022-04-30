package view;

import controller.GameController;
import model.City;
import model.Units.Unit;
import model.Units.UnitType;

import java.util.ArrayList;

public class ProductionCityMenu extends Menu{
    {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "^(\\d+)$"
        };
    }

    private ArrayList<UnitType> possibleUnits = new ArrayList<>();
    private void printDetails()
    {
        City city = GameController.getSelectedCity();
        System.out.println("name: " + city.getName());
        if(city.getProduct() ==null)
            System.out.println("production: null");
        else if(city.getProduct() instanceof Unit)
            System.out.println("production: " + ((Unit) city.getProduct()).getUnitType());

        for(int i = 0 ; i < UnitType.VALUES.size();i++)
        {
            if(!GameController.getSelectedCity().getCivilization().doesContainTechnology(UnitType.VALUES.get(i).technologyRequired))
                continue;
            if(GameController.getSelectedCity().getCivilization().getResourcesAmount().get(UnitType.VALUES.get(i).resourcesType)==0)
                System.out.println("(not enough resources) " + UnitType.VALUES.get(i));
            else
            {
                System.out.println(possibleUnits.size()+1 + ". " + UnitType.VALUES.get(i));
                possibleUnits.add(UnitType.VALUES.get(i));
            }
        }
    }

    private void createUnit(String command)
    {
        int number = Integer.parseInt(command);
        if(number-1>possibleUnits.size() || number<1)
        {
            System.out.println("invalid number");
            return;
        }
        GameController.getSelectedCity().createUnit(possibleUnits.get(number-1));

    }
    @Override
    protected boolean commands(String command)
    {
        commandNumber = getCommandNumber(command, regexes);
        switch (commandNumber) {
            case -1:
                System.out.println("invalid command");
                break;
            case 0:
                nextMenu = 1;
                return true;
            case 1:
                System.out.println("Production Menu");
                break;
            case 2:
                createUnit(command); break;

        }
        return false;
    }
}
