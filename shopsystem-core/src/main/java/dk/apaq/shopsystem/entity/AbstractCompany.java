package dk.apaq.shopsystem.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author michael
 */
@Entity
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractCompany implements BasicEntity, Serializable, Company {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    protected String id;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateChanged = new Date();
    
    private String companyName;
    private String companyRegistration;
    private String contactName;
    private String telephone;
    private String email;
    private String street;
    private String city;
    private String stateOrProvince;
    private String postalCode;
    private String countryCode;
    private String bankAccount;
    private String websiteUrl;

    public AbstractCompany() {
    }

    @Override
    public String getCity() {
        return city;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String url) {
        this.websiteUrl = url;
    }

    @Override
    public String getCompanyRegistration() {
        return companyRegistration;
    }

    @Override
    public String getContactName() {
        return contactName;
    }

    @Override
    public String getCountryCode() {
        return countryCode;
    }


    public Date getDateChanged() {
        return dateChanged;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getCompanyName() {
        return companyName;
    }

    @Override
    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public String getStateOrProvince() {
        return stateOrProvince;
    }

    @Override
    public String getStreet() {
        return street;
    }

    @Override
    public String getTelephone() {
        return telephone;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public void setCompanyRegistration(String reg) {
        this.companyRegistration = reg;
    }

    @Override
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Override
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    public void setDateCreated(Date date) {
        this.dateCreated = date;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCompanyName(String name) {
        this.companyName = name;
    }

    @Override
    public void setPostalCode(String postalcode) {
        this.postalCode = postalcode;
    }

    @Override
    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    @Override
    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
}
