package dk.apaq.shopsystem.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * An embeddable entity that specifies an Address.
 */
@Embeddable
public class Address implements Serializable {

    private String street;
    private String city;
    private String stateOrProvince;
    private String postalCode;
    private String country;
    

    public Address() {
    }

    public Address(String street, String postalCode, String city, String stateOrProvince, String country) {
        this.street = street;
        this.city = city;
        this.stateOrProvince = stateOrProvince;
        this.postalCode = postalCode;
        this.country = country;
    }

    /**
     * Retrieves the ciy name.
     */
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
    public String getCountry() {
        return country;
    }

    /**
     * Sets countrycode.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Retrieves state(fx 'Alabama') or province(fx 'Nordjylland'). 
     */
    public String getStateOrProvince() {
        return stateOrProvince;
    }

    /**
     * Sets state(fx 'Alabama') or province(fx 'Nordjylland').
      */
    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
    
    
}
