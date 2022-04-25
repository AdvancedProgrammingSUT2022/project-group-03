package model.technologies;

import java.util.ArrayList;
import java.util.HashMap;

public class Technology {
    private TechnologyType technologyType;
    private int remainedScienceUntilCompleteTechnology;

    public Technology(TechnologyType technologyType){
        this.technologyType = technologyType;
        remainedScienceUntilCompleteTechnology = technologyType.cost;
    }

    public TechnologyType getTechnologyType() {
        return technologyType;
    }

    public int getRemainedScienceUntilCompleteTechnology() {
        return remainedScienceUntilCompleteTechnology;
    }

    public void changeRemainedScienceUntilCompleteTechnology(int remainedScienceUntilCompleteTechnology) {
        this.remainedScienceUntilCompleteTechnology += remainedScienceUntilCompleteTechnology;
    }
}
