package dk.apaq.shopsystem.pay;

import dk.apaq.shopsystem.pay.PaymentGateway;
import dk.apaq.shopsystem.pay.PaymentStatus;

/**
 *
 * @author krog
 */
public class MockPaymentGateway implements PaymentGateway{

    @Override
    public void capture(long amountInCents, String transactionId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void recurring(String orderNumber, long amountInCents, String currency, boolean autocapture, String transactionId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void renew(long amountInCents, String transactionId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void refund(long amountInCents, String transactionId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cancel(String transactionId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PaymentStatus status(String transactionId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
