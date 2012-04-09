package dk.apaq.shopsystem.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author krog
 */
@Entity
public class Subscription extends AbstractCommodity {
    
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


    @Override
    public CommodityType getCommodityType() {
        return CommodityType.Subscription;
    }

}
