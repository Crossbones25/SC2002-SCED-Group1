package restaurant.model.menu;

/**
 * A combo made of two existing menu items.
 * Preparation units are the sum of both items.
 * Price is 90% of their combined price, rounded to the nearest cent.
 * For example, Salad + Pasta = (900 + 1800) x 0.9 = 2430 cents, 3 units.
 */
public class ComboMeal implements MenuItem {
    private static final int COMBO_PERCENT = 90;

    private final MenuItem first;
    private final MenuItem second;

    public ComboMeal(MenuItem first, MenuItem second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String getName() {
        return first.getName() + " + " + second.getName();
    }

    @Override
    public int getPriceCents() {
        int combined = first.getPriceCents() + second.getPriceCents();
        return (int) Math.round(combined * COMBO_PERCENT / 100.0);
    }

    @Override
    public int getPrepUnits() {
        return first.getPrepUnits() + second.getPrepUnits();
    }
}