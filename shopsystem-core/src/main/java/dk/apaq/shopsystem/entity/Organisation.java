package dk.apaq.shopsystem.entity;

import dk.apaq.crud.HasId;
import dk.apaq.shopsystem.pay.PaymentGatewayType;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

/**
 * Specifies an organisation
 */
@Entity
public class Organisation implements HasId<String>, Company, HasContactInformation{

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
    
    private int defaultPaymentPeriodInDays = 8;
    private String currency = "USD";
    private String annexNote;
    
    @Enumerated(EnumType.STRING)
    private OrganisationType organisationType = OrganisationType.Corporate;
    
    @Column(length=4096)
    private String notes;
    
    @Enumerated(EnumType.STRING)
    private PaymentGatewayType paymentGatewayType = PaymentGatewayType.QuickPay;
    private String merchantId;
    private String merchantSecret;
    private String merchantApiKey;
    private boolean mainOrganisation = false;
    
    private boolean subscriber = false;
    
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

    public String getCompanyRegistration() {
        return companyRegistration;
    }

    public String getContactName() {
        return contactName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Date getDateChanged() {
        return dateChanged;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public String getStreet() {
        return street;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCompanyRegistration(String reg) {
        this.companyRegistration = reg;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    public void setDateCreated(Date date) {
        this.dateCreated = date;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCompanyName(String name) {
        this.companyName = name;
    }

    public void setPostalCode(String postalcode) {
        this.postalCode = postalcode;
    }

    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
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
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantSecret() {
        return merchantSecret;
    }

    public void setMerchantSecret(String merchantSecret) {
        this.merchantSecret = merchantSecret;
    }

    public String getMerchantApiKey() {
        return merchantApiKey;
    }

    public void setMerchantApiKey(String merchantApiKey) {
        this.merchantApiKey = merchantApiKey;
    }

    public PaymentGatewayType getPaymentGatewayType() {
        return paymentGatewayType;
    }

    public void setPaymentGatewayType(PaymentGatewayType paymentGatewayType) {
        this.paymentGatewayType = paymentGatewayType;
    }
    
    public boolean isMainOrganisation() {
        return mainOrganisation;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public OrganisationType getOrganisationType() {
        return organisationType;
    }

    public void setOrganisationType(OrganisationType organisationType) {
        this.organisationType = organisationType;
    }

}
