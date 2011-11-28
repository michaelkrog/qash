package dk.apaq.shopsystem.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

/**
 * Specifies an organisation
 */
@Entity
public class Organisation extends AbstractCompany {

    private long initialOrdernumber = 1;
    private long initialInvoiceNumber = 1;
    private int defaultPaymentPeriodInDays = 8;
    private String currency = "USD";
    private String annexNote;
    
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public long getInitialInvoiceNumber() {
        return initialInvoiceNumber;
    }

    public void setInitialInvoiceNumber(long initialInvoiceNumber) {
        this.initialInvoiceNumber = initialInvoiceNumber;
    }

    public long getInitialOrdernumber() {
        return initialOrdernumber;
    }

    public void setInitialOrdernumber(long initialOrdernumber) {
        this.initialOrdernumber = initialOrdernumber;
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
