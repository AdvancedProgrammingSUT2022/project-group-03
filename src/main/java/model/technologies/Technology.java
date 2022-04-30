package model.technologies;

import model.producible;

public class Technology implements producible {
    private final TechnologyType technologyType;
    private int remainedCost;

    public Technology(TechnologyType technologyType){
        this.technologyType = technologyType;
        remainedCost = technologyType.cost;
    }

    public TechnologyType getTechnologyType() {
        return technologyType;
    }
    public int getCost(){
        return technologyType.cost;
    }
    public int getRemainedCost() {
        return remainedCost;
    }

    public void changeRemainedCost(int remainedScienceUntilCompleteTechnology) {
        this.remainedCost += remainedScienceUntilCompleteTechnology;
    }
}
