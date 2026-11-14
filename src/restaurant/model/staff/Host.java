package restaurant.model.staff;

import restaurant.model.Table;
import restaurant.model.customer.Customer;
import restaurant.model.customer.CustomerState;

import java.util.Objects;

/**
The Host seats at most one queued customer per turn. The Host has its own action,
separate from the Waiter's, so seating by the Host does not use up the Waiter's task.
The Host acts before the Waiter, so the Waiter can take the order of a customer the
Host has just seated.
 **/
public class Host extends Staff {

    public Host(String name) { super(name); }

    /**
     * Seats a customer at a free table and marks the Host's action as used.
     * The caller (controller) removes the customer from the waiting queue,
     * e.g. via state.getWaitingQueue().poll().
     *
     * @throws IllegalStateException if the Host already acted this turn or the table is occupied
     */
    public void seatCustomer(Customer c, Table t) {
        requireActionAvailable();
        Objects.requireNonNull(c, "customer");
        Objects.requireNonNull(t, "table");
        if (t.isOccupied()) {
            throw new IllegalStateException("Table " + t.getId() + " is already occupied.");
        }
        t.seat(c);
        c.setState(CustomerState.SEATED);
        markActionUsed();
    }
}