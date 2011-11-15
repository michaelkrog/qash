package dk.apaq.shopsystem.entity;

import javax.persistence.Embeddable;

/**
 *
 * @author krog
 */
@Embeddable
public class Address {

    private String street;
    private String city;
    private String stateOrProvince;
    private String postalCode;
    private String country;

    protected Address() {
    }

    public Address(String street, String postalCode, String city, String country) {
        this.street = street;
        this.city = city;
        this.stateOrProvince = null;
        this.postalCode = postalCode;
        this.country = country;
    }
    
    public Address(String street, String postalCode, String city, String stateOrProvince, String country) {
        this.street = street;
        this.city = city;
        this.stateOrProvince = stateOrProvince;
        this.postalCode = postalCode;
        this.country = country;
    }
    
    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStreet() {
        return street;
    }
}
