package restaurant.controller;

import java.util.Arrays;

import restaurant.model.RestaurantState;
import restaurant.model.customer.RegularCustomer;
import restaurant.model.menu.Menu;
import restaurant.ui.*;

public class GameController {
    private static final int MAXTURNS = 18;
    private static final int MIN_PAID_CUSTOMERS = 4;
    private static final int MIN_REVENUE_CENTS = 4500;
    private static final double MIN_AVG_SATISFACTION = 60.0;

    private RestaurantState state;
    private TurnProcessor turnProcessor;
    private ConsoleView view;
    private InputHandler input;
    private ArrivalSchedule arrivalSchedule;

    private int nextCustomerId = 1;

    public GameController() {
        state = new RestaurantState();
        turnProcessor = new TurnProcessor();
        view = new ConsoleView();
        input = new InputHandler();
        arrivalSchedule = new ArrivalSchedule();
        nextCustomerId = 1;
    }

    private void evaluateGameResult() {
        int paidCustomerCount = state.getPaidCustomerCount();
        int revenue = state.getTotalRevenueCents();
        double satisfaction = state.getAveragePaidSatisfaction();
        boolean isVictory = paidCustomerCount >= MIN_PAID_CUSTOMERS && revenue >= MIN_REVENUE_CENTS && satisfaction >= MIN_AVG_SATISFACTION;
        view.displayGameResult(isVictory, paidCustomerCount, revenue, satisfaction);
    }

    public void startGame() {
        for (int i=1; i <= MAXTURNS; i++) {
            if (arrivalSchedule.hasArrival(i)) {
                Customer customer = arrivalSchedule.createCustomer(i, nextCustomerId);
                customer.setSelectedItem(menu.selectItemDeterministically(nextCustomerId));
                nextCustomerId++;
                state.getWaitingQueue().add(customer);
            }
            view.displayTurnHeader(i, state);
            turnProcessor.processTurn(i, state, input, view);
            view.displayTurnSummary(i, state);
        }
        evaluateGameResult();
    }
}