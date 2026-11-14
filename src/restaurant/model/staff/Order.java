package restaurant.model.staff;

import restaurant.model.Table;
import restaurant.model.customer.Customer;
import restaurant.model.menu.MenuItem;

import java.util.Objects;

/** One customer's order for one menu item, tracking remaining kitchen preparation. */
public class Order {
    private final Customer customer;
    private final Table table;
    private final MenuItem item;
    private final int priceCents;
    private int remainingPrepUnits;
    private OrderStatus status;

    public Order(Customer customer, Table table, MenuItem item, int priceCents) {
      this.customer = Objects.requireNonNull(customer, "customer");
      this.table = Objects.requireNonNull(table, "table");
      this.item = Objects.requireNonNull(item, "item");
      if (priceCents < 0) {
         throw new IllegalArgumentException("priceCents must not be negative.");
      }
      this.priceCents = priceCents;
      this.remainingPrepUnits = item.getPrepUnits();
      this.status = OrderStatus.IN_PROGRESS;
    }

    public Order(Customer customer, Table table, MenuItem item) {
      this(customer, table, item, customer.calculateFinalBill(item.getPriceCents()));
    }

    public Customer getCustomer() { return customer; }
    public Table getTable() { return table; }
    public MenuItem getItem() { return item; }
    public int getPriceCents() { return priceCents; }
    public int getRemainingPrepUnits() { return remainingPrepUnits; }
    public OrderStatus getStatus() { return status; }

    /** Deducts 1 prep unit; when none remain the order becomes READY. */
    public void advancePrep() {
        if (status != OrderStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cannot cook an order that is " + status + ".");
        }
        remainingPrepUnits--;
        if (remainingPrepUnits <= 0) {
            remainingPrepUnits = 0;
            status = OrderStatus.READY;
        }
    }

    public void setStatus(OrderStatus status) {
        this.status = Objects.requireNonNull(status, "status");
    }
}
