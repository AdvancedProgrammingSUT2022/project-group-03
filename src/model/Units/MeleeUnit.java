package model.Units;

import model.Civilization;
import model.resources.Resource;
import model.technologies.Technology;
import model.tiles.Tile;

import java.util.ArrayList;

public class MeleeUnit extends NonCivilian {
    public MeleeUnit(Tile tile, Civilization civilization) {
        super(tile,civilization);

    }

    private void attack(Tile tile) {
    }

    private boolean defense(Tile tile) {
        return true;
    }
}
