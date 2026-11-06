package restaurant.model.customer;

public class RegularCustomer extends Customer {
    private static final int SATISFACTION_LOSS = 8;

    public RegularCustomer(int id) {
        super(id);
    }

    @Override
    public void decaySatisfaction() {
        loseSatisfaction(SATISFACTION_LOSS);
    }

    @Override
    public int calculateFinalBill(int basePriceCents) {
        return basePriceCents;
    }
}