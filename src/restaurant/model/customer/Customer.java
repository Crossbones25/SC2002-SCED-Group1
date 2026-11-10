package restaurant.model.customer;

import restaurant.model.menu.MenuItem;

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

    public abstract void decaySatisfaction();
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