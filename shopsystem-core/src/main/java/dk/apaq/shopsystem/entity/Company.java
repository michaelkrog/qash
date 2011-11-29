package dk.apaq.shopsystem.entity;

/**
 *
 * @author michael
 */
public interface Company extends HasAddress{

    
    String getCompanyRegistration();

    String getContactName();

    String getEmail();

    String getCompanyName();

    String getTelephone();

    void setCompanyRegistration(String reg);

    void setContactName(String contactName);

    void setEmail(String email);

    void setCompanyName(String name);

    void setTelephone(String telephone);
    
}
