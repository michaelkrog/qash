package dk.apaq.shopsystem.model;

import java.io.Serializable;

import javax.persistence.Entity;

import dk.apaq.crud.HasId;

/**
 * Defines a physical store.
 *
 */
@Entity
public class Store extends AbstractContentEntity implements Serializable {

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
