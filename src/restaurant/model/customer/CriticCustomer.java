package restaurant.model.customer;

import restaurant.model.menu.Menu;
import restaurant.model.menu.MenuItem;

/**
 * Critic customer: loses 12 satisfaction per unserved turn and pays full price.
 * Special behaviour: a Critic always orders Pasta, the restaurant's signature dish.
 */
public class CriticCustomer extends Customer {
    private static final int SATISFACTION_LOSS = 12;
    private static final String PREFERRED_DISH = "Pasta";

    public CriticCustomer(int id) {
        super(id);
    }

    @Override
    public void decaySatisfaction() {
        loseSatisfaction(SATISFACTION_LOSS);
    }

    @Override
    public int calculateFinalBill(int basePriceCents) {
        return basePriceCents;
    }

    /** Always orders Pasta. Falls back to the default rule if Pasta is missing. */
    @Override
    public MenuItem chooseItem(Menu menu) {
        MenuItem pasta = menu.getItemByName(PREFERRED_DISH);
        return pasta != null ? pasta : super.chooseItem(menu);
    }

    @Override
    public String getTypeName() {
        return "Critic";
    }
}