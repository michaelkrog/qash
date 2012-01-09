package dk.apaq.shopsystem.entity;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author krog
 */
@Embeddable
public class ContactInformation implements Serializable, HasContactInformation {
    
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
    
    public ContactInformation(){}

    public ContactInformation(HasContactInformation hasContactInformation){
        this(hasContactInformation.getCompanyName(),
            null,
            hasContactInformation.getContactName(),
            hasContactInformation.getTelephone(),
            hasContactInformation.getEmail(),
            hasContactInformation.getStreet(),
            hasContactInformation.getPostalCode(),
            hasContactInformation.getCity(),
            hasContactInformation.getStateOrProvince(),
            hasContactInformation.getCountryCode());
        
    }

    public ContactInformation(String companyName, String companyRegistration, String contactName, String telephone, String email, String street, String postalCode, String city, String stateOrProvince, String countryCode) {
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.stateOrProvince = stateOrProvince;
        this.countryCode = countryCode;
        this.companyName = companyName;
        this.companyRegistration = companyRegistration;
        this.contactName = contactName;
        this.telephone = telephone;
        this.email = email;
    }
    
    @Override
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyRegistration() {
        return companyRegistration;
    }

    public void setCompanyRegistration(String companyRegistration) {
        this.companyRegistration = companyRegistration;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Override
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    /**
     * Retrieves the ciy name.
     */
    @Override
    public String getCity() {
        return city;
    }

    /**
     * Sets the city name.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Get countrycode.
     */
    @Override
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets countrycode.
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Retrieves state(fx 'Alabama') or province(fx 'Nordjylland'). 
     */
    @Override
    public String getStateOrProvince() {
        return stateOrProvince;
    }

    /**
     * Sets state(fx 'Alabama') or province(fx 'Nordjylland').
      */
    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    @Override
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
    
}
