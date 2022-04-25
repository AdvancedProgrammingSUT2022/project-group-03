package model.resources;

public enum ResourcesTypes {
    BANANA(ResourcesCategory.BONUS),
    COW(ResourcesCategory.BONUS),
    DEER(ResourcesCategory.BONUS),
    SHEEP(ResourcesCategory.BONUS),
    WHEAT(ResourcesCategory.BONUS),
    COTTON(ResourcesCategory.LUXURY),
    COLOR(ResourcesCategory.LUXURY),
    FUR(ResourcesCategory.LUXURY),
    GEMSTONE(ResourcesCategory.LUXURY),
    GOLD(ResourcesCategory.LUXURY),
    INCENSE(ResourcesCategory.LUXURY),
    IVORY(ResourcesCategory.LUXURY),
    MARBLE(ResourcesCategory.LUXURY),
    SILK(ResourcesCategory.LUXURY),
    SILVER(ResourcesCategory.LUXURY),
    SUGAR(ResourcesCategory.LUXURY),
    COAL(ResourcesCategory.STRATEGIC),
    HORSE(ResourcesCategory.STRATEGIC),
    IRON(ResourcesCategory.STRATEGIC);
    public final ResourcesCategory resourcesCategory;


    ResourcesTypes(ResourcesCategory resourcesCategory) {
        this.resourcesCategory = resourcesCategory;
    }
}
