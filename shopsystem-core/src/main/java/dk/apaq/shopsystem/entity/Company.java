package dk.apaq.shopsystem.entity;

/**
 *
 * @author michael
 */
public interface Company {

    String getCity();

    String getCompanyRegistration();

    String getContactName();

    String getCountry();

    String getEmail();

    String getCompanyName();

    String getPostalCode();

    String getStateOrProvince();

    String getStreet();

    String getTelephone();

    void setCity(String city);

    void setCompanyRegistration(String reg);

    void setContactName(String contactName);

    void setCountry(String country);

    void setEmail(String email);

    void setCompanyName(String name);

    void setPostalCode(String postalcode);

    void setStateOrProvince(String stateOrProvince);

    void setStreet(String street);

    void setTelephone(String telephone);
    
}
