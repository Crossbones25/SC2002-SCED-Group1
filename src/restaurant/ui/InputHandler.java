package restaurant.ui;

import restaurant.model.RestaurantState;
import restaurant.model.Table;
import restaurant.model.customer.Customer;
import restaurant.model.staff.Order;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

/**
 * Boundary class: collects the manager's decisions and validates them against the current state.
 * Invalid input is explained and re-prompted; no staff action is consumed.
 */
public class InputHandler {

    public enum ActionType {
        SEAT,
        TAKE_ORDER,
        SERVE,
        COOK,
        WAIT
    }

    public record StaffAction(ActionType type, int targetTableId) {
        // Convenience constructor for actions without a target table (e.g. WAIT)
        public StaffAction(ActionType type) {
            this(type, -1);
        }
    }

    private final Scanner scanner;
    private final ConsoleView view = new ConsoleView();

    public InputHandler() {
        this(System.in);
    }

    public InputHandler(InputStream in) {
        this.scanner = new Scanner(in);
    }

    // Prompts manager for Waiter action (SEAT, TAKE_ORDER, SERVE, WAIT)
    // Re-prompts on invalid input without consuming the action
    public StaffAction getWaiterChoice(RestaurantState state) {
        while (true) {
            System.out.println();
            System.out.println("WAITER - choose one task:");
            System.out.println("  1) Seat the next queued customer");
            System.out.println("  2) Take an order at a table");
            System.out.println("  3) Serve a READY dish");
            System.out.println("  4) Wait");
            String line = readLine("> ");
            if (line == null) return endOfInput("Waiter");

            Integer option = parseInt(line);
            if (option == null || option < 1 || option > 4) {
                view.displayErrorMessage("Invalid choice \"" + line.trim() + "\". Enter a number from 1 to 4.");
                continue;
            }

            switch (option) {
                case 1 -> {
                    if (state.getWaitingQueue().isEmpty()) {
                        view.displayErrorMessage("Cannot seat: no customers are waiting in the queue.");
                        continue;
                    }
                    List<Table> free = state.getFreeTables();
                    if (free.isEmpty()) {
                        view.displayErrorMessage("Cannot seat: every table is occupied.");
                        continue;
                    }
                    Customer next = state.getWaitingQueue().peek();
                    Integer id = askTableId("Seat Customer #" + next.getId() + " at which table? Free: " + ids(free) + " > ");
                    if (id == null) return endOfInput("Waiter");
                    if (id == NOT_A_NUMBER) {
                        view.displayErrorMessage("Please enter a table number.");
                        continue;
                    }
                    Table t = state.getTableById(id);
                    if (t == null) {
                        view.displayErrorMessage("Table " + id + " does not exist.");
                    } else if (t.isOccupied()) {
                        view.displayErrorMessage("Table " + id + " is occupied. Free tables: " + ids(free) + ".");
                    } else {
                        return new StaffAction(ActionType.SEAT, id);
                    }
                }
                case 2 -> {
                    List<Table> awaiting = state.getTablesAwaitingOrder();
                    if (awaiting.isEmpty()) {
                        view.displayErrorMessage("Cannot take an order: no seated customer is waiting to order.");
                        continue;
                    }
                    Integer id = askTableId("Take order at which table? Waiting to order: " + ids(awaiting) + " > ");
                    if (id == null) return endOfInput("Waiter");
                    if (id == NOT_A_NUMBER) {
                        view.displayErrorMessage("Please enter a table number.");
                        continue;
                    }
                    Table t = state.getTableById(id);
                    if (t == null) {
                        view.displayErrorMessage("Table " + id + " does not exist.");
                    } else if (!t.isOccupied()) {
                        view.displayErrorMessage("Table " + id + " has no customer.");
                    } else if (t.getCurrentOrder() != null) {
                        view.displayErrorMessage("Table " + id + " has already ordered.");
                    } else if (!awaiting.contains(t)) {
                        view.displayErrorMessage("Table " + id + "'s customer is not ready to order.");
                    } else {
                        return new StaffAction(ActionType.TAKE_ORDER, id);
                    }
                }
                case 3 -> {
                    List<Table> ready = state.getTablesWithReadyOrder();
                    if (ready.isEmpty()) {
                        view.displayErrorMessage("Cannot serve: no order is READY yet "
                                + "(a dish finished this turn can only be served next turn).");
                        continue;
                    }
                    Integer id = askTableId("Serve which table? READY: " + ids(ready) + " > ");
                    if (id == null) return endOfInput("Waiter");
                    if (id == NOT_A_NUMBER) {
                        view.displayErrorMessage("Please enter a table number.");
                        continue;
                    }
                    Table t = state.getTableById(id);
                    Order o = (t == null) ? null : t.getCurrentOrder();
                    if (t == null) {
                        view.displayErrorMessage("Table " + id + " does not exist.");
                    } else if (o == null) {
                        view.displayErrorMessage("Table " + id + " has no order.");
                    } else if (!ready.contains(t)) {
                        view.displayErrorMessage("Table " + id + "'s order is " + o.getStatus()
                                + ", not READY. READY: " + ids(ready) + ".");
                    } else {
                        return new StaffAction(ActionType.SERVE, id);
                    }
                }
                default -> {
                    return new StaffAction(ActionType.WAIT);
                }
            }
        }
    }

