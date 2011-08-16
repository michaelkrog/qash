package dk.apaq.shopsystem.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Cascade;

/**
 * Specifies an organisation
 */
@Entity
public class Organisation extends AbstractEntity implements Serializable {

    @OneToMany
    private List<SystemUser> users = new ArrayList<SystemUser>();

    private String name;

    private String company_reg;

    private String address;

    private String zip;
    
    private String city;

    private String country = "US";
    
    private String email;
    
    private String telephone;

    private long initialOrdernumber=1;

    private long initialInvoiceNumber=1;

    private int defaultPaymentPeriodInDays=8;

    private String currency = "USD";

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

    public String getCompany_reg() {
        return company_reg;
    }

    public void setCompany_reg(String company_reg) {
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

    public List<SystemUser> getUsers() {
        return users;
    }

    public void setUsers(List<SystemUser> users) {
        this.users = users;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    

}
