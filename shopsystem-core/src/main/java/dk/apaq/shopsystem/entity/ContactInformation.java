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
public class ContactInformation extends Address implements Serializable {
    
    private String companyName;
    private String companyRegistration;
    private String contactName;
    private String telephone;
    private String email;

    public ContactInformation(){}

    public ContactInformation(String companyName, String companyRegistration, String contactName, String telephone, String email, String street, String postalCode, String city, String stateOrProvince, String countryCode) {
        super(street, postalCode, city, stateOrProvince, countryCode);
        this.companyName = companyName;
        this.companyRegistration = companyRegistration;
        this.contactName = contactName;
        this.telephone = telephone;
        this.email = email;
    }
    
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    
}
