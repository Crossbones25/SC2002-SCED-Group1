package restaurant.controller;

import java.util.HashMap;
import java.util.Map;

import restaurant.model.customer.CriticCustomer;
import restaurant.model.customer.Customer;
import restaurant.model.customer.RegularCustomer;
import restaurant.model.customer.VIPCustomer;


public class ArrivalSchedule {

    private enum CustomerType { REGULAR, VIP, CRITIC }

    private final Map<Integer, CustomerType> arrivals = new HashMap<>();

    public ArrivalSchedule() {
        arrivals.put(1, CustomerType.REGULAR);
        arrivals.put(4, CustomerType.VIP);
        arrivals.put(7, CustomerType.REGULAR);
        arrivals.put(10, CustomerType.CRITIC);
        arrivals.put(13, CustomerType.REGULAR);
        arrivals.put(16, CustomerType.REGULAR);
    }

    public boolean hasArrival(int turn) {
        return arrivals.containsKey(turn);
    }

    public Customer createCustomer(int turn, int customerId) {
        CustomerType type = arrivals.get(turn);
        if (type == null) {
            throw new IllegalArgumentException("No customer is scheduled for turn " + turn);
        }
        switch (type) {
            case VIP:
                return new VIPCustomer(customerId);
            case CRITIC:
                return new CriticCustomer(customerId);
            default:
                return new RegularCustomer(customerId);
        }
    }
}