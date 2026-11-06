package restaurant.model.staff;

/** Base class for staff members. Every staff member gets one action per turn. */
public abstract class Staff {
    protected final String name;
    protected boolean actionUsedThisTurn;

    public Staff(String name) {
        this.name = name;
        this.actionUsedThisTurn = false;
    }

    public String getName() { return name; }
    public boolean hasUsedAction() { return actionUsedThisTurn; }
    public void resetTurn() { this.actionUsedThisTurn = false; }
    public void markActionUsed() { this.actionUsedThisTurn = true; }

    /** Guard used by subclasses: fail loudly if the single action was already spent. */
    protected void requireActionAvailable() {
        if (actionUsedThisTurn) {
            throw new IllegalStateException(name + " has already used their action this turn.");
        }
    }
}
