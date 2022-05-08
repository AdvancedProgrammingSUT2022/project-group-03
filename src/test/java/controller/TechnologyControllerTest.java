package controller;

import model.Civilization;
import model.User;
import model.building.controller.GameController;
import model.building.controller.TechnologyController;
import model.technologies.Technology;
import model.technologies.TechnologyType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechnologyControllerTest {
    @Mock
    User user;
    @Mock Civilization civilization;

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
            assertEquals(TechnologyController.getCivilizationsResearches().get(0).getTechnologyType(),TechnologyType.MINING);
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
            assertFalse(TechnologyController.addTechnologyToProduction(technologies, 0));
            assertTrue(TechnologyController.addTechnologyToProduction(technologies, 1));

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
            assertEquals(TechnologyController.cyclesToComplete(new Technology(TechnologyType.WRITING)),12345);
            when(civilization.collectScience()).thenReturn(5);
            assertEquals(TechnologyController.cyclesToComplete(new Technology(TechnologyType.WRITING)),11);
        }
    }
}