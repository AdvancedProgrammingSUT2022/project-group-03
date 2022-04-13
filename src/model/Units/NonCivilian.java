package model.Units;

import model.Civilization;
import model.resources.Resource;
import model.technologies.Technology;
import model.tiles.Tile;

import java.util.ArrayList;

public class NonCivilian extends Unit {
    private int fortifiedCycle = 0;
    private boolean isFortifiedUntilCompleteHealth;
    private boolean isOnAlert = false;

    public NonCivilian(Tile tile, Civilization civilization) {
        super(tile, civilization);
    }

    private void Fortify() {
        if (fortifiedCycle == 0)
            return;
    }
}
