package dk.apaq.shopsystem.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Temporal;

/**
 * Specifies an organisation
 */
@Entity
public class Organisation extends AbstractCompany {

    private int defaultPaymentPeriodInDays = 8;
    private String currency = "USD";
    private String annexNote;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateSubscribed = new Date();
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateCharged = new Date();
    
    private double feePercentage = 0;
    private boolean subscriber = false;
    private String merchantId;
    
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public int getDefaultPaymentPeriodInDays() {
        return defaultPaymentPeriodInDays;
    }

    public void setDefaultPaymentPeriodInDays(int paymentPeriodInDays) {
        this.defaultPaymentPeriodInDays = paymentPeriodInDays;
    }

    public String getAnnexNote() {
        return annexNote;
    }

    public void setAnnexNote(String annexNote) {
        this.annexNote = annexNote;
    }

    public boolean isSubscriber() {
        return subscriber;
    }

    public void setSubscriber(boolean subscriber) {
        this.subscriber = subscriber;
        this.dateSubscribed = subscriber ? new Date() : null;
    }

    public Date getDateSubscribed() {
        return dateSubscribed;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public double getFeePercentage() {
        return feePercentage;
    }

    public Date getDateCharged() {
        return dateCharged;
    }

    public void setDateCharged(Date dateCharged) {
        this.dateCharged = dateCharged;
    }
    
}
