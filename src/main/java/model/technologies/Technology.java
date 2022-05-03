package model.technologies;


import model.Producible;

public class Technology implements Producible {
    private final TechnologyType technologyType;
    private int remainedCost;

    public Technology(TechnologyType technologyType){
        this.technologyType = technologyType;
        remainedCost = technologyType.cost;
    }

    public TechnologyType getTechnologyType() {
        return technologyType;
    }
    @Override
    public int getRemainedCost() {
        return remainedCost;
    }

    public void changeRemainedCost(int remainedScienceUntilCompleteTechnology) {
        this.remainedCost += remainedScienceUntilCompleteTechnology;
    }
    @Override
    public int getCost(){return technologyType.cost;}
    @Override
    public void setRemainedCost(int remainedCost){this.remainedCost = remainedCost;}

    @Override
    public String getName()
    {
        return technologyType.toString();
    }
}
