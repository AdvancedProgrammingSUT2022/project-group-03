package model.technologies;

import java.util.ArrayList;

public class Technology {
    protected static int cost;
    protected static ArrayList<Technology> PrerequisiteTechs;

    public static int getCost() {
        return cost;
    }

    public static ArrayList<Technology> getPrerequisiteTechs() {
        return PrerequisiteTechs;
    }
}
