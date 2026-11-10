package restaurant.model.staff;

import restaurant.model.RestaurantState;
import restaurant.model.Table;
import restaurant.model.customer.Customer;
import restaurant.model.customer.CustomerState;

/** The Cashier automatically handles at most one READY_TO_PAY customer per turn. */
public class Cashier extends Staff {

    public Cashier(String name) { super(name); }

    /**
     * Collects the bill from the oldest READY_TO_PAY customer (lowest customer id = earliest
     * arrival), records revenue and satisfaction, marks customer and order PAID, frees the table.
     * Does nothing if nobody is ready to pay or a payment was already made this turn.
     */
    public void processPayment(RestaurantState state) {
        if (actionUsedThisTurn) {
            return;
        }
        Table target = null;
        for (Table t : state.getTables()) {
            Customer c = t.getCurrentCustomer();
            if (c == null || c.getState() != CustomerState.READY_TO_PAY || t.getCurrentOrder() == null) {
                continue;
            }
            if (target == null || c.getId() < target.getCurrentCustomer().getId()) {
                target = t;
            }
        }
        if (target == null) {
            return;
        }
        Customer customer = target.getCurrentCustomer();
        Order order = target.getCurrentOrder();

        int bill = customer.calculateFinalBill(order.getItem().getPriceCents());
        state.addRevenueCents(bill);
        state.recordPaidCustomer(customer.getSatisfaction());
        customer.setState(CustomerState.PAID);
        order.setStatus(OrderStatus.PAID);
        target.clear();
        markActionUsed();
    }
}
