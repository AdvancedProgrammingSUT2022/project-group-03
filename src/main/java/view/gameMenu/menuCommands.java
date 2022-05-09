package view.gameMenu;

import com.beust.jcommander.Parameter;
import controller.gameController.GameController;
import controller.TechnologyAndProductionController;
import view.Runnable;

public class menuCommands {
    static class InnerClass implements Runnable {
        @Parameter(names = {"exit", "-x"},
                description = "exits the current menu")
        boolean exit = false;
        @Parameter(names = {"show-current", "-s"},
                description = "shows the naem of the current menu")
        boolean show = false;
        @Parameter(names = {"enter", "-e"},
                description = "enters the menu you select(if it was not forbidden to do so)")
        String nextMenu = "init";

        public int run(String name) {
            if (exit && !show && nextMenu.equals("init"))
                return 2;
            else if (show && nextMenu.equals("init"))
                System.out.println("Game Menu");
            else if (nextMenu.equals("technologies") || nextMenu.equals("t")) {
                if (GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities().size() == 0)
                    System.out.println("you need at least one city to enter the technology menu");
                else {
                    ChooseTechnology chooseTechnology = new ChooseTechnology();
                    chooseTechnology.printDetails();
                    chooseTechnology.run(GameMenu.scanner);
                }
            } else if (nextMenu.equals("city-production") || nextMenu.equals("cp")) {
                if (GameController.getSelectedCity() == null)
                    System.out.println("no city is selected");
                else if (GameController.getSelectedCity().getCivilization() != GameController.getCivilizations().get(GameController.getPlayerTurn()))
                    System.out.println("the selected city is not yours");
                else {
                    ProductionCityMenu productionCityMenu = new ProductionCityMenu();
                    System.out.println(TechnologyAndProductionController
                            .printDetails(productionCityMenu.possibleUnits));
                    productionCityMenu.run(GameMenu.scanner);
                }
            } else if (!nextMenu.equals("init"))
                System.out.println("menu navigation is not possible");
            else
                System.out.println("invalid command");
            return 3;
        }
    }
}
