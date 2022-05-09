package view;

import org.junit.jupiter.api.Test;
import view.gameMenu.CitiesList;

class
CitiesListTest {

    @Test
    void openCityBanner() {
        CitiesList citiesList = new CitiesList();
        citiesList.printCities();
        citiesList.openCityBanner("1");
    }
}