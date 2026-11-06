package restaurant.model;

import restaurant.model.customer.Customer;
import restaurant.model.customer.CustomerState;
import restaurant.model.staff.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/** Holds all mutable game state: tables, queue, staff and running totals. */
public class RestaurantState {
    private static final int TABLE_COUNT = 2;

    private final Queue<Customer> waitingQueue = new LinkedList<>();
    private final List<Table> tables = new ArrayList<>();
    private final Waiter waiter = new Waiter("Waiter");
    private final Chef chef = new Chef("Chef");
    private final Cashier cashier = new Cashier("Cashier");

    private int totalRevenueCents;
    private int paidCustomerCount;
    private int totalPaidSatisfaction;
    private int departedCustomerCount;

    public RestaurantState() {
        for (int i = 1; i <= TABLE_COUNT; i++) {
            tables.add(new Table(i));
        }
    }

    public Queue<Customer> getWaitingQueue() { return waitingQueue; }
    public List<Table> getTables() { return Collections.unmodifiableList(tables); }

    /** Orders still in the restaurant (IN_PROGRESS, READY or SERVED), derived from the tables. */
    public List<Order> getActiveOrders() {
        List<Order> active = new ArrayList<>();
        for (Table t : tables) {
            Order o = t.getCurrentOrder();
            if (o != null && (o.getStatus() == OrderStatus.IN_PROGRESS
                    || o.getStatus() == OrderStatus.READY
                    || o.getStatus() == OrderStatus.SERVED)) {
                active.add(o);
            }
        }
        return active;
    }

    public Waiter getWaiter() { return waiter; }
    public Chef getChef() { return chef; }
    public Cashier getCashier() { return cashier; }

    public int getTotalRevenueCents() { return totalRevenueCents; }
    public void addRevenueCents(int cents) { totalRevenueCents += cents; }

    public int getPaidCustomerCount() { return paidCustomerCount; }
    public double getAveragePaidSatisfaction() {
        return paidCustomerCount == 0 ? 0.0 : (double) totalPaidSatisfaction / paidCustomerCount;
    }
    public void recordPaidCustomer(int satisfaction) {
        paidCustomerCount++;
        totalPaidSatisfaction += satisfaction;
    }

    public int getDepartedCustomerCount() { return departedCustomerCount; }
    public void incrementDepartedCustomers() { departedCustomerCount++; }

    // ---- Query helpers (rules about what each staff action may target) ----

    public Table getTableById(int id) {
        for (Table t : tables) {
            if (t.getId() == id) return t;
        }
        return null;
    }

    public List<Table> getFreeTables() {
        List<Table> result = new ArrayList<>();
        for (Table t : tables) {
            if (!t.isOccupied()) result.add(t);
        }
        return result;
    }

    /** Occupied tables whose customer is seated but has not ordered yet. */
    public List<Table> getTablesAwaitingOrder() {
        List<Table> result = new ArrayList<>();
        for (Table t : tables) {
            Customer c = t.getCurrentCustomer();
            if (t.isOccupied() && c != null && t.getCurrentOrder() == null
                    && c.getState() == CustomerState.SEATED) {
                result.add(t);
            }
        }
        return result;
    }

    public List<Table> getTablesWithReadyOrder() {
        List<Table> result = new ArrayList<>();
        for (Table t : tables) {
            Order o = t.getCurrentOrder();
            if (o != null && o.getStatus() == OrderStatus.READY) result.add(t);
        }
        return result;
    }

    public List<Table> getTablesWithOrderInProgress() {
        List<Table> result = new ArrayList<>();
        for (Table t : tables) {
            Order o = t.getCurrentOrder();
            if (o != null && o.getStatus() == OrderStatus.IN_PROGRESS) result.add(t);
        }
        return result;
    }

    /** Customers who have been served: paid, plus those currently eating or ready to pay. */
    public int getServedCustomerCount() {
        int count = paidCustomerCount;
        for (Table t : tables) {
            Customer c = t.getCurrentCustomer();
            if (c != null && (c.getState() == CustomerState.SERVED
                    || c.getState() == CustomerState.READY_TO_PAY)) {
                count++;
            }
        }
        return count;
    }
}
