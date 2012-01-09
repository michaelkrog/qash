package dk.apaq.shopsystem.entity;

/**
 *
 * @author michael
 */
public interface HasAddress {

    /**
     * Retrieves the ciy name.
     */
    String getCity();

    /**
     * Get countrycode.
     */
    String getCountryCode();

    String getPostalCode();

    /**
     * Retrieves state(fx 'Alabama') or province(fx 'Nordjylland').
     */
    String getStateOrProvince();

    String getStreet();

    /**
     * Sets the city name.
     */
    //void setCity(String city);

    /**
     * Sets countrycode.
     */
    //void setCountryCode(String countryCode);

    //void setPostalCode(String postalCode);

    /**
     * Sets state(fx 'Alabama') or province(fx 'Nordjylland').
     */
    //void setStateOrProvince(String stateOrProvince);

    //void setStreet(String street);
    
}
