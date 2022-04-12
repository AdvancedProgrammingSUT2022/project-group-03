package model.Units;

import model.Civilization;
import model.Map;
import model.resources.Resource;
import model.technologies.Technology;
import model.tiles.Tile;

import java.util.ArrayList;
enum state{
    moving,
    sleeping,
    gettingReady,
    boosting,
    boostingUntilFullRecovery,
    deployed,
    gettingReadyToDoRangedAttack,
    RangedAttacking,
    raiding,
    foundingCity,
    cancelling,
    wakingUp,
    Deleting
}
public class Unit {
    private Tile currentTile;
    private Tile destinationTile;
    private static Civilization civilization;
    private static int cost;
    private int defaultMovementPrice;
    private int movementPrice;
    private static int combatStrength;
    private static Resource resource;
    private static int state;
    private int health=10;
    private static int combatType; // 0 civilian
    //  1 archery
    //  2 melee
    // 3 mounted
    // 4 naval
    // 5 recon
    // 6 siege
    // 7 gunpowder
    // 8

    private void OpenNewArea() {


    }

    private void move(Tile tile) {
        Tile tempTile = Map.FindBestMove(this);


    }

    public int getDefaultMovementPrice() {
        return defaultMovementPrice;
    }

    public void setMovementPrice(int movementPrice) {
        this.movementPrice = movementPrice;
    }

    public int getMovementPrice() {
        return movementPrice;
    }
    public static boolean canBeMade(ArrayList<Resource> resources, ArrayList<Technology> technologies, int civilizationGold)
    {
        return false;
    }
    public Unit(Tile tile, ArrayList<Resource> resources, ArrayList<Technology> technologies, int civilizationGold)
    {

    }

    public void setTheNumbers()
    {

    }

}
