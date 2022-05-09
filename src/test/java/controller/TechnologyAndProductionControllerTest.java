package controller;

import controller.gameController.GameController;
import model.Civilization;
import model.User;
import model.technologies.Technology;
import model.technologies.TechnologyType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import view.gameMenu.ChooseTechnology;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechnologyAndProductionControllerTest {
    @Mock
    User user;
    @Mock Civilization civilization;

    @Test
    void initializeResearchInfo() {
//        GameController.getCivilizations().add()
        Civilization civilization1 = new Civilization(user,3);
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.getCivilizations().add(civilization1);
        civilization1.setGettingResearchedTechnology(new Technology(TechnologyType.TELEGRAPH));
        civilization1.getResearches().add(new Technology(TechnologyType.WRITING));

        ChooseTechnology chooseTechnology = new ChooseTechnology();
        chooseTechnology.printDetails();
    }

    @Test
    void getCivilizationsResearches() {
        ArrayList<Civilization> civilizations = new ArrayList<>();
        civilizations.add(civilization);
        try (MockedStatic<GameController> utilities = Mockito.mockStatic(GameController.class)) {
            utilities.when(GameController::getCivilizations).thenReturn(civilizations);
            utilities.when(GameController::getPlayerTurn).thenReturn(0);
            civilizations.get(0).getResearches().add(new Technology(TechnologyType.MINING));
            ArrayList <Technology> technologies = new ArrayList<>();
            technologies.add(new Technology(TechnologyType.MINING));
            when(civilization.getResearches()).thenReturn(technologies);
            assertEquals(TechnologyAndProductionController.getCivilizationsResearches().get(0).getTechnologyType(),TechnologyType.MINING);
        }
    }

    @Test
    void addTechnologyToProduction() {
        ArrayList<Civilization> civilizations = new ArrayList<>();
        civilizations.add(civilization);
        try (MockedStatic<GameController> utilities = Mockito.mockStatic(GameController.class)) {
            utilities.when(GameController::getCivilizations).thenReturn(civilizations);
            utilities.when(GameController::getPlayerTurn).thenReturn(0);
            ArrayList <Technology> technologies = new ArrayList<>();
            technologies.add(new Technology(TechnologyType.MINING));
            when(civilization.doesContainTechnology(any())).thenReturn(3);
            Assertions.assertFalse(TechnologyAndProductionController.addTechnologyToProduction(technologies, 0));
            Assertions.assertTrue(TechnologyAndProductionController.addTechnologyToProduction(technologies, 1));

        }
    }

    @Test
    void cyclesToComplete() {

        ArrayList<Civilization> civilizations = new ArrayList<>();
        civilizations.add(civilization);
        try (MockedStatic<GameController> utilities = Mockito.mockStatic(GameController.class)) {
            utilities.when(GameController::getCivilizations).thenReturn(civilizations);
            utilities.when(GameController::getPlayerTurn).thenReturn(0);
            civilizations.get(0).getResearches().add(new Technology(TechnologyType.MINING));
            when(civilization.collectScience()).thenReturn(0);
            assertEquals(TechnologyAndProductionController.cyclesToComplete(new Technology(TechnologyType.WRITING)),12345);
            when(civilization.collectScience()).thenReturn(5);
            assertEquals(TechnologyAndProductionController.cyclesToComplete(new Technology(TechnologyType.WRITING)),11);
        }
    }
}