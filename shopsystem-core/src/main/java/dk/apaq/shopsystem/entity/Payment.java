package dk.apaq.shopsystem.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

/**
 *
 * @author michaelzachariassenkrog
 */
@Entity
public class Payment implements Serializable, ContentEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    protected String id;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateChanged = new Date();

    @ManyToOne
    private Organisation organisation;
    
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    
    private Money amount = Money.zero(CurrencyUnit.USD);

    private String orderId;
    private String paymentDetails;

    public Payment(String currencyCode, double value) {
        this.amount = Money.of(CurrencyUnit.of(currencyCode),value);
    }

    public Payment() {
    }
    
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date date) {
        this.dateCreated = date;
    }

    public Date getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    @Override
    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        if(amount==null) {
            amount = Money.zero(CurrencyUnit.USD);
        }
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

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    
    
    
}
