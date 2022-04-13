package model.Units;

import model.resources.Resource;
import model.technologies.Technology;
import model.tiles.Tile;

import java.util.ArrayList;

public class NonCivilian extends Unit {
    private int fortifiedCycle = 0;
    private boolean isFortifiedUntilCompleteHealth;
    private boolean isOnAlert = false;

    public NonCivilian(Tile tile, ArrayList<Resource> resources, ArrayList<Technology> technologies, int civilizationGold) {
        super(tile, resources, technologies, civilizationGold);
    }

    private void Fortify() {
        if (fortifiedCycle == 0)
            return;
    }
}
