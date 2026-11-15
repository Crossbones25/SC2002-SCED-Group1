package restaurant.model.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The restaurant menu: three single items and three combos.
 * The deterministic food rule only cycles through the single items,
 * so earlier stages' orders stay the same.
 */
public class Menu {
    private final List<MenuItem> singleItems;
    private final List<MenuItem> allItems;

    public Menu() {
        MenuItem salad = new SingleMenuItem("Salad", 900, 1);
        MenuItem burger = new SingleMenuItem("Burger", 1500, 1);
        MenuItem pasta = new SingleMenuItem("Pasta", 1800, 2);
        singleItems = List.of(salad, burger, pasta);

        List<MenuItem> items = new ArrayList<>(singleItems);
        items.add(new ComboMeal(salad, burger));
        items.add(new ComboMeal(salad, pasta));
        items.add(new ComboMeal(burger, pasta));
        allItems = Collections.unmodifiableList(items);
    }

    public List<MenuItem> getAllItems() {
        return allItems;
    }

    // Returns null if no item has this name.
    public MenuItem getItemByName(String name) {
        for (MenuItem item : allItems) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    // Customers 1, 2, 3 order Salad, Burger, Pasta, then the cycle repeats.
    public MenuItem selectItemDeterministically(int customerId) {
        return singleItems.get(Math.floorMod(customerId - 1, singleItems.size()));
    }
}