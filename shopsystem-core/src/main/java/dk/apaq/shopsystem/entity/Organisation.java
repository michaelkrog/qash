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
    private Date planExpireDate = null;
    private Plan plan = Plan.Free;
    
    
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

    public Date getPlanExpireDate() {
        return planExpireDate;
    }

    public void setPlanExpireDate(Date planExpireDate) {
        this.planExpireDate = planExpireDate;
    }

    public Plan getPlan() {
        return plan;
    }

    
    public void setPlan(Plan plan) {
        this.plan = plan;
    }
    
}
