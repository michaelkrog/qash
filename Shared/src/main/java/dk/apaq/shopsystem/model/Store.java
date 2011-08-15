package dk.apaq.shopsystem.model;

import dk.apaq.crud.HasId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

/**
 * A shop that sells stuff.
 * @author michael
 *
 */
@Entity
public class Store extends AbstractOrganisationEntity implements Serializable, HasId<String> {

    
    private String name;

    private String road;
    
    private String zipCode;
    
    private String city;

    private String phoneNo;
    
    private String faxNo;

    private String email;
    
    private String countryCode = "US";


    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        if (countryCode == null) {
            countryCode = "US";
        }
        this.countryCode = countryCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFaxNo() {
        return faxNo;
    }

    public void setFaxNo(String faxNo) {
        this.faxNo = faxNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
