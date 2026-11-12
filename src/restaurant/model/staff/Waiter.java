package restaurant.model.staff;

import restaurant.model.Table;
import restaurant.model.customer.Customer;
import restaurant.model.customer.CustomerState;
import restaurant.model.menu.Menu;
import restaurant.model.menu.MenuItem;

import java.util.Objects;

/** The Waiter completes at most one task per turn: seat, take an order, or serve a dish. */
public class Waiter extends Staff {

    public Waiter(String name) { super(name); }

    /**
     * Seats a customer at a free table. The caller (controller) removes the customer
     * from the waiting queue, e.g. via state.getWaitingQueue().poll().
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

    /**
     * Takes the order of the customer seated at the table. The customer's food choice must
     * already be set; if not, it is chosen with the Menu's deterministic rule.
     * The caller can read the choice from the returned Order.
     */
    public Order takeOrder(Table t) {
        requireActionAvailable();
        Objects.requireNonNull(t, "table");
        Customer c = t.getCurrentCustomer();
        if (!t.isOccupied() || c == null) {
            throw new IllegalStateException("Table " + t.getId() + " has no customer.");
        }
        if (t.getCurrentOrder() != null) {
            throw new IllegalStateException("Table " + t.getId() + " has already ordered.");
        }
        MenuItem item = c.getSelectedItem();
        if (item == null) {
            item = new Menu().selectItemDeterministically(c.getId());
            c.setSelectedItem(item);
        }
        Order order = new Order(c, t, item);
        t.setOrder(order);
        c.setState(CustomerState.ORDERED);
        markActionUsed();
        return order;
    }

    /** Serves the table's order. Only valid if the order was already READY. */
    public void serveDish(Table t) {
        requireActionAvailable();
        Objects.requireNonNull(t, "table");
        Order order = t.getCurrentOrder();
        if (order == null || order.getStatus() != OrderStatus.READY) {
            throw new IllegalStateException("Table " + t.getId() + " has no READY dish to serve.");
        }
        order.setStatus(OrderStatus.SERVED);
        order.getCustomer().setState(CustomerState.SERVED);
        markActionUsed();
    }
}
