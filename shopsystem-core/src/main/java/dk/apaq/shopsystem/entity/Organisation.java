package dk.apaq.shopsystem.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

/**
 * Specifies an organisation
 */
@Entity
public class Organisation implements Serializable, BasicEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    protected String id;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateChanged = new Date();

    private String name;
    private String company_reg;
    private String address;
    private String zip;
    private String city;
    private String country = "US";
    private String email;
    private String telephone;
    private long initialOrdernumber = 1;
    private long initialInvoiceNumber = 1;
    private int defaultPaymentPeriodInDays = 8;
    private String currency = "USD";

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompanyReg() {
        return company_reg;
    }

    public void setCompanyReg(String company_reg) {
        this.company_reg = company_reg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
