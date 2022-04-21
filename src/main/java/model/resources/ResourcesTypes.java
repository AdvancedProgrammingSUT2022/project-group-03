package model.resources;

public enum ResourcesTypes {
    BANANA(4),
    COW,
    DEER,
    SHEEP,
    WHEAT,
    COTTON,
    COLOR,
    FUR,
    GEMSTONE,
    GOLD,
    BOKHOOR,
    IVORY,
    MARBLE,
    SILK,
    SILVER,
    SUGAR,
    COAL,
    HORSE,
    IRON;

    int number;

    ResourcesTypes(int number)
    {
        this.number = number;
    }
}
