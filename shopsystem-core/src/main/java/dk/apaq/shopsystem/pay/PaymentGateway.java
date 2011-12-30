package dk.apaq.shopsystem.pay;

/**
 *
 * @author krog
 */
public interface PaymentGateway {
    
    public void capture(long amountInCents, String transactionId);
    public void recurring(String orderNumber, long amountInCents, String currency, boolean autocapture, String transactionId);

    public void renew(long amountInCents, String transactionId);

    public void refund(long amountInCents, String transactionId);
    public void cancel(String transactionId);

    public PaymentStatus status(String transactionId);

    
}
