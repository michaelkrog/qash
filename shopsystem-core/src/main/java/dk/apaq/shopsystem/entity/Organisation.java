package dk.apaq.shopsystem.entity;

import javax.persistence.Entity;

/**
 * Specifies an organisation
 */
@Entity
public class Organisation extends AbstractCompany {

    private int defaultPaymentPeriodInDays = 8;
    private String currency = "USD";
    private String annexNote;
    
    
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

    
}
