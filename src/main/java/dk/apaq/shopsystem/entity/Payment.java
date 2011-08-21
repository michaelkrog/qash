package dk.apaq.shopsystem.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import dk.apaq.crud.HasId;

/**
 *
 * @author michaelzachariassenkrog
 */
@Entity
public class Payment extends AbstractContentEntity implements Serializable {

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    
    private double amount;

    private String orderId;

    public Payment() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    
}
