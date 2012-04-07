package dk.apaq.shopsystem.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author krog
 */
@Entity
public class Subscription implements ContentEntity {
    
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    protected String id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateChanged = new Date();

    @ManyToOne
    private Organisation organisation;

    @Column(name="subscriptioninterval")
    private int interval;
    
    @Enumerated(EnumType.STRING)
    private IntervalUnit intervalUnit = IntervalUnit.Month;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCharged;

    @ManyToOne
    private CustomerRelationship customer;
    private SubscriptionPricingType pricingType;
    private boolean autoRenew;
    private boolean enabled;
    private long price;
    private String currency;

    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }

    @Override
    public void setDateCreated(Date date) {
        this.dateCreated = date;
    }

    @Override
    public Date getDateChanged() {
        return dateChanged;
    }

    @Override
    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    @Override
    public Organisation getOrganisation() {
        return organisation;
    }

    @Override
    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public boolean isAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(boolean autoRenew) {
        this.autoRenew = autoRenew;
    }

    public CustomerRelationship getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerRelationship customer) {
        this.customer = customer;
    }

    public Date getDateCharged() {
        return dateCharged;
    }

    public void setDateCharged(Date dateCharged) {
        this.dateCharged = dateCharged;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public IntervalUnit getIntervalUnit() {
        return intervalUnit;
    }

    public void setIntervalUnit(IntervalUnit intervalUnit) {
        this.intervalUnit = intervalUnit;
    }

    public SubscriptionPricingType getPricingType() {
        return pricingType;
    }

    public void setPricingType(SubscriptionPricingType pricingType) {
        this.pricingType = pricingType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * The price collected every time the subscription is renewed. The price is in the smallest
     * denomination of the currency, fx. cents.
     */
    public long getPrice() {
        return price;
    }

    /**
     * Price fo the subscription given in the smaller denomination of the currency fx. cents for dollars.
     * @param price 
     */
    public void setPrice(long price) {
        this.price = price;
    }

    /*
    
    public String getSubscriptionPaymentId() {
        return subscriptionPaymentId;
    }

    public void setSubscriptionPaymentId(String subscriptionPaymentId) {
        this.subscriptionPaymentId = subscriptionPaymentId;
    }
*/
    
}
