package restaurant.model.menu;

import java.util.List;

public class Menu {
    private final List<MenuItem> items;

    public Menu() {
        items = List.of(
                new SingleMenuItem("Salad", 900, 1),
                new SingleMenuItem("Burger", 1500, 1),
                new SingleMenuItem("Pasta", 1800, 2));
    }

    public List<MenuItem> getAllItems() {
        return items;
    }

    // Returns null if no item has this name.
    public MenuItem getItemByName(String name) {
        for (MenuItem item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    // Customers 1, 2, 3 order Salad, Burger, Pasta, then the cycle repeats.
    public MenuItem selectItemDeterministically(int customerId) {
        return items.get(Math.floorMod(customerId - 1, items.size()));
    }
}