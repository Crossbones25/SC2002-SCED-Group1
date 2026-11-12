package restaurant.model.customer;

import restaurant.model.menu.Menu;
import restaurant.model.menu.MenuItem;

/**
 * Base class for all customers. Every type follows the same state flow,
 * starts at 100 satisfaction, never drops below 0, and becomes ABANDONED at 0.
 * Subclasses define how much satisfaction they lose, how they are billed,
 * and optionally how they choose their food.
 */
public abstract class Customer {
    private final int id;
    private int satisfaction;
    private CustomerState state;
    private MenuItem selectedItem;
    private int turnServed = -1;

    public Customer(int id) {
        this.id = id;
        this.satisfaction = 100;
        this.state = CustomerState.WAITING_QUEUE;
    }

    public int getTurnServed() { return turnServed; }
    public void setTurnServed(int turn) { this.turnServed = turn; }

    public int getId() { return id; }
    public int getSatisfaction() { return satisfaction; }
    public CustomerState getState() { return state; }
    public MenuItem getSelectedItem() { return selectedItem; }

    public void setState(CustomerState state) { this.state = state; }
    public void setSelectedItem(MenuItem item) { this.selectedItem = item; }

    /**
     * Chooses this customer's dish. By default, customers follow the menu's
     * deterministic rule based on their ID. Subclasses may override this.
     */
    public MenuItem chooseItem(Menu menu) {
        return menu.selectItemDeterministically(id);
    }

    /** Returns the customer type shown in the console, such as "Regular". */
    public abstract String getTypeName();

    /** Applies this customer type's satisfaction loss for one unserved turn. */
    public abstract void decaySatisfaction();

    /** Returns the bill in cents after this customer type's pricing rule. */
    public abstract int calculateFinalBill(int basePriceCents);

    protected void loseSatisfaction(int points) {
        if (!isWaitingForService()) {
            return;
        }
        satisfaction = Math.max(0, satisfaction - points);
        if (satisfaction == 0) {
            state = CustomerState.ABANDONED;
        }
    }

    private boolean isWaitingForService() {
        return state == CustomerState.WAITING_QUEUE
                || state == CustomerState.SEATED
                || state == CustomerState.ORDERED;
    }
}