    // Prompts manager for Chef action (COOK, WAIT)
    public StaffAction getChefChoice(RestaurantState state) {
        while (true) {
            System.out.println();
            System.out.println("CHEF - choose one task:");
            System.out.println("  1) Cook one preparation unit of an order");
            System.out.println("  2) Wait");
            String line = readLine("> ");
            if (line == null) return endOfInput("Chef");

            Integer option = parseInt(line);
            if (option == null || option < 1 || option > 2) {
                view.displayErrorMessage("Invalid choice \"" + line.trim() + "\". Enter 1 or 2.");
                continue;
            }
            if (option == 2) {
                return new StaffAction(ActionType.WAIT);
            }

            List<Table> cookable = state.getTablesWithOrderInProgress();
            if (cookable.isEmpty()) {
                view.displayErrorMessage("Cannot cook: there are no orders in preparation.");
                continue;
            }
            Integer id = askTableId("Cook the order at which table? In preparation: " + ids(cookable) + " > ");
            if (id == null) return endOfInput("Chef");
            if (id == NOT_A_NUMBER) {
                view.displayErrorMessage("Please enter a table number.");
                continue;
            }
            Table t = state.getTableById(id);
            Order o = (t == null) ? null : t.getCurrentOrder();
            if (t == null) {
                view.displayErrorMessage("Table " + id + " does not exist.");
            } else if (o == null) {
                view.displayErrorMessage("Table " + id + " has no order to cook.");
            } else if (!cookable.contains(t)) {
                view.displayErrorMessage("Table " + id + "'s order is already " + o.getStatus()
                        + ". In preparation: " + ids(cookable) + ".");
            } else {
                return new StaffAction(ActionType.COOK, id);
            }
        }
    }

    // ---- helpers ----

    private static final int NOT_A_NUMBER = Integer.MIN_VALUE;

    /** Returns null on end of input, NOT_A_NUMBER on non-numeric text, else the table id. */
    private Integer askTableId(String prompt) {
        String line = readLine(prompt);
        if (line == null) return null;
        Integer id = parseInt(line);
        return id == null ? NOT_A_NUMBER : id;
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        System.out.flush();
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine();
    }

    private Integer parseInt(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** Input ran out (e.g. piped file ended): wait instead of looping forever. */
    private StaffAction endOfInput(String role) {
        System.out.println();
        System.out.println("[End of input - " + role + " waits]");
        return new StaffAction(ActionType.WAIT);
    }

    private String ids(List<Table> tables) {
        StringBuilder sb = new StringBuilder();
        for (Table t : tables) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(t.getId());
        }
        return sb.toString();
    }
}
