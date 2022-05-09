package view;

import controller.gameController.GameController;
import controller.TechnologyAndProductionController;
import model.City;
import model.Civilization;
import model.Units.Civilian;
import model.Units.Unit;
import model.Units.UnitType;
import model.tiles.Tile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import view.gameMenu.ProductionCityMenu;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class ProductionCityMenuTest {
    @Mock
    City city;
    @Mock
    Tile tile;
    @Mock
    Civilization civilization;
    @Test
    void printDetails() {
        ProductionCityMenu productionCityMenu = new ProductionCityMenu();
        TechnologyAndProductionController.printDetails(productionCityMenu.possibleUnits);
        when(city.getCivilization()).thenReturn(civilization);
        GameController.setSelectedCity(city);
        when(city.getProduct()).thenReturn(new Civilian(tile,civilization, UnitType.SETTLER));
        productionCityMenu = new ProductionCityMenu();
        ArrayList<Unit> units = new ArrayList<>();
        units.add(new Civilian(tile,civilization, UnitType.SETTLER));
        when(city.getHalfProducedUnits()).thenReturn(units);
        when(civilization.doesContainTechnology(any())).thenReturn(1);
        TechnologyAndProductionController.printDetails(productionCityMenu.possibleUnits);
    }
}