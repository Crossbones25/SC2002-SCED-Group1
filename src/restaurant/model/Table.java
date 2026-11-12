package restaurant.model;

import restaurant.model.customer.Customer;
import restaurant.model.staff.Order;

public class Table {
    private final int id;
    private Customer currentCustomer;
    private Order currentOrder;

    public Table(int id) {
        this.id = id;
    }

    public int getId() { return id; }
    public boolean isOccupied() { return currentCustomer != null; }
    public Customer getCurrentCustomer() { return currentCustomer; }
    public Order getCurrentOrder() { return currentOrder; }

    public void seat(Customer customer) {
        if (isOccupied()) {
            throw new IllegalStateException("Table " + id + " is already occupied.");
        }
        currentCustomer = customer;
    }

    public void setOrder(Order order) {
        currentOrder = order;
    }

    public void clear() {
        currentCustomer = null;
        currentOrder = null;
    }
}