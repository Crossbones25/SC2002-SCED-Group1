package restaurant.controller;

import restaurant.model.*;
import restaurant.model.customer.*;
import restaurant.model.menu.*;
import restaurant.model.staff.*;
import restaurant.ui.*;
import restaurant.ui.InputHandler.*;

public class TurnProcessor {

    public TurnProcessor() {}

    public record Actions(StaffAction waiterAction, StaffAction chefAction) {}

    private void arrivalReset(RestaurantState state) {
        state.getWaiter().resetTurn();
        state.getChef().resetTurn();
        state.getCashier().resetTurn();
    }

    private Actions getManagerInputs(InputHandler input, RestaurantState state) {
        StaffAction waiterAction = input.getWaiterChoice(state);
        StaffAction chefAction = input.getChefChoice(state);
        return new Actions(waiterAction, chefAction);
    }

    private void executeStaffActions(int turn, RestaurantState state, ConsoleView view, Actions actions) {
        //Waiter acts first
        StaffAction waiterAct = actions.waiterAction();
        Table waiterTargetTable = state.getTableById(waiterAct.targetTableId());

        switch (waiterAct.type()) {
            case SEAT -> {
                if (!state.getWaitingQueue().isEmpty() && waiterTargetTable != null && !waiterTargetTable.isOccupied()) {
                    Customer customer = state.getWaitingQueue().poll();
                    waiterTargetTable.seat(customer);
                    customer.setState(CustomerState.SEATED);
                    state.getWaiter().markActionUsed();
                }
            }
            case TAKE_ORDER -> {
                if (waiterTargetTable != null) {
                    Customer customer = waiterTargetTable.getCurrentCustomer();
                    MenuItem item = customer.getSelectedItem();
                    Order order = new Order(customer, waiterTargetTable, item);
                    order.setStatus(OrderStatus.IN_PROGRESS);
                    waiterTargetTable.setOrder(order);
                    customer.setState(CustomerState.ORDERED);
                    view.displayOrderPlaced(order);
                }
                state.getWaiter().markActionUsed();
            }
            case SERVE -> {
                if (waiterTargetTable != null) {
                    Customer customer = waiterTargetTable.getCurrentCustomer();
                    Order order = waiterTargetTable.getCurrentOrder();
                    if (order != null && order.getStatus() == OrderStatus.READY) {
                        order.setStatus(OrderStatus.SERVED);
                        customer.setState(CustomerState.SERVED);
                        customer.setTurnServed(turn);
                    }
                }
                state.getWaiter().markActionUsed();
            }
            case WAIT -> {
                state.getWaiter().markActionUsed();
            }
        }

        //Chef acts next
        StaffAction chefAct = actions.chefAction();
        Table chefTargetTable = state.getTableById(waiterAct.targetTableId());

        switch (chefAct.type()) {
            case COOK -> {
                if (chefTargetTable != null) {
                    Order order = chefTargetTable.getCurrentOrder();
                    if (order != null && order.getStatus() == OrderStatus.IN_PROGRESS) {
                        order.advancePrep();
                    }
                }
                state.getChef().markActionUsed();
            }
            case WAIT -> {
                state.getChef().markActionUsed();
            }
        }
    }

    private void eatAndPay(int currentTurn, RestaurantState state) {
        //Cashier Processes Payment 
        Table paymentTable = null;
        Customer payingCustomer = null;

        // Find the first (or oldest) table with a customer in READY_TO_PAY
        for (Table table : state.getTables()) {
            if (table.isOccupied()) {
                Customer c = table.getCurrentCustomer();
                if (c != null && c.getState() == CustomerState.READY_TO_PAY) {
                    paymentTable = table;
                    payingCustomer = c;
                    break; // Cashier only serves 1 customer per turn
                }
            }
        }

        if (payingCustomer != null) {
            Order order = paymentTable.getCurrentOrder();
            int basePriceCents = order.getItem().getPriceCents();
            int finalBillCents = payingCustomer.calculateFinalBill(basePriceCents);

            // Update state metrics
            state.addRevenueCents(finalBillCents);
            state.recordPaidCustomer(payingCustomer.getSatisfaction());

            // Clean up customer and table
            payingCustomer.setState(CustomerState.PAID);
            paymentTable.clear();

            state.getCashier().markActionUsed();
        }

        for (Table table : state.getTables()) {
            if (table.isOccupied()) {
                Customer c = table.getCurrentCustomer();
                if (c != null && c.getState() == CustomerState.SERVED) {
                    if (c.getTurnServed() < currentTurn) {
                        c.setState(CustomerState.READY_TO_PAY);
                    }
                }
            }
        }
    }

    private void satisfactionDecay(RestaurantState state) {
        // Decay queue customers and count abandonments
        for (Customer c : state.getWaitingQueue()) {
            c.decaySatisfaction();
            if (c.getState() == CustomerState.ABANDONED) {
                state.incrementDepartedCustomers();
            }
        }
        // Clean up abandoned customers from the queue
        state.getWaitingQueue().removeIf(c -> c.getState() == CustomerState.ABANDONED);

        for (Table table : state.getTables()) {
            if (table.isOccupied()) {
                Customer c = table.getCurrentCustomer();
                if (c != null) {
                    c.decaySatisfaction();
                    if (c.getState() == CustomerState.ABANDONED) {
                        table.clear();
                        state.incrementDepartedCustomers();
                    }
                }
            }
        }
    }

    public void processTurn(int turn, RestaurantState state, InputHandler input, ConsoleView view) {
        arrivalReset(state);
        Actions actions = getManagerInputs(input, state);
        executeStaffActions(turn, state, view, actions);
        eatAndPay(turn, state);
        satisfactionDecay(state);
    }
}