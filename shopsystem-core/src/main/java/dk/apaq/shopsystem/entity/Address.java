package dk.apaq.shopsystem.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * An embeddable entity that specifies an Address.
 */
@Embeddable
public class Address implements Serializable, HasAddress {

    private String street;
    private String city;
    private String stateOrProvince;
    private String postalCode;
    private String countryCode;
    

    public Address() {
    }

    public Address(String street, String postalCode, String city, String stateOrProvince, String countryCode) {
        this.street = street;
        this.city = city;
        this.stateOrProvince = stateOrProvince;
        this.postalCode = postalCode;
        this.countryCode = countryCode;
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
    @Override
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
    @Override
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String getPostalCode() {
        return postalCode;
    }

    @Override
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
    @Override
    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    @Override
    public String getStreet() {
        return street;
    }

    @Override
    public void setStreet(String street) {
        this.street = street;
    }
    
    
}
