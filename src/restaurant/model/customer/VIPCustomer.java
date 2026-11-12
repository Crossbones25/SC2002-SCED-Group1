package restaurant.model.customer;

/**
 * VIP customer: loses 5 satisfaction per unserved turn and gets 10% off the bill.
 * For example, Salad 900 -> 810, Burger 1500 -> 1350, Pasta 1800 -> 1620 cents.
 */
public class VIPCustomer extends Customer {
    private static final int SATISFACTION_LOSS = 5;
    private static final int DISCOUNT_PERCENT = 10;

    public VIPCustomer(int id) {
        super(id);
    }

    @Override
    public void decaySatisfaction() {
        loseSatisfaction(SATISFACTION_LOSS);
    }

    /** Applies the 10% VIP discount, rounded to the nearest cent. */
    @Override
    public int calculateFinalBill(int basePriceCents) {
        return (int) Math.round(basePriceCents * (100 - DISCOUNT_PERCENT) / 100.0);
    }

    @Override
    public String getTypeName() {
        return "VIP";
    }
}