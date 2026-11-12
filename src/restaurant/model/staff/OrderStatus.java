package restaurant.model.staff;

/** Lifecycle of an Order: IN_PROGRESS -> READY -> SERVED -> PAID (or CANCELLED if the customer leaves). */
public enum OrderStatus {
    IN_PROGRESS,
    READY,
    SERVED,
    PAID,
    CANCELLED
}
