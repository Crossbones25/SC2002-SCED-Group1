package restaurant.controller;

import restaurant.model.*;
import restaurant.model.customer.*;
import restaurant.model.staff.*;
import restaurant.ui.*;
import restaurant.ui.InputHandler.*;

public class TurnProcessor {

    public TurnProcessor() {}

    private void arrivalReset(RestaurantState state) {
        state.getHost().resetTurn();
        state.getWaiter().resetTurn();
        state.getChef().resetTurn();
        state.getCashier().resetTurn();
    }

    private void offerHappyHour(int turn, RestaurantState state, InputHandler input, ConsoleView view) {
        HappyHour happyHour = state.getHappyHour();
        if (!happyHour.canActivate()) {
            return;
        }
        if (input.askHappyHour(state)) {
            happyHour.activate();
            recoverWaitingCustomers(state);
            view.displayTurnHeader(turn, state);
        }
    }

    private void recoverWaitingCustomers(RestaurantState state) {
        for (Customer c : state.getWaitingQueue()) {
            c.recoverSatisfaction(HappyHour.SATISFACTION_BONUS);
        }
        for (Table table : state.getTables()) {
            Customer c = table.getCurrentCustomer();
            if (c != null) {
                c.recoverSatisfaction(HappyHour.SATISFACTION_BONUS);
            }
        }
    }

    public void applyHappyHourRecovery(RestaurantState state) {
        if (state.getHappyHour().isActive()) {
            recoverWaitingCustomers(state);
        }
    }

    private void executeHostAction(RestaurantState state, StaffAction act) {
        if (act.type() == ActionType.SEAT) {
            Table table = state.getTableById(act.targetTableId());
            state.getHost().seatCustomer(state.getWaitingQueue().poll(), table);
        } else {
            state.getHost().markActionUsed();
        }
    }

    private void executeWaiterAction(int turn, RestaurantState state, ConsoleView view, StaffAction act) {
        Table table = state.getTableById(act.targetTableId());
        switch (act.type()) {
            case SEAT ->  {
                state.getWaiter().seatCustomer(state.getWaitingQueue().poll(), table);
            }
            case TAKE_ORDER ->  {
                view.displayOrderPlaced(state.getWaiter().takeOrder(table, state.getHappyHour()));
            }
            case SERVE -> {
                state.getWaiter().serveDish(table);
                table.getCurrentCustomer().setTurnServed(turn);
            }
            case WAIT ->  {
                state.getWaiter().markActionUsed();
            }
            default -> { 
                state.getWaiter().markActionUsed();
            }
        }
    }

    private void executeChefAction(RestaurantState state, StaffAction act) {
        if (act.type() == InputHandler.ActionType.COOK) {
            Table table = state.getTableById(act.targetTableId());  
            state.getChef().cookOrder(table.getCurrentOrder());
        } else {
            state.getChef().markActionUsed();
        }
    }

    private void eatAndPay(int currentTurn, RestaurantState state) {
        state.getCashier().processPayment(state);
 
        for (Table table : state.getTables()) {
            Customer c = table.getCurrentCustomer();
            if (c != null && c.getState() == CustomerState.SERVED && c.getTurnServed() < currentTurn) {
                c.setState(CustomerState.READY_TO_PAY);
            }
        }
    }

    private void abandon(Table table, RestaurantState state) {
        Order order = table.getCurrentOrder();
        if (order != null) 
            order.setStatus(OrderStatus.CANCELLED);
        table.clear();
        state.incrementDepartedCustomers();
    }

    private void satisfactionDecay(RestaurantState state) {
        for (Customer c : state.getWaitingQueue()) {
            c.decaySatisfaction();
            if (c.getState() == CustomerState.ABANDONED) state.incrementDepartedCustomers();
        }
        state.getWaitingQueue().removeIf(c -> c.getState() == CustomerState.ABANDONED);
 
        for (Table table : state.getTables()) {
            Customer c = table.getCurrentCustomer();
            if (c != null) {
                c.decaySatisfaction();
                if (c.getState() == CustomerState.ABANDONED) 
                    abandon(table, state);
            }
        }
    }

    public void processTurn(int turn, RestaurantState state, InputHandler input, ConsoleView view) {
        arrivalReset(state);
        offerHappyHour(turn, state, input, view);

        executeHostAction(state, input.getHostChoice(state));
        executeWaiterAction(turn, state, view, input.getWaiterChoice(state));
        executeChefAction(state, input.getChefChoice(state));

        eatAndPay(turn, state);
        satisfactionDecay(state);
        
        state.getHappyHour().endTurn();
    }
}