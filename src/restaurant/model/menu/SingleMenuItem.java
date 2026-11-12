package restaurant.model.menu;

public class SingleMenuItem implements MenuItem {
    private final String name;
    private final int priceCents;
    private final int prepUnits;

    public SingleMenuItem(String name, int priceCents, int prepUnits) {
        this.name = name;
        this.priceCents = priceCents;
        this.prepUnits = prepUnits;
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getPriceCents() { return priceCents; }

    @Override
    public int getPrepUnits() { return prepUnits; }
}