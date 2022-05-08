package view;

import controller.GameController;
import model.City;
import model.Units.*;

import java.util.ArrayList;

public class ProductionCityMenu extends Menu{
    {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "^(\\d+)$"
        };
    }

    private final ArrayList<Unit> possibleUnits = new ArrayList<>();
    public void printDetails()
    {
        City city = GameController.getSelectedCity();
        System.out.print("name: " + city.getName() + " | ");
        if(city.getProduct() ==null)
            System.out.println("production: -");
        else if(city.getProduct() instanceof Unit)
            System.out.println("production: " +
                    ((Unit) city.getProduct()).getUnitType() +
                    ", " + city.cyclesToComplete(city.getProduct().getRemainedCost()) +
                    " cycles to finish");
        boolean doesHaveAny = false;
        mainFor: for(int i = 0 ; i < UnitType.VALUES.size();i++)
        {
            if(GameController.getSelectedCity().getCivilization().doesContainTechnology(UnitType.VALUES.get(i).technologyRequired)!=1)
                continue;
            doesHaveAny=true;
            if( (UnitType.VALUES.get(i).resourcesType!=null &&
                    !GameController.getSelectedCity().getCivilization().getResourcesAmount().containsKey(UnitType.VALUES.get(i).resourcesType)) ||
                   (GameController.getSelectedCity().getCivilization().getResourcesAmount().containsKey(UnitType.VALUES.get(i).resourcesType) &&
                    GameController.getSelectedCity().getCivilization().getResourcesAmount().get(UnitType.VALUES.get(i).resourcesType)==0))
                System.out.println("(not enough " + UnitType.VALUES.get(i).resourcesType + ") " + UnitType.VALUES.get(i));
            else
            {
                for (Unit unit : GameController.getSelectedCity().getHalfProducedUnits())
                    if(unit.getRemainedCost()!=0 && unit.getUnitType()==UnitType.VALUES.get(i) &&
                            (city.getProduct()==null || (city.getProduct()!= null &&((Unit) city.getProduct()).getUnitType()!=unit.getUnitType())))
                    {
                        possibleUnits.add(unit);
                        continue mainFor;
                    }
                if(city.getProduct()==null ||
                        (city.getProduct()!=null && ((Unit) city.getProduct()).getUnitType()!=UnitType.VALUES.get(i)))
                {
                    if(UnitType.VALUES.get(i).combatType == CombatType.CIVILIAN)
                        possibleUnits.add(new Civilian(GameController.getSelectedCity().getMainTile(),
                                GameController.getSelectedCity().getCivilization(),UnitType.VALUES.get(i)));
                    else
                        possibleUnits.add(new NonCivilian(GameController.getSelectedCity().getMainTile(),
                                GameController.getSelectedCity().getCivilization(),UnitType.VALUES.get(i)));
                }

            }
        }
        for(int i = 0 ; i< possibleUnits.size();i++)
        {
            int cyclesToComplete = GameController.getSelectedCity().cyclesToComplete(possibleUnits.get(i).getRemainedCost());
            System.out.print(i+1 + ". " + possibleUnits.get(i).getUnitType() + ": ");
            if(cyclesToComplete == 12345)
                System.out.println("never, your production is 0");
            else
                System.out.println(GameController.getSelectedCity().cyclesToComplete(possibleUnits.get(i).getRemainedCost()) +
                        " cycles to finish");
        }
        if(!doesHaveAny)
            System.out.println("you can't produce anything right now");
    }

    private boolean createUnit(String command)
    {
        int number = Integer.parseInt(command);
        if(number-1>possibleUnits.size() || number<1)
        {
            System.out.println("invalid number");
            return false;
        }
        return GameMenu.startProducingUnit(possibleUnits.get(number-1).getUnitType().toString());
    }
    @Override
    protected boolean commands(String command)
    {
        commandNumber = getCommandNumber(command, regexes,true);
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
                if(createUnit(command))
                    return true;
                break;

        }
        return false;
    }
}
