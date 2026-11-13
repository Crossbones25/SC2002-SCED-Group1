package restaurant.ui;

import restaurant.model.RestaurantState;
import restaurant.model.Table;
import restaurant.model.customer.Customer;
import restaurant.model.staff.Order;

/** Boundary class: displays state only. It contains no game rules. */
public class ConsoleView {

    public void displayTurnHeader(int turn, RestaurantState state) {
        System.out.println();
        System.out.println("==================== TURN " + turn + " ====================");
        System.out.println("Revenue: " + dollars(state.getTotalRevenueCents())
                + " | Avg satisfaction (paid): " + averageText(state)
                + " | Paid customers: " + state.getPaidCustomerCount());

        System.out.println("Waiting queue:");
        if (state.getWaitingQueue().isEmpty()) {
            System.out.println("  (empty)");
        } else {
            for (Customer c : state.getWaitingQueue()) {
                System.out.println("  " + describe(c) + " | satisfaction " + c.getSatisfaction());
            }
        }

        System.out.println("Tables:");
        for (Table t : state.getTables()) {
            if (!t.isOccupied() || t.getCurrentCustomer() == null) {
                System.out.println("  Table " + t.getId() + ": free");
                continue;
            }
            Customer c = t.getCurrentCustomer();
            Order o = t.getCurrentOrder();
            String orderText = (o == null)
                    ? "no order yet"
                    : o.getItem().getName() + " [" + o.getStatus()
                      + (o.getRemainingPrepUnits() > 0 ? ", " + o.getRemainingPrepUnits() + " prep unit(s) left" : "")
                      + "]";
            System.out.println("  Table " + t.getId() + ": " + describe(c)
                    + " | satisfaction " + c.getSatisfaction()
                    + " | " + c.getState() + " | order: " + orderText);
        }

        System.out.println("Staff tasks available:");
        System.out.println("  Waiter: " + waiterTasks(state));
        System.out.println("  Chef:   " + chefTasks(state));
        System.out.println("  Cashier: automatic (pays the oldest READY_TO_PAY customer)");
    }

    /** Shown right after an order is taken, so the customer's food choice is visible. */
    public void displayOrderPlaced(Order order) {
        System.out.println("  -> " + describe(order.getCustomer()) + " at Table " + order.getTable().getId()
                + " ordered " + order.getItem().getName()
                + " (" + dollars(order.getItem().getPriceCents()) + ", "
                + order.getItem().getPrepUnits() + " prep unit(s)).");
    }

    public void displayTurnSummary(int turn, RestaurantState state) {
        System.out.println("---------- End of turn " + turn + " ----------");
        System.out.println("Served customers:   " + state.getServedCustomerCount());
        System.out.println("Unhappy departures: " + state.getDepartedCustomerCount());
        System.out.println("Revenue:            " + dollars(state.getTotalRevenueCents()));
        System.out.println("Avg satisfaction:   " + averageText(state));
        System.out.println("Turns used:         " + turn);
    }

    public void displayGameResult(boolean victory, int paidCount, int revenueCents, double avgSatisfaction) {
        System.out.println();
        System.out.println("==================== GAME OVER ====================");
        System.out.println(victory ? "RESULT: VICTORY" : "RESULT: DEFEAT");
        System.out.println("Paid customers: " + paidCount + " (need at least 4)");
        System.out.println("Revenue:        " + dollars(revenueCents) + " (need at least $45.00)");
        System.out.println("Avg satisfaction of paid customers: "
                + (paidCount == 0 ? "N/A" : String.format("%.1f", avgSatisfaction)) + " (need at least 60)");
    }

    public void displayErrorMessage(String message) {
        System.out.println("  [!] " + message);
    }

    // ---- helpers ----

    private String waiterTasks(RestaurantState state) {
        StringBuilder sb = new StringBuilder();
        if (!state.getWaitingQueue().isEmpty() && !state.getFreeTables().isEmpty()) {
            sb.append("Seat | ");
        }
        if (!state.getTablesAwaitingOrder().isEmpty()) sb.append("Take order | ");
        if (!state.getTablesWithReadyOrder().isEmpty()) sb.append("Serve | ");
        sb.append("Wait");
        return sb.toString();
    }

    private String chefTasks(RestaurantState state) {
        return state.getTablesWithOrderInProgress().isEmpty() ? "Wait" : "Cook | Wait";
    }

    private String describe(Customer c) {
        return "Customer #" + c.getId() + " (" + c.getTypeName() + ")";
    }

    private String averageText(RestaurantState state) {
        return state.getPaidCustomerCount() == 0
                ? "N/A" : String.format("%.1f", state.getAveragePaidSatisfaction());
    }

    private static String dollars(int cents) {
        return String.format("$%d.%02d", cents / 100, cents % 100);
    }
}
