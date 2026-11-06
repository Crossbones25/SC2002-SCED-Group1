package restaurant.model.staff;

import java.util.Objects;

/** The Chef performs one preparation unit per turn. */
public class Chef extends Staff {

    public Chef(String name) { super(name); }

    /** Advances the given order by 1 prep unit; the order becomes READY on its last unit. */
    public void cookOrder(Order order) {
        requireActionAvailable();
        Objects.requireNonNull(order, "order");
        if (order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new IllegalStateException("That order is " + order.getStatus() + ", not in preparation.");
        }
        order.advancePrep();
        markActionUsed();
    }
}
