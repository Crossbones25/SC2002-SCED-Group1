package restaurant.model;

public class HappyHour {

    public static final int SATISFACTION_BONUS = 15;
    private static final int DURATION_TURNS = 2;
    private static final int DISCOUNT_PERCENT = 20;

    private boolean alreadyUsed = false;
    private int turnsRemaining = 0;

    public boolean canActivate() {
        return !alreadyUsed;
    }

    public void activate() {
        if (!canActivate()) {
            throw new IllegalStateException("Happy Hour has already been used this game.");
        }
        alreadyUsed = true;
        turnsRemaining = DURATION_TURNS;
    }

    public boolean isActive() {
        return turnsRemaining > 0;
    }

    public int getTurnsRemaining() {
        return turnsRemaining;
    }

    public void endTurn() {
        if (turnsRemaining > 0) {
            turnsRemaining--;
        }
    }

    // Takes 20% off the price in cents, rounded to the nearest cents
    public int applyDiscount(int cents) {
        if (!isActive()) {
            return cents;
        }
        int percentToPay = 100 - DISCOUNT_PERCENT;
        return (cents * percentToPay + 50) / 100;
    }
}