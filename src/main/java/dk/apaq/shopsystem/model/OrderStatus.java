package dk.apaq.shopsystem.model;

/**
 * An enum with the status of an order. The states have the following order:
 * - New (When the order has just been created but still has no ordernumber)
 * - Processing (When the order has been persisted and has an ordernumber)
 * - Accepted (When the order is accepted and invoiced. Invoice or order confirmation can be send)
 * - Completed (When the order has been paid. Receipt can be send out)
 *
 * And a single status stands out of order:
 * - Rejected (When the order is somehow unacceptable.)
 *
 * An order can shift its state forward, fx from New to Pending, but NOT
 * Pending to New. Further more an order that has been accepted cannot be
 * rejected. If an accepted order needs to be withdrawn, a counterorder needs
 * to be placed.
 *
 * @author michael
 *
 */
public enum OrderStatus {

    /**
     * Status for a new empty order.
     */
    New(false),


    /**
     * Status for an order that has orderlines added but not yet complete.
     */
    Processing(false),

    /**
     * Status for an order that has been accepted. At this stage the
     * order has been invoiced and it is safe to give the customer confirmation
     * or invoice.
     */
    Accepted(true),

    /**
     * Status for an order that has been paid and therefore is completed.
     * At this stage it is safe to give the customer a receipt for payment.
     */
    Completed(true),

    
    /**
     * Status for an order that has been rejected.
     * This could be caused by payments that did not succeed, orders
     * that cannot be fullfilled and so on.
     */
    Rejected(false);

    private final boolean confirmedState;

    private OrderStatus(boolean confirmed) {
        this.confirmedState = confirmed;
    }

    public boolean isConfirmedState() {
        return confirmedState;
    }

}
