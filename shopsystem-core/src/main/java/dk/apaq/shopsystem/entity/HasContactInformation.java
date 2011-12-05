package dk.apaq.shopsystem.entity;

/**
 *
 * @author krog
 */
public interface HasContactInformation extends HasAddress {

    String getCompanyName();

    String getContactName();

    String getEmail();

    String getTelephone();
    
}